
package com.isso.idm.dto;

import java.io.Serializable;
import java.util.List;

import com.isso.idm.base.dto.BaseDTO;

public class AccountDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -8761644545414263773L;

	private Long accountId; // 帐号编号
	private String accountCode; // 帐号编码
	private String accountName; // 帐号姓名
	private Integer accountStatus;// 帐号状态
	private Integer lockStatus;// 帐号锁定状态
	private List<String> positions;// 用户的岗位
	private List<String> systems;// 用户可以使用的系统
	private String firstName; //名
	private String lastName; //姓
	private String mobile; //手机
	private String email; //邮箱
	private String password; //密码
	
	/**
	 * @return the accountId
	 */
	public Long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountCode
	 */
	public String getAccountCode() {
		return accountCode;
	}

	/**
	 * @param accountCode
	 *            the accountCode to set
	 */
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the accountStatus
	 */
	public Integer getAccountStatus() {
		return accountStatus;
	}

	/**
	 * @param accountStatus
	 *            the accountStatus to set
	 */
	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}

	/**
	 * @return the lockStatus
	 */
	public Integer getLockStatus() {
		return lockStatus;
	}

	/**
	 * @param lockStatus
	 *            the lockStatus to set
	 */
	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	/**
	 * @return the roles
	 */
	public List<String> getPositions() {
		return positions;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setPositions(List<String> positions) {
		this.positions = positions;
	}

	/**
	 * @return the useSystems
	 */
	public List<String> getSystems() {
		return systems;
	}

	/**
	 * @param useSystems
	 *            the useSystems to set
	 */
	public void setSystems(List<String> systems) {
		this.systems = systems;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
}
