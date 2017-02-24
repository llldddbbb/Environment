<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Cloud Platform</title>
	<script src="${pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>
	<link href="${pageContext.request.contextPath }/css/bootstrap.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath }/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
	<link href="${pageContext.request.contextPath }/css/environment.css" rel="stylesheet" >
	<link href="${pageContext.request.contextPath }/css/environment_640.css" media="screen and (max-width:640px)" rel="stylesheet"/>
	<script src="${pageContext.request.contextPath }/js/environment.js"></script>
	<script src="${pageContext.request.contextPath }/js/echarts.min.js"></script>
	<script src="${pageContext.request.contextPath }/js/china.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=Um05HtLey0wwsiGAy01LWoQu9KaMAaEq"></script>
	<style type="text/css">
	#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
	.BMap_bubble_title{  
        color:black;  
        font-size:15px;  
        text-align:left;  
    }  
    .BMap_pop div:nth-child(1){  
        border-radius:7px 0 0 0;  
    }  
    .BMap_pop div:nth-child(3){  
        border-radius:0 7px 0 0;
    }  
    .BMap_pop div:nth-child(3) div{  
        border-radius:7px;  
    }  
    .BMap_pop div:nth-child(5){  
        border-radius:0 0 0 7px;  
    }  
    .BMap_pop div:nth-child(5) div{  
        border-radius:7px;  
    }  
    .BMap_pop div:nth-child(7){  
        border-radius:0 0 7px 0 ;  
    }  
    .BMap_pop div:nth-child div(7){  
        border-radius:7px ;  
    }  
    
   
	</style>
<%
if(session.getAttribute("currentAccount")==null){
	response.sendRedirect("login/a.jsp");
}
%>
</head>
<body style="background:#4585d7">
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
	                <li><a href="${pageContext.request.contextPath }/account/changeLanguage.do?language=Chinese&fromPageName=map">中/Eng</a></li>
	                <li><a href="${pageContext.request.contextPath}/city/list.do?cityID=${currentAccount.place}">List</a></li>
	                <li class="map_btn"><a href="map.jsp">Map</a></li>
	                <li><a href="compare.jsp">Compare</a></li>
	                <li><a href="mgmt.jsp">Manage</a></li>
	                <li><a href="${pageContext.request.contextPath}/account/logout.do">Logout</a></li>
	            </ul>
	        </div><!-- /.navbar-collapse -->
	    </div><!-- /.container-fluid -->
	</nav>
		
	<div class="detail-main" style="position:fixed">
		<div style="background:#4585d7; float:left; width:20%; height:100%; display:inline;color:#fff;">
		<div class="places">			
			<c:forEach var="city" items="${cityList }">
			<div style="margin:20px 5px;text-align:center;">
				<a href="javascript:loadGeo('${city.cityName }')" onMouseOver="javascript:loadGeo('${city.cityName }',6)">
				<img style="margin-bottom:5px;" src="../img/cityImg/${city.picture }" width="95%" height="70px" alt="${city.cityName }"/>
				${city.cityName }</a>
			</div>
			</c:forEach>
			
			
		</div>
		</div>
		
		<div style="background:#fff; float:left; width:80%; height:100%; display:inline;">
		<div id="allmap" style="min-height: 600px;height:89%;width: 100%; float:left; overflow:hidden;">
            </div>
		</div>
	</div>

	<script type="text/javascript">
	// 百度地图API功能
	var map = new BMap.Map("allmap");    // 创建Map实例
	map.centerAndZoom(new BMap.Point(116.404, 39.915), 6);  // 初始化地图,设置中心点坐标和地图级别
	map.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_SATELLITE_MAP ]}));   //添加地图类型控件（没有三维）
	map.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
	map.addControl(top_left_navigation);     
	map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
	
	function loadGeo(highlightCityName,zoomLevel){
		var url;
		url='${pageContext.request.contextPath}/city/getCityGeo.do?cityID=${currentAccount.place}'
		map.clearOverlays();
		//异步加载地图数据
		$.post(url,{},function(result){ 
			var result=eval("("+result+")");
			if(result){
				$.each(result, function(i,val){
					var marker;
					var point;
					point = new BMap.Point(val.geo[0],val.geo[1]);
					marker = new BMap.Marker(point);
				    map.addOverlay(marker);
					if(val.name==highlightCityName){
						if(zoomLevel==null){
					    	marker.setAnimation(BMAP_ANIMATION_BOUNCE);
		                    map.centerAndZoom(marker.getPosition(),12);
					    }else{
					    	marker.setAnimation(BMAP_ANIMATION_BOUNCE);
					    	map.centerAndZoom(new BMap.Point(116.404, 39.915), zoomLevel);
					    }
					}
				    marker.addEventListener("mouseover",function(e){
					var opts = {
							width : 320,     // 信息窗口宽度
							height: 420,     // 信息窗口高度
							title : "<div class='mapInfo_title'><div class='mapInfo_title_left'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+val.name+"</div><div class='mapInfo_title_right'>"+val.deviceName_alias+"</div></div>", // 信息窗口标题
							enableMessage:true//设置允许信息窗发送短息
					};
					
					var p = e.target;
					var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
					var content="<div class='mapInfo_content'><div class='mapInfo_content_left' style='padding-top:20px;'><span>"+val.AQIOrAQHI+"</span></div><div class='mapInfo_content_middle'>"+parseFloat(val.AQI_value).toFixed(0)+"</div><div class='mapInfo_content_right'><span>Temp:"+parseFloat(val.Temp).toFixed(0)+"℃<br/>Humi:"+parseFloat(val.Humi).toFixed(0)+"%</span></div></div><table class='map-view'><tr><td >PM<sub>2.5</sub></td><td >"+parseFloat(val.PM25).toFixed(1)+
					"&nbsp;&micro;g/m<sup>3</sup></td></tr><tr><td >CO</td><td >"+parseFloat(val.CO).toFixed(3)+
					"&nbsp;mg/m<sup>3</sup></td></tr><tr><td >PM10</td><td >"+parseFloat(val.PM10).toFixed(1)+
					"&nbsp;&micro;g/m<sup>3</sup></td></tr><tr><td >NO<sub>2</sub></td><td >"+parseFloat(val.NO2).toFixed(1)+
					"&nbsp;&micro;g/m<sup>3</sup></td></tr><tr><td >O<sub>3</sub></td><td >"+parseFloat(val.O3).toFixed(1)+
					"&nbsp;&micro;g/m<sup>3</sup></td></tr><tr><td >SO<sub>2</sub></td><td >"+parseFloat(val.SO2).toFixed(1)+
					"&nbsp;&micro;g/m<sup>3</sup></td></tr><tr><td>VOC</td><td>"+parseFloat(val.VOC).toFixed(1)+
					"&nbsp;ppb</td></tr></table><div class='mapInfo_foot'>Last Update&nbsp;&nbsp;&nbsp;"+val.TimeStamp+"<div>";
					var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
					map.openInfoWindow(infoWindow,point); //开启信息窗口
					});
				    marker.addEventListener("click",function(e){
				    	window.location.href='${pageContext.request.contextPath}/device/findDeviceList.do?deviceName='+val.deviceName+"&cityName="+val.name+"&cityID=${currentAccount.place}&interval=1h&lineNum=30"; 
				    });
				});
			}else{
				alert("Failed to load map!");
			}
		});
		} 
		//调用方法
		loadGeo();
		
	</script>
</body>
</html>