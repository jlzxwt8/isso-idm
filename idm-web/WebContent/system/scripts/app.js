var amAdminApp = angular.module('amAdminApp', [ 'ngTouch', 'common.ui.framework', 'common.ui.esayui',
		'system.controllers','system.services' ]);


//register the interceptor as a service
amAdminApp.factory('myHttpInterceptor', function($window,$q) {
	return {
		// optional method
		'request' : function(config) {
			// do something on success
			return config;
		},

		// optional method
		'requestError' : function(rejection) {
			// do something on error
			if (canRecover(rejection)) {
				return responseOrNewPromise
			}
			return $q.reject(rejection);
		},

		// optional method
		'response' : function(response) {
			if (response.status == 401){
				$window.location.reload(true);
			}
            return response || $q.when(response);
		},

		// optional method
		'responseError' : function(rejection) {
			// do something on error
			if (rejection.status == 0) {
				$window.location.reload(true);
			}
			return $q.reject(rejection);
		}
	};
});
	
amAdminApp.config(function($httpProvider, $stateProvider, menuServiceProvider, permissionServiceProvider) {
	$httpProvider.interceptors.push('myHttpInterceptor');
	permissionServiceProvider.doConfig("../../account_service/accounts/currentLoginUser");
	menuServiceProvider.doConfig("../../data/menu_data.json", $stateProvider);
});

amAdminApp.run([ 'menuService', 'permissionService', function(menuService, permissionService) {
	permissionService.init().then(function(result) {
		menuService.init();
	}, function(reason) {
	});
} ]);

function getCookie(name) 
{ 
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
 
    if(arr=document.cookie.match(reg))
 
        return unescape(arr[2]); 
    else 
        return null; 
} 

function setCookie(name,value,time)
{  
    var exp = new Date(); 
    exp.setTime(exp.getTime() + time*1000); 
    document.cookie = name + "="+ escape (value) + ";Domain=.saic-gm.com;;Expires=" + exp.toGMTString() + ";Path=/";
}
