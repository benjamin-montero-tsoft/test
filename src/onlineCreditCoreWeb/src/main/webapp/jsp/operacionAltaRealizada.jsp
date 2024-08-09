<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="bodyFragmentChild">
	<form:form id="formOKOperacionAlta" action="${pageContext.request.contextPath}/formAltaRiesgoCrediticio.do">
		<table class="title">
			<tr><td class="	title">
				<spring:message code="altaDNIProspectos.title"/>
			</td></tr>
		</table>
		<table>
			<tr>
				<td>
					<br><spring:message code="operacionAltaRealizada.mensaje"/>					
				</td>
			</tr>
			<tr>
				<td>
					<br><input id="regresarAFormularioAlta" type="button" value="<spring:message code="operacionAltaRealizada.regresar"/>"/>
				</td>
			</tr>
		</table>
	</form:form>	
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "regresarAFormularioAlta",
			formId: "formOKOperacionAlta",		
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#regresarAFormularioAlta').click(function(){
			$('#inline').trigger('click');
		});
	});
</script>
</div>	 	 	