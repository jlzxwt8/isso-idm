
package com.isso.idm.service;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.isso.common.CommonServiceException;
import com.isso.common.ICacheService;
import com.isso.idm.IAccountService;
import com.isso.idm.IdmServiceException;
import com.isso.idm.constant.IdmServiceConstant;
import com.isso.idm.constant.IdmServiceErrorConstant;
import com.isso.idm.domain.Account;
import com.isso.idm.dto.AccountDTO;
import com.isso.idm.dto.AccountPageDTO;
import com.isso.idm.repository.IAccountRepository;


@Service("AccountService")
@Transactional(readOnly = true)
public class AccountServiceImpl implements IAccountService {
	private final static char[] HEX = "0123456789abcdef".toCharArray();
	// 用户登录错误允许次数
	@Value("#{propertyConfigurer['com.isso.idm.account.login.failed.times']}")
	private int loginfailedTimes;
	
	// 缓存命名空间
	@Value("#{propertyConfigurer['com.isso.idm.resetpassword.cache.namespace']}")
	private String cacheNamespace;

	// 缓存存储时间（秒）
	@Value("#{propertyConfigurer['com.isso.idm.account.resetpassword.retrieve.time']}")
	private int resetPasswordRetrieveTime;
	
	private final Logger logger = LoggerFactory.getLogger("OPERATION");
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private IAccountRepository accountRepository;
	
	@Autowired
	private LDAPConnectionService ldapConnectionService;
	
	@Autowired
	private ICacheService cacheService;
	
	@Autowired
	private AccountMailService accountMailService;
	
	/* (non-Javadoc)
	 * @see com.isso.idm.IAccountService#createAccount(com.isso.idm.dto.AccountDTO)
	 */
	@Override
	@Transactional
	public long createAccount(AccountDTO accountDTO) throws IdmServiceException {
		boolean result = false;
		long accountId = 0;
		Account account = null;
		try {
			account = toDomain(accountDTO);
			account.setCreateDate(accountDTO.getCreateDate());
			account.setCreateBy(accountDTO.getCreateBy());
			account.setModifyBy(accountDTO.getModifyBy());
			account.setModifyDate(accountDTO.getModifyDate());
			result = ldapConnectionService.createAccount(accountDTO);
			if(result)
				account = accountRepository.save(account);
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
			account = accountRepository.findOne(accountDTO.getAccountId());
			account.setAccountName(accountDTO.getAccountName());
			account.setIsLocked(accountDTO.getLockStatus());
			account.setStatus(accountDTO.getAccountStatus());
			account.setFirstName(accountDTO.getFirstName());
			account.setLastName(accountDTO.getLastName());
			account.setMobile(accountDTO.getMobile());
			account.setEmail(accountDTO.getEmail());
			account.setModifyBy(accountDTO.getModifyBy());
			account.setModifyDate(accountDTO.getModifyDate());
			account = accountRepository.save(account);
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
			account = accountRepository.findOne(accountDTO.getAccountId());
			account.setStatus(IdmServiceConstant.ACCOUNT_INACTIVE);
			account.setModifyBy(accountDTO.getModifyBy());
			account.setModifyDate(accountDTO.getModifyDate());
			account = accountRepository.save(account);
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
			accountList = accountRepository.findByAccountCode(accountCode);
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
			account = accountRepository.findOne(accountId);
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
		account.setFirstName(accountDTO.getFirstName());
		account.setLastName(accountDTO.getLastName());
		account.setMobile(accountDTO.getMobile());
		account.setEmail(accountDTO.getEmail());
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
		accountDTO.setFirstName(account.getFirstName());
		accountDTO.setLastName(account.getLastName());
		accountDTO.setMobile(account.getMobile());
		accountDTO.setEmail(account.getEmail());
		return accountDTO;
	}

	@Override
	@Transactional
	public void verifyPassword(String accountCode, String password) throws IdmServiceException {
		boolean verify = false;
		List<Account> accountList = null;
		Account account = null;
		int failedTimes = 0;
		try {
			accountList = accountRepository.findByAccountCode(accountCode);
			// 检查帐号是否存在
			if (!CollectionUtils.isEmpty(accountList)) {
				account = accountList.get(0);
				// 检查用户是否活动
				if (account.getStatus() == IdmServiceConstant.ACCOUNT_ACTIVE) {
					// 检查用户是否锁定
					if (account.getIsLocked() == IdmServiceConstant.ACCOUNT_UNLOCK) {
						// 验证帐号和密码
						verify = ldapConnectionService.bindUserDN(ldapConnectionService.getUserDN(accountCode),
								password);
						if (verify) {
							// 记录登录信息
							accountRepository.updateLoginSuccessInfo(new Date(), accountCode);
						} else {
							// 记录登录失败次数
							// 如果失败次数超过规定，锁定该帐号
							if (null != account.getLoginFailedTimes())
								failedTimes = account.getLoginFailedTimes() + 1;
							else
								failedTimes = 1;
							if (failedTimes >= loginfailedTimes) {
								accountRepository.updateLoginLockInfo(new Date(), IdmServiceConstant.ACCOUNT_LOCK_6TIMES, accountCode);
							} else {
								accountRepository.updateLoginFailedTimes(failedTimes, accountCode);
							}
							throw new IdmServiceException(IdmServiceErrorConstant.ERROR_ACCOIUNT_VERIFY, "帐号或密码错误");
						}
					} else if (account.getIsLocked() == IdmServiceConstant.ACCOUNT_LOCK_6TIMES) {
						throw new IdmServiceException(IdmServiceErrorConstant.ERROR_ACCOIUNT_VERIFY, "验证失败超过"
								+ loginfailedTimes + "次，帐号已锁定");
					} else if (account.getIsLocked() == IdmServiceConstant.ACCOUNT_LOCK_PASSWORD_90DAYS) {
						throw new IdmServiceException(IdmServiceErrorConstant.ERROR_ACCOIUNT_VERIFY, "密码90天未更改，帐号已锁定");
					} else {
						throw new IdmServiceException(IdmServiceErrorConstant.ERROR_ACCOIUNT_VERIFY, "用户锁定状态不正确，请联系系统管理员");
					}
				} else if (account.getStatus() == IdmServiceConstant.ACCOUNT_MANUAL_LOCKED) {
					throw new IdmServiceException(IdmServiceErrorConstant.ERROR_ACCOIUNT_VERIFY, "账号被手动锁定");
				} else if (account.getStatus() == IdmServiceConstant.ACCOUNT_INACTIVE) {
					throw new IdmServiceException(IdmServiceErrorConstant.ERROR_ACCOIUNT_VERIFY, "账号已禁用");
				} else {
					throw new IdmServiceException(IdmServiceErrorConstant.ERROR_ACCOIUNT_VERIFY, "用户状态不正确，请联系系统管理员");
				}
			} else {

				throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND, "帐号或密码错误");
			}
		} catch (IdmServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new IdmServiceException(IdmServiceErrorConstant.ERROR_ACCOIUNT_VERIFY, "帐号验证失败");
		} finally {
			accountList = null;
			account = null;
		}
	}

	@Override
	public boolean checkSystem(String accountCode, String systemCode) throws IdmServiceException {
		boolean checkSystem = false;
		List<String> sysCodeList = null;
		try {
			sysCodeList = accountRepository.findSystemCodeByAccount(accountCode);
			for (String sysCode : sysCodeList) {
				if (systemCode.equals(sysCode)) {
					checkSystem = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sysCodeList = null;
		}
		return checkSystem;
	}

	@Override
	@Transactional
	public void updatePassword(String accountCode, String oldPassword, String newPassword) throws IdmServiceException {
		String userDN = null;
		boolean isBind = false;
		boolean result = false;
		userDN = ldapConnectionService.getUserDN(accountCode);
		isBind = ldapConnectionService.bindUserDN(userDN, oldPassword);
		if (isBind) {
				result = ldapConnectionService.changePassword(userDN, newPassword);
				if(result)
					accountRepository.updatePassword(new Date(), accountCode);
				else
					throw new IdmServiceException(IdmServiceErrorConstant.ERROR_PASSWORD_CHANGE,"帐号密码修改错误");
			logger.info("用户 " + accountCode + " 修改密码");
		} else {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND, "帐号没有找到 ");
		}
	}

	@Override
	public void retrievePassword(String accountCode, String accountMail) throws IdmServiceException {
		AccountDTO account = null;
		String retrieveKey = null;
		try {
			account = findByAccountCode(accountCode);
			if (account != null) {
				if (account.getEmail().equals(accountMail)) {
					// 生成找回密码Key
					retrieveKey = toHex(md5(accountCode + getRandomCode()));
					cacheService.setCacheValue(cacheNamespace, retrieveKey, resetPasswordRetrieveTime, accountCode);
					accountMailService.sendRetrievePasswordMail(accountMail, retrieveKey,accountCode);
				} else {
					throw new IdmServiceException(IdmServiceErrorConstant.ERROR_RETRIEVE_PASSWORD,"邮箱与输入不匹配");
				}
			} else {
				throw new IdmServiceException(IdmServiceErrorConstant.ERROR_RETRIEVE_PASSWORD,"帐号找不到");
			}
		} catch (IdmServiceException e) {
			throw e;
		} catch (CommonServiceException e) {
			throw new IdmServiceException(IdmServiceErrorConstant.ERROR_CACHE_OPERATION,"缓存retrieveKey失败");
		} finally {
			account = null;
			retrieveKey = null;
		}
	}

	@Override
	public void resetPassword(String retrieveKey, String newPassword) throws IdmServiceException {
		String accountCode = null;
		String userDN = null;
		boolean result = false;
		try {
			accountCode = (String) cacheService.getCacheValue(cacheNamespace, retrieveKey);
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.ERROR_CACHE_READ,
					"读取缓存retrieveKey失败");
		}
		if(accountCode == null)
			throw new IdmServiceException(IdmServiceErrorConstant.ERROR_CACHE_OPERATION,"重置密码链接已失效,请重新尝试");
		userDN = ldapConnectionService.getUserDN(accountCode);
		result = ldapConnectionService.changePassword(userDN, newPassword);
		if(result)
			accountRepository.resetPasswordInfo(new Date(), null, IdmServiceConstant.ACCOUNT_UNLOCK, accountCode);
		else
			throw new IdmServiceException(IdmServiceErrorConstant.ERROR_PASSWORD_CHANGE,"帐号密码修改错误,请通知系统管理员");
		logger.info("用户 " + accountCode + " 重置密码");
	}

	@Override
	@Transactional
	public void manualLockAccount(String accountCode) throws IdmServiceException {
		accountRepository.manualLockAccount(accountCode);
	}

	@Override
	@Transactional
	public void activeAccount(String accountCode) throws IdmServiceException {
		accountRepository.activeAccount(accountCode);
	}

	@Override
	@Transactional
	public void inactiveAccount(String accountCode) throws IdmServiceException {
		accountRepository.inactiveAccount(accountCode);
	}
	
	private static String getRandomCode() {
		String s = UUID.randomUUID().toString();
		// 去掉“-”符号
		return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23)
				+ s.substring(24);
	}
	
	private static byte[] md5(String strSrc) {
		byte[] returnByte = null;
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			returnByte = md5.digest(strSrc.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			md5 = null;
		}
		return returnByte;
	}
	
	private static String toHex(byte[] bys) {
        if (bys == null) {
            return null;
        }
        char[] chs = new char[bys.length * 2];
        for (int i = 0, k = 0; i < bys.length; i++) {
            chs[k++] = HEX[(bys[i] & 0xf0) >> 4];
            chs[k++] = HEX[bys[i] & 0xf];
        }
        return new String(chs);
    }

	@Override
	public List<AccountDTO> findAccounts() throws IdmServiceException {
		List<Account> accountList = null;
		List<AccountDTO> accountDTOList = new ArrayList<AccountDTO>();
		AccountDTO accountDTO = null;
		try {
			accountList = (List<Account>) accountRepository.findAll();
			for(int i = 0; i < accountList.size(); i++){
				accountDTO = toDto(accountList.get(i));
				accountDTOList.add(accountDTO);
				accountDTO = null;
			}
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_PROCESS, "用户查询失败", e);
		} finally {
			accountList = null;
		}
		return accountDTOList;
	}
}
