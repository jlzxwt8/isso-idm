
package com.isso.idm.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.isso.idm.IAccountService;
import com.isso.idm.IdmServiceException;
import com.isso.idm.constant.IdmServiceConstant;
import com.isso.idm.constant.IdmServiceErrorConstant;
import com.isso.idm.domain.Account;
import com.isso.idm.dto.AccountDTO;
import com.isso.idm.dto.AccountPageDTO;
import com.isso.idm.repository.IAccoutRepository;


@Service("AccountService")
@Transactional(readOnly = true)
public class AccountServiceImpl implements IAccountService {

	private final Logger logger = LoggerFactory.getLogger("OPERATION");
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private IAccoutRepository accoutRepository;
	
	/* (non-Javadoc)
	 * @see com.isso.idm.IAccountService#createAccount(com.isso.idm.dto.AccountDTO)
	 */
	@Override
	@Transactional
	public long createAccount(AccountDTO accountDTO) throws IdmServiceException {
		long accountId = 0;
		Account account = null;
		try {
			account = toDomain(accountDTO);
			account.setCreateDate(accountDTO.getCreateDate());
			account.setCreateBy(accountDTO.getCreateBy());
			account = accoutRepository.save(account);
			logger.info("新增用户 " + account.getAccountName());
			accountId = account.getAccountId();
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_CREATE, "用户创建失败", e);
		} finally {
			account = null;
		}
		return accountId;
	}

	/* (non-Javadoc)
	 * @see com.isso.idm.IAccountService#updateAccount(com.isso.idm.dto.AccountDTO)
	 */
	@Override
	@Transactional
	public void updateAccount(AccountDTO accountDTO) throws IdmServiceException {
		Account account = null;
		try {
			account = accoutRepository.findOne(accountDTO.getAccountId());
			account.setAccountName(accountDTO.getAccountName());
			account.setIsLocked(accountDTO.getLockStatus());
			account.setStatus(accountDTO.getAccountStatus());
			account.setModifyBy(accountDTO.getModifyBy());
			account.setModifyDate(accountDTO.getModifyDate());
			account = accoutRepository.save(account);
			logger.info("更新用户 " + account.getAccountName());
		} catch (IllegalArgumentException e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND, "用户不存在");
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_UPDATE, "用户更新失败", e);
		} finally {
			account = null;
		}
	}

	/* (non-Javadoc)
	 * @see com.isso.idm.IAccountService#deleteAccount(com.isso.idm.dto.AccountDTO)
	 */
	@Override
	@Transactional
	public void deleteAccount(AccountDTO accountDTO) throws IdmServiceException {
		Account account = null;
		try {
			account = accoutRepository.findOne(accountDTO.getAccountId());
			account.setStatus(IdmServiceConstant.ACCOUNT_INACTIVE);
			account.setModifyBy(accountDTO.getModifyBy());
			account.setModifyDate(accountDTO.getModifyDate());
			account = accoutRepository.save(account);
			logger.info("禁用用户 " + account.getAccountName());
		} catch (IllegalArgumentException e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND, "用户不存在");
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_DELETE, "用户禁用失败", e);
		} finally {
			account = null;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.isso.idm.IAccountService#findAccounts(int, int)
	 */
	@Override
	public AccountPageDTO findAccounts(int pageIndex, int pageSize)
			throws IdmServiceException {
		AccountPageDTO accountPage = null;
		List<AccountDTO> accountDtoList = null;
		AccountDTO accountDTO = null;
		Long totalElements = 0L;
		List<Account> accountList = null;
		String sql = "from Account s";

		try {
			totalElements = entityManager.createQuery("select count(s) " + sql, Long.class)
					.getSingleResult();
			if (totalElements > 0) {
				accountList = entityManager
						.createQuery("select s " + sql + " order by s.createDate",
								Account.class).setFirstResult((pageIndex - 1) * pageSize)
						.setMaxResults(pageSize).getResultList();

				accountDtoList = new ArrayList<AccountDTO>();
				for (Account account : accountList) {
					accountDTO = toDto(account);
					accountDtoList.add(accountDTO);
					accountDTO = null;
				}

				accountPage = new AccountPageDTO(totalElements, pageSize, pageIndex, accountDtoList);
			}
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_PROCESS, "用户查询失败", e);
		} finally {
			accountDtoList = null;
			accountDTO = null;
			totalElements = null;
			accountList = null;
		}
		return accountPage;
	}

	/* (non-Javadoc)
	 * @see com.isso.idm.IAccountService#findByAccountCode(java.lang.String)
	 */
	@Override
	public AccountDTO findByAccountCode(String accountCode)
			throws IdmServiceException {
		List<Account> accountList = null;
		AccountDTO accountDTO = null;
		try {
			accountList = accoutRepository.findByAccountCode(accountCode);
			if (!CollectionUtils.isEmpty(accountList)) {
				accountDTO = toDto(accountList.get(0));
			} else {
				throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND, "用户不存在");
			}
		} catch (IdmServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_PROCESS, "用户查询失败", e);
		} finally {
			accountList = null;
		}
		return accountDTO;
	}

	/* (non-Javadoc)
	 * @see com.isso.idm.IAccountService#findByAccountId(java.lang.Long)
	 */
	@Override
	public AccountDTO findByAccountId(Long accountId)
			throws IdmServiceException {
		AccountDTO accountDTO = null;
		Account account = null;
		try {
			account = accoutRepository.findOne(accountId);
			if (account != null) {
				accountDTO = toDto(account);
			} else {
				throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND, "用户不存在");
			}
		} catch (IdmServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_PROCESS, "用户查询失败", e);
		} finally {
			account = null;
		}
		
		return accountDTO;
	}
	
	private Account toDomain(AccountDTO accountDTO) {
		Account account = new Account();
		account.setAccountCode(accountDTO.getAccountCode());
		account.setAccountName(accountDTO.getAccountName());
		account.setIsLocked(accountDTO.getLockStatus());
		account.setStatus(accountDTO.getAccountStatus());
		account.setUserId(accountDTO.getUserId());
		return account;
	}

	private AccountDTO toDto(Account account) {
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setAccountId(account.getAccountId());
		accountDTO.setAccountCode(account.getAccountCode());
		accountDTO.setAccountName(account.getAccountName());
		accountDTO.setLockStatus(account.getIsLocked());
		accountDTO.setAccountStatus(account.getStatus());
		accountDTO.setCreateBy(account.getCreateBy());
		accountDTO.setCreateDate(account.getCreateDate());
		accountDTO.setModifyBy(account.getModifyBy());
		accountDTO.setModifyDate(account.getModifyDate());
		accountDTO.setUserId(account.getUserId());
		return accountDTO;
	}
}
