<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	<script src="${pageContext.request.contextPath }/js/bootstrap-datepicker.js"></script>
	<script src="${pageContext.request.contextPath }/js/bootstrap-datepicker.zh-CN.js"></script>
	<script src="${pageContext.request.contextPath }/js/echarts.min.js"></script>
	<link href='${pageContext.request.contextPath }/css/bootstrap-datetimepicker.css' rel='stylesheet'>
    <link href='${pageContext.request.contextPath }/css/bootstrap-datetimepicker.min.css' rel='stylesheet'>
	<script src="${pageContext.request.contextPath }/js/bootstrap-datetimepicker.js"></script>
	<script src="${pageContext.request.contextPath }/js/bootstrap-datetimepicker.fr.js"></script>
	<script src="${pageContext.request.contextPath }/js/bootstrap-datetimepicker.min.js"></script>
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
	        //定义两个变量time1,time2分别保存开始和结束时间
	        /* var time1=$("#lastUpdateTime").html();
	        var start= new Date(Date.parse(time1.replace(/-/g,"/")));
	        var end=new Date();
	        var diff=end.getTime() - start.getTime();//时间差的毫秒数  
	        var minutes=Math.floor(diff/(60*1000)); 
	        if(minutes>90){
	        	$("#status").html('离线');
	        	$("#status").css("color","#FF0000");
	        }else{
	        	$("#status").html('在线');
	        	$("#status").css("color","green");
	        }  */
		});
		function showcharts(){
			$(".detail-left").css("display","none");
			$(".detail-right").css("display","block");
			$(".detail-right").css("width","100%");
		}
	</script>
<%
if(session.getAttribute("currentAccount")==null){
	response.sendRedirect("login/a.jsp");
}
%>
</head>
<body style="background:#4585d7;overflow: hidden">
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
	                <li><a href="${pageContext.request.contextPath }/account/changeLanguage.do?language=Chinese&fromPageName=detail">中/Eng</a></li>
	                <li><a href="${pageContext.request.contextPath}/city/list.do?cityID=${currentAccount.place}">List</a></li>
	                <li class="map_btn"><a href="map.jsp">Map</a></li>
	                <li><a href="compare.jsp">Compare</a></li>
	                <li><a href="mgmt.jsp">Manage</a></li>
	                <li><a href="${pageContext.request.contextPath}/account/logout.do">Logout</a></li>
	            </ul>
	        </div><!-- /.navbar-collapse -->
	    </div><!-- /.container-fluid -->
	</nav>
	<div class="detail-main">
		<div class="detail-left">
			
			<div class="detail-left_title" >
				<div class="title_content">${cityName}&nbsp;&nbsp;&nbsp;&nbsp;${deviceName_alias }</div>
			</div>
			<div class="detail-left_content" >
				<div class="content_top">
					<div class="content_top_left"><span>${AQIOrAQHI}</span></div>
					<div class="content_top_right"><span>Last Update:<br><span><fmt:formatDate value="${device.getTimeStamp()}" type="Date" pattern="yyyy-MM-dd HH:mm"/></span></span><br/>
					<c:choose>
						<c:when test="${device.getStatus()=='OFFLINE' }">
							<span  style="color:#FF0000">${device.getStatus() }</span>
						</c:when>
						<c:otherwise>
							<span  style="color:green">${device.getStatus() }</span>
						</c:otherwise>
					</c:choose>
					</div>
				</div>
				<div class="content_middle"><span class="AQI"><fmt:formatNumber type="number" value="${AQI_value}" pattern="0" maxFractionDigits="0"/></span></div>
				<div class="content_foot">
					<span style="font-size: 21px;margin-left: 35px">Temp:<fmt:formatNumber type="number" value="${device.getTemp()}" pattern="0" maxFractionDigits="0"/>℃</span>&nbsp;&nbsp;&nbsp;&nbsp;
					<span style="font-size: 21px;">RH:<fmt:formatNumber type="number" value="${device.getHumi() }" pattern="0" maxFractionDigits="0"/>%</span>
					<table >
						<tr>
							<td>PM<b><sub>2.5</sub></b></td><td><fmt:formatNumber type="number" value="${device.getPM25_ug() }" pattern="0.0" maxFractionDigits="1"/>&nbsp;&micro;g/m<b><sup>3</sup></b></td>
						</tr>
						<tr>
							<td>PM<b><sub>10</sub></b></td><td><fmt:formatNumber type="number" value="${device.getPM10_ug() }" pattern="0.0" maxFractionDigits="1"/>&nbsp;&micro;g/m<b><sup>3</sup></b></td>
						</tr>
						<tr>
							<td>CO</td><td><fmt:formatNumber type="number" value="${device.getCO_mg() }" pattern="0.000" maxFractionDigits="3"/>&nbsp;mg/m<b><sup>3</sup></b></td>
						</tr>
						<tr>
							<td>NO<b><sub>2</sub></b></td><td><fmt:formatNumber type="number" value="${device.getNO2_ug() }" pattern="0.0" maxFractionDigits="1"/>&nbsp;&micro;g/m<b><sup>3</sup></b></td>
						</tr>
						<tr>
							<td>SO<b><sub>2</sub></b></td><td><fmt:formatNumber type="number" value="${device.getSO2_ug() }" pattern="0.0" maxFractionDigits="1"/>&nbsp;&micro;g/m<b><sup>3</sup></b></td>
						</tr>
						<tr>
							<td>O<b><sub>3</sub></b></td><td><fmt:formatNumber type="number" value="${device.getO3_ug() }" pattern="0.0" maxFractionDigits="1"/>&nbsp;&micro;g/m<b><sup>3</sup></b></td>
						</tr>
					</table>
				</div>
			</div>
			<a class="showcharts"  href="javascript:showcharts()">Graph</a>	
			<a class="backhome" href="${pageContext.request.contextPath}/city/list.do?cityID=${currentAccount.place}">Back</a>	
		</div>
		<div class="detail-right" id="detail_detail_right">
			<table >
			<tr>
			<th>Pollutant</th>
			<th>Interval</th>
			<th>Start Time</th>
			<th>End Time</th>
			<th>Graph</th>
			</tr>
			<tr>
			<td>
			<select name="airQulityType" id="airQulityType">
				<option value="PM25_ug">PM<sub>2.5</sub></option>
				<option value="PM10_ug">PM10</option>
				<option value="CO_mg">CO</option>
				<option value="O3_ug">O₃</option>
				<option value="SO2_ug">SO₂</option>
				<option value="NO2_ug">NO₂</option>
				<option value="Temp">Temp</option>
				<option value="Humi">Humi</option>
			</select>
			</td>
			<td>
			<select name="timepicker" id="timepicker">
			<c:choose>
				<c:when test="${deviceName=='dev9100d161200007'||deviceName=='dev9100d161200008'||deviceName=='dev9100d170100024'||deviceName=='dev9100d170100027'}">
					<option value="15min">15min</option>
					<option value="1h" selected>1h</option>
					<option value="1d">1d</option>
				</c:when>
				<c:otherwise>
					<option value="1min">1min</option>
					<option value="10min">10min</option>
					<option value="1h" selected>1h</option>
					<option value="1d">1d</option>
				</c:otherwise>
			</c:choose>
			</select>
			</td>
			<td>
			<input type="text" id="s_TimeStamp"  value="${s_TimeStamp==null?'2016-8-15 00:00':s_TimeStamp}">
			</td>
			<td>
			<input type="text" id="e_TimeStamp"  value="${e_TimeStamp}">
			</td>
			<td>
			<select name="chartstype" id="chartstype">
				<option value="line">line</option>
				<option value="bar">bar</option>
			</select>
			</td>
			<td ><a id="detail-submit" href="javascript:getChartsData()">Select</a></td>
			<td ><a style="margin-left:20px" id="detail-submit" href='${pageContext.request.contextPath}/device/findDeviceList.do?deviceName=${deviceName}&cityName=${cityName }&cityID=${currentAccount.place}&interval=1h&lineNum=30'>Data</a></td>
			</tr>
			</table>	
			<div style="width:100%;">
			<a id="detail-submit" class="detail-submit_mobile" href="javascript:getChartsData()">Select</a>
			</div>		
			<div id="main" class="detail_detail_main"></div>
		</div>
	</div>
<script type="text/javascript">
		//获取图表实例
var myChart = echarts.init(document.getElementById('main'));
       
//动态改变图表数据的方法
function getChartsData(){
    var chartstype=$("#chartstype").val();
	var airQulityType=$("#airQulityType").val();
	var interval=$("#timepicker").val();
	var s_TimeStamp=$("#s_TimeStamp").val();
	var e_TimeStamp=$("#e_TimeStamp").val();
	var unit;
	if(airQulityType=="PM25_ug"){
		unit="μg/m³";
	}else if(airQulityType=='CO_mg'){
		unit="mg/m³";
	}else if(airQulityType=='NO_ug'||airQulityType=='NO2_ug'||airQulityType=='O3_ug'||airQulityType=='SO2_ug'||airQulityType=='PM10_ug'){
		unit="μg/m³";
	}else if(airQulityType=="Temp"){
		unit="°C";
	}else{
		unit="%";
	}
	myChart.clear();
	myChart.showLoading(); 
	$.post("${pageContext.request.contextPath}/device/getChartsData.do?deviceName=${deviceName}",{"airQulityType":airQulityType,"interval":interval,"s_TimeStamp":s_TimeStamp,"e_TimeStamp":e_TimeStamp},function(result){
		var result=eval("("+result+")");
		if(airQulityType=="PM25_ug"){
			airQulityType="PM2.5_ug"
		}
		if(result){
		     var date=[];   
		     var data=[];   
		    	 for(i in result){
	           		   date.push(i);
	           		   data.push(result[i]);
	           	   }
                myChart.hideLoading();
                myChart.setOption({ 
                	tooltip: {
            	        trigger: 'axis',
            	        position: function (pt) {
            	            return [pt[0], '10%'];
            	        },
            	        formatter: '{b0}<br/>'+airQulityType.split("_")[0]+' : {c0} '+unit
            	    },
                
                    yAxis: {
                     	name: "unit("+unit+")",
                     	type: 'value',
                        boundaryGap: [0, '30%']
                    },
                    toolbox: {
                        feature: {
                            restore: {},
                            saveAsImage: {}
                        }
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
	                 series: [
	                          {
	                        	  name: 'PM25_ug',
	                              type: chartstype,
	                              smooth:true,
	                              symbol: 'none',
	                              sampling: 'average',
	                              itemStyle: {
	                                  normal: {
	                                      color: 'rgb(255, 70, 131)'
	                                  }
	                              },
	                              areaStyle: {
	                                  normal: {
	                                      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
	                                          offset: 0,
	                                          color: 'rgb(255, 158, 68)'
	                                      }, {
	                                          offset: 1,
	                                          color: 'rgb(255, 70, 131)'
	                                      }])
	                                  }
	                              },
	                              data: data
	                          }
	                      ]
                });
			 
		}else{
			alert("Request data failed!");
	        myChart.hideLoading();
		}
	});
	
}
getChartsData();

</script>


	
</body>
</html>