
package com.isso.idm.constant;

public class OAuthErrorConstant {

	public static final String ERROR_CLIENTID_DESCRIPTION = "客户端验证失败，错误的client_id";
	public static final String ERROR_CLIENTSECRET_DESCRIPTION = "客户端验证失败，错误的client_secret";
	public static final String ERROR_REDIRECTURI_DESCRIPTION = "客户端验证失败,必须提供redirect_uri";
	public static final String ERROR_GRANTTYPE_DESCRIPTION = "不支持的授权码生成方式，本系统只支持code方式";

	public static final String ERROR_AUTHORIZE_DESCRIPTION = "用户验证失败";
	public static final String ERROR_AUTHCODE_DESCRIPTION = "错误的授权码";
	public static final String ERROR_REQUEST_SYSTEM_DESCRIPTION = "无权访问目标系统";
	public static final String ERROR_INVALID_TOKEN_DESCRIPTION = "错误的访问令牌";

}
