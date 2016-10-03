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
<title>找回密码</title>
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
			<!-- mail Content-->
			<div class="content_w">
				<div class="retrieve_password widget-box">
					<p>通过邮件找回密码：</p>
					<br/>
					<ol style="padding-bottom: 10px">
						<li>1.填写个人账号及注册邮箱，点击“下一步”后，重置密码链接将发送到您的邮箱；</li>
						<li>2.登录邮箱通过“重置密码”邮件提供的链接，完成密码重置操作。</li>
					</ol>

					<p class="grayredtext">${message}</p>
					<form name="retrievePasswordForm" action="retrievePassword"
						method="post">
						<table>
							<tr>
								<td><label>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</label>
									<div class="itembox" style="margin-right: 100px;">
										<input type="text" name="userId" class="easyui-textbox"
											data-options="required:true" />
									</div></td>
							</tr>
							<tr>
								<td><label>注册邮箱</label>
									<div class="itembox" style="margin-right: 100px;">
										<input type="text" name="email" class="easyui-textbox"
											data-options="required:true,validType:'email'" />
									</div></td>
							</tr>
						</table>
						<div class="widget-content operating_button">
							<input class="easyui-linkbutton c8" type="submit" value="下一步"
								style="margin-left: 780px;" />
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
</body>
</html>
