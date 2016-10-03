
package com.isso.idm.client;



/**
 * 单点登录用户安全证书
 * 
 * @author wang tong
 *
 */
public interface SSOCertificate {

	/**
	 * 取得登录用户编码
	 * 
	 * @Author : wang tong
	 * @return 登录用户编码
	 */
	public String getUserId();

	/**
	 * 当前登录用户与上一次相比，是否改变
	 * 
	 * @Author : wang tong
	 * @return "true"发生改变,"false"未改变
	 */
	public boolean isChange();
}
