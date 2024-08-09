<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="bodyFragmentChild">
	<form:form id="formAltaRegistroTipoDoc" commandName="altaTipoDocBean" action="${pageContext.request.contextPath}/darAltaRegistroTipoDoc.do">
		<table class="title">
			<tr><td class="	title">
				<spring:message code="altaRegistroTipoDoc.title"/>
			</td></tr>
		</table>
		<br>		
		<table>
			<tr>
				<td>
					<spring:message code="altaRegistroTipoDoc.tipoDocDW"/>
				</td>
				<td>
					<form:input path="tipoDocDW" cssStyle="width: 100px" maxlength="2"/>
				</td>
				<td>
					<form:errors path="tipoDocDW" cssClass="error" />
				</td>				
			</tr>
			<tr>
				<td>
					<spring:message code="altaRegistroTipoDoc.tipoDocAmdoc"/>
				</td>
				<td>
					<form:input path="tipoDocAmdoc" cssStyle="width: 100px" maxlength="8"/>
				</td>
				<td>
					<form:errors path="tipoDocAmdoc" cssClass="error" />
				</td>				
			</tr>
			<tr>
				<td>
					<spring:message code="altaRegistroTipoDoc.cargaGenero"/>
				</td>
				<td>
					<form:select id="cargaGenero" path="cargaGenero" cssStyle="width: 105px">
						<form:option label="S" value="S"/>
						<form:option label="N" value="N"/>								
					</form:select>					
				</td>
				<td>
					<form:errors path="cargaGenero" cssClass="error" />
				</td>								
			</tr>			
			<tr>
				<td colspan="2">
					<br>
					<input id="validarFormulario" type="button" value="<spring:message code="button.alta"/>"/>
					<input id="buttonAlta" type="submit" style="display: none;"/>
					<input id="buttonCancelar" type="submit" value="<spring:message code="button.cancelar"/>" onclick="$('#formAltaRegistroTipoDoc')[0].action='${pageContext.request.contextPath}/tiposDocumento.do'"/>				
				</td>
			</tr>			
		</table>
	</form:form>	
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "buttonAlta",
			formId: "formAltaRegistroTipoDoc",		
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>	
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "buttonCancelar",
			formId: "formAltaRegistroTipoDoc",		
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#buttonAlta').click(function(){
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
			$('#buttonAlta').click();
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