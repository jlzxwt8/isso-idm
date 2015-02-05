package com.isso.idm.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
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
import com.isso.idm.repository.IAccoutRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-config.xml")
public class AccountServiceTest {

	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private IAccoutRepository accoutRepository;
	
	private AccountDTO accountDto = null; 
	@Before
	public void setup() {
		accoutRepository.deleteAll();
		accountDto = new AccountDTO();
		accountDto.setAccountCode("wangtong");
		accountDto.setAccountName("王桐");
		accountDto.setAccountStatus(IdmServiceConstant.ACCOUNT_INACTIVE);
		accountDto.setEmail("jlzxwt8@126.com");
		accountDto.setMobile("15000555632");
		accountDto.setLockStatus(IdmServiceConstant.ACCOUNT_UNLOCK);
		accountDto.setCreateDate(new Date());
	}
	
	@Test
	public void testCreateAccount() throws IdmServiceException {
		Long accountId = accountService.createAccount(accountDto);
		AccountDTO accountDTO = accountService.findByAccountId(accountId);
		assertEquals(accountDto.getAccountCode(),accountDTO.getAccountCode());
		accountDTO.setAccountStatus(IdmServiceConstant.ACCOUNT_ACTIVE);
		accountService.updateAccount(accountDTO);
		accountDTO = accountService.findByAccountCode(accountDto.getAccountCode());
		assertEquals(Integer.valueOf(IdmServiceConstant.ACCOUNT_ACTIVE),accountDTO.getAccountStatus());
		accountService.deleteAccount(accountDTO);
		accountDTO = accountService.findByAccountId(accountId);
		assertEquals(Integer.valueOf(IdmServiceConstant.ACCOUNT_INACTIVE),accountDTO.getAccountStatus());
	}
	@After
	public void destroy() {
		accoutRepository.deleteAll();
	}
}
