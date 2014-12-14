package com.isso.idm.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.isso.idm.IAccountService;
import com.isso.idm.IdmServiceException;
import com.isso.idm.constant.IdmServiceConstant;
import com.isso.idm.dto.AccountDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-config.xml")
public class AccountServiceTest {

	@Autowired
	private IAccountService accountService;
	
	private AccountDTO accountDto = null; 
	@Before
	public void prepareAccount() {
		accountDto.setAccountCode("wangtong");
		accountDto.setAccountName("王桐");
		accountDto.setAccountStatus(IdmServiceConstant.ACCOUNT_INACTIVE);
		accountDto.setEmail("jlzxwt8@126.com");
		accountDto.setMobile("15000555632");
		accountDto.setLockStatus(IdmServiceConstant.ACCOUNT_UNLOCK);
	}
	
	@Test
	public void testAccount() throws IdmServiceException {
		Long accountId = accountService.createAccount(accountDto);
		AccountDTO accountDTO = accountService.findByAccountId(accountId);
		assertEquals(accountDto.getAccountCode(),accountDTO.getAccountCode());
		accountDTO.setAccountStatus(IdmServiceConstant.ACCOUNT_ACTIVE);
		accountService.updateAccount(accountDTO);
		accountDTO = accountService.findByAccountCode(accountDto.getAccountCode());
		assertEquals(IdmServiceConstant.ACCOUNT_ACTIVE,accountDTO.getAccountStatus());
		accountService.deleteAccount(accountDTO.getAccountId());
		accountDTO = accountService.findByAccountId(accountId);
		assertEquals(IdmServiceConstant.ACCOUNT_INACTIVE,accountDTO.getAccountStatus());
		
		
	}

}
