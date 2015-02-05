
package com.isso.idm;

import com.isso.idm.dto.AccountDTO;
import com.isso.idm.dto.AccountPageDTO;


public interface IAccountService {
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
