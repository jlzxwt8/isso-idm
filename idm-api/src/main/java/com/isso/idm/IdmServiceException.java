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
