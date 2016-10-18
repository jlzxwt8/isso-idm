
package com.isso.idm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.isso.common.CommonServiceException;
import com.isso.common.ICacheService;
import com.isso.idm.IAuthorizeService;
import com.isso.idm.ISystemService;
import com.isso.idm.IdmServiceException;
import com.isso.idm.constant.IdmServiceErrorConstant;
import com.isso.idm.dto.SystemDTO;



@Service("AuthorizeService")
public class AuthorizeServiceImpl implements IAuthorizeService {

	private final Logger logger = LoggerFactory.getLogger(AuthorizeServiceImpl.class);

	// 授权控制域名称
	@Value("#{propertyConfigurer['com.isso.idm.authorize.domainname']}")
	private String domainName;

	// 授权控制域路径
	@Value("#{propertyConfigurer['com.isso.idm.authorize.path']}")
	private String path;
	
	// 授权超时时间（秒）
	@Value("#{propertyConfigurer['com.isso.idm.authorize.expirein']}")
	private long expireIn;

	// 缓存命名空间
	@Value("#{propertyConfigurer['com.isso.idm.authorize.cache.namespace']}")
	private String cacheNamespace;

	// 缓存存储时间（秒）
	@Value("#{propertyConfigurer['com.isso.idm.authorize.cache.time.token']}")
	private int cacheTokenTime;

	@Autowired
	private ISystemService systemService;

	@Autowired
	private ICacheService cacheService;

	
	@Override
	public boolean checkApplicationSystemCode(String systemCode) {
		boolean check = false;
		try {
			if (systemService.findBySystemCode(systemCode) != null) {
				check = true;
			}
		} catch (IdmServiceException e) {
		}
		return check;
	}

	@Override
	public boolean checkApplicationSystem(String systemCode, String systemSecret) {
		boolean check = false;
		SystemDTO systemDTO = null;
		try {
			systemDTO = systemService.findBySystemCode(systemCode);
			if (systemDTO != null && systemDTO.getSystemSecret().equals(systemSecret)) {
				check = true;
			}
		} catch (IdmServiceException e) {
		} finally {
			systemDTO = null;
		}
		return check;
	}

	@Override
	public void putAuthCode(String authCode, String accountCode) throws IdmServiceException {
		boolean result = false;
		try {
			result = cacheService.setCacheValue(cacheNamespace, authCode, cacheTokenTime, accountCode);
		} catch (CommonServiceException e) {
			throw new IdmServiceException(e.getErrorKey(),e.getMessage());
		}
		if (!result) {
			throw new IdmServiceException(IdmServiceErrorConstant.ERROR_CACHE_WRITE, "缓存AuthCode失败");
		}
	}

	@Override
	public void putAccessToken(String accessToken, String accountCode) throws IdmServiceException {
		boolean result = false;
		try {
			result = cacheService.setCacheValue(cacheNamespace, accessToken, cacheTokenTime,
					accountCode);
		} catch (CommonServiceException e) {
			throw new IdmServiceException(e.getErrorKey(),e.getMessage());
		}
		if (!result) {
			throw new IdmServiceException(IdmServiceErrorConstant.ERROR_CACHE_WRITE, "缓存AccessToken失败");
		}
	}

	@Override
	public void deleteAuthCode(String authCode) throws IdmServiceException {
		boolean result = false;
		try {
			result = cacheService.deleteCacheValue(cacheNamespace, authCode);
		} catch (CommonServiceException e) {
			throw new IdmServiceException(e.getErrorKey(),e.getMessage());
		}
		if (!result) {
			throw new IdmServiceException(IdmServiceErrorConstant.ERROR_CACHE_DELETE, "删除AccessToken失败");
		}
	}
	
	@Override
	public void deleteAccessToken(String accessToken) throws IdmServiceException {
		boolean result = false;
		try {
			result = cacheService.deleteCacheValue(cacheNamespace, accessToken);
		} catch (CommonServiceException e) {
			throw new IdmServiceException(e.getErrorKey(),e.getMessage());
		}
		if (!result) {
			throw new IdmServiceException(IdmServiceErrorConstant.ERROR_CACHE_DELETE, "删除AccessToken失败");
		}
	}

	@Override
	public boolean checkAuthCode(String authCode) throws IdmServiceException {
		boolean result = false;
		Object obj;
		try {
			obj = cacheService.getCacheValue(cacheNamespace, authCode);
		} catch (CommonServiceException e) {
			throw new IdmServiceException(e.getErrorKey(),e.getMessage());
		}
		if (obj != null) {
			result = true;
		}
		return result;
	}

	@Override
	public boolean checkAccessToken(String accessToken) throws IdmServiceException{
		boolean result = false;
		Object obj;
		try {
			obj = cacheService.getCacheValue(cacheNamespace, accessToken);
		} catch (CommonServiceException e) {
			throw new IdmServiceException(e.getErrorKey(),e.getMessage());
		}
		if (obj != null) {
			result = true;
		}
		return result;
	}

	@Override
	public String getAccountCodeByAuthCode(String authCode) throws IdmServiceException {
		String account = null;
		Object obj;
		try {
			obj = cacheService.getCacheValue(cacheNamespace, authCode);
		} catch (CommonServiceException e) {
			throw new IdmServiceException(e.getErrorKey(),e.getMessage());
		}
		if (obj != null) {
			account = (String) obj;
		} else {
			throw new IdmServiceException(IdmServiceErrorConstant.ERROR_CACHE_READ,
					"根据AuthCode从缓存获取AccountCode失败");
		}
		return account;
	}

	@Override
	public String getAccountCodeByAccessToken(String accessToken) throws IdmServiceException {
		String account = null;
		Object obj;
		try {
			obj = cacheService.getCacheValue(cacheNamespace, accessToken);
		} catch (CommonServiceException e) {
			throw new IdmServiceException(e.getErrorKey(),e.getMessage());
		}
		if (obj != null) {
			account = (String) obj;
		} else {
			throw new IdmServiceException(IdmServiceErrorConstant.ERROR_CACHE_READ,
					"根据AccessToken从缓存获取AccountCode失败");
		}
		return account;
	}

	@Override
	public long getExpireIn() {
		return expireIn;
	}

	@Override
	public String getDomainName() {
		return domainName;
	}

	@Override
	public String getPath() {
		return path;
	}
}
