<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
	<script src="js/environment.js"></script>
	<script type="text/javascript">
		function changeShowAQI(index){
			var showAQIId="showAQI"+index;
			var showAQI_valueId="showAQI_value"+index;
			var showAQI=$("#" + showAQIId + "").val();
			if(showAQI=='AQI'){
				$("#" + showAQIId + "").val("AQHI");
				$("#" + showAQI_valueId + "").val("AQHI");
			}else{
				$("#" + showAQIId + "").val("AQI");
				$("#" + showAQI_valueId + "").val("AQI");
			}
		}
	</script>
</head>
<body>
	<nav class="navbar navbar-default navbar-fixed-top header" role="navigation">
	    <div class="container-fluid ">
	        <!-- Brand and toggle get grouped for better mobile display -->
	        <div class="navbar-header ">
	            <button type="button" class="navbar-toggle collapsed " style="position: absolute;top: 10px;right: 0px;" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
	                <span class="sr-only">Toggle navigation</span>
	                <span class="icon-bar" style="background-color:#fff"></span>
	                <span class="icon-bar" style="background-color:#fff"></span>
	                <span class="icon-bar" style="background-color:#fff"></span>
	            </button>
	            <div class="logo">
	                <span>Sapiens Environmental IoT Network Cloud (SEIN)<br/>环境物联网云平台</span>
	            </div>
	        </div>
	        <div class="collapse navbar-collapse collapse_list" id="bs-example-navbar-collapse-1">
	            <ul class="nav navbar-nav navbar-right menu">
	                <li><a href="${pageContext.request.contextPath }/account/changeLanguage.do?language=English&fromPageName=mgmt">中/Eng</a></li>
	                <li><a href="${pageContext.request.contextPath}/city/list.do?cityID=${currentAccount.place}">列表</a></li>
	                <li class="map_btn"><a href="map.jsp">地图</a></li>
	                <li><a href="compare.jsp">比较</a></li>
	                <li><a href="mgmt.jsp">管理</a></li>
	                <li><a href="${pageContext.request.contextPath}/account/logout.do">注销</a></li>
	            </ul>
	        </div><!-- /.navbar-collapse -->
	    </div><!-- /.container-fluid -->
	</nav>

	<div class="content">
			<div class="content-main">
				<div class="citylist">
				<c:forEach var="city" items="${cityList }" varStatus="status">
					<div class="cities">
						<span>
						<img src="img/cityImg/${city.picture }" alt=""/>
							<h3>${city.cityName }</h3>
							<div class="city-bottom" id="city-bottom_mgmt">
							<form id="fm" method="post" action="${pageContext.request.contextPath }/city/updateCity.do" enctype="multipart/form-data">
							<div class="city-latlng" >
							<label style="margin-right:5px;">地点</label><input type="text" id="cityName" name="cityName" value="${city.cityName }"/>
							<label style="margin-right:5px;">纬度</label><input type="text" id="lat" name="lat" value="${city.lat }"/>
							<label style="margin:0 5px;">经度</label><input type="text" id="lng" name="lng" value="${city.lng }"/>
							<input id="hiddenFile${status.index }" type='file' name="file" style="display: none;"/>
							<input  type='text' name="cityID" value="${city.cityID }" style="display: none;"/>
							<input  type='text' name="deviceInfo.Item" value="${city.deviceInfo.getItem()}" style="display: none;"/>
							<input type="button"  value="图片" style="folat:right;" onclick="javascript:$('#hiddenFile${status.index }').click()"/>
							<input type="button" id="showAQI${status.index }" value="${city.showAQI}" onclick="changeShowAQI(${status.index })" style="folat:right;"/>
							<input type="hidden" name="showAQI" id="showAQI_value${status.index }" value="${city.showAQI}"/>
							<input type="submit"  value="修改" style="folat:right;"/>
							</div>
							</form>
							</div>
						</span>
					</div>
				</c:forEach>
				</div>
			</div>
	</div>


</body>
</html>