
package com.isso.idm.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.isso.idm.IAccountService;
import com.isso.idm.IAuthorizeService;
import com.isso.idm.ISystemService;
import com.isso.idm.IdmServiceException;
import com.isso.idm.client.constant.OAuthClientConstant;
import com.isso.idm.constant.IdmServiceErrorConstant;
import com.isso.idm.constant.OAuthConstant;
import com.isso.idm.constant.OAuthErrorConstant;
import com.isso.idm.dto.AccountVerifyDTO;


@Controller
public class OAuthController{

	private final Logger loggerLoginLogout = LoggerFactory
			.getLogger("LOGIN_LOGOUT");
	private final Logger loggerSystemAccess = LoggerFactory
			.getLogger("SYSTEM_ACCESS");

	@Value("#{propertyConfigurer['com.isso.idm.authorize.url']}")
	protected String authorizeUrl = "";
		

	@Autowired
	private IAccountService accountService;

	@Autowired
	private IAuthorizeService authorizeService;

	@Autowired
	private ISystemService systemService;


	@RequestMapping("/login")
	public String login(Model model) throws Exception {
		return "login";
	}

	@RequestMapping("/logout")
	public String logout(Model model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String cookieAuthCode = getCookieAuthCode(request);
		String accountCode = "";
		request.getSession().removeAttribute(OAuthClientConstant.ISSO_SSO_AUTHENTICATION);
		if (cookieAuthCode != null) {
			try {
				accountCode = authorizeService
						.getAccountCodeByAuthCode(cookieAuthCode);
				authorizeService.deleteAuthCode(cookieAuthCode);
				loggerLoginLogout.info("用户 " + accountCode + "单点退出");
			} catch (IdmServiceException e) {
			}
		}

		Cookie cookie = new Cookie(OAuthConstant.OAUTH_COOKIE_NAME, null);
		cookie.setMaxAge(0);
		cookie.setDomain(authorizeService.getDomainName());
		cookie.setPath(authorizeService.getPath());
		response.addCookie(cookie);
		model.addAttribute("title", "退出页面");
		model.addAttribute("message", "您已安全注销当前登录用户！");
		return "info";
	}

	@RequestMapping("/reject")
	public String reject(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		model.addAttribute("title", "拒绝页面");
		model.addAttribute("message", "该账号没有权限访问该系统，请尝试其他账号");
		// 构建OAuth 授权请求
		OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
		model.addAttribute("client_id", oauthRequest.getClientId());
		model.addAttribute("response_type", oauthRequest.getResponseType());
		model.addAttribute("redirect_uri", oauthRequest.getRedirectURI());
		return "reject";
	}

	@RequestMapping("/relogin")
	public void relogin(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String cookieAuthCode = getCookieAuthCode(request);
		if (cookieAuthCode != null) {
			authorizeService.deleteAuthCode(cookieAuthCode);
		}

		Cookie cookie = new Cookie(OAuthConstant.OAUTH_COOKIE_NAME, null);
		cookie.setMaxAge(0);
		cookie.setDomain(authorizeService.getDomainName());
		cookie.setPath(authorizeService.getPath());
		response.addCookie(cookie);

		String client_id = request.getParameter("client_id");
		String response_type = request.getParameter("response_type");
		String redirect_uri = request.getParameter("redirect_uri");

		response.sendRedirect(authorizeUrl + "/authorize?client_id="
				+ client_id + "&response_type=" + response_type
				+ "&redirect_uri=" + redirect_uri);
	}

	@RequestMapping("/authorize")
	public Object authorize(Model model, HttpServletRequest request,
			HttpServletResponse response) throws URISyntaxException,
			OAuthSystemException {
		OAuthAuthzRequest oauthRequest = null;
		OAuthIssuerImpl oauthIssuerImpl = null;
		OAuthASResponse.OAuthAuthorizationResponseBuilder builder = null;
		AccountVerifyDTO accountVerify = null;
		OAuthResponse oAuthResponse = null;
		try {
			// 构建OAuth 授权请求
			oauthRequest = new OAuthAuthzRequest(request);

			// 检查传入的客户端id是否正确
			if (!authorizeService.checkApplicationSystemCode(oauthRequest
					.getClientId())) {
				model.addAttribute("message",
						OAuthErrorConstant.ERROR_CLIENTID_DESCRIPTION);
				model.addAttribute("title", "提示页面");
				return "info";
			}

			// 检查授权码生成方式是否为CODE
			if (!oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE).equals(
					ResponseType.CODE.toString())) {
				model.addAttribute("message",
						OAuthErrorConstant.ERROR_GRANTTYPE_DESCRIPTION);
				model.addAttribute("title", "提示页面");
				return "info";
			}

			// 检查客户端重定向地址
			String redirectUri = oauthRequest
					.getParam(OAuth.OAUTH_REDIRECT_URI);
			if (OAuthUtils.isEmpty(redirectUri)) {
				model.addAttribute("message",
						OAuthErrorConstant.ERROR_REDIRECTURI_DESCRIPTION);
				model.addAttribute("title", "提示页面");
				return "info";
			}
			// 检查是否已登录
			accountVerify = verifyLogin(request, response);
			if (!accountVerify.getVerifyResult()) {
				model.addAttribute(
						"client",
						systemService.findBySystemCode(
								oauthRequest.getClientId()).getSystemName());
				model.addAttribute("message", accountVerify.getMessage());
				return "login";
			}
			String loginType = (String) request.getAttribute("LOGIN_TYPE");
			String loginData = (String) request.getAttribute("LOGIN_DATA");
			String authorizationCode = null;
			if (loginType.equals(OAuthConstant.LOGIN_TYPE_COOKIE)) {
				authorizationCode = loginData;
			} else {
				// 生成授权码
				oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
				authorizationCode = oauthIssuerImpl.authorizationCode();
				authorizeService.putAuthCode(authorizationCode, loginData);
				Cookie cookie = new Cookie(OAuthConstant.OAUTH_COOKIE_NAME,
						authorizationCode);
				cookie.setMaxAge(new Long(authorizeService.getExpireIn())
						.intValue());
				cookie.setDomain(authorizeService.getDomainName());
				cookie.setPath(authorizeService.getPath());
				response.addCookie(cookie);
			}

			// 进行OAuth响应构建
			builder = OAuthASResponse.authorizationResponse(request,
					HttpServletResponse.SC_FOUND);
			// 设置授权码,但因为SGM的要求删除该功能
			// builder.setCode(authorizationCode);

			// 构建响应
			oAuthResponse = builder.location(redirectUri).buildQueryMessage();
			// 根据OAuthResponse返回ResponseEntity响应
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(oAuthResponse.getLocationUri()));
			return new ResponseEntity(headers, HttpStatus.valueOf(oAuthResponse
					.getResponseStatus()));

		} catch (IdmServiceException e) {
			// 处理IdmServiceException
			if (e.getErrorKey().equals(IdmServiceErrorConstant.DATA_NOT_FOUND)) {
				model.addAttribute("message",
						OAuthErrorConstant.ERROR_CLIENTID_DESCRIPTION);
				model.addAttribute("title", "提示页面");
				return "info";
			} else {
				model.addAttribute("message",
						OAuthErrorConstant.ERROR_AUTHORIZE_DESCRIPTION);
				model.addAttribute("title", "提示页面");
				return "info";
			}
		} catch (OAuthProblemException e) {
			// 处理OAuthProblemException
			String errorRedirectUri = e.getRedirectUri();
			if (OAuthUtils.isEmpty(errorRedirectUri)) {
				model.addAttribute("message",
						OAuthErrorConstant.ERROR_REDIRECTURI_DESCRIPTION);
				model.addAttribute("title", "提示页面");
				return "info";
			}
			// 返回错误消息
			final OAuthResponse oAuthErrorResponse = OAuthASResponse
					.errorResponse(HttpServletResponse.SC_FOUND).error(e)
					.location(errorRedirectUri).buildQueryMessage();
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(oAuthErrorResponse.getLocationUri()));
			return new ResponseEntity(headers,
					HttpStatus.valueOf(oAuthErrorResponse.getResponseStatus()));
		}
	}

	@RequestMapping("/accessToken")
	public ResponseEntity accessToken(HttpServletRequest request)
			throws OAuthSystemException {
		OAuthTokenRequest oauthRequest = null;
		OAuthResponse oAuthResponse = null;
		try {
			// 构建OAuth请求
			oauthRequest = new OAuthTokenRequest(request);

			// 检查提交的客户端id是否正确
			if (!authorizeService.checkApplicationSystemCode(oauthRequest
					.getClientId())) {
				return getErrorResponseEntity(
						HttpServletResponse.SC_BAD_REQUEST,
						OAuthError.TokenResponse.INVALID_CLIENT,
						OAuthErrorConstant.ERROR_CLIENTID_DESCRIPTION);
			}

			// 检查客户端安全KEY是否正确
			if (!authorizeService.checkApplicationSystem(
					oauthRequest.getClientId(), oauthRequest.getClientSecret())) {
				return getErrorResponseEntity(
						HttpServletResponse.SC_UNAUTHORIZED,
						OAuthError.TokenResponse.UNAUTHORIZED_CLIENT,
						OAuthErrorConstant.ERROR_CLIENTSECRET_DESCRIPTION);
			}

			String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);

			// 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.AUTHORIZATION_CODE.toString())) {
				if (!authorizeService.checkAuthCode(authCode)) {
					return getErrorResponseEntity(
							HttpServletResponse.SC_UNAUTHORIZED,
							OAuthError.TokenResponse.INVALID_GRANT,
							OAuthErrorConstant.ERROR_AUTHCODE_DESCRIPTION);
				}
			} else {
				return getErrorResponseEntity(
						HttpServletResponse.SC_BAD_REQUEST,
						OAuthError.TokenResponse.UNSUPPORTED_GRANT_TYPE,
						OAuthErrorConstant.ERROR_GRANTTYPE_DESCRIPTION);
			}

			// 检查登录用户是否可以使用客户端系统
			String accountCode = authorizeService
					.getAccountCodeByAuthCode(authCode);
			if (!accountService.checkSystem(accountCode,
					oauthRequest.getClientId())) {
				return getErrorResponseEntity(
						HttpServletResponse.SC_UNAUTHORIZED,
						OAuthError.TokenResponse.INVALID_SCOPE,
						OAuthErrorConstant.ERROR_REQUEST_SYSTEM_DESCRIPTION);
			}

			loggerSystemAccess.info("用户 "
					+ accountCode
					+ "访问系统"
					+ systemService
							.findBySystemCode(oauthRequest.getClientId())
							.getSystemName());
			// 生成Access Token
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(
					new MD5Generator());
			final String accessToken = oauthIssuerImpl.accessToken();
			authorizeService.putAccessToken(accessToken,
					authorizeService.getAccountCodeByAuthCode(authCode));

			// 生成OAuth响应
			oAuthResponse = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessToken)
					.setExpiresIn(
							String.valueOf(authorizeService.getExpireIn()))
					.buildJSONMessage();

			// 根据OAuthResponse生成ResponseEntity
			return new ResponseEntity(oAuthResponse.getBody(),
					HttpStatus.valueOf(oAuthResponse.getResponseStatus()));

		} catch (IdmServiceException e) {
			// 构建错误响应
			return getErrorResponseEntity(HttpServletResponse.SC_BAD_REQUEST,
					OAuthError.TokenResponse.INVALID_SCOPE,
					OAuthErrorConstant.ERROR_REQUEST_SYSTEM_DESCRIPTION);
		} catch (OAuthProblemException e) {
			// 构建错误响应
			return getErrorResponseEntity(HttpServletResponse.SC_BAD_REQUEST, e);
		}
	}

	@RequestMapping("/userInfo")
	public ResponseEntity userInfo(HttpServletRequest request)
			throws OAuthSystemException {
		try {
			// 构建OAuth资源请求
			OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(
					request, ParameterStyle.QUERY);
			// 获取Access Token
			String accessToken = oauthRequest.getAccessToken();

			// 验证Access Token
			if (!authorizeService.checkAccessToken(accessToken)) {
				// 如果不存在或过期了，返回未验证错误
				return getErrorResponseEntity(
						HttpServletResponse.SC_UNAUTHORIZED,
						OAuthError.ResourceResponse.INVALID_TOKEN,
						OAuthErrorConstant.ERROR_INVALID_TOKEN_DESCRIPTION);
			}
			// 返回用户名
			String username = authorizeService
					.getAccountCodeByAccessToken(accessToken);
			return new ResponseEntity(username, HttpStatus.OK);
		} catch (IdmServiceException e) {
			// 构建错误响应
			return getErrorResponseEntity(HttpServletResponse.SC_UNAUTHORIZED,
					OAuthError.ResourceResponse.INVALID_TOKEN,
					OAuthErrorConstant.ERROR_INVALID_TOKEN_DESCRIPTION);
		} catch (OAuthProblemException e) {
			// 构建错误响应
			return getErrorResponseEntity(HttpServletResponse.SC_UNAUTHORIZED,
					e);
		}
	}

	@RequestMapping("/error")
	public String error(Model model) throws Exception {
		model.addAttribute("title", "提示页面");
		return "info";
	}

	private AccountVerifyDTO verifyLogin(HttpServletRequest request,
			HttpServletResponse response) {
		AccountVerifyDTO accountVerify = new AccountVerifyDTO();
		String oauthCookie = getCookieAuthCode(request);
		try {
		if (oauthCookie != null) {
			
				if (authorizeService.checkAuthCode(oauthCookie)) {
					request.setAttribute("LOGIN_TYPE",
							OAuthConstant.LOGIN_TYPE_COOKIE);
					request.setAttribute("LOGIN_DATA", oauthCookie);
					accountVerify.setVerifyResult(true);
					return accountVerify;
				} else {
					Cookie cookie = new Cookie(OAuthConstant.OAUTH_COOKIE_NAME,
							null);
					cookie.setMaxAge(0);
					cookie.setDomain(authorizeService.getDomainName());
					cookie.setPath(authorizeService.getPath());
					response.addCookie(cookie);
					accountVerify.setVerifyResult(false);
					accountVerify.setMessage("请输入用户名、密码");
					return accountVerify;
				}
			}
		else {
			String userId = request.getParameter("userid");
			String password = request.getParameter("password");
			if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(password)) {
				accountVerify.setVerifyResult(false);
				accountVerify.setMessage("请输入用户名、密码");
				return accountVerify;
			} else if (StringUtils.isEmpty(userId)
					|| StringUtils.isEmpty(password)) {
				accountVerify.setVerifyResult(false);
				accountVerify.setMessage("用户名、密码不能为空");
				return accountVerify;
			}
			userId = userId.toLowerCase();
			accountService.verifyPassword(userId, password);
			request.setAttribute("LOGIN_TYPE",
					OAuthConstant.LOGIN_TYPE_CHECK);
			request.setAttribute("LOGIN_DATA", userId);
			accountVerify.setVerifyResult(true);
			loggerLoginLogout.info("用户 " + userId + "登录单点登录平台");
			return accountVerify;
			}
		}catch (IdmServiceException e) {
				accountVerify.setVerifyResult(false);
				accountVerify.setMessage(e.getMessage());
				return accountVerify;
			}	
	}

	private ResponseEntity getErrorResponseEntity(int errorStatus,
			String error, String errorDescription) throws OAuthSystemException {
		final OAuthResponse oAuthErrorResponse = OAuthASResponse
				.errorResponse(errorStatus).setError(error)
				.setErrorDescription(errorDescription).buildJSONMessage();
		return new ResponseEntity(oAuthErrorResponse.getBody(),
				HttpStatus.valueOf(oAuthErrorResponse.getResponseStatus()));
	}

	private ResponseEntity getErrorResponseEntity(int errorStatus,
			OAuthProblemException exception) throws OAuthSystemException {
		final OAuthResponse oAuthErrorResponse = OAuthASResponse
				.errorResponse(errorStatus).error(exception).buildJSONMessage();
		return new ResponseEntity(oAuthErrorResponse.getBody(),
				HttpStatus.valueOf(oAuthErrorResponse.getResponseStatus()));
	}

	private String getCookieAuthCode(HttpServletRequest request) {
		String oauthCookie = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(OAuthConstant.OAUTH_COOKIE_NAME)) {
					oauthCookie = cookie.getValue();
				}
			}
		}
		return oauthCookie;
	}

	@RequestMapping("/forgetPassword")
	public String forgetPassword(Model model) throws Exception {
		return "forgetPassword";
	}

	@RequestMapping("/retrievePassword")
	public String retrievePassword(Model model, HttpServletRequest request)
			throws Exception {
		String userId = request.getParameter("userId");
		String email = request.getParameter("email");
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(email)) {
			model.addAttribute("message", "用户名、邮箱不能为空");
			return "forgetPassword";
		}
		try {
			accountService.retrievePassword(userId, email);
			model.addAttribute("message", "请进入邮箱 " + email + " 查看");
			model.addAttribute("title", "提示页面");
			return "info";
		} catch (IdmServiceException e) {
			model.addAttribute("message", e.getMessage());
			return "forgetPassword";
		}
	}
	

	@RequestMapping("/resetPassword")
	public String resetPassword(Model model, HttpServletRequest request)
			throws Exception {
		model.addAttribute("message", "请输入新密码");
		model.addAttribute("retrieveKey", request.getParameter("retrieveKey")
				.toString());
		return "resetPassword";
	}

	@RequestMapping("/changePassword")
	public String changePassword(Model model, HttpServletRequest request)
			throws Exception {
		String retrieveKey = request.getParameter("retrieveKey");
		String newPassword = request.getParameter("newPassword");
		String confirmNewPassword = request.getParameter("confirmNewPassword");
		if (StringUtils.isEmpty(newPassword)
				|| StringUtils.isEmpty(confirmNewPassword)) {
			model.addAttribute("message", "密码不能为空");
			model.addAttribute("retrieveKey",
					request.getParameter("retrieveKey").toString());
			return "resetPassword";
		}
		if (!newPassword.equals(confirmNewPassword)) {
			model.addAttribute("message", "密码不一致");
			model.addAttribute("retrieveKey",
					request.getParameter("retrieveKey").toString()); 
		}
		try {
			accountService.resetPassword(retrieveKey, newPassword);
		} catch (IdmServiceException e) {
			model.addAttribute("message", e.getMessage());
			model.addAttribute("retrieveKey",
					request.getParameter("retrieveKey").toString());
			return "resetPassword";
		}
		model.addAttribute("title", "密码修改成功");
		model.addAttribute("message", "密码修改成功,请重新登录");
		return "info";
	}


	@RequestMapping("/currentServerInfo")
	public String currentServerInfo(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		InetAddress ip;
		try {

			ip = InetAddress.getLocalHost();
			model.addAttribute("title", "当前访问机器IP");
			model.addAttribute("message", ip.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "info";
	}
}