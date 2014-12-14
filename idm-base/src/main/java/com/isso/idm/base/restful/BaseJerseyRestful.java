
package com.isso.idm.base.restful;

import javax.ws.rs.core.HttpHeaders;

public class BaseJerseyRestful {

	protected String getUserId(HttpHeaders headers) {
		String userId =headers.getHeaderString("SSO_UESRID");
			if (userId == null) {
				// 支持Jersey单元测试功能，取得测试用户
				userId = headers.getHeaderString("TSET_USERID");
			}
		return userId;
	}
}
