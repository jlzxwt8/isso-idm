
package com.isso.idm.dto;

import java.io.Serializable;
import java.util.List;

import com.isso.idm.base.dto.BaseDTO;

public class AccountDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -8761644545414263773L;

	private Long accountId; // 帐号编号
	private String accountCode; // 帐号编码
	private String accountName; // 帐号姓名
	private String mobile;// 手机
	private String email;// 电子邮件
	private Integer accountStatus;// 帐号状态
	private Integer lockStatus;// 帐号锁定状态
	private List<String> roles;// 用户属于的角色
	private List<String> systems;// 用户可以使用的客户系统

	
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
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
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

}
