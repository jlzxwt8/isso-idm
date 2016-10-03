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
<title>${title}</title>
</head>
<body class="login">
	<div class="page">
		<section>
			<div class="content">
				<!--Login-->
				<div class="loginstyle">
					<div class="cl"></div>
					<div class="login_leftbox"></div>
					<div class="login_bg001"></div>
					<div class="login_midbox">
						<div class="login_midtopbox">

							<div class="sgm_dp_login_content">
								<div class="logout_msg" style="padding-top:70px">
									<span class="bluetext">${message}</span>
								</div>
								<form action="relogin" method="post">
									<input id="client_id" name="client_id" type="hidden"
										value="${client_id}"> <input id="response_type"
										name="response_type" type="hidden" value="${response_type}">
									<input id="redirect_uri" name="redirect_uri" type="hidden"
										value="${redirect_uri}">
									<div class="login_bt02">
										<input type="submit"
											class="easyui-linkbutton c8" value="重新登录" />
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