
package com.isso.idm.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.isso.idm.domain.Account;


public interface IAccountRepository extends CrudRepository<Account, Long> {

	@Query("select a from Account a where a.accountCode=:accountCode")
	public List<Account> findByAccountCode(@Param("accountCode") String accountCode);
	
	@Modifying
	@Query("update Account a set a.lastLoginTime=:lastLoginTime,a.loginFailedTimes=0 where a.accountCode=:accountCode")
	public void updateLoginSuccessInfo(@Param("lastLoginTime") Date lastLoginTime,
			@Param("accountCode") String accountCode);
	
	@Modifying
	@Query("update Account a set a.loginFailedTimes=:loginFailedTimes where a.accountCode=:accountCode")
	public void updateLoginFailedTimes(@Param("loginFailedTimes") int loginFailedTimes,
			@Param("accountCode") String accountCode);

	@Modifying
	@Query("update Account a set a.loginFailedTimes=0,a.lockTime=:lockTime,a.isLocked=:isLocked where a.accountCode=:accountCode")
	public void updateLoginLockInfo(@Param("lockTime") Date lockTime,
			@Param("isLocked") int isLocked, @Param("accountCode") String accountCode);
	
	@Query(value = "select systemCode from accountsystem a where a.accountCode=:accountCode", nativeQuery = true)
	public List<String> findSystemCodeByAccount(@Param("accountCode") String accountCode);
	
	@Modifying
	@Query("update Account a set a.lastPwdChangeDate=:lastPwdChangeDate where a.accountCode=:accountCode")
	public void updatePassword(@Param("lastPwdChangeDate") Date lastPwdChangeDate,
			@Param("accountCode") String accountCode);
	
	@Modifying
	@Query("update Account a set a.lastPwdChangeDate=:lastPwdChangeDate,a.lockTime=:lockTime,a.isLocked=:isLocked "
			+ "where a.accountCode=:accountCode")
	public void resetPasswordInfo(@Param("lastPwdChangeDate") Date lastPwdChangeDate,
			@Param("lockTime") Date lockTime, @Param("isLocked") int isLocked,
			@Param("accountCode") String accountCode);

	@Modifying
	@Query("update Account a set a.status=1 "
			+ "where a.accountCode=:accountCode")
	public void activeAccount(@Param("accountCode") String accountCode);

	@Modifying
	@Query("update Account a set a.status=0 "
			+ "where a.accountCode=:accountCode")
	public void inactiveAccount(@Param("accountCode") String accountCode);
	
	@Modifying
	@Query("update Account a set a.status=2 "
			+ "where a.accountCode=:accountCode")
	public void manualLockAccount(@Param("accountCode") String accountCode);
}
