
package com.isso.idm.client.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.error.OAuthError;

import com.isso.idm.client.OAuthClientException;
import com.isso.idm.client.constant.OAuthClientConstant;
import com.isso.idm.client.constant.OAuthClientErrorConstant;
import com.isso.idm.client.dto.SSOAccessToken;
import com.isso.idm.client.dto.SSOCertificateDTO;
import com.isso.idm.client.util.SSOClientUtil;



/**
 * OAuth 2.0 SSO 服务客户端Filter
 * 
 * @author wang tong
 *
 */
public class OAuthClientFilter implements Filter {

	protected FilterConfig filterConfig = null;

	private String clientId;
	private String clientSecret;

	private String authorizeUrl;
	private String accessTokenUrl;
	private String userInfoUrl;
	private String rejectUrl;

	private List<String> excludePathList = Collections.synchronizedList(new ArrayList<String>());
	private List<String> excludeFileList = Collections.synchronizedList(new ArrayList<String>());

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		// 初始化SSO Client标识
		clientId = filterConfig.getInitParameter("clientId");
		clientSecret = filterConfig.getInitParameter("clientSecret");

		// 初始化SSO 服务地址
		String verifyUrl = filterConfig.getInitParameter("verifyUrl");
		String tokenUrl = filterConfig.getInitParameter("tokenUrl");
		authorizeUrl = verifyUrl + "/authorize";
		rejectUrl = verifyUrl + "/reject";
		accessTokenUrl = tokenUrl + "/accessToken";
		userInfoUrl = tokenUrl + "/userInfo";

		// 初始化排除安全验证目录清单
		String excludePath = filterConfig.getInitParameter("excludePath");
		if (excludePath != null && !excludePath.trim().equals("")) {
			String[] paths = excludePath.split(",");
			for (String path : paths) {
				excludePathList.add(path);
			}
		}

		// 初始化排除安全验证文件类型清单
		String excludeFile = filterConfig.getInitParameter("excludeFile");
		if (excludeFile != null && !excludeFile.trim().equals("")) {
			String[] files = excludeFile.split(",");
			for (String file : files) {
				excludeFileList.add(file.toUpperCase());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		

		String requestUrl = SSOClientUtil.getFullURL(request);
		System.out.println("Request URL in idm-client filter: " + request.getRequestURL());
		String fileType = null;
		String oauthCode = null;
		SSOAccessToken accessToken = null;

		SSOCertificateDTO certificate = null;

		try {
			certificate = SSOClientUtil.getCertificate(request);

			// 排除不需要安全检查的URL路径
			for (String excludePath : excludePathList) {
				String temprequestUrl=requestUrl;
				if (requestUrl.indexOf("?")>=0)
				 temprequestUrl=requestUrl.substring(0,requestUrl.indexOf("?"));
				if (temprequestUrl.indexOf(excludePath) != -1) {
					filterChain.doFilter(servletRequest, servletResponse);
					return;
				}
			}
			// 排除不需要安全检查的文件类型
			fileType = SSOClientUtil.getFileType(request);
			if (excludeFileList.contains(fileType)) {
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			oauthCode = SSOClientUtil.getOAuthCode(request);

			// 检查当前回话是否有证书
			if (certificate == null) {
				// 如果没有OAuth code，则重定向到服务端授权
				if (oauthCode == null) {
					request.getSession()
							.removeAttribute(OAuthClientConstant.ISSO_SSO_AUTHENTICATION);
					response.sendRedirect(authorizeUrl + "?client_id=" + clientId
							+ "&response_type=code&redirect_uri=" + requestUrl);
					return;
				} else {

					accessToken = SSOClientUtil.getAccessToken(oauthCode, accessTokenUrl, clientId,
							clientSecret, requestUrl);
					
					certificate = new SSOCertificateDTO(SSOClientUtil.extractUserId(
							accessToken.getAccessToken(), userInfoUrl), false, oauthCode,
							accessToken.getExpiresIn());

					request.getSession().setAttribute(OAuthClientConstant.ISSO_SSO_AUTHENTICATION,
							certificate);
				}
			} else {
				if (oauthCode != null) {
					// 检查Cookie内Auth Code与会话Auth Code是否一致，如果不一致则说明单点登录用户以切换
					if (!certificate.getLastAuthCode().equals(oauthCode)) {
						accessToken = SSOClientUtil.getAccessToken(oauthCode, accessTokenUrl,
								clientId, clientSecret, requestUrl);

						request.getSession().removeAttribute(
								OAuthClientConstant.ISSO_SSO_AUTHENTICATION);
						certificate = new SSOCertificateDTO(SSOClientUtil.extractUserId(
								accessToken.getAccessToken(), userInfoUrl), true, oauthCode,
								accessToken.getExpiresIn());
						request.getSession().setAttribute(
								OAuthClientConstant.ISSO_SSO_AUTHENTICATION, certificate);
					} else {
						certificate.setChange(false);
					}
				} else {
					throw new OAuthClientException(OAuthClientErrorConstant.SSO_SYSTEM, "不存在有效的oauthcode cookie");
				}
			}

			// 重新设置SSO回话超时时间
			SSOClientUtil.resetOAuthCookie(request, response, certificate.getLastAuthCode(),
					Long.valueOf(certificate.getExpiresIn()).intValue());
		} catch (OAuthClientException e) {
			String errorKey = e.getErrorKey();
			e.printStackTrace();
			if (errorKey.equals(OAuthError.TokenResponse.INVALID_SCOPE)) {
				response.sendRedirect(rejectUrl + "?client_id=" + clientId
						+ "&response_type=code&redirect_uri=" + requestUrl);
				return;
			}else {
				request.getSession().removeAttribute(OAuthClientConstant.ISSO_SSO_AUTHENTICATION);
				response.sendRedirect(authorizeUrl + "?client_id=" + clientId
						+ "&response_type=code&redirect_uri=" + requestUrl);
				return;
			}
		} finally {
			requestUrl = null;
			fileType = null;
			oauthCode = null;
			accessToken = null;
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}


	@Override
	public void destroy() {
		excludePathList.clear();
		excludeFileList.clear();
	}

}
