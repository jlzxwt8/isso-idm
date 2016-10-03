package com.isso.idm.restful;

import java.util.Date;

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

import com.isso.idm.ISystemService;
import com.isso.idm.IdmServiceException;
import com.isso.idm.base.restful.BaseRestful;
import com.isso.idm.constant.IdmServiceErrorConstant;
import com.isso.idm.dto.SystemDTO;
import com.isso.idm.dto.SystemPageDTO;

@Path("/systems")
public class SystemServiceRestful extends BaseRestful {

	@Autowired
	private ISystemService systemService;

	@GET
	@Path("/{systemId}")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public SystemDTO findBySystemId(@PathParam("systemId") Long systemId) {
		SystemDTO system = null;
		try {
			system = systemService.findBySystemId(systemId);
		} catch (IdmServiceException e) {
			if (e.getErrorKey().equals(IdmServiceErrorConstant.DATA_NOT_FOUND)) {
				throw new NotFoundException(e.getMessage());
			} else {
				throw new InternalServerErrorException(e.getMessage());
			}
		} catch (Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
		return system;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public SystemPageDTO findSystems(@QueryParam("page") Integer pageIndex,
			@QueryParam("rows") Integer pageSize) {
		SystemPageDTO systemPageDTO = null;

		if (pageIndex == null) {
			throw new BadRequestException(IdmServiceErrorConstant.PARAM_NULL
					+ ": page " + "页码不能为空");
		}
		if (pageSize == null) {
			throw new BadRequestException(IdmServiceErrorConstant.PARAM_NULL
					+ ": rows " + "每页长度不能为空");
		}

		try {
			systemPageDTO = systemService.findSystems(pageIndex, pageSize);
		} catch (IdmServiceException e) {
			throw new InternalServerErrorException(e.getErrorKey() + ": "
					+ e.getMessage());
		} catch (Exception e) {
			throw new InternalServerErrorException(
					IdmServiceErrorConstant.INTERNAL_ERROR + ": "
							+ e.getMessage());
		}
		return systemPageDTO;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public long createSystem(@Context HttpServletRequest request,
			SystemDTO SystemDTO) {
		long systemId = 0;
		String userId = null;

		try {
			userId = getLoginUser(request);
			SystemDTO.setCreateBy(userId);
			SystemDTO.setCreateDate(new Date());
			SystemDTO.setModifyBy(userId);
			SystemDTO.setModifyDate(new Date());
			systemId = systemService.createSystem(SystemDTO);
		} catch (IdmServiceException e) {
			throw new InternalServerErrorException(e.getErrorKey() + ": "
					+ e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException(
					IdmServiceErrorConstant.INTERNAL_ERROR + ": "
							+ e.getMessage());
		} finally {
			SystemDTO = null;
		}
		return systemId;
	}

	@PUT
	@Path("/{systemId}")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public void updateSystem(@Context HttpServletRequest request,
			@PathParam("systemId") Long systemId, SystemDTO SystemDTO) {
		String userId = null;
		if (systemId != SystemDTO.getSystemId()) {
			throw new BadRequestException(
					IdmServiceErrorConstant.DATA_UPDATE_NOT_MAPPING
							+ " 更新对象不匹配");
		}
		try {
			userId = getLoginUser(request);
			SystemDTO.setModifyBy(userId);
			SystemDTO.setModifyDate(new Date());
			systemService.updateSystem(SystemDTO);
		} catch (IdmServiceException e) {
			throw new InternalServerErrorException(e.getErrorKey() + ": "
					+ e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerErrorException(
					IdmServiceErrorConstant.INTERNAL_ERROR + ": "
							+ e.getMessage());
		} finally {
			SystemDTO = null;
		}
	}

	@DELETE
	@Path("/{systemId}")
	@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public void deleteSystem(@Context HttpServletRequest request,
			@PathParam("systemId") Long systemId) {
		String userId = null;
		SystemDTO system = null;
		try {
			userId = getLoginUser(request);
			system = new SystemDTO();
			system.setSystemId(systemId);
			system.setModifyBy(userId);
			system.setModifyDate(new Date());
			systemService.deleteSystem(system);
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
			system = null;
		}
	}
}
