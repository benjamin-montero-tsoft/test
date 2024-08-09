<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<div id="bodyFragmentChild">
	<form:form id="formRiesgoCrediticioHist" commandName="busquedaRiesgoCrediticioHistBean" action="${pageContext.request.contextPath}/paginarRiesgoCrediticioHist.do">
		<table class="title">
			<tr><td class="	title">
				<spring:message code="riesgoCrediticioHist.title"/>
			</td></tr>
		</table>
		<br>
		<table>
			<tr>
				<td style="width: 90px">
					<spring:message code="riesgoCrediticioHist.tipoDocumento"/>
				</td>
				<td style="font-size: 11px;">	
					<form:select id="tipoDocAmdocPipeCargaGenero" path="tipoDocAmdocPipeCargaGeneroBusq" cssStyle="width: 105px">
						<c:if test="${empty listaTiposDocumentoHist}">
							<form:option label="" value=""/>
						</c:if>	
						<c:if test="${not empty listaTiposDocumentoHist}">
							<form:options items="${listaTiposDocumentoHist}" itemLabel="tipoDocAmdoc" itemValue="tipoDocAmdocPipeCargaGenero"/>
						</c:if>								
					</form:select>
					<form:hidden id="tipoDoc" path="tipoDoc"/>
				</td>
				<td>
					<c:if test="${empty listaTiposDocumentoHist}">
						<FONT COLOR="RED"><B><spring:message code="riesgoCrediticioHist.results.tipoDoc.empty"/></B></FONT>
					</c:if>							
					<form:errors path="tipoDoc" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td style="width: 90px">
					<spring:message code="riesgoCrediticioHist.nroDocumento"/>
				</td>
				<td>
					<form:input id="nroDoc" path="nroDoc" cssStyle="width: 100px" maxlength="11"/>
				</td>
				<td>
					<form:errors path="nroDoc" cssClass="error" />
				</td>
			</tr>
			<tr>				
				<c:if test="${not empty listaTiposDocumentoHist}">	
					<td style="width: 90px">
						<form:hidden id="cargaGenero" path="cargaGenero"/>								
						<div id="tituloGenero">
							<c:if test="${'S' == busquedaRiesgoCrediticioHistBean.cargaGenero}">
								<spring:message code="riesgoCrediticioHist.genero"/>
							</c:if>
						</div>
					</td>							
					<td style="font-size: 11px;">					
					<div id="divSelectGenero">
						<c:if test="${'S' eq busquedaRiesgoCrediticioHistBean.cargaGenero}">
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
				<td colspan="3">
					<br>
					<input id="consultar" type="submit" value="<spring:message code="button.consultar"/>" onclick="$('#formRiesgoCrediticioHist')[0].action='${pageContext.request.contextPath}/consultarRiesgoCrediticioHist.do'"/>
					<input id="limpiarFormulario" type="submit" value="<spring:message code="button.limpiar"/>" onclick="$('#formRiesgoCrediticioHist')[0].action='${pageContext.request.contextPath}/formConsultaRiesgoCrediticioHist.do'"/>
				</td>
			</tr>		
		</table>
		<c:choose>
			<c:when test="${mostrarListaHistorica == true}">
				<c:choose>
					<c:when test="${empty listaClientesRiesgoCrediticioHist}">
						<table>
							<tr>
								<td><spring:message code="riesgoCrediticioHist.results.clienteRiesgo.empty"/></td>
							</tr>
						</table>
					</c:when>
					<c:otherwise>
						<c:if test="${not empty listaClientesRiesgoCrediticioHist}">
							<table BORDER="0" CELLSPACING="9" CELLPADDING="0" WIDTH=570>
								<tbody>
									<tr>
										<td>
											<div class="scroll">
												<jmesa:tableModel id="tablaClientesRiesgoHist" toolbar="ar.com.latam.jmesa.view.CustomToolBar" items="${listaClientesRiesgoCrediticioHist}" var="itemClienteHist">
											        <jmesa:htmlTable>               
											            <jmesa:htmlRow>									            
															<jmesa:htmlColumn property="tipoDocAMDOCS" titleKey="riesgoCrediticioHist.grilla.tipoDoc" sortable="false" filterable="false">
																<div align="center">${itemClienteHist.tipoDocAMDOCS}</div>
															</jmesa:htmlColumn>															
															<jmesa:htmlColumn property="nroDocFormato" titleKey="riesgoCrediticioHist.grilla.nroDoc" sortable="false" filterable="false">
																<div align="center" style="width:90px">${itemClienteHist.nroDocFormato}</div>
															</jmesa:htmlColumn>	
															<jmesa:htmlColumn property="genero" titleKey="riesgoCrediticioHist.grilla.genero" sortable="false" filterable="false">
																<div align="center">${itemClienteHist.genero}</div>
															</jmesa:htmlColumn>														            
											                <jmesa:htmlColumn property="scoreCalculadoIN" titleKey="riesgoCrediticioHist.grilla.scoreCalculadoIn" sortable="false" filterable="false">
																<div align="center">${itemClienteHist.scoreCalculadoIN}</div>
															</jmesa:htmlColumn>										                
											             	<jmesa:htmlColumn property="fechaCargaScoreIN" titleKey="riesgoCrediticioHist.grilla.fechaCargaScoreIn" sortable="false" filterable="false">
																<div align="center">${itemClienteHist.fechaCargaScoreIN}</div>
															</jmesa:htmlColumn>										             			            
											                <jmesa:htmlColumn property="scoreLegadoAM" titleKey="riesgoCrediticioHist.grilla.scoreLegadoAM" sortable="false" filterable="false">
																<div align="center">${itemClienteHist.scoreLegadoAM}</div>
															</jmesa:htmlColumn>										                
											             	<jmesa:htmlColumn property="fechaUpdateAM" titleKey="riesgoCrediticioHist.grilla.fechaUpdateAM" sortable="false" filterable="false">
																<div align="center">${itemClienteHist.fechaUpdateAM}</div>
															</jmesa:htmlColumn>										             			            
											                <jmesa:htmlColumn property="scoreManual" titleKey="riesgoCrediticioHist.grilla.scoreManual" sortable="false" filterable="false">
																<div align="center">${itemClienteHist.scoreManual}</div>
															</jmesa:htmlColumn>										                
											             	<jmesa:htmlColumn property="fechaCargaScoreManual" titleKey="riesgoCrediticioHist.grilla.fechaCargaScoreManual" sortable="false" filterable="false">
																<div align="center">${itemClienteHist.fechaCargaScoreManual}</div>
															</jmesa:htmlColumn>										             			            
											                <jmesa:htmlColumn property="usuarioCargaCambio" titleKey="riesgoCrediticioHist.grilla.usuarioCargaCambio" sortable="false" filterable="false">
																<div align="center">${itemClienteHist.usuarioCargaCambio}</div>
															</jmesa:htmlColumn>										                									             			            									                
											        	</jmesa:htmlRow>
											        </jmesa:htmlTable> 
											    </jmesa:tableModel>
								    		</div>
							    		</td>
							    	</tr>
							    	<tr>
								    	<td>
								    		<input id="gridSubmit" type="submit" style="display: none"/>
								    	</td>
							    	</tr> 
								</tbody>
							</table>
						</c:if>
					</c:otherwise>
				</c:choose>
			</c:when>
		</c:choose>		
	</form:form>	
		
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "consultar",
			formId: "formRiesgoCrediticioHist",		
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "limpiarFormulario",	
			formId: "formRiesgoCrediticioHist",	
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>		
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "gridSubmit",
			formId: "formRiesgoCrediticioHist",
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#consultar').click(function(){
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
	$(document).ready(function() {
		$('#gridSubmit').click(function(){
			$('#inline').trigger('click');
		});
	});
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