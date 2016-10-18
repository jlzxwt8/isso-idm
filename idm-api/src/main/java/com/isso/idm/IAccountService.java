
package com.isso.idm;

import java.util.List;

import com.isso.idm.dto.AccountDTO;
import com.isso.idm.dto.AccountPageDTO;


public interface IAccountService {
	/**
	 * 验证用户名和密码是否正确
	 * @Author : Wang Tong
	 * @param accountCode
	 * @param password
	 * @throws IdmServiceException
	 */
	public void verifyPassword(String accountCode, String password) throws IdmServiceException;

	/**
	 * 用户是否拥有应用系统权限
	 * @Author : Wang Tong
	 * @param accountCode
	 * @param systemCode
	 * @return
	 * @throws IdmServiceException
	 */
	public boolean checkSystem(String accountCode, String systemCode) throws IdmServiceException;
	
	/**
	 * 更新用户密码
	 * @Author : Wang Tong
	 * @param accountCode
	 * @param oldPassword
	 * @param newPassword
	 * @throws IdmServiceException
	 */
	public void updatePassword(String accountCode, String oldPassword, String newPassword)
			throws IdmServiceException;

	/**
	 * 要求发送重置密码邮件
	 * @Author : Wang Tong
	 * @param accountCode
	 * @param accountMail
	 * @throws IdmServiceException
	 */
	public void retrievePassword(String accountCode, String accountMail) throws IdmServiceException;
	
	/**
	 * 重置用户密码
	 * @Author : Wang Tong
	 * @param retrieveKey
	 * @param newPassword
	 * @throws IdmServiceException
	 */
	public void resetPassword(String retrieveKey, String newPassword) throws IdmServiceException;

	/**
	 * 系统管理员手动锁定账号
	 * @Author : Wang Tong
	 * @param accountCode
	 * @throws IdmServiceException
	 */
	public void manualLockAccount(String accountCode)  throws IdmServiceException;

	/**
	 * 激活账号
	 * @Author : Wang Tong
	 * @param accountCode
	 * @throws IdmServiceException
	 */
	public void activeAccount(String accountCode)  throws IdmServiceException;
	
	/**
	 * 禁用账号
	 * @Author : Wang Tong
	 * @param accountCode
	 * @throws IdmServiceException
	 */
	public void inactiveAccount(String accountCode)  throws IdmServiceException;
	
	/**
	 * 创建用户账号
	 * @Author : Wang Tong
	 * @param accountDto
	 * @return
	 * @throws IdmServiceException
	 */
	public long createAccount(AccountDTO accountDto) throws IdmServiceException;

	/**
	 * 更新用户账号
	 * @Author : Wang Tong
	 * @param accountDto
	 * @throws IdmServiceException
	 */
	public void updateAccount(AccountDTO accountDto) throws IdmServiceException;

	/**
	 * 删除用户账号
	 * @Author : Wang Tong
	 * @param accountDto
	 * @throws IdmServiceException
	 */
	public void deleteAccount(AccountDTO accountDto) throws IdmServiceException;

	/**
	 * 查询用户账号
	 * @Author : Wang Tong
	 * @param pageIndex
	 * @param pageSize
	 * @return 账号列表
	 * @throws IdmServiceException
	 */
	public AccountPageDTO findAccounts(int pageIndex, int pageSize) throws IdmServiceException;

	/**
	 * 查询所有用户账号
	 * @Author : Wang Tong
	 * @return 账号列表
	 * @throws IdmServiceException
	 */
	public List<AccountDTO> findAccounts() throws IdmServiceException;
	
	/**
	 * 根据用户账号查找用户
	 * @Author : Wang Tong
	 * @param accountCode
	 * @return 用户实体
	 * @throws IdmServiceException
	 */
	public AccountDTO findByAccountCode(String accountCode) throws IdmServiceException;
	
	/**
	 * 根据用户Id查找用户
	 * @Author : Wang Tong
	 * @param accountId
	 * @return 应用系统实体
	 * @throws IdmServiceException
	 */
	public AccountDTO findByAccountId(Long accountId) throws IdmServiceException;
}
