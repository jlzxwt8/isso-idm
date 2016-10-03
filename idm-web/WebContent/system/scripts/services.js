angular.module('system.services',[]).factory('System',function($resource){
    return $resource('../../system_service/systems/:systemId',{systemId:'@systemId'},{
    	create: {
            method: 'POST',
            headers: { 'Content-Type': 'application/json; charset=UTF-8'}
        },
    	update: {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json; charset=UTF-8'}
        }
    }
    );
});