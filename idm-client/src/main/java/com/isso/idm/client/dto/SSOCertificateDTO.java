
package com.isso.idm.client.dto;

import java.io.Serializable;

import com.isso.idm.client.SSOCertificate;


public class SSOCertificateDTO implements SSOCertificate, Serializable {

	private static final long serialVersionUID = 1727966621930883215L;

	private String userId;
	private boolean change;
	private String lastAuthCode;
	private long expiresIn;

	public SSOCertificateDTO() {
		super();
	}

	public SSOCertificateDTO(String userId, boolean change, String lastAuthCode, long expiresIn) {
		super();
		this.userId = userId;
		this.change = change;
		this.lastAuthCode = lastAuthCode;
		this.expiresIn = expiresIn;
	}

	@Override
	public String getUserId() {
		return this.userId;
	}

	@Override
	public boolean isChange() {
		return this.change;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param change
	 *            the change to set
	 */
	public void setChange(boolean change) {
		this.change = change;
	}

	/**
	 * @return the lastAuthCode
	 */
	public String getLastAuthCode() {
		return lastAuthCode;
	}

	/**
	 * @param lastAuthCode
	 *            the lastAuthCode to set
	 */
	public void setLastAuthCode(String lastAuthCode) {
		this.lastAuthCode = lastAuthCode;
	}

	/**
	 * @return the expiresIn
	 */
	public long getExpiresIn() {
		return expiresIn;
	}

	/**
	 * @param expiresIn
	 *            the expiresIn to set
	 */
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

}
