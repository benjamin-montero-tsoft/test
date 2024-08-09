<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<div id="bodyFragmentChild">
	<table>
		<tr>
			<td class="error">
				<spring:message code="errorPage.backendError"/>
			</td>
		</tr>
		<tr></tr>
		<tr>
			<td><input id="details" type="button" value="Detalles" /></td>

		</tr>
		<tr>
			<td>
				<div id="error" style="visibility: hidden;">
									<div  style="width: 700px; color: red;">
					${errorSpecified}
					</div>
					<textarea rows="20" cols="105" style="resize:none;" readonly="readonly">${error}
					</textarea>
				</div></td>
		</tr>
		<tr>
			<td><input id="hide" type="button" value="Ocultar"
				style="visibility: hidden;"></td>
		</tr>
	</table>
</div>

<script>

	document.getElementById("details").onclick = showError;
	document.getElementById("hide").onclick = hideError;
	function showError() {
		document.getElementById('error').style.visibility = 'visible';
 		document.getElementById('hide').style.visibility = 'visible';
	}
	function hideError() {
		document.getElementById('error').style.visibility = 'hidden';
 		document.getElementById('hide').style.visibility = 'hidden';
	}
</script>