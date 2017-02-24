<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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
	<link rel="stylesheet" href="css/searchbar.css">
	<script src="js/environment.js"></script>
	<script type="text/javascript">
		function searchPlace(){
			var placesearch=$("#placesearch").val();
			window.location.href="${pageContext.request.contextPath}/city/list.do?cityID=${currentAccount.place}&placesearch="+placesearch;
		}
		
		/* $(function(){
			var cityID=${currentAccount.place};
			//取得div层
			var $search = $('#searchbar');
			//取得输入框JQuery对象
			var $searchInput = $search.find('#placesearch');
			//关闭浏览器提供给输入框的自动完成
			$searchInput.attr('autocomplete','off');
			//创建自动完成的下拉列表，用于显示服务器返回的数据,插入在搜索按钮的后面，等显示的时候再调整位置
			var $autocomplete = $('#autocomplete').hide().insertAfter('#placesearch');
			//清空下拉列表的内容并且隐藏下拉列表区
			var clear = function(){
				$autocomplete.empty().hide();
			};
			//注册事件，当输入框失去焦点的时候清空下拉列表并隐藏
			$searchInput.blur(function(){
				setTimeout(clear,500);
			});
			//下拉列表中高亮的项目的索引，当显示下拉列表项的时候，移动鼠标或者键盘的上下键就会移动高亮的项目，想百度搜索那样
			var selectedItem = null;
			//timeout的ID
			var timeoutid = null;
			//设置下拉项的高亮背景
			var setSelectedItem = function(item){
			//更新索引变量
			selectedItem = item ;
			//按上下键是循环显示的，小于0就置成最大的值，大于最大值就置成0
			if(selectedItem < 0){
				selectedItem = $autocomplete.find('li').length - 1;
			}
			else if(selectedItem > $autocomplete.find('li').length-1 ) {
				selectedItem = 0;
			}
			//首先移除其他列表项的高亮背景，然后再高亮当前索引的背景
			$autocomplete.find('li').removeClass('highlight').eq(selectedItem).addClass('highlight');};
			var ajax_request = function(){
			//ajax服务端通信
			$.ajax({
			'url':'city/searchTip.do', //服务器的地址
			'data':{'search-text':$searchInput.val(),'cityID':cityID}, //参数
			'dataType':'json', //返回数据类型
			'type':'POST', //请求类型
			'success':function(data){
			if(data.length) {
			//遍历data，添加到自动完成区
			$.each(data, function(index,term) {
			//创建li标签,添加到下拉列表中
			$('<li ></li>').text(term).appendTo($autocomplete).addClass('clickable').hover(function(){
				//下拉列表每一项的事件，鼠标移进去的操作
				$(this).siblings().removeClass('highlight');
				$(this).addClass('highlight');
				selectedItem = index;
			},function(){
				//下拉列表每一项的事件，鼠标离开的操作
				$(this).removeClass('highlight');
				//当鼠标离开时索引置-1，当作标记
				selectedItem = -1;
			}).click(function(){
				//鼠标单击下拉列表的这一项的话，就将这一项的值添加到输入框中
				$searchInput.val(term);
				//清空并隐藏下拉列表
				$autocomplete.empty().hide();
				});
			});//事件注册完毕
			//设置下拉列表的位置，然后显示下拉列表
			var ypos = $searchInput.position().top;
			var xpos = $searchInput.position().left;
			$autocomplete.css('width',$searchInput.css('width'));
			$autocomplete.css({'position':'relative','left':xpos + "px",'top':ypos +"px"});
			setSelectedItem(0);
			//显示下拉列表
			$autocomplete.show();
			}
			}
			});
			};
			//对输入框进行事件注册
			$searchInput.keyup(function(event) {
				//字母数字，退格，空格
				if(event.keyCode > 40 || event.keyCode == 8 || event.keyCode ==32) {
				//首先删除下拉列表中的信息
				$autocomplete.empty().hide();
				clearTimeout(timeoutid);
				timeoutid = setTimeout(ajax_request,100);
			}
			else if(event.keyCode == 38){
			//上
			//selectedItem = -1 代表鼠标离开
			if(selectedItem == -1){
			setSelectedItem($autocomplete.find('li').length-1);
			}
			else {
			//索引减1
			setSelectedItem(selectedItem - 1);
			}
			event.preventDefault();
			}
			else if(event.keyCode == 40) {
			//下
			//selectedItem = -1 代表鼠标离开
			if(selectedItem == -1){
			setSelectedItem(0);
			}
			else {
			//索引加1
			setSelectedItem(selectedItem + 1);
			}
			event.preventDefault();
			}
			})
			.keypress(function(event){
			//enter键
			if(event.keyCode == 13) {
			//列表为空或者鼠标离开导致当前没有索引值
			if($autocomplete.find('li').length == 0 || selectedItem == -1) {
			return;
			}
			$searchInput.val($autocomplete.find('li').eq(selectedItem).text());
			$autocomplete.empty().hide();
			event.preventDefault();
			}
			})
			.keydown(function(event){
			//esc键
			if(event.keyCode == 27 ) {
			$autocomplete.empty().hide();
			event.preventDefault();
			}
			});
			//注册窗口大小改变的事件，重新调整下拉列表的位置
			$(window).resize(function() {
				var ypos = $searchInput.position().top;
				var xpos = $searchInput.position().left;
				$autocomplete.css('width',$searchInput.css('width'));
				$autocomplete.css({'position':'relative','left':xpos + "px",'top':ypos +"px"});
				});
			}); */
	</script>
<%
if(session.getAttribute("currentAccount")==null){
	response.sendRedirect("login/a.jsp");
}
%>
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
	                <li><a href="${pageContext.request.contextPath }/account/changeLanguage.do?language=Chinese&fromPageName=list">中/Eng</a></li>
	                <li><a href="${pageContext.request.contextPath}/city/list.do?cityID=${currentAccount.place}">List</a></li>
	                <li class="map_btn"><a href="map.jsp">Map</a></li>
	                <li><a href="compare.jsp">Compare</a></li>
	                <li><a href="mgmt.jsp">Manage</a></li>
	                <li><a href="${pageContext.request.contextPath}/account/logout.do">Logout</a></li>
	            </ul>
	        </div><!-- /.navbar-collapse -->
	    </div><!-- /.container-fluid -->
	</nav>
	<div class="content">
			<div class="content-main">
			
				<div class="searchbar" id="searchbar">
					<div style="float:left;margin-top: 33px;margin-left: -500px;font-size:20px; z-index:2" class="autocomplete" id="autocomplete"></div>			
					
					<input style="float: left" id="placesearch" type="text" placeholder="search" onkeyup="if(event.keyCode==13) searchPlace();">
					<a id="placesearchbtn" href="javascript:searchPlace()"></a>
				</div>
				<div class="citylist" >
				<c:forEach var="city" items="${cityList }">
					<div class="cities">
						<a href="${pageContext.request.contextPath }/device/detail.do?deviceName=${city.deviceInfo.deviceName}&cityName=${city.cityName}">
						<img src="../img/cityImg/${city.picture }" alt=""/>
							<h3>${city.cityName }</h3>
							<div class="city-bottom">
							<c:forEach var="device" items="${city.deviceList }">
								<marquee drection="left" onMouseOver="this.stop()" onMouseOut="this.start()" behavior="scroll">
								<span>[ PM<sub>2.5</sub> :
								<fmt:formatNumber type="number" value="${device.getPM25_ug() }" pattern="0.0" maxFractionDigits="1"/> &micro;g/m<sup>3</sup> ] 
								&nbsp;[ PM<sub>10</sub> :
								<fmt:formatNumber type="number" value="${device.getPM10_ug()}" pattern="0.0" maxFractionDigits="1"/> &micro;g/m<sup>3</sup> ] 
								&nbsp;[ NO<sub>2</sub> :
								<fmt:formatNumber type="number" value="${device.getNO2_ug() }" pattern="0.0" maxFractionDigits="1"/> &micro;g/m<sup>3</sup> ] 
								&nbsp;[ CO :
								<fmt:formatNumber type="number" value="${device.getCO_mg() }" pattern="0.00" maxFractionDigits="2"/> mg/m<sup>3</sup> ] 
								&nbsp;[ O<sub>3</sub> :
								<fmt:formatNumber type="number" value="${device.getO3_ug() }" pattern="0.0" maxFractionDigits="1"/> &micro;g/m<sup>3</sup> ] 
								&nbsp;[ SO<sub>2</sub> :
								<fmt:formatNumber type="number" value="${device.getSO2_ug() }" pattern="0.0" maxFractionDigits="1"/> &micro;g/m<sup>3</sup> ] 
								&nbsp;[ 温度 :
								<fmt:formatNumber type="number" value="${device.getTemp() }" pattern="0" maxFractionDigits="0"/> ℃ ] 
								&nbsp;[ 湿度 :
								<fmt:formatNumber type="number" value="${device.getHumi() }" pattern="0" maxFractionDigits="0"/> % ] </span>
								</marquee>
								<c:choose>
									<c:when test="${device.getStatus() =='OFFLINE'}">
										<p class="airlevel level-3">${device.getStatus() }</p>
									</c:when>
									<c:otherwise>
										<p class="airlevel level-1">${device.getStatus() }</p>
									</c:otherwise>
								</c:choose> 
							</c:forEach>
							</div>
						</a>
					</div>
				</c:forEach>
				</div>
		</div>
	</div>
</body>
</html>