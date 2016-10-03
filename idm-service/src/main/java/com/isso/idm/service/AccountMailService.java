
package com.isso.idm.service;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.isso.common.CommonServiceException;
import com.isso.common.IMailService;
import com.isso.idm.IdmServiceException;

@Service("AccountMailService")
public class AccountMailService {
	// 找回密码邮件标题
	@Value("#{propertyConfigurer['com.isso.idm.account.resetpassword.mail.subject']}")
	protected String retrievePasswordSubject;

	// 找回密码邮件模版文件名称
	@Value("#{propertyConfigurer['com.isso.idm.account.resetpassword.mail.templateName']}")
	protected String retrievePasswordTemplate;
	
	// 找回密码邮件模版文件名称
	@Value("#{propertyConfigurer['com.isso.idm.account.resetpassword.url']}")
	protected String retrievePasswordUrl;

	@Autowired
	private IMailService mailService;



	public void sendRetrievePasswordMail(String toAddress, String retrieveKey,String accountCode) throws IdmServiceException {
		Map<String, String> editPointMap = null; 
		String [] toAddresses = new String[1];
		toAddresses[0] = toAddress;
		try {
			editPointMap = new HashMap<String, String>();
			editPointMap.put("url", retrievePasswordUrl +"?retrieveKey=" + retrieveKey);
			mailService.sendMail(toAddresses, retrievePasswordSubject, retrievePasswordTemplate, editPointMap);
		} catch (CommonServiceException e) {
			throw new IdmServiceException(e.getErrorKey(),e.getMessage());
		}finally {
			toAddresses = null;
			editPointMap = null;
		}
	}

}
