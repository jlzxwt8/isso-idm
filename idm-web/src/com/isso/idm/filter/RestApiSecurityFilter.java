
package com.isso.idm.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.isso.idm.base.constant.BaseConstant;
import com.isso.idm.client.dto.SSOCertificateDTO;
import com.isso.idm.constant.IdmServiceConstant;
import com.isso.idm.constant.OAuthConstant;

public class RestApiSecurityFilter implements Filter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String userId = null;
		SSOCertificateDTO authentication = null;
		authentication = (SSOCertificateDTO) request.getSession().getAttribute(OAuthConstant.ISSO_SSO_AUTHENTICATION);
		if (authentication != null) {
			userId = authentication.getUserId();
		}
		request.setAttribute(BaseConstant.LOGIN_USER, userId);
		filterChain.doFilter(servletRequest, servletResponse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
