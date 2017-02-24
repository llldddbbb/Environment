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
	<link href="css/bootstrap.min.css" rel="stylesheet">
    <link href='css/bootstrap-datepicker.css' rel='stylesheet'>
	<link rel="stylesheet" href="css/environment.css">
	<link href='css/bootstrap-datetimepicker.css' rel='stylesheet'>
    <link href='css/bootstrap-datetimepicker.min.css' rel='stylesheet'>
    <link href="${pageContext.request.contextPath }/css/environment_640.css" media="screen and (max-width:640px)" rel="stylesheet"/>
	<script src="js/jquery-1.9.1.min.js"></script>
	<script src="js/environment.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/bootstrap-datepicker.js"></script>
	<script src="js/bootstrap-datepicker.zh-CN.js"></script>
	<script src="js/echarts.min.js"></script>
	<script src="js/bootstrap-datetimepicker.js"></script>
	<script src="js/bootstrap-datetimepicker.fr.js"></script>
	<script src="js/bootstrap-datetimepicker.min.js"></script>
	<script type="text/javascript">
	$(function(){
		
		
		$('.dateControl').datepicker();
		
		$('#timepicker').change(function(event) {
		});
		
		$('#s_TimeStamp').datetimepicker({
            language: 'zh-CN',	            
            autoclose: true,//选中之后自动隐藏日期选择框
            clearBtn: true,//清除按钮
            todayBtn: true,//今日按钮
            todayHighlight: true
        });
        $('#e_TimeStamp').datetimepicker({
        	language: 'zh-CN',
            autoclose: true,//选中之后自动隐藏日期选择框
            clearBtn: true,//清除按钮
            todayBtn: true,//今日按钮
            todayHighlight: true
        });
	})
	</script>
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
	                <li><a href="${pageContext.request.contextPath }/account/changeLanguage.do?language=English&fromPageName=compare">中/Eng</a></li>
	                <li><a href="${pageContext.request.contextPath}/city/list.do?cityID=${currentAccount.place}">列表</a></li>
	                <li class="map_btn"><a href="map.jsp">地图</a></li>
	                <li><a href="compare.jsp">比较</a></li>
	                <li><a href="mgmt.jsp">管理</a></li>
	                <li><a href="${pageContext.request.contextPath}/account/logout.do">注销</a></li>
	            </ul>
	        </div><!-- /.navbar-collapse -->
	    </div><!-- /.container-fluid -->
	</nav>
		
	<div class="detail-main" style="position:fixed;">
		<div class="compare_left" >
		<div class="places">			
			<c:forEach var="city" items="${cityList }">
			<div style="margin:20px 5px;text-align:center;">
				<a href="#">
				<img style="margin-bottom:5px;" src="img/cityImg/${city.picture }" width="95%" height="70px" alt="${city.cityName }"/>
				${city.cityName }</a>
			</div>
			</c:forEach>
		</div>
		</div><!-- left结束 -->
		
		<div class="detail-right" id="compare_detail_right" >
		
			<table >
			<tr>
			<th>污染物</th>
			<th>时间间隔</th>
			<th>开始时间</th>
			<th>结束时间</th>
			</tr>
			<tr>
			<td>
			<c:choose>
				<c:when test="${deviceName=='dev9002a170100001'||deviceName=='dev9002a170100002'||deviceName=='dev9002a170100003'||deviceName=='dev9002a170100004'||deviceName=='dev9002a170100005'||deviceName=='dev9002a170100006'}">
					<select name="airQulityType" id="airQulityType">
						<option value="PM25_ug">PM<sub>2.5</sub></option>
						<option value="CO_mg">CO</option>
						<option value="CO2_mg">CO₂</option>
						<option value="NO_ug">NO</option>
						<option value="NO2_ug">NO₂</option>
						<option value="Temp">温度</option>
						<option value="Humi">湿度</option>
					</select>
				</c:when>
				<c:otherwise>
					<select name="airQulityType" id="airQulityType">
					<option value="PM25_ug">PM<sub>2.5</sub></option>
					<option value="PM10_ug">PM10</option>
					<option value="CO_mg">CO</option>
					<option value="O3_ug">O₃</option>
					<option value="SO2_ug">SO₂</option>
					<option value="NO2_ug">NO₂</option>
					<option value="Temp">温度</option>
					<option value="Humi">湿度</option>
				</select>
				</c:otherwise>
			</c:choose>
			</td>
			<td>
			<select name="timepicker" id="timepicker">
			<c:choose>
				<c:when test="${deviceName=='dev9100d161200007'||deviceName=='dev9100d161200008'||deviceName=='dev9100d170100024'||deviceName=='dev9100d170100027'}">
					<option value="15min">15分钟</option>
					<option value="1h" selected>1小时</option>
					<option value="1d">1天</option>
				</c:when>
				<c:otherwise>
					<option value="1min">1分钟</option>
					<option value="10min">10分钟</option>
					<option value="1h" selected>1小时</option>
					<option value="1d">1天</option>
				</c:otherwise>
			</c:choose>
			</select>
			</td>
			<td>
			<input type="text" id="s_TimeStamp"   value="${s_TimeStamp==null?'2016-8-15 00:00':s_TimeStamp}">
			</td>
			<td>
			<input type="text" id="e_TimeStamp"   value="${e_TimeStamp}">
			</td>
			<td>
			</td>
			<td ><a id="detail-submit" href="javascript:getAllDevice()">提交</a></td>
			</tr>
			</table>	
			<div style="width:100%"><a id="detail-submit" class="detail-submit_mobile" href="javascript:getAllDevice()">提交</a></div>					
			<div id="main" class="compare_main"></div>
		</div>
	</div>
<script type="text/javascript">
		//获取图表实例
        var myChart = echarts.init(document.getElementById('main'));
        //初始化图表数据，异步加载PM25数据
       
function getAllDevice(){
		myChart.clear();
		myChart.showLoading(); 
        var series=[];
        var cityName=[];
        var index=0;
        var date=[];
		var color=["rgb(255, 70, 131)","#B0E11E","#0080FF"]
		var airQulityType=$("#airQulityType").val();
		var interval=$("#timepicker").val();
		var s_TimeStamp=$("#s_TimeStamp").val();
		var e_TimeStamp=$("#e_TimeStamp").val();
		var unit;
		if(airQulityType=="PM25_ug"){
			unit="μg/m³";
		}else if(airQulityType=='CO_mg'){
			unit="mg/m³";
		}else if(airQulityType=='NO_ug'||airQulityType=='NO2_ug'||airQulityType=='O3_ug'||airQulityType=='SO2_ug'){
			unit="μg/m³";
		}else if(airQulityType=="Temp"){
			unit="°C";
		}else{
			unit="%";
		}
        $.ajax({type : "post",async : true,url : "${pageContext.request.contextPath}/device/getAllDevice.do?cityID=${currentAccount.place}", data : {"airQulityType":airQulityType,"interval":interval,"s_TimeStamp":s_TimeStamp,"e_TimeStamp":e_TimeStamp},dataType : "json",        //返回数据形式为json
        success : function(result) {
            if (result) {
            	for(j in result){
			        var data=[];
			        var date_compa=[];
			        cityName.push(j);
            	   for(i in result[j]){
            		   date_compa.push(i);
            		   data.push(result[j][i]);
            	   }
            	   if(date_compa.length>date.length){
            		   date=date_compa;
            	   }
            	  
            		var city={
            				name:j,
                            type:'line',
                            smooth:true,
                            symbol: 'none',
                            sampling: 'average',
                            itemStyle: {
                                normal: {
                                    color:color[index],
                                }
                            },
                            lineStyle:{
                            	normal: {
                                    color:color[index],
                                }
                            },
                            data: data
                      };
            		series.push(city);
            		index=index+1;
            	}
                 myChart.hideLoading();
            	 myChart.setOption({
              	   tooltip: {
              	        trigger: 'axis',
              	        position: function (pt) {
              	            return [pt[0], '10%'];
              	        },
              	      
              	    },
	              	  legend: {
	                      data:cityName
	                  },
                     toolbox: {
                         feature: {
                             restore: {},
                             saveAsImage: {}
                         }
                     },
                     yAxis: {
                    		name: "单位("+unit+")",
	                       	type: 'value',
	                        boundaryGap: [0, '30%']
                     },
		               	 xAxis: {
		                     type: 'category',
		                     boundaryGap: false,
		                     data: date
		                 },
		                 dataZoom: [{
		                     type: 'inside',
		                     start: 0,
		                     end: 100
		                 }, {
		                     start: 0,
		                     end: 10,
		                     handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
		                     handleSize: '80%',
		                     handleStyle: {
		                         color: '#fff',
		                         shadowBlur: 3,
		                         shadowColor: 'rgba(0, 0, 0, 0.6)',
		                         shadowOffsetX: 2,
		                         shadowOffsetY: 2
		                     }
		                 }],
		                 series: series
                 });
          	}
       },
        error : function(errorMsg) {
      	  alert("图表请求数据失败!");
        	myChart.hideLoading();
        }
   });
}
getAllDevice();
</script>
	
</body>
</html>