package com.isso.idm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the account database table.
 * 
 */
@Entity
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long accountId;

	private String accountCode;

	private String accountName;

	private String createBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	private String email;

	private long userId;

	private Integer isLocked;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastPwdChangeDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lockTime;

	private Integer loginFailedTimes;

	private String mobile;

	private String modifyBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date pwdExpireDate;

	private Integer status;

	public Account() {
	}

	public long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public String getAccountCode() {
		return this.accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Integer getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(Integer isLocked) {
		this.isLocked = isLocked;
	}

	public Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastPwdChangeDate() {
		return this.lastPwdChangeDate;
	}

	public void setLastPwdChangeDate(Date lastPwdChangeDate) {
		this.lastPwdChangeDate = lastPwdChangeDate;
	}

	public Date getLockTime() {
		return this.lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public Integer getLoginFailedTimes() {
		return this.loginFailedTimes;
	}

	public void setLoginFailedTimes(Integer loginFailedTimes) {
		this.loginFailedTimes = loginFailedTimes;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Date getPwdExpireDate() {
		return this.pwdExpireDate;
	}

	public void setPwdExpireDate(Date pwdExpireDate) {
		this.pwdExpireDate = pwdExpireDate;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}