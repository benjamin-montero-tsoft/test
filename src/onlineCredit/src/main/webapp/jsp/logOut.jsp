<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Log Out</title>
</head>

<script>

function closeMe()
{
var win=window.open("","_self");
win.close();
}
</script>


<body>
	<div id="container">
		<div id="header">
			<div style="position: relative; left: 139px; top: 67px; width: 88%;">
			</div>
		</div>	
		
			<div align="left" id="bodyFragment"><spring:message code="logOut"/>	
			<div style="position: relative; top: 20px; width: 88%;"><input type="button" value="CERRAR" name="Back" onclick="closeMe()" /></div>			
			</div>
	
		<a id="delete"></a> <a id="inline"></a>
	</div>

</body>
</html>