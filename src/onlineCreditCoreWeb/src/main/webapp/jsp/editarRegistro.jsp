<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="bodyFragmentChild">
	<form:form id="formEditarRegistro" commandName="editarClienteBean"
	action="${pageContext.request.contextPath}/actualizarRegistro.do">
	<table class="title">
		<tr>
			<td class="	title"><spring:message code="editarRegistro.title" />
			</td>
		</tr>
	</table>
	<br>
	<table>
		<tr>
			<td><spring:message code="editarRegistro.tipoDocumento" /></td>
			<td><form:input path="tipoDocAMDOCS" cssStyle="width: 100px"
				disabled="true" /> <form:hidden path="tipoDocAMDOCS" /> <form:hidden
				path="tipoDoc" /></td>
			<td></td>
		</tr>
		<tr>
			<td><spring:message code="editarRegistro.nroDocumento" /></td>
			<td><form:input path="nroDocFormato" cssStyle="width: 100px"
				disabled="true" /> <form:hidden path="nroDoc" /></td>
			<td></td>
		</tr>
		<tr>
			<td><spring:message code="editarRegistro.genero" /></td>
			<td><form:input path="genero" cssStyle="width: 100px"
				disabled="true" /> <form:hidden path="genero" /></td>
			<td></td>
		</tr>
		<tr>
			<td><spring:message code="editarRegistro.scoreCalculadoIn" /></td>
			<td><form:input path="scoreCalculadoIN" cssStyle="width: 100px"
				disabled="true" /> <form:hidden path="scoreCalculadoIN" /></td>
			<td></td>
		</tr>
		<tr>
			<td><spring:message code="editarRegistro.fechaCargaScoreIn" /></td>
			<td><form:input path="fechaCargaScoreIN" cssStyle="width: 100px"
				disabled="true" /> <form:hidden path="fechaCargaScoreIN" /></td>
			<td></td>
		</tr>
		<tr>
			<td><spring:message code="editarRegistro.scoreLegadoAM" /></td>
			<td><form:input path="scoreLegadoAM" cssStyle="width: 100px"
				disabled="true" /> <form:hidden path="scoreLegadoAM" /></td>
			<td></td>
		</tr>
		<tr>
			<td><spring:message code="editarRegistro.fechaUpdateAM" /></td>
			<td><form:input path="fechaUpdateAM" cssStyle="width: 100px"
				disabled="true" /> <form:hidden path="fechaUpdateAM" /></td>
			<td></td>
		</tr>
		<tr>
			<td><spring:message code="editarRegistro.scoreManual" /></td>
			<td><form:input path="scoreManual" cssStyle="width: 100px"
				maxlength="3" /></td>
			<td><form:errors path="scoreManual" cssClass="error" /></td>
		</tr>
		
		<tr>
			<td colspan="2"><br>
			<input id="validarFormulario" type="button"
				value="<spring:message code="button.guardarCambios"/>" /> <input
				id="buttonActualizar" type="submit" style="display: none;" /> <input
				id="buttonCancelar" type="submit"
				value="<spring:message code="button.cancelar"/>"
				onclick="$('#formEditarRegistro')[0].action='${pageContext.request.contextPath}/formConsultaModifRiesgoCrediticio.do'" />
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<c:if test="${not empty   errorSFTP}">
				<spring:message code="error.sftp"/>
			</c:if>
		</tr>
	</table>
</form:form>
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "buttonActualizar",
			formId: "formEditarRegistro",		
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>	
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "buttonCancelar",
			formId: "formEditarRegistro",		
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#buttonActualizar').click(function(){
			$('#inline').trigger('click');
		});
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#buttonCancelar').click(function(){
			$('#inline').trigger('click');
		});
	});
</script>
<script type="text/javascript">
if(document.getElementById("validarFormulario")!=null){
	document.getElementById("validarFormulario").onclick = confirmation;
}
function confirmation(){
		var state = confirm("CONFIRMAR");
		if (state) {
			$('#buttonActualizar').click();
		}

}
</script>
<script language=javascript type=text/javascript>
	document.onkeypress = stopEnterKey;
	function stopEnterKey(evt) {
	var evt = (evt) ? evt : ((event) ? event : null);
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
	if ((evt.keyCode == 13) && (node.type=="text")) {return false;}
	}
</script>	
</div>	 	 	