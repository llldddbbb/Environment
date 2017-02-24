<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Cloud Platform</title>
	<script src="${pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>
	<link href="${pageContext.request.contextPath }/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/css/environment.css">
	<link href="${pageContext.request.contextPath }/css/environment_640.css" media="screen and (max-width:640px)" rel="stylesheet"/>
	<script src="${pageContext.request.contextPath }/js/environment.js"></script>
	<style>
		body{
			background: url(${pageContext.request.contextPath }/img/cityImg/img1.jpg) no-repeat fixed;
		}
	</style>
	<script type="text/javascript">
	function login(){
		var username=$("#username").val();
		var password=$("#password").val();
		if(username==''||username==null){
			alert("account cannot be empty!");
			return;
		}
		if(password==''||password==null){
			alert("password cannot be empty!");
			return;
		}
		$.post("${pageContext.request.contextPath}/account/login.do",{username:username,password:password},function(result){
			var result=eval("("+result+")");
			if(result.success){
				window.location.href="${pageContext.request.contextPath}/city/list.do?view=list&cityID="+result.cityID;
			}else{
				alert(result.error);
				return;
			}
		});
	}
	</script>
</head>
<body>
	 <div class="login_nav">
        <a href="${pageContext.request.contextPath}/account/login_vistors.do">For Vistors</a>
        <a href="${pageContext.request.contextPath }/account/changeLanguage.do?language=Chinese&fromPageName=en/login/b">中 / Eng</a>
    </div><!--导航区结束-->
    <div class="login_main">
        <h2>Sapiens Environmental IoT Network (SEIN)</h2>
        <h2>Cloud Platform</h2>
         <h2>环境物联网云平台</h2>
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <h2>ACCOUNT</h2>
                    <input type="text" id="username" name="username" />
                </div>
                <div class="col-md-6">
                    <h2>PASSWORD</h2>
                    <input type="password" id="password" name="password"/>
                </div>
            </div>
        </div><!--输入框结束-->
        <div class="login_btn">
            <h2>LOGIN</h2>
            <a href="javascript:login()"></a>
        </div>
    </div><!--登录模块结束-->
<div class="copyright">
    <h2>2016 © Environmental Technology Co Limited. ALL Rights Reserved.</h2>
</div><!--底部版权区-->
		
</body>
</html>