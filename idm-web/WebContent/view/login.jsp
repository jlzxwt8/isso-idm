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
<title>登入页面</title>
</head>
<body class="login">
	<div class="page">
		<section>
			<div class="content">
				<!--Login-->
				<div class="loginstyle">
					<div class="sgmdp_title">认证服务管理平台</div>
					<div class="cl"></div>
					<div class="login_leftbox"></div>
					<div class="login_bg001"></div>
					<div class="login_midbox">
						<div class="login_midtopbox">
							
							<div class="sgm_dp_login_content">
								<form name="loginform" action="" method="post" novalidate>
									<div class="login_infobox">
										<div class="redtext">${message}</div>
										<input type="hidden" name="uuid" value="${uuid}"/>
										<div class="sgm_username">
											<input id="userid" name="userid" class="easyui-textbox"
												style="width: 220px; height: 28px; padding-left: 4px;"
												data-options="prompt:'用户名',iconCls:'icon-man',iconWidth:38"
												type="text" />
										</div>
										<div class="sgm_password">
											<input id="password" class="easyui-textbox" type="password"
												style="width: 220px; height: 28px; padding-left: 4px;"
												data-options="prompt:'密码',iconCls:'icon-lock',iconWidth:38"
												name="password" type="password" />
										</div>
									</div>
									<div class="cl"></div>
									<div class="login_msg">
										<span class="graybluetext"><a href="forgetPassword"
											target="_blank">忘记密码？</a></span>
									</div>
									<div class="login_bt01">
										<input class="easyui-linkbutton c8" type="submit"
											value="登&nbsp录" />
									</div>
								</form>
							</div>
						</div>
						<div class="login_midbottombox"></div>
					</div>
					<div class="login_bg003"></div>
					<div class="login_rightbox"></div>
					<div class="cl"></div>
				</div>
			</div>
		</section>
		<!-- Footer -->
	</div>
	<script type="text/javascript" src="../vendor/jquery/jquery.min.js"></script>
	<script type="text/javascript"
		src="../vendor/jquery-easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript"
		src="../vendor/jquery-easyui/easyloader.js"></script>
	<script type="text/javascript"
		src="../vendor/jquery-easyui/locale/easyui-lang-zh_CN.js"></script>
</body>
</html>