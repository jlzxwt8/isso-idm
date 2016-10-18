package com.isso.idm.restful;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.isso.idm.IAccountService;
import com.isso.idm.IdmServiceException;
import com.isso.idm.base.restful.BaseRestful;
import com.isso.idm.constant.IdmServiceErrorConstant;
import com.isso.idm.dto.AccountDTO;
import com.isso.idm.dto.AccountPageDTO;
import com.isso.idm.dto.CurrentLoginUserDTO;


@Path("/accounts")
public class AccountServiceRestful extends BaseRestful {

	@Autowired
	private IAccountService accountService;

	@GET
	@Path("/{accountId}")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public AccountDTO findByAccountId(@PathParam("accountId") Long accountId) {
		AccountDTO account = null;
		try {
			account = accountService.findByAccountId(accountId);
		} catch (IdmServiceException e) {
			if (e.getErrorKey().equals(IdmServiceErrorConstant.DATA_NOT_FOUND)) {
				throw new NotFoundException(e.getMessage());
			} else {
				throw new InternalServerErrorException(e.getMessage());
			}
		} catch (Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
		return account;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public AccountPageDTO findAccounts(@QueryParam("page") Integer pageIndex,
			@QueryParam("rows") Integer pageSize) {
		AccountPageDTO accountPageDTO = null;

		if (pageIndex == null) {
			throw new BadRequestException(IdmServiceErrorConstant.PARAM_NULL
					+ ": page " + "页码不能为空");
		}
		if (pageSize == null) {
			throw new BadRequestException(IdmServiceErrorConstant.PARAM_NULL
					+ ": rows " + "每页长度不能为空");
		}

		try {
			accountPageDTO = accountService.findAccounts(pageIndex, pageSize);
		} catch (IdmServiceException e) {
			throw new InternalServerErrorException(e.getErrorKey() + ": "
					+ e.getMessage());
		} catch (Exception e) {
			throw new InternalServerErrorException(
					IdmServiceErrorConstant.INTERNAL_ERROR + ": "
							+ e.getMessage());
		}
		return accountPageDTO;
	}
	
	@GET
	@Path("/all")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<AccountDTO> findAccounts() {
		List<AccountDTO> accountDTOList = null;
		try {
			accountDTOList = accountService.findAccounts();
		} catch (IdmServiceException e) {
			throw new InternalServerErrorException(e.getErrorKey() + ": "
					+ e.getMessage());
		} catch (Exception e) {
			throw new InternalServerErrorException(
					IdmServiceErrorConstant.INTERNAL_ERROR + ": "
							+ e.getMessage());
		}
		return accountDTOList;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public long createAccount(@Context HttpServletRequest request,
			AccountDTO accountDTO) {
		long accountId = 0;
		String userId = null;

		try {
			userId = getLoginUser(request);
			accountDTO.setCreateBy(userId);
			accountDTO.setModifyBy(userId);
			accountDTO.setCreateDate(new Date());
			accountDTO.setModifyDate(new Date());
			accountId = accountService.createAccount(accountDTO);
		} catch (IdmServiceException e) {
			throw new InternalServerErrorException(e.getErrorKey() + ": "
					+ e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException(
					IdmServiceErrorConstant.INTERNAL_ERROR + ": "
							+ e.getMessage());
		} finally {
			accountDTO = null;
		}
		return accountId;
	}

	@PUT
	@Path("/{accountId}")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public void updateAccount(@Context HttpServletRequest request,
			@PathParam("accountId") Long accountId, AccountDTO accountDTO) {
		String userId = null;
		if (accountId != accountDTO.getAccountId()) {
			throw new BadRequestException(
					IdmServiceErrorConstant.DATA_UPDATE_NOT_MAPPING
							+ " 更新对象不匹配");
		}
		try {
			userId = getLoginUser(request);
			accountDTO.setModifyBy(userId);
			accountDTO.setModifyDate(new Date());
			accountService.updateAccount(accountDTO);
		} catch (IdmServiceException e) {
			throw new InternalServerErrorException(e.getErrorKey() + ": "
					+ e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException(
					IdmServiceErrorConstant.INTERNAL_ERROR + ": "
							+ e.getMessage());
		} finally {
			accountDTO = null;
		}
	}

	@DELETE
	@Path("/{accountId}")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public void deleteSystem(@Context HttpServletRequest request,
			@PathParam("accountId") Long accountId) {
		String userId = null;
		AccountDTO account = null;
		try {
			userId = getLoginUser(request);
			account = new AccountDTO();
			account.setAccountId(accountId);
			account.setModifyBy(userId);
			account.setModifyDate(new Date());
			accountService.deleteAccount(account);
		} catch (IdmServiceException e) {
			if (e.getErrorKey().equals(IdmServiceErrorConstant.DATA_NOT_FOUND)) {
				throw new NotFoundException(e.getErrorKey() + ": " + e.getMessage());
			} else {
				throw new InternalServerErrorException(e.getErrorKey() + ": " + e.getMessage());
			}
		} catch (Exception e) {
			throw new InternalServerErrorException(IdmServiceErrorConstant.INTERNAL_ERROR + ": "
					+ e.getMessage());
		} finally {
			account = null;
		}
	}
	
	@GET
	@Path("/currentLoginUser")
	@Produces(MediaType.APPLICATION_JSON+ ";charset=utf-8")
	public CurrentLoginUserDTO findCurrentAccount(@Context HttpServletRequest request) {
		String userId = null;
		CurrentLoginUserDTO currentLoginUserDTO = null;
		AccountDTO accountDto = null;
		String[] permission = null;
 		try {
 			userId = getLoginUser(request);
			if(userId == null)
				throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND,"no login user found");
 			accountDto = accountService.findByAccountCode(userId);
			currentLoginUserDTO = new CurrentLoginUserDTO();
			currentLoginUserDTO.setUserId(accountDto.getAccountCode());
			currentLoginUserDTO.setUserName(accountDto.getAccountName());
			permission = new String[]{"amAdmin"};
			currentLoginUserDTO.setPermission(permission);
		} catch (IdmServiceException e) {
			if (e.getErrorKey().equals(IdmServiceErrorConstant.DATA_NOT_FOUND)) {
				throw new NotFoundException(e.getMessage());
			} else {
				throw new InternalServerErrorException(e.getMessage());
			}
		} catch (Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
		return currentLoginUserDTO;
	}
}
