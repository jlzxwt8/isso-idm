package com.isso.idm.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.isso.idm.IAccountService;
import com.isso.idm.IdmServiceException;
import com.isso.idm.dto.AccountDTO;
import com.isso.idm.service.LDAPConnectionService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-config.xml")
public class AccountServiceTest {

	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private LDAPConnectionService ldapConnectionService;
	
	private AccountDTO accountDto = null; 
	@Before
	public void setup() {
	}
	
	@Test
	public void testAccount() throws IdmServiceException {
		accountService.retrievePassword("152730", "tongwang.zh@hotmail.com");
		}
	@After
	public void destroy() {
	}
}
