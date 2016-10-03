
package com.isso.idm.restful;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

@ApplicationPath("/system_service")
public class SystemRestfulConfig extends ResourceConfig {

	public SystemRestfulConfig() {
		packages(SystemServiceRestful.class.getPackage().toString());
		register(MultiPartFeature.class);
		register(RequestContextFilter.class);
	}
}
