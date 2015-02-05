package com.isso.idm;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.isso.idm.dto.AccountDTO;
import com.isso.idm.repository.IAccoutRepository;
import com.isso.idm.restful.IdmRestfulConfig;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountServiceRestFulTest extends JerseyTest {

	@Override
	protected Application configure() {
		Map<String, String> properties = new HashMap<String, String>() {
			{
				put("contextConfigLocation", "classpath:test-config.xml");
			}
		};
		return new IdmRestfulConfig().setProperties(properties);
	}

	@Override
	protected void configureClient(ClientConfig config) {
		config.register(MultiPartFeature.class);
	}

	
	@Test
	public void test00_findByAccountId() {
		Form form = new Form();
		form.param("accountCode", "wangtong");
		form.param("accountName", "王桐");
		Long accountId = this
				.target("rest/account")
				.request().header("TSET_USERID", "zhulei")
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE),
						Long.class);
		AccountDTO output = this.target("rest/account/" + accountId).request()
				.get(AccountDTO.class);
		Assert.assertEquals(accountId, output.getAccountId());
	}

	@Test
	public void test01_findByAccountCode() {
		AccountDTO output = this.target("rest/account")
				.queryParam("accountCode", "wangtong")
				.request().get(AccountDTO.class);
		Assert.assertEquals("wangtong", output.getAccountCode());
	}

	@Test
	public void test02_createAndDeleteAccount() {
		Form form = new Form();
		form.param("accountCode", "zhulei");
		form.param("accountName", "朱雷");
		Long accountId = this
				.target("rest/account")
				.request().header("TSET_USERID", "zhulei")
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE),
						Long.class);
		AccountDTO accountDTO = this.target("rest/account/" + accountId).request()
				.get(AccountDTO.class);
		Assert.assertEquals("zhulei", accountDTO.getAccountCode());
		this.target("rest/account/" + accountDTO.getAccountId()).request()
				.header("TSET_USERID", "zhulei").delete();
		String output = this.target("rest/accounts").queryParam("page", 1)
				.queryParam("rows", 10).request().get(String.class);
		System.out.println(output);
	}

	@Test
	public void test03_updateAccount() {
		Form form = new Form();
		form.param("accountCode", "zhulei");
		form.param("accountName", "朱雷");
		Long accountId = this
				.target("rest/account")
				.request().header("TSET_USERID", "zhulei")
				.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE),
						Long.class);
		form = new Form();
		form.param("accountCode", "duyong");
		form.param("accountName", "杜勇");
		this.target("rest/account/" + accountId).request().header("TSET_USERID", "zhulei")
		.put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		AccountDTO output = this.target("rest/account/" + accountId).request()
				.get(AccountDTO.class);
		Assert.assertEquals("duyong", output.getAccountCode());
	}

	@Test
	public void test04_findAccounts() {
		String output = this.target("rest/accounts").queryParam("page", 1)
				.queryParam("rows", 10).request().get(String.class);
		System.out.println(output);
	}
}
