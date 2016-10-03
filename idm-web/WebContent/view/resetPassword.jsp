<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, user-scalable=no" />
<link rel="stylesheet" type="text/css"
	href="../vendor/jquery-easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="../vendor/jquery-easyui/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="../vendor/jquery-easyui/themes/color.css">
<link rel="stylesheet" type="text/css" href="../static/css/page.css">
<title>重置密码</title>
</head>
<body class="sgm_sys_page_w">
	<div class="page">
		<header>
			<div id="header">
				<div class="sgmdp_title"></div>
			</div>
		</header>
		<!--Section-->
		<section>
			<!--Content-->
			<div class="content_w">
				<div class="widget-box reset_password">

					<p class="grayredtext" style="padding-left: 120px">${message}</p>
					<form id="resetPasswordForm" name="resetPasswordForm" action=changePassword method="post">
					<input type="hidden" name="retrieveKey" value="${retrieveKey}"/>
						<table>
							<tr>
								<td><div class="itembox" style = "margin-right:100px;">
										<label>新密码</label>
										<input type="password" name="newPassword" class="easyui-textbox" id="newPassword" 
											data-options="required:true,validType:'safepassTwo[$(\'#password\').val()]'" />
									</div></td>
							</tr>
							<tr>
								<td><div class="itembox" style = "margin-right:100px;">
										<label>确认密码</label>
										<input type="password" name="confirmNewPassword" class="easyui-textbox" id="confirmNewPassword" 
											data-options="required:true,validType:'renewpasswordNotDiff[$(\'#newPassword\').val()]'" />
									</div></td>
							</tr>
						</table>
						<p style="padding-left: 120px">密码设置规则：</p>
						<ol style="padding-left: 120px">
							<li>1.长度至少8位；</li>
							<li>2.必须包含大写字母、小写字母、数字；</li>
							<li>3.账号需90天更新一次密码，否则将被锁定；</li>
							<li>4.新密码不得与用户名相近；</li>
							<li>5.新密码不得与原密码以及前五次密码相同；</li>
						</ol>

						<div class="widget-content operating_button">
							<input class="easyui-linkbutton c8" type="button" onclick="javascript:validateForm()" value="确认" style="margin-left: 780px;"/>
						</div>
					</form>
				</div>
			</div>
		</section>
	</div>
	
	<script type="text/javascript" src="../vendor/jquery/jquery.min.js"></script>
	<script type="text/javascript"
		src="../vendor/jquery-easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript"
		src="../vendor/jquery-easyui/easyloader.js"></script>
	<script type="text/javascript"
		src="../vendor/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript"
		src="../common/scripts/easyui-extend-validate.js"></script>	
	<script type="text/javascript">
		function validateForm(){
			var rule = /^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,}$/;
			var newPassword = $('#newPassword').val();
			var comFirmPassword = $('#confirmNewPassword').val();
			if(newPassword!=comFirmPassword){
				alert("新密码和确认密码不一致，请重新输入！");
			}else{
				if(rule.test(newPassword)){
					$('#resetPasswordForm').submit();
				}else{
					alert("密码设置不符合规则，请根据提示输入！");
				}
			}
			/* if($('#resetPasswordForm').form('validate')){
				$('#resetPasswordForm').submit();
			} */
		}
	</script>	
</body>
</html>