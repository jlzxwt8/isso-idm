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

import com.isso.idm.ISystemService;
import com.isso.idm.IdmServiceException;
import com.isso.idm.dto.SystemDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-config.xml")
public class SystemServiceTest {

	@Autowired
	private ISystemService systemService;
	
	private SystemDTO systemDto = null; 
	@Before
	public void setup() {
	}
	
	@Test
	public void testSystem() throws IdmServiceException {
		List<SystemDTO> systemDtoList = systemService.findSystemByAccount("152730");
		assertEquals(systemDtoList.size(),1);
	}
	@After
	public void destroy() {
	}
}
