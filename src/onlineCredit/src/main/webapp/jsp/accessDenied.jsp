<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE unspecified PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript">
		dojo.addOnLoad(function(){
			desbloquear();
		});
	</script>
</head>
<body>
	<div id="bodyFragment">
		<table>
			<tr><td class="title" colspan="2">
				<spring:message code="accessDenied"/>
			</td></tr>
		</table>
	</div>	
</body>
