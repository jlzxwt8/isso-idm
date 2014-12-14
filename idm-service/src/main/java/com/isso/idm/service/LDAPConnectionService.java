package com.isso.idm.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPMessage;
import com.novell.ldap.LDAPResponse;
import com.novell.ldap.LDAPResponseQueue;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchQueue;
import com.novell.ldap.LDAPSearchResult;

@Service("LDAPConnectionService")
public class LDAPConnectionService {

	private static final int ldapVersion = 3;

	@Value("#{propertyConfigurer['com.sgm.dms.am.ldap.host']}")
	private String serverHost;

	@Value("#{propertyConfigurer['com.sgm.dms.am.ldap.port']}")
	private int serverPort;

	@Value("#{propertyConfigurer['com.sgm.dms.am.ldap.user']}")
	private String ldapUserID;

	@Value("#{propertyConfigurer['com.sgm.dms.am.ldap.password']}")
	private String userPassword;

	@Value("#{propertyConfigurer['com.sgm.dms.am.ldap.dealerdn']}")
	private String dealerDN;

	@Value("#{propertyConfigurer['com.sgm.dms.am.ldap.pool.max']}")
	private int maxpoolsize;

	@Value("#{propertyConfigurer['com.sgm.dms.am.ldap.pool.min']}")
	private int minpoolsize;

	private List<LDAPConnection> workPool = new ArrayList<LDAPConnection>();
	private List<LDAPConnection> waitPool = new ArrayList<LDAPConnection>();

	private volatile Object semaphore = new Boolean(true);

	public LDAPConnection getConnection() {
		LDAPConnection conn = null;
		synchronized (semaphore) {
			try {
				if (waitPool.size() > 0) {
					conn = waitPool.get(0);
					waitPool.remove(conn);
					if (conn.isConnectionAlive()) {
						workPool.add(conn);
					} else {
						conn = null;
						conn = build();
						if (conn != null) {
							workPool.add(conn);
						}
					}
				} else {
					conn = build();
					if (conn != null) {
						workPool.add(conn);
					}
				}
			} catch (Exception e) {
			}
		}
		return conn;
	}

	public void closeConnection(LDAPConnection conn) {
		if (conn != null) {
			synchronized (semaphore) {
				try {
					workPool.remove(conn);
					if (waitPool.size() + 1 <= maxpoolsize) {
						waitPool.add(conn);
					} else {
						conn.disconnect();
					}
				} catch (Exception e) {
				}
			}
		}
	}

	public boolean bindUserDN(String userDN, String password) {
		boolean success = false;
		LDAPConnection conn = null;
		LDAPResponseQueue queue = null;
		LDAPResponse rsp = null;
		try {
			conn = new LDAPConnection();
			conn.connect(serverHost, serverPort);
			queue = conn.bind(ldapVersion, userDN, password.getBytes("UTF8"),
					(LDAPResponseQueue) null);

			rsp = (LDAPResponse) queue.getResponse();
			if (rsp.getResultCode() == LDAPException.SUCCESS) {
				success = true;
			}
		} catch (Exception e) {
		} finally {
			userDN = null;
			if (conn != null && conn.isConnected()) {
				try {
					conn.disconnect();
				} catch (Exception e) {
				}
			}
			conn = null;
			queue = null;
			rsp = null;
		}
		return success;
	}

	public String getUserDN(String userCode) {
		String userDN = null;
		LDAPConnection conn = null;
		LDAPSearchQueue queue = null;
		LDAPMessage message = null;
		LDAPEntry entry = null;

		try {
			conn = getConnection();
			if (conn != null) {
				queue = conn.search(dealerDN, 2, "(uid=" + userCode + ")", null, false,
						(LDAPSearchQueue) null, (LDAPSearchConstraints) null);
				while ((message = queue.getResponse()) != null) {
					if (message instanceof LDAPSearchResult) {
						entry = ((LDAPSearchResult) message).getEntry();
						userDN = entry.getDN();
						entry = null;
					}
				}
			}
		} catch (Exception e) {
		} finally {
			if (conn != null) {
				closeConnection(conn);
				conn = null;
			}
			queue = null;
			message = null;
			entry = null;
		}
		return userDN;
	}

	private LDAPConnection build() {
		LDAPConnection conn = null;
		LDAPResponseQueue queue = null;
		LDAPResponse rsp = null;
		try {
			conn = new LDAPConnection();
			conn.connect(serverHost, serverPort);
			queue = conn.bind(ldapVersion, ldapUserID, userPassword.getBytes("UTF8"),
					(LDAPResponseQueue) null);

			rsp = (LDAPResponse) queue.getResponse();
			if (rsp.getResultCode() != LDAPException.SUCCESS) {
				conn.disconnect();
				conn = null;
			}
		} catch (Exception e) {
		} finally {
			queue = null;
			rsp = null;
		}
		return conn;
	}

	@PostConstruct
	public void postConstruct() {
		try {
			for (int i = 0; i < minpoolsize; i++)
				waitPool.add(build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PreDestroy
	public void preDestroy() {
		try {
			for (int i = 0; i < workPool.size(); i++) {
				((LDAPConnection) workPool.get(i)).disconnect();
			}
			workPool.clear();

			for (int i = 0; i < waitPool.size(); i++) {
				((LDAPConnection) waitPool.get(i)).disconnect();
			}
			waitPool.clear();
		} catch (Exception exception) {
		}
		workPool = null;
		waitPool = null;
	}

}
