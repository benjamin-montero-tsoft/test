<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="bodyFragmentChild">
	<form:form id="formAltaDNIProspectos" commandName="formAltaDNIProspectosBean" action="${pageContext.request.contextPath}/darAltaRegistro.do">
		<table class="title">
			<tr><td class="	title">
				<spring:message code="altaDNIProspectos.title"/>
			</td></tr>
		</table>
		<br>		
		<table>
			<tr>
				<td>
					<spring:message code="altaDNIProspectos.tipoDocumento"/>
				</td>
				<td style="font-size: 11px;">	
					<form:select id="tipoDocAmdocPipeCargaGenero" path="tipoDocAmdocPipeCargaGeneroBusq" cssStyle="width: 105px">
						<c:if test="${empty listaTiposDocumento}">
							<form:option label="" value=""/>
						</c:if>	
						<form:options items="${listaTiposDocumento}" itemLabel="tipoDocAmdoc" itemValue="tipoDocAmdocPipeCargaGenero"/>
					</form:select>
					<form:hidden id="tipoDoc" path="tipoDoc"/>
				</td>
				<td>
					<c:if test="${empty listaTiposDocumento}">
						<FONT COLOR="RED"><B><spring:message code="altaDNIProspectos.results.empty"/></B></FONT>
					</c:if>							
					<form:errors path="tipoDoc" cssClass="error" />
				</td>								
			</tr>
			<tr>
				<td>
					<spring:message code="altaDNIProspectos.nroDocumento"/>
				</td>
				<td>
					<form:input id="nroDoc" path="nroDoc" cssStyle="width: 100px" maxlength="11"/>
				</td>
				<td>
					<form:errors path="nroDoc" cssClass="error" />
				</td>
			</tr>
			<tr>				
				<c:if test="${not empty listaTiposDocumento}">	
					<td style="width: 90px">
						<form:hidden id="cargaGenero" path="cargaGenero"/>								
						<div id="tituloGenero">
							<c:if test="${'S' == formAltaDNIProspectosBean.cargaGenero}">
								<spring:message code="riesgoCrediticioHist.genero"/>
							</c:if>
						</div>
					</td>							
					<td style="font-size: 11px;">					
					<div id="divSelectGenero">
						<c:if test="${'S' eq formAltaDNIProspectosBean.cargaGenero}">
							<form:select id="genero" path="genero" cssStyle="width: 105px">
								<form:option label="F" value="F"/>
								<form:option label="M" value="M"/>
								<form:option label="" value=""/>								
							</form:select>
						</c:if>					
					</div>	
					</td>
				</c:if>			
			</tr>			
			<tr>
				<td>
					<spring:message code="altaDNIProspectos.scoreManual"/>
				</td>
				<td>
					<form:input path="scoreManual" cssStyle="width: 100px" maxlength="3"/>
				</td>
				<td>
					<form:errors path="scoreManual" cssClass="error" />
				</td>
			</tr>							
			<tr>
				<td colspan="2">
					<br>
					<input id="validarFormulario" type="button" value="<spring:message code="button.alta"/>"/>
					<input id="buttonDarAlta" type="submit" style="display: none;"/>
					<input id="limpiarFormulario" type="submit" value="<spring:message code="button.limpiar"/>" onclick="$('#formAltaDNIProspectos')[0].action='${pageContext.request.contextPath}/formAltaRiesgoCrediticio.do'"/>
				</td>
			</tr>			
		</table>	
	</form:form>	
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "limpiarFormulario",
			formId: "formAltaDNIProspectos",		
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "buttonDarAlta",
			formId: "formAltaDNIProspectos",		
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#buttonDarAlta').click(function(){
			$('#inline').trigger('click');
		});
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#limpiarFormulario').click(function(){
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
			$('#buttonDarAlta').click();
		}

}
</script>
<script language=javascript type=text/javascript>
$(document).ready(ejecutarMostrarOcultarGenero);
function ejecutarMostrarOcultarGenero() {
	if(document.getElementById('tipoDocAmdocPipeCargaGenero').value != ""){
		mostrarOcultarGenero();
	}
}
document.getElementById('tipoDocAmdocPipeCargaGenero').onchange = mostrarOcultarGenero;
function mostrarOcultarGenero() {
	var auxTipoDocAmdocPipeCargaGenero = document.getElementById("tipoDocAmdocPipeCargaGenero").value;
	if (auxTipoDocAmdocPipeCargaGenero != null){
		var posicionPipe = auxTipoDocAmdocPipeCargaGenero.indexOf('|');		
		document.getElementById("tipoDoc").value = auxTipoDocAmdocPipeCargaGenero.substring(0,posicionPipe);
		document.getElementById("cargaGenero").value = auxTipoDocAmdocPipeCargaGenero.substring(posicionPipe+1,auxTipoDocAmdocPipeCargaGenero.length);
	}
	if(document.getElementById("cargaGenero")!= null){
		if(document.getElementById("cargaGenero").value == "S"){
			document.getElementById("tituloGenero").innerHTML="Sexo";
			if(document.getElementById("genero")!= null){
				if(document.getElementById("genero").value == "F"){
					document.getElementById("divSelectGenero").innerHTML ="<select name='genero' id='genero' style='width:105px'><option value='F' selected='selected'>F</option><option value='M'>M</option><option value=''></option></select>";
					
				}else if(document.getElementById("genero").value == "M"){
					document.getElementById("divSelectGenero").innerHTML ="<select name='genero' id='genero' style='width:105px'><option value='F'>F</option><option value='M' selected='selected'>M</option><option value=''></option></select>";				
				}else{
					document.getElementById("divSelectGenero").innerHTML ="<select name='genero' id='genero' style='width:105px'><option value='F'>F</option><option value='M'>M</option><option value=''></option></select>";
				}
			}else{
				document.getElementById("divSelectGenero").innerHTML ="<select name='genero' id='genero' style='width:105px'><option value='F'>F</option><option value='M'>M</option><option value=''></option></select>";				
			}							
		}else if(document.getElementById("cargaGenero").value == "N"){
			document.getElementById("tituloGenero").innerHTML="";
			document.getElementById("divSelectGenero").innerHTML ="";	
		}
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