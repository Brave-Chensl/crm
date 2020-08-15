<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(function () {
			if (window.top!=window){
				window.top.location=window.location
			}
			//每次重新加载清空用户名内容
			$("#name").val("")
			//让用户名获得焦点
			$("#name").focus();

			//点击登录
			$("#button").click(function () {
				// 验证用户名密码
				login()
			})
			//回车登录
			$(window).keydown(function (event) {
				 //alert(event)
				if (event.keyCode === 13){
					login()
				}
			})


			function login() {
				//验证用户密码
				var name = $("#name").val().trim()
				var pass = $("#pass").val().trim()
				if (name == "" || pass ==""){
					$("#msg").html("用户名密码不能为空")
					return false;
				}
				$.ajax({
					url:"user/login.do",
					data:{
						"name":name,
						"pass":pass
					},
					type:"post",
					dataType:"json",
					success:function (resp) {
						if (resp.success){
							//alert("登录")
							 $("#submit").submit();
						}else {
							$("#msg").html(resp.msg)
						}
					}
				})
			}
		})
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;陈省龙</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form id="submit" action="workbench/index.jsp" class="form-horizontal" role="form" method="post">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input id="name" name="name" class="form-control" type="text" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input id="pass" name="pass" class="form-control" type="password" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<button id="button" type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>