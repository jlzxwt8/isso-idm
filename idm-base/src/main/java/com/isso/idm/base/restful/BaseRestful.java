
package com.isso.idm.base.restful;

import javax.servlet.http.HttpServletRequest;

import com.isso.idm.base.constant.BaseConstant;

public class BaseRestful {
	
	
	protected String getLoginUser(HttpServletRequest request) {
		String userId = null;
		try {
			userId = (String) request.getSession().getAttribute(
					BaseConstant.LOGIN_USER);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return userId;
	}
}
