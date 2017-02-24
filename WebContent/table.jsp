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
	<link href="${pageContext.request.contextPath }/css/bootstrap.css" rel="stylesheet">
    <link href='${pageContext.request.contextPath }/css/bootstrap-datetimepicker.css' rel='stylesheet'>
    <link href='${pageContext.request.contextPath }/css/bootstrap-datetimepicker.min.css' rel='stylesheet'>
	<link rel="stylesheet" href="${pageContext.request.contextPath }/css/environment.css">
	<script src="${pageContext.request.contextPath }/js/jquery-1.9.1.min.js"></script>
	<script src="${pageContext.request.contextPath }/js/environment.js"></script>
	<script src="${pageContext.request.contextPath }/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath }/js/bootstrap-datetimepicker.js"></script>
	<script src="${pageContext.request.contextPath }/js/bootstrap-datetimepicker.fr.js"></script>
	<script src="${pageContext.request.contextPath }/js/bootstrap-datetimepicker.min.js"></script>
	<script src="${pageContext.request.contextPath }/js/bootstrap-datepicker.zh-CN.js"></script>
	<script src="${pageContext.request.contextPath }/js/echarts.min.js"></script>
	<script>
		$(function(){
			var now = new Date();
			$('.dateControl').datetimepicker();
			
			$('#timepicker').change(function(event) {
				if ($(this).val()=="custom") {
					$('.dateControl').show();
				}else{
					$('.dateControl').hide();
				}
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
			
			var airQualityType='${airQulityType}'.split(",");
			if('${airQulityType}'==null){
				$("[name='airQulityType']").attr("checked",'true');
			}
			for(var i in airQualityType){
				$('input[name="airQulityType"]').each(function(){
				    if($(this).val()==airQualityType[i].split("_")[0]){
				    	 $(this).attr("checked",'true');
				    }else{
				    	if(airQualityType[i]=="`PM2.5_ug`"){
							$("#PM25_ug").attr("checked",'true');
						}
				    	if(airQualityType[i]=="PM10_ug"){
							$("#PM10_ug").attr("checked",'true');
						}
				    }
					
				});  
			}
			
			if('${unit}'=='ppbm'){
				$("#ppbm").attr("checked",'true');
			}else{
				$("#umgm").attr("checked",'true');
			}
			
		});
		
		
		function findDeviceTable(exportExcel){
			var place= $('#place').val().split("-")[0];
			var cityName=$('#place').val().split("-")[1];
			var interval= $('#timepicker').val();
			var lineNum=$("#lineNum").val();
			if(lineNum<=0||lineNum>50){
				alert("每页行数限制为1-50，请重新输入");
				return ;
			}
			var unit=$('input[name="standard"]:checked ').val();
			var chk_value =[];
			chk_value.push("TimeStamp");
			$('input[name="airQulityType"]:checked').each(function(){
				if(unit=='ppbm'){
					if($(this).val()=="NO"||$(this).val()=="NO2"||$(this).val()=="O3"||$(this).val()=="SO2"){
					    chk_value.push($(this).val()+"_ppb");
					}else if($(this).val()=="CO"){
						 chk_value.push($(this).val()+"_ppm");
					}else if($(this).val()=="PM10"){
						chk_value.push($(this).val()+"_ug");
					}else{
					
						chk_value.push($(this).val());
					}
				}else{
					if($(this).val()=="NO"||$(this).val()=="NO2"||$(this).val()=="O3"||$(this).val()=="SO2"||$(this).val()=="PM10"){
					    chk_value.push($(this).val()+"_ug");
					}else if($(this).val()=="CO"){
						 chk_value.push($(this).val()+"_mg");
					}else{
						chk_value.push($(this).val());
					}
				}
			});    
			if(chk_value.length==1){
				alert("请选择污染物类别");
				return;
			}
			var airQulityType=chk_value.join(",");
			
			var s_TimeStamp=$("#s_TimeStamp").val();
			var e_TimeStamp=$("#e_TimeStamp").val();
			
			var alermURL=''; 
			if($("#PM25_alerm").val()!=''){
				if(isNaN($("#PM25_alerm").val())){
					alert("请输入合法数字");
					return;
				}
				alermURL=alermURL+'&PM25_alerm='+$("#PM25_alerm").val();
			}else{
				alermURL=alermURL+'&PM25_alerm=clean';
			}
			
			if($("#PM10_alerm").val()!=''){
				if(isNaN($("#PM10_alerm").val())){
					alert("请输入合法数字");
					return;
				}
				alermURL=alermURL+'&PM10_alerm='+$("#PM10_alerm").val();
			}else{
				alermURL=alermURL+'&PM10_alerm=clean';
			}
			/* if($("#NO_alerm").val()!=''){
				if(isNaN($("#NO_alerm").val())){
					alert("请输入合法数字");
					return;
				}
				if(unit=='ppbm'){
					alermURL=alermURL+'&NO_ppb_alerm='+$("#NO_alerm").val();
				}else{
					alermURL=alermURL+'&NO_ug_alerm='+$("#NO_alerm").val();
				}
			}else{
				if(unit=='ppbm'){
					alermURL=alermURL+'&NO_ppb_alerm=clean';
				}else{
					alermURL=alermURL+'&NO_ug_alerm=clean';
				}
			} */
			if($("#NO2_alerm").val()!=''){
				if(isNaN($("#NO2_alerm").val())){
					alert("请输入合法数字");
					return;
				}
				if(unit=='ppbm'){
					alermURL=alermURL+'&NO2_ppb_alerm='+$("#NO2_alerm").val();
				}else{
					alermURL=alermURL+'&NO2_ug_alerm='+$("#NO2_alerm").val();
				}
			}else{
				if(unit=='ppbm'){
					alermURL=alermURL+'&NO2_ppb_alerm=clean';
				}else{
					alermURL=alermURL+'&NO2_ug_alerm=clean';
				}
			}
			if($("#CO_alerm").val()!=''){
				if(isNaN($("#CO_alerm").val())){
					alert("请输入合法数字");
					return;
				}
				if(unit=='ppbm'){
					alermURL=alermURL+'&CO_ppm_alerm='+$("#CO_alerm").val();
				}else{
					alermURL=alermURL+'&CO_mg_alerm='+$("#CO_alerm").val();
				}
			}else{
				if(unit=='ppbm'){
					alermURL=alermURL+'&CO_ppm_alerm=clean';
				}else{
					alermURL=alermURL+'&CO_mg_alerm=clean';
				}
			}
			if($("#O3_alerm").val()!=''){
				if(isNaN($("#O3_alerm").val())){
					alert("请输入合法数字");
					return;
				}
				if(unit=='ppbm'){
					alermURL=alermURL+'&O3_ppb_alerm='+$("#O3_alerm").val();
				}else{
					alermURL=alermURL+'&O3_ug_alerm='+$("#O3_alerm").val();
				}
			}else{
				if(unit=='ppbm'){
					alermURL=alermURL+'&O3_ppb_alerm=clean';
				}else{
					alermURL=alermURL+'&O3_ug_alerm=clean';
				}
			}
			if($("#SO2_alerm").val()!=''){
				if(isNaN($("#SO2_alerm").val())){
					alert("请输入合法数字");
					return;
				}
				if(unit=='ppbm'){
					alermURL=alermURL+'&SO2_ppb_alerm='+$("#SO2_alerm").val();
				}else{
					alermURL=alermURL+'&SO2_ug_alerm='+$("#SO2_alerm").val();
				}
			}else{
				if(unit=='ppbm'){
					alermURL=alermURL+'&SO2_ppb_alerm=clean';
				}else{
					alermURL=alermURL+'&SO2_ug_alerm=clean';
				}
			}
			if($("#Temp_alerm").val()!=''){
				if(isNaN($("#Temp_alerm").val())){
					alert("请输入合法数字");
					return;
				}
				alermURL=alermURL+'&Temp_alerm='+$("#Temp_alerm").val();
			}else{
				alermURL=alermURL+'&Temp_alerm=clean';
			}
			if($("#Humi_alerm").val()!=''){
				if(isNaN($("#Humi_alerm").val())){
					alert("请输入合法数字");
					return;
				}
				alermURL=alermURL+'&Humi_alerm='+$("#Humi_alerm").val();
			}else{
				alermURL=alermURL+'&Humi_alerm=clean';
			}
			
			if(exportExcel==null){
				window.location.href='${pageContext.request.contextPath}/device/findDeviceList.do?deviceName='+place+"&cityName="+cityName+"&interval="+interval+"&airQulityType="+airQulityType+"&s_TimeStamp="+s_TimeStamp+"&e_TimeStamp="+e_TimeStamp+"&lineNum="+lineNum+"&unit="+unit+alermURL;
			}else{
				window.location.href="${pageContext.request.contextPath}/device/exportExcel.do?deviceName="+place+"&cityName="+cityName+"&interval="+interval+"&airQulityType="+airQulityType+"&s_TimeStamp="+s_TimeStamp+"&e_TimeStamp="+e_TimeStamp+"&unit="+unit+alermURL;
			}
			
		}
		
		
		var show=false;
		function showAlerm(){
			if(show){
				$("#alerm").show();
				show=false;
			}else{
				$("#alerm").hide();
				show=true;
			}
		}
		
		
	</script>

<%
if(session.getAttribute("currentAccount")==null){
	response.sendRedirect("login/a.jsp");
}
%>


</head>
<body style="background:#4585d7">
	<div class="header">
		<div class="header-nav">
			<div class="logo">
				<span>Sapiens Environmental IoT Network Cloud (SEIN)<br/>环境物联网云平台</span>
			</div>
			<div class="menu">
				<a class="header-btn" href="${pageContext.request.contextPath}/account/logout.do">注销</a>
				<a class="header-btn" href="mgmt.jsp">管理</a>
				<a class="header-btn" href="compare.jsp">比较</a>
				<a class="header-btn" href="map.jsp">地图</a>
				<a class="header-btn" href="${pageContext.request.contextPath}/city/list.do?cityID=${currentAccount.place}">列表</a>	
				<a class="header-btn" href="${pageContext.request.contextPath }/account/changeLanguage.do?language=English&fromPageName=table">中/Eng</a>
			</div>
		</div>
	</div>
	
	
	<div class="detail-main">
		<div class="detail-left">
			<a class="map-left" href="javascript:historySearch()">历史数据查询</a>
			<a class="map-left" href="javascript:showAlerm()">报警数据查询</a>
			<a class="map-left" href="javascript:history.go(-1)">返回</a>	
		</div>
		
		<div class="detail-right" style="height: auto">	
		<div>
				<div style="height:100px;">
				<div style="display:inline;">
				<div class="table-select" style="margin-left:30px;">
				<div class="table-select2">地点</div>
					<select name="place" id="place">
						<c:forEach var="city" items="${cityList }">
						<option value="${city.getDeviceInfo().getDeviceName() }-${city.cityName}" ${city.cityName==cityName?'selected':'' }>	
						${city.cityName }</option>
						</c:forEach>
					</select>
				</div>
				<div class="table-select">
				<div class="table-select2">时间间隔</div>
					<select name="timepicker" id="timepicker">
					<c:choose>
						<c:when test="${deviceName=='dev9100d161200007'||deviceName=='dev9100d161200008'||deviceName=='dev9100d170100024'||deviceName=='dev9100d170100027'}">
							<option value="15min" ${interval=='15min'?'selected':'' }>15分钟</option>
							<option value="1h" ${interval=='1h'?'selected':'' }>1小时</option>
							<option value="1d" ${interval=='1d'?'selected':'' } >1天</option>
						</c:when>
						<c:otherwise>
							<option value="1min" ${interval=='1min'?'selected':'' }>1分钟</option>
							<option value="10min" ${interval=='10min'?'selected':'' }>10分钟</option>
							<option value="1h" ${interval=='1h'?'selected':'' }>1小时</option>
							<option value="1d" ${interval=='1d'?'selected':'' } >1天</option>
						</c:otherwise>
					</c:choose>
					</select>
				</div>
				<div class="table-select">
				<div class="table-select2">开始时间</div>
				<div style="margin-top:0px;">
					<input type="text" id="s_TimeStamp" style="height:20px;" value="${s_TimeStamp==null?'2016-08-15 00:00':s_TimeStamp}">
				</div>
				</div>
				<div class="table-select">
				<div class="table-select2">结束时间</div>
				<div style="margin-top:0px;">
					<input type="text" id="e_TimeStamp" style="height:20px;line-height:18px;" value="${e_TimeStamp}">
				</div>
				</div>
				<div class="table-select">
				<div class="table-select2">每页行数</div>
				<div style="margin-top:-14px;">
					<input type="number" id="lineNum" name="lineNum" value="${lineNum }" style="width:45px;height:20px;line-height:18px;" value="30">
				</div>
				</div>
				
				<div class="table-select">
				<div style="margin-top: 30px;margin-left: -25px;"><a id="detail-submit"href="${pageContext.request.contextPath }/device/detail.do?deviceName=${deviceName}&cityName=${cityName}">切换图表</a></div>
				</div>
				</div>
				</div>
				
				<div style="height:100px;margin-top:-25px">
				<div style="display:inline;">
				<div class="table-select" style="margin-left:30px;">
				<div class="table-select2">单位标准</div>
					<input type="radio" class="radio" id="umgm" name="standard" value="umgm"  /><span style="width:20px;margin-left:-10px;margin-right:10px;" >µg/m³ or mg/m³</span><br>
					<input type="radio" class="radio" id="ppbm" name="standard" value="ppbm" /><span style="margin-left:-10px;">ppb or ppm</span>	
				</div>
				<div class="table-select">
				<div class="table-select2">污染物</div>
					<input type="checkbox" name="airQulityType" id="PM25_ug" value="`PM2.5_ug`"  class="radio" >PM<sub>2.5</sub>
					<br>			
				</div>				
				<div class="table-select">
				<div>&nbsp;</div>
					<input type="checkbox" name="airQulityType" id="PM10" value="PM10"  class="radio" >PM<sub>10</sub><br>
				</div>
				<div class="table-select">
				<div>&nbsp;</div>
					<input type="checkbox" name="airQulityType" id="NO2" value="NO2"  class="radio" >NO<sub>2</sub><br>				
				</div>
				<div class="table-select">
				<div>&nbsp;</div>
					<input type="checkbox" name="airQulityType" id="CO" value="CO"  class="radio" >CO<br>
				</div>
				<div class="table-select">
				<div>&nbsp;</div>
					<input type="checkbox" name="airQulityType" id="O3" value="O3"  class="radio" >O<sub>3</sub><br>		
				</div>
				<div class="table-select">
				<div>&nbsp;</div>
					<input type="checkbox" name="airQulityType" id="SO2" value="SO2"  class="radio" >SO<sub>2</sub><br>					
				</div>
				<div class="table-select">
				<div>&nbsp;</div>
					<input type="checkbox" name="airQulityType" id="Temp" value="Temp"  class="radio" >温度<br>
				</div>
				<div class="table-select">
				<div>&nbsp;</div>
					<input type="checkbox" name="airQulityType" id="Humi" value="Humi"  class="radio" >湿度<br>		
				</div>
				
				</div>
				</div>
				
				<div id="alerm" style="height:70px;margin-top:-25px;">
				<div style="display:inline;">
					<div class="table-select" style="margin-left:30px; ">
					<div class="table-select2">警戒值</div>
					<table style="margin-top: 0px;">
						<tr>
							<td style="padding-top: 10px;">PM<sub>2.5</sub>&nbsp;</td>
							<td><input type="text" id="PM25_alerm" value="${PM25_alerm }"/></td>
							<td style="padding-top: 10px;" >PM<sub>10</sub>&nbsp;</td>
							<td><input type="text" id="PM10_alerm" value="${PM10_alerm }"/></td>
							<td style="padding-top: 10px;">NO<sub>2</sub>&nbsp;</td>
							<td><input type="text" id="NO2_alerm" value="${NO2_alerm }"/></td>
							<td style="padding-top: 10px;">CO&nbsp;</td>
							<td><input type="text" id="CO_alerm" value="${CO_alerm }"/></td>
							<td style="padding-top: 10px;">O<sub>3</sub>&nbsp;</td>
							<td><input type="text" id="O3_alerm" value="${O3_alerm }"/></td>
							<td style="padding-top: 10px;">SO<sub>2</sub>&nbsp;</td>
							<td><input type="text" id="SO2_alerm" value="${SO2_alerm }"/></td>
							<td style="padding-top: 10px;">温度&nbsp;</td>
							<td><input type="text"id="Temp_alerm" value="${Temp_alerm }"/></td>
							<td style="padding-top: 10px;">湿度&nbsp;</td>
							<td><input type="text"id="Humi_alerm" value="${Humi_alerm }"/></td>
						</tr>
					</table>
					
				</div>
				</div>
				</div>
				<div style="margin-left: 30px;margin-top: 5px;">
					<input type="button" style="display:inline-block;padding-left:0px;vertical-align:top;width:100px; height:27px; font-size:10px; text-align:bottom;" onclick="javascript:findDeviceTable(1)" value="导出到Excel"/>
					<input type="button" style="display:inline-block;padding-left:0px;vertical-align:top;width:100px; height:27px; font-size:10px; text-align:bottom;" value="选择" onclick="javascript:findDeviceTable()"/>	
				</div>
				
			</div>
				
			<table style="solid:#000; width: 95%; margin-top:20px; margin-left:30px; text-align:center" border="2 " >
			<tr>
				 <c:forEach var="a" items="${airType}">
				 	
					<th>${a }</th>
				
				</c:forEach> 
			</tr>
			<c:forEach var="device" items="${deviceList }">
				<tr>
					<td><fmt:formatDate value="${device.getTimeStamp()}" type="Date" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<c:if test="${device.PM25_ug !=null}"><td>${device.PM25_ug }</td></c:if>
					
					<c:if test="${device.PM10_ug !=null}"><td>${device.PM10_ug}</td></c:if>
					
					 <c:if test="${device.NO_ug !=null}"><td>${device.NO_ug}</td></c:if> 
					
					 <c:if test="${device.NO_ppb !=null}"><td>${device.NO_ppb }</td></c:if>
					
					<c:if test="${device.NO2_ug !=null }"><td>${device.NO2_ug }</td></c:if>
										
					<c:if test="${device.NO2_ppb !=null}"><td>${device.NO2_ppb }</td></c:if>
					
					<c:if test="${device.CO_mg !=null}"><td>${device.CO_mg }</td></c:if>
										
					<c:if test="${device.getCO_ppm() !=null}"><td>${device.getCO_ppm() }</td></c:if>
					
					<c:if test="${device.getO3_ug() !=null}"><td>${device.getO3_ug() }</td></c:if>
											
					<c:if test="${device.getO3_ppb() !=null}"><td>${device.getO3_ppb() }</td></c:if>
					
					<c:if test="${device.SO2_ug !=null}"><td>${device.SO2_ug }</td></c:if>
					
					<c:if test="${device.SO2_ppb !=null}"><td>${device.SO2_ppb }</td></c:if>
					
					<c:if test="${device.getTemp() !=null}"><td>${device.getTemp() }</td></c:if>
										
					<c:if test="${device.getHumi() !=null}"><td>${device.getHumi() }</td></c:if>
				</tr>	
			</c:forEach>
			</table>
			<div class="pagination" style="margin-left:30px;">
  				<ul>${pageCode }</ul>
  			</div>
		</div>
	</div>	
</body>
</html>