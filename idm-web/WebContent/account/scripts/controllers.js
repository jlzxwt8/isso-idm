var systemManagerModule = angular.module('system.controllers', [ 'ngResource']);

systemManagerModule.controller('SystemManagerCntl', function($scope,
		$resource,System) {
	
	$scope.reloadTable = function() {
		$('#templateTable').datagrid('reload');
		$('#editSystem').linkbutton('disable');
		$('#removeSystem').linkbutton('disable');
	}
	
	$scope.selectTableRow = function(index, row) {
		if (row != null) {
			$('#editSystem').linkbutton('enable');
			$('#removeSystem').linkbutton('enable');
			$scope.selectedSystem = {};
			$scope.selectedSystem.systemId = row.systemId;
			$scope.selectedSystem.systemCode = row.systemCode;
			$scope.selectedSystem.systemName = row.systemName;
			$scope.selectedSystem.systemSecret = row.systemSecret;
			$scope.selectedSystem.systemUrl = row.systemUrl;
			$scope.selectedSystem.ldapGroupCode = row.ldapGroupCode;
		}
	}

	$scope.createSystem = function() {
		$('#createSystemTemplate').window('open');
		
	}
	
	$scope.submitCreateForm = function(){
    	if($('#createForm').form('validate')){
    		var system = new System();
    		system.systemName = $scope.system.systemName;
    		system.systemCode = $scope.system.systemCode;
    		system.systemSecret = $scope.system.systemSecret;
    		system.systemUrl = $scope.system.systemUrl;
    		system.$create(function(){
    			$('#createSystemTemplate').window('close');
    			$scope.reloadTable();
    		});
    	}
    }
	
	$scope.editSystem = function() {
			$('#editForm').form('load',$scope.selectedSystem);
			$('#editSystemTemplate').window('open');
	}

	
	
	$scope.submitEditForm = function(){
		if($('#editForm').form('validate')){
			var system = new System();
			system.systemId = $scope.selectedSystem.systemId;
			system.systemCode = $scope.selectedSystem.systemCode;
			system.systemName = $scope.selectedSystem.systemName;
			system.systemSecret = $scope.selectedSystem.systemSecret;
			system.systemUrl = $scope.selectedSystem.systemUrl;
				system.$update(function(){
	    			$('#editSystemTemplate').window('close');
	    			$scope.reloadTable();
	    		});	
    	}
    }
	
	$scope.removeSystem = function() {
		$('#removeSystemTemplate').window('open');
		
	}
	
	$scope.submitRemoveForm = function(){
			var system = new System();
			system.systemId = $scope.selectedSystem.systemId;
			system.$remove(function(){
				$('#removeSystemTemplate').window('close');
				$scope.reloadTable();
			});
    }
	
	$scope.closeWindow = function(){
		$('#removeSystemTemplate').window('close');
    }
});