
package com.isso.idm.dto;

import java.io.Serializable;

import com.isso.idm.base.dto.BaseDTO;

public class SystemDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 7692974485108347108L;

	private long systemId;
	private String systemCode;
	private String systemName;
	private String systemSecret;
	private String systemUrl;
	/**
	 * @return the systemId
	 */
	public long getSystemId() {
		return systemId;
	}

	/**
	 * @param systemId
	 *            the systemId to set
	 */
	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	/**
	 * @return the systemCode
	 */
	public String getSystemCode() {
		return systemCode;
	}

	/**
	 * @param systemCode
	 *            the systemCode to set
	 */
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	/**
	 * @return the systemName
	 */
	public String getSystemName() {
		return systemName;
	}

	/**
	 * @param systemName
	 *            the systemName to set
	 */
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	/**
	 * @return the systemSecret
	 */
	public String getSystemSecret() {
		return systemSecret;
	}

	/**
	 * @param systemSecret
	 *            the systemSecret to set
	 */
	public void setSystemSecret(String systemSecret) {
		this.systemSecret = systemSecret;
	}

	/**
	 * @return the systemUrl
	 */
	public String getSystemUrl() {
		return systemUrl;
	}

	/**
	 * @param systemUrl
	 *            the systemUrl to set
	 */
	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
	}
}
