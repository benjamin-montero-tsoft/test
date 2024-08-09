<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<!--DOCTYPE unspecified PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">-->
<html>
<title>Interfaz Riesgo Crediticio</title>
<head>
	<META content="IE=8" http-equiv="X-UA-Compatible">
	<link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/16x16.png" />	

	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.6.2.js"></script>

	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui-1.8.16.custom.min.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dropmenu.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jmesa.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jmesa.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui-timepicker-addon.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-ui-sliderAccess.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.ui.datepicker-es.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/anytime.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.menu.js"></script>
	
	<script type="text/javascript" src="<c:url value="/resources/dojo/dojo.js.uncompressed.js" />"></script>
  	<script type="text/javascript" src="<c:url value="/resources/spring/Spring.js.uncompressed.js" />"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/Spring-Dojo.js.uncompressed.js"></script>
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"></link>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery-ui-1.8.16.custom.css"></link>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jmesa.css"></link>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/anytime.css"></link>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/menu.css" />
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/blockui.js"></script>

	<script type="text/javascript">
		function onInvokeAction(id, action) {
			setExportToLimit(id, '');
	    	var tableFacade =   jQuery.jmesa.getTableFacade(id);
	        var form = jQuery.jmesa.getFormByTableId(id);
	        var created = tableFacade.createHiddenInputFields(form);
	        if (created) {
	        	if($('#gridSubmit'+id).length){
	        		$('#gridSubmit'+id).click();
	        	} else{
		        	$('#gridSubmit').click();
	        	}
			}
		};
	</script>
	
</head>
<body>
	<div id="container">
		<div id="header">
			<div style="position: relative; left: 139px; top: 67px; width: 88%;">
				<a style="font-size: xx-small;"></a> <a style="font-size: xx-small;"></a>
				<a id="logOut" href="jsp/logOut.do" style="font-size: xx-small; float: right;"> Logout </a>
			</div>
				
		</div>	
		<div>
			
			<div id="menu">
				<tiles:insertAttribute name="menu"/>
			</div>
		
			<div id="bodyFragment">
				<tiles:insertAttribute name="body"/>
			</div>	
		</div>
		<a id="delete" ></a>
		<a id="inline"></a>
	</div>
	<script type="text/javascript">
		var isBlockByUI=false;
	</script>
	<script>
	dojo.connect(Spring.RemotingHandler.prototype, 'handleResponse', null,'desbloquear');
		function desbloquear(){
			if(isBlockByUI == true){
				$.unblockUI();
				isBlockByUI = false;				
			}
		}
	</script>
	<script type="text/javascript">
		$(function() {
			$('#inline').click(function(){
				$.blockUI({ 
					message: '<div>Su consulta esta siendo procesada...</div>'
				});
				isBlockByUI = true;
			});
		});
	</script>
	<script type="text/javascript">
		function executeJavascriptEvent(){
			if($('#loadBotton').length){
				$('#loadBotton').trigger('click');
			}
		}
	</script>	
	<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "logOut",		
			event: "onclick", 
			params: { fragments: "bodyFragment" }
		}));
	});
	</script>	
</body>
</html>

