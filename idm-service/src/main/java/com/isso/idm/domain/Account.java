package com.isso.idm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the TM_DMAP_ACCOUT database table.
 * 
 */
@Entity
@Table(name="TM_IDM_ACCOUNT")
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TM_IDM_ACCOUT_ACCOUNTID_GENERATOR", sequenceName="SQ_ACCOUNT")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TM_IDM_ACCOUT_ACCOUNTID_GENERATOR")
	@Column(name="ACCOUNT_ID")
	private long accountId;

	@Column(name="ACCOUNT_CODE")
	private String accountCode;

	@Column(name="ACCOUNT_NAME")
	private String accountName;

	@Column(name="CREATE_BY")
	private String createBy;

	@Temporal(TemporalType.DATE)
	@Column(name="CREATE_DATE")
	private Date createDate;

	private String email;

	@Column(name="EXT_USER_ID")
	private java.math.BigDecimal extUserId;

	@Column(name="IS_LOCKED")
	private Integer isLocked;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_LOGIN_TIME")
	private Date lastLoginTime;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_PWD_CHANGE_DATE")
	private Date lastPwdChangeDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LOCK_TIME")
	private Date lockTime;

	@Column(name="LOGIN_FAILED_TIMES")
	private Integer loginFailedTimes;

	private String mobile;

	@Column(name="MODIFY_BY")
	private String modifyBy;

	@Temporal(TemporalType.DATE)
	@Column(name="MODIFY_DATE")
	private Date modifyDate;

	@Temporal(TemporalType.DATE)
	@Column(name="PWD_EXPIRE_DATE")
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

	public java.math.BigDecimal getExtUserId() {
		return this.extUserId;
	}

	public void setExtUserId(java.math.BigDecimal extUserId) {
		this.extUserId = extUserId;
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