
package com.isso.idm.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import com.isso.idm.client.OAuthClientException;
import com.isso.idm.client.constant.OAuthClientConstant;
import com.isso.idm.client.constant.OAuthClientErrorConstant;
import com.isso.idm.client.dto.SSOAccessToken;
import com.isso.idm.client.dto.SSOCertificateDTO;


public class SSOClientUtil {

	/**
	 * 
	 * @Author : wang tong
	 * @param request
	 * @return
	 */
	public static SSOCertificateDTO getCertificate(HttpServletRequest request) {
		SSOCertificateDTO authentication = null;
		try {
			authentication = (SSOCertificateDTO) request.getSession(true).getAttribute(
					OAuthClientConstant.ISSO_SSO_AUTHENTICATION);
		} catch (Exception e) {
		}
		return authentication;
	}

	/**
	 * 
	 * @Author : wang tong
	 * @param request
	 * @return
	 */
	public static String getOAuthCode(HttpServletRequest request) {
		String oauthCode = null;
		Cookie oauthCodeCookie = null;
		Cookie[] cookies = null;
		try {
			cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(OAuthClientConstant.ISSO_OAUTH_CODE)) {
						oauthCodeCookie = cookie;
						break;
					}
				}
			}
			if (oauthCodeCookie != null) {
				oauthCode = oauthCodeCookie.getValue();
			} else {
				oauthCode = request.getParameter(OAuthClientConstant.ISSO_OAUTH_RESPONSE_TYPE);
			}
		} finally {
			oauthCodeCookie = null;
			cookies = null;
		}
		return oauthCode;
	}

	/**
	 * 
	 * @Author : wang tong
	 * @param oauthCode
	 * @param accessTokenUrl
	 * @param clientId
	 * @param clientSecret
	 * @param redirectUrl
	 * @return
	 * @throws OAuthClientException
	 */
	public static SSOAccessToken getAccessToken(String oauthCode, String accessTokenUrl, String clientId,
			String clientSecret, String redirectUrl) throws OAuthClientException {
		SSOAccessToken accessToken = null;
		try {
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			OAuthClientRequest accessTokenRequest = OAuthClientRequest.tokenLocation(accessTokenUrl)
					.setGrantType(GrantType.AUTHORIZATION_CODE).setClientId(clientId).setClientSecret(clientSecret)
					.setCode(oauthCode).setRedirectURI(redirectUrl).buildQueryMessage();

			OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(accessTokenRequest, OAuth.HttpMethod.POST);

			accessToken = new SSOAccessToken();
			accessToken.setAccessToken(oAuthResponse.getAccessToken());
			accessToken.setExpiresIn(oAuthResponse.getExpiresIn());
		} catch (OAuthProblemException e) {
			throw new OAuthClientException(e.getError(), e.getDescription());
		} catch (Exception e) {
			throw new OAuthClientException(OAuthClientErrorConstant.SSO_SYSTEM, e.getMessage());
		}
		return accessToken;
	}

	/**
	 * 
	 * @Author : wang tong
	 * @param accessToken
	 * @param userInfoUrl
	 * @return
	 * @throws OAuthClientException
	 */
	public static String extractUserId(String accessToken, String userInfoUrl) throws OAuthClientException {
		String userId = null;
		try {
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

			OAuthClientRequest userInfoRequest = new OAuthBearerClientRequest(userInfoUrl).setAccessToken(accessToken)
					.buildQueryMessage();

			OAuthResourceResponse resourceResponse = oAuthClient.resource(userInfoRequest, OAuth.HttpMethod.GET,
					OAuthResourceResponse.class);
			userId = resourceResponse.getBody();
		} catch (OAuthProblemException e) {
			throw new OAuthClientException(e.getError(), e.getDescription());
		} catch (Exception e) {
			throw new OAuthClientException(OAuthClientErrorConstant.SSO_SYSTEM, e.getMessage());
		}
		return userId;
	}

	/**
	 * 
	 * @Author : wang tong
	 * @param request
	 * @return
	 */
	public static String getFullURL(HttpServletRequest request) {
		StringBuffer url = new StringBuffer();
		String queryString = null;
		StringBuffer attributeString = null;
		String[] attributes = null;
		Properties properties = new Properties();
		InputStream in = SSOClientUtil.class.getResourceAsStream("/am-client.properties");
		if (in == null) {
			in = SSOClientUtil.class.getResourceAsStream("/default-am-client.properties");
		}
		try {
			properties.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 url = request.getRequestURL();
		queryString = request.getQueryString();
		attributeString = new StringBuffer();
		attributes = null;
		if (queryString != null) {
			attributes = queryString.split("&");
			for (String attribute : attributes) {
				if (attribute.indexOf("code=") == -1) {
					if (attributeString.toString().length() > 0) {
						attributeString.append("&");
					}
					attributeString.append(attribute);
				}
			}
			if (attributeString.toString().length() > 0) {
				url.append("?" + attributeString.toString());
			}
		}
		return url.toString();
	}


	/**
	 * 
	 * @Author : wang tong
	 * @param request
	 * @return
	 */
	public static String getFileType(HttpServletRequest request) {
		StringBuffer url = request.getRequestURL();
		String fileType = "";
		int i = url.lastIndexOf(".");
		if (i != -1) {
			fileType = url.substring(i + 1).toUpperCase();
		}
		return fileType;
	}

	/**
	 * 
	 * @Author : wang tong
	 * @param request
	 *            Http请求
	 * @param response
	 *            Http回复
	 * @param authCode
	 *            OAuth 2.0 用户验证Code
	 * @param expiresIn
	 *            超时时间长度
	 * @return
	 */
	public static void resetOAuthCookie(HttpServletRequest request, HttpServletResponse response, String authCode,
			int expiresIn) {
		Properties properties = new Properties();
		InputStream in = SSOClientUtil.class.getResourceAsStream("/am-client.properties");
		if (in == null) {
			in = SSOClientUtil.class.getResourceAsStream("/default-am-client.properties");
		}
		try {
			properties.load(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Cookie oauthCodeCookie = new Cookie(OAuthClientConstant.ISSO_OAUTH_CODE, authCode);
		oauthCodeCookie.setMaxAge(expiresIn);
		oauthCodeCookie.setDomain(properties.getProperty("com.isso.idm.authorize.domainname"));
		oauthCodeCookie.setPath(properties.getProperty("com.isso.idm.authorize.path"));
		response.addCookie(oauthCodeCookie);
	}

}
