
package com.isso.idm.dto;

import java.io.Serializable;

public class AccountVerifyDTO implements Serializable {

	private static final long serialVersionUID = 8087008715553195220L;
	
	private boolean verifyResult;
	private String message;

	/**
	 * @return the verifyResult
	 */
	public boolean getVerifyResult() {
		return verifyResult;
	}

	/**
	 * @param verifyResult
	 *            the verifyResult to set
	 */
	public void setVerifyResult(boolean verifyResult) {
		this.verifyResult = verifyResult;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
