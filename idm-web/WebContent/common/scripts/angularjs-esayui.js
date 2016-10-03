var esayuiModule = angular.module('common.ui.esayui', []);

esayuiModule.directive('aePanel', function() {
	return {
		restrict : 'EA',
		replace : true,
		link : function(scope, elm, attr) {
			$(elm).panel({});
		}
	};
});

esayuiModule.directive('aeLayout', function() {
	return {
		restrict : 'EA',
		replace : true,
		link : function(scope, elm, attr) {
			$(elm).layout({});
		}
	};
});

esayuiModule.directive('aeTabs', function() {
	return {
		restrict : 'EA',
		replace : true,
		link : function(scope, elm, attr) {
			$(elm).tabs({});
		}
	};
});

esayuiModule.directive('aeWindow', function() {
	return {
		restrict : 'EA',
		replace : true,
		link : function(scope, elm, attr) {
			$(elm).window({});
		}
	};
});

esayuiModule.directive('aeDialog', function() {
	return {
		restrict : 'EA',
		replace : true,
		link : function(scope, elm, attr) {
			$(elm).dialog({});
		}
	};
});

esayuiModule.directive('aeDatagrid', function() {
	return {
		restrict : 'EA',
		replace : true,
		scope : {
			onSelectFunction : '=',
			onUnselectFunction : '='
		},
		link : function(scope, elm, attr) {
			$(elm).datagrid({
				onSelect : function(index, row) {
					scope.$apply(function() {
						if (scope.onSelectFunction != null) {
							scope.onSelectFunction(index, row);
						}
					});
				},
				onUnselect : function(index, row) {
					scope.$apply(function() {
						if (scope.onSelectFunction != null) {
							scope.onSelectFunction(index, row);
						}
					});
				}
			});
		}
	};
});

esayuiModule.directive('aeTree', function() {
	return {
		restrict : 'EA',
		replace : true,
		scope : {
			onSelectFunction : '='
		},
		link : function(scope, elm, attr) {
			$(elm).tree({
				onSelect : function(node) {
					scope.$apply(function() {
						if (scope.onSelectFunction != null) {
							scope.onSelectFunction(node);
						}
					});
				}
			});
		}
	};
});

esayuiModule.directive('aeLinkbutton', function(permissionService) {
	return {
		restrict : 'EA',
		replace : true,
		scope : {
			onClickFunction : '=',
			permissions : '@'
		},
		link : function(scope, elm, attr) {
			if (scope.permissions) {
				var permissionArray = scope.permissions.split(",");
				var isCheck = false;
				for (var i = 0; i < permissionArray.length; i++) {
					isCheck = permissionService.check(permissionArray[i]);
					if (isCheck) {
						break;
					}
				}
				if (isCheck) {
					$(elm).linkbutton({
						onClick : function() {
							if (scope.onClickFunction != null) {
								scope.onClickFunction();
							}
						}
					});
				} else {
					elm.remove();
					elm = null;
				}
			} else {
				$(elm).linkbutton({
					onClick : function() {
						if (scope.onClickFunction != null) {
							scope.onClickFunction();
						}
					}
				});
			}
		}
	};
});

esayuiModule.directive('aeTextbox', function() {
	return {
		restrict : 'EA',
		replace : true,
		scope : {
			textboxValue : '=',
			onClickButtonFunction : '='
		},
		link : function(scope, elm, attr) {
			$(elm).textbox({
				value : scope.textboxValue,
				onChange : function(newValue, oldValue) {
					scope.$apply(function() {
						scope.textboxValue = newValue;
					});
				},
				onClickButton : function() {
					if (scope.onClickButtonFunction != null) {
						scope.onClickButtonFunction();
					}
				}
			});
		}
	};
});

esayuiModule.directive('aeDatebox', function() {
	return {
		restrict : 'EA',
		replace : true,
		scope : {
			dateboxValue : '='
		},
		link : function(scope, elm, attr) {
			$(elm).datebox({
				onSelect : function(date) {
					scope.$apply(function() {
						scope.dateboxValue = date;
					});
				}
			});
			$(elm).datebox('setValue', scope.dateboxValue);
		}
	};
});

esayuiModule.directive('aeCombobox', function() {
	return {
		restrict : 'EA',
		replace : true,
		scope : {
			comboboxValue : '='
		},
		link : function(scope, elm, attr) {
			$(elm).combobox({
				onSelect : function(record) {
					scope.$apply(function() {
						scope.comboboxValue = record.id;
					});
				}
			});
			$(elm).combobox('setValue', scope.comboboxValue);
		}
	};
});

esayuiModule.directive('aeFilebox', function() {
	return {
		restrict : 'EA',
		replace : true,
		link : function(scope, elm, attr) {
			$(elm).filebox({});
		}
	};
});
