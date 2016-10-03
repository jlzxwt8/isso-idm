package com.isso.idm.service;

import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.isso.idm.dto.AccountDTO;
import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPMessage;
import com.novell.ldap.LDAPModification;
import com.novell.ldap.LDAPResponse;
import com.novell.ldap.LDAPResponseQueue;
import com.novell.ldap.LDAPSearchConstraints;
import com.novell.ldap.LDAPSearchQueue;
import com.novell.ldap.LDAPSearchResult;

@Service("LDAPConnectionService")
public class LDAPConnectionService {

	private static final int ldapVersion = 3;

	@Value("#{propertyConfigurer['com.isso.idm.ldap.host']}")
	private String serverHost;

	@Value("#{propertyConfigurer['com.isso.idm.ldap.port']}")
	private int serverPort;

	@Value("#{propertyConfigurer['com.isso.idm.ldap.user']}")
	private String ldapUserID;

	@Value("#{propertyConfigurer['com.isso.idm.ldap.password']}")
	private String userPassword;

	@Value("#{propertyConfigurer['com.isso.idm.ldap.account.dn']}")
	private String usersDN;

	@Value("#{propertyConfigurer['com.isso.idm.ldap.pool.max']}")
	private int maxpoolsize;

	@Value("#{propertyConfigurer['com.isso.idm.ldap.pool.min']}")
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
			if (userDN != null) {
				conn = new LDAPConnection();
				conn.connect(serverHost, serverPort);
				queue = conn.bind(ldapVersion, userDN,
						password.getBytes("UTF8"), (LDAPResponseQueue) null);

				rsp = (LDAPResponse) queue.getResponse();
				if (rsp.getResultCode() == LDAPException.SUCCESS) {
					success = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	public boolean createAccount(AccountDTO accountDto) {
		boolean success = false;
		LDAPAttribute attribute = null;
		LDAPAttributeSet attributeSet = null;
		LDAPConnection conn = null;
		LDAPResponseQueue queue = null;
		LDAPResponse rsp = null;
		String userDN = "cn=" + accountDto.getAccountCode() +"," + usersDN;
		try {
			conn = getConnection();
			attributeSet = new LDAPAttributeSet();
			attribute = new LDAPAttribute("objectClass", "organizationalPerson");
			attributeSet.add(attribute);
			attribute = new LDAPAttribute("sn", accountDto.getAccountName());
			attributeSet.add(attribute);
			attribute = new LDAPAttribute("userPassword", accountDto.getPassword());
			attributeSet.add(attribute);
			
			LDAPEntry entry = new LDAPEntry(userDN,attributeSet);
	        queue = conn.add(entry, queue);
	        rsp = (LDAPResponse) queue.getResponse();
			if (rsp.getResultCode() == LDAPException.SUCCESS) {
				success = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	
	public boolean changePassword(String userDN, String password) {
		boolean success = false;
		LDAPConnection conn = null;
		LDAPResponseQueue queue = null;
		LDAPResponse rsp = null;

		try {
			if (userDN != null) {
				conn = getConnection();
	            LDAPAttribute attribute = new LDAPAttribute("userPassword", password);
	            LDAPModification mod = new LDAPModification(LDAPModification.REPLACE,attribute);
	            queue = conn.modify(userDN, mod,queue);
				rsp = (LDAPResponse) queue.getResponse();
				if (rsp.getResultCode() == LDAPException.SUCCESS) {
					success = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
				queue = conn.search(usersDN, 2, "(cn=" + userCode + ")",
						null, false, (LDAPSearchQueue) null,
						(LDAPSearchConstraints) null);
				while ((message = queue.getResponse()) != null) {
					if (message instanceof LDAPSearchResult) {
						entry = ((LDAPSearchResult) message).getEntry();
						userDN = entry.getDN();
						entry = null;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			queue = conn.bind(ldapVersion, ldapUserID,
					userPassword.getBytes("UTF8"), (LDAPResponseQueue) null);

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
