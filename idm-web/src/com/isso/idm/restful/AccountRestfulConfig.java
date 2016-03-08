
package com.isso.idm.restful;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

@ApplicationPath("/account_service")
public class AccountRestfulConfig extends ResourceConfig {

	public AccountRestfulConfig() {
		packages(AccountServiceRestful.class.getPackage().toString());
		register(MultiPartFeature.class);
		register(RequestContextFilter.class);
	}
}
