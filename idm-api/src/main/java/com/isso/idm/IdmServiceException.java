/****************************************************************************************
 * @File name   :      AmServiceException.java
 *
 * @Author      :      LEIKZHU
 *
 * @Date        :      Sep 1, 2014
 *
 * @Copyright Notice: 
 * Copyright (c) 2014 SGM, Inc. All  Rights Reserved.
 * This software is published under the terms of the SGM Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 * 
 * 
 * --------------------------------------------------------------------------------------
 * Date								Who					Version				Comments
 * Sep 1, 2014 1:20:58 PM			LEIKZHU				1.0				Initial Version
 ****************************************************************************************/
package com.isso.idm;

import com.isso.idm.base.BaseException;


public class IdmServiceException extends BaseException {

	private static final long serialVersionUID = 5757333134914661807L;

	public IdmServiceException() {
	}

	public IdmServiceException(String errorKey, String errorMessage) {
		super(errorKey, errorMessage);
	}

	public IdmServiceException(String errorKey, String errorMessage, Throwable rootCause) {
		super(errorKey, errorMessage, rootCause);
	}

	public IdmServiceException(Throwable rootCause) {
		super(rootCause);
	}

}
