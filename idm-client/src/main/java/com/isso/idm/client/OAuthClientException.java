
package com.isso.idm.client;

import java.io.PrintStream;
import java.io.PrintWriter;

public class OAuthClientException extends Exception {

	private static final long serialVersionUID = -7119086666982392100L;

	private String errorKey;
	private Throwable rootCause;

	public OAuthClientException() {
	}

	public OAuthClientException(String errorKey, String errorMessage) {
		super(errorMessage);
		this.errorKey = errorKey;
	}

	public OAuthClientException(String errorKey, String errorMessage, Throwable rootCause) {
		super(errorMessage);
		this.errorKey = errorKey;
		this.rootCause = rootCause;
	}

	public OAuthClientException(Throwable rootCause) {
		this.rootCause = rootCause;
	}

	/**
	 * Get Error Key
	 * 
	 * @return the errorKey
	 */
	public String getErrorKey() {
		return errorKey;
	}

	/**
	 * Get Root Cause
	 * 
	 * @return the rootCause
	 */
	public Throwable getRootCause() {
		return rootCause;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		StringBuffer sb = new StringBuffer();
		String msg = super.getMessage();

		if (msg != null) {
			sb.append(msg);

			if (rootCause != null) {
				sb.append(": ");
			}
		}

		if (rootCause != null) {
			sb.append("root cause: "
					+ ((rootCause.getMessage() == null) ? rootCause.toString() : rootCause
							.getMessage()));
		}

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#printStackTrace()
	 */
	@Override
	public void printStackTrace() {
		super.printStackTrace();

		if (rootCause != null) {
			synchronized (System.err) {
				System.err.println("\nRoot cause:");
				rootCause.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 */
	@Override
	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);

		if (rootCause != null) {
			synchronized (s) {
				s.println("\nRoot cause:");
				rootCause.printStackTrace(s);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
	 */
	@Override
	public void printStackTrace(PrintWriter s) {
		super.printStackTrace(s);

		if (rootCause != null) {
			synchronized (s) {
				s.println("\nRoot cause:");
				rootCause.printStackTrace(s);
			}
		}
	}

}
