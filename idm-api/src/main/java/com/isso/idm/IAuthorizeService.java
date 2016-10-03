
package com.isso.idm;

public interface IAuthorizeService {

	/**
	 * 检查应用系统是否存在
	 * @Author : Wang Tong
	 * @param systemCode
	 * @return
	 */
	public boolean checkApplicationSystemCode(String systemCode);

	/**
	 * 检查应用系统和应用系统密钥是否正确
	 * @Author : Wang Tong
	 * @param systemCode
	 * @param systemSecret
	 * @return
	 */
	public boolean checkApplicationSystem(String systemCode, String systemSecret);

	/**
	 * 添加 auth code
	 * @Author : Wang Tong
	 * @param authCode
	 * @param accountCode
	 * @throws IdmServiceException
	 */
	public void putAuthCode(String authCode, String accountCode) throws IdmServiceException;

	/**
	 * 添加 access token
	 * @Author : Wang Tong
	 * @param accessToken
	 * @param accountCode
	 * @throws IdmServiceException
	 */
	public void putAccessToken(String accessToken, String accountCode) throws IdmServiceException;
	
	/**
	 * 删除auth code
	 * @Author : Wang Tong
	 * @param authCode
	 * @throws IdmServiceException
	 */
	public void deleteAuthCode(String authCode) throws IdmServiceException;

	/**
	 * 删除access code
	 * @Author : Wang Tong
	 * @param accessCode
	 * @throws IdmServiceException
	 */
	public void deleteAccessToken(String accessToken) throws IdmServiceException;
	
	/**
	 * 验证auth code是否有效
	 * @Author : Wang Tong
	 * @param authCode
	 * @return
	 */
	public boolean checkAuthCode(String authCode) throws IdmServiceException;

	/**
	 * 验证access token是否有效
	 * @Author : Wang Tong
	 * @param accessToken
	 * @return
	 */
	public boolean checkAccessToken(String accessToken) throws IdmServiceException;

	/**
	 * 通过auth code获得用户账号
	 * @Author : Wang Tong
	 * @param authCode
	 * @return
	 * @throws IdmServiceException
	 */
	public String getAccountCodeByAuthCode(String authCode) throws IdmServiceException;

	/**
	 * 通过access token获得用户账号
	 * @Author : Wang Tong
	 * @param accessToken
	 * @return
	 * @throws IdmServiceException
	 */
	public String getAccountCodeByAccessToken(String accessToken) throws IdmServiceException;

	/**
	 * 获得AuthCode、AccessToken 过期时间
	 * @Author : Wang Tong
	 * @return
	 */
	public long getExpireIn();

	/**
	 * 获得AuthCode Cookie域名
	 * @Author : Wang Tong
	 * @return
	 */
	public String getDomainName();

	/**
	 * 获得AuthCode Cookie路径
	 * @Author : Wang Tong
	 * @return
	 */
	public String getPath();
}
