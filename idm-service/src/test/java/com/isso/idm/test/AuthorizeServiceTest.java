package com.isso.idm.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.isso.idm.IAuthorizeService;
import com.isso.idm.IdmServiceException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-config.xml")
public class AuthorizeServiceTest {

	@Autowired
	private IAuthorizeService authorizeService;
	
	@Before
	public void setup() {
	}
	
	@Test
	public void testAuthorize() throws IdmServiceException {
		assertEquals(authorizeService.checkApplicationSystem("1002", "utd"),true);
		assertEquals(authorizeService.checkApplicationSystemCode("1002"),true);
		authorizeService.putAuthCode("1", "152730");
		assertEquals(authorizeService.checkAuthCode("1"),true);
		authorizeService.putAccessToken("2", "152730");
		assertEquals(authorizeService.checkAccessToken("2"),true);
		assertEquals(authorizeService.getAccountCodeByAuthCode("1"),"152730");
		assertEquals(authorizeService.getAccountCodeByAccessToken("2"),"152730");
		authorizeService.deleteAuthCode("1");
		authorizeService.deleteAccessToken("2");
		assertEquals(authorizeService.checkAuthCode("1"),false);
		assertEquals(authorizeService.checkAccessToken("2"),false);
	}
	@After
	public void destroy() {
	}
}
