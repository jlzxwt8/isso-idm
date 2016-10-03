
package com.isso.idm.dto;

import java.io.Serializable;

public class CurrentLoginUserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6850785557539372539L;
	
	private String userId;
	private String userName;
	private String[] permission;
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the permission
	 */
	public String[] getPermission() {
		return permission;
	}
	/**
	 * @param permission the permission to set
	 */
	public void setPermission(String[] permission) {
		this.permission = permission;
	}
	
	
}

