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
	<form:form id="formRiesgoCrediticio"  commandName="busquedaRiesgoCrediticioBean" action="${pageContext.request.contextPath}/paginarRiesgoCrediticio.do">
		<table class="title">
			<tr><td class="	title">
				<spring:message code="riesgoCrediticio.title"/>
			</td></tr>
		</table>
		<br>
			<div id="filtros">
				<div id="realizarConsulta">
					<br>
					<table>
						<tr>
							<td style="width: 90px">
								<spring:message code="riesgoCrediticio.tipoDocumento"/>
							</td>
							<td style="font-size: 11px;">	
								<form:select id="tipoDocAmdocPipeCargaGenero" path="tipoDocAmdocPipeCargaGeneroBusq" cssStyle="width: 105px">
									<form:option label="" value=""/>										
									<c:if test="${not empty listaTiposDocumento}">
										<form:options items="${listaTiposDocumento}" itemLabel="tipoDocAmdoc" itemValue="tipoDocAmdocPipeCargaGenero"/>	
									</c:if>								
								</form:select>
								<form:hidden id="tipoDoc" path="tipoDoc"/>
							</td>				
							<td>
								<c:if test="${empty listaTiposDocumento}">
									<FONT COLOR="RED"><B><spring:message code="riesgoCrediticio.results.tipoDoc.empty"/></B></FONT>
								</c:if>
								<form:errors path="tipoDoc" cssClass="error" />
							</td>
						</tr>
						<tr>
							<td style="width: 90px">
								<spring:message code="riesgoCrediticio.nroDocumento"/>
							</td>
							<td>
								<form:input id="nroDoc" path="nroDoc" cssStyle="width: 100px" maxlength="11"/>
							</td>
							<td>
								<form:errors path="nroDoc" cssClass="error"/>
							</td>
						</tr>
						<tr>				
							<c:if test="${not empty listaTiposDocumento}">	
								<td style="width: 90px">
									<form:hidden id="cargaGenero" path="cargaGenero"/>								
									<div id="tituloGenero">
										<c:if test="${'S' == busquedaRiesgoCrediticioBean.cargaGenero}">
											<spring:message code="riesgoCrediticio.genero"/>
										</c:if>
									</div>
								</td>							
								<td style="font-size: 11px;">					
									<div id="divSelectGenero">
										<c:if test="${'S' eq busquedaRiesgoCrediticioBean.cargaGenero}">
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
							<td></td>
							<td colspan="3">
								<input id="consultar" type="submit" value="<spring:message code="button.consultar"/>" onclick="$('#formRiesgoCrediticio')[0].action='${pageContext.request.contextPath}/consultarRiesgoCrediticio.do'"/>			
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<br><form:errors path="mensajeError" cssClass="error" />
							</td>
						</tr>			
					</table>
				</div>
				<div id="limpiarFormularios">
					<input id="limpiarFormulario" type="submit" value="<spring:message code="button.limpiar"/>" 
						onclick="$('#formRiesgoCrediticio')[0].action='${pageContext.request.contextPath}/formConsultaModifRiesgoCrediticio.do'"/>	
				</div>
				<div id="realizarBajada">
					<table>
						<tr>
							<td colspan="3" style="font-weight: bold">
								<spring:message code="riesgoCrediticio.cabeceraBajada"/>
							</td>
						</tr>
						<tr>
							<td style="width: 90px">
								<spring:message code="riesgoCrediticio.fechaDesde"/>
							</td>
							<td style="font-size: 11px;">
								<form:input id="fechaDesde" path="fechaDesde" cssStyle="width: 100px" maxlength="11"/>
							</td>
							<td>
								<div id="mensajeErrorFechaDesde" style="visibility:hidden" />
							</td>
						</tr>
						<tr>
							<td style="width: 90px">
								<spring:message code="riesgoCrediticio.fechaHasta"/>
							</td>
							<td style="font-size: 11px;">
								<form:input id="fechaHasta" path="fechaHasta" cssStyle="width: 100px" maxlength="11"/>
							</td>
							<td >
								<div id="mensajeErrorFechaHasta" style="visibility: hidden" />
							</td>
						</tr>
						<tr>
							<td></td>
							<td colspan="3">
								<input id="bajadaReporteManual" title="Bajada del Reporte de Riesgos Crediticios Manuales" type="button" value="<spring:message code="button.bajadaReporteManual"/>" 
								onclick="validarFechas();"/>
							</td>
						</tr>
					</table>
				</div>
			</div>
		<div id="table">
		<c:choose>
			<c:when test="${empty listaClientesRiesgoCrediticio}">
				<table>
					<tr>
						<td><spring:message code="riesgoCrediticio.results.clienteRiesgo.empty"/></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<c:if test="${not empty listaClientesRiesgoCrediticio}">
					<table BORDER="0" CELLSPACING="9" CELLPADDING="0" WIDTH=570>
						<tbody>
							<tr>
								<td>
									<div class="scroll">
										<jmesa:tableModel id="tablaClientesRiesgo" toolbar="ar.com.latam.jmesa.view.CustomToolBar" items="${listaClientesRiesgoCrediticio}" var="itemCliente">
									        <jmesa:htmlTable>               
									            <jmesa:htmlRow>									            
									                <sec:authorize ifAnyGranted="ROLE_CRED_MODIF">
									                <jmesa:htmlColumn titleKey="EDITAR" sortable="false" filterable="false">
									               		<div align="center">
									               			<a href="#" onclick="$(this).modificarDatos('${itemCliente.tipoDoc}', '${itemCliente.tipoDocAMDOCS}', 
																	'${itemCliente.nroDoc}','${itemCliente.genero}','${itemCliente.scoreCalculadoIN}',
																	'${itemCliente.fechaCargaScoreIN}','${itemCliente.scoreLegadoAM}',
																	'${itemCliente.fechaUpdateAM}','${itemCliente.scoreManual}',
																	'${itemCliente.fechaCargaScoreManual}','${itemCliente.usuarioCargaCambio}');">
											            		<img src="${pageContext.request.contextPath}/images/editar2.gif" 
											            			 title="EDITAR REGISTRO" 
											            			 style="border:0px;width:15px;height:15px;"/></a>
											            </div>													            			 
									                </jmesa:htmlColumn>
									                </sec:authorize>
													<jmesa:htmlColumn property="tipoDocAMDOCS" titleKey="riesgoCrediticio.grilla.tipoDoc" sortable="false" filterable="false">
														<div align="center">${itemCliente.tipoDocAMDOCS}</div>
													</jmesa:htmlColumn>
													<jmesa:htmlColumn property="nroDocFormato" titleKey="riesgoCrediticio.grilla.nroDoc" sortable="false" filterable="false">
														<div align="center" style="width:90px">${itemCliente.nroDocFormato}</div>
													</jmesa:htmlColumn>
													<jmesa:htmlColumn property="genero" titleKey="riesgoCrediticio.grilla.genero" sortable="false" filterable="false">
														<div align="center">${itemCliente.genero}</div>
													</jmesa:htmlColumn>
									                <jmesa:htmlColumn property="scoreCalculadoIN" titleKey="riesgoCrediticio.grilla.scoreCalculadoIn" sortable="false" filterable="false">
														<div align="center">${itemCliente.scoreCalculadoIN}</div>
													</jmesa:htmlColumn>										                
									             	<jmesa:htmlColumn property="fechaCargaScoreIN" titleKey="riesgoCrediticio.grilla.fechaCargaScoreIn" sortable="false" filterable="false">
														<div align="center">${itemCliente.fechaCargaScoreIN}</div>
													</jmesa:htmlColumn>										             			            
									                <jmesa:htmlColumn property="scoreLegadoAM" titleKey="riesgoCrediticio.grilla.scoreLegadoAM" sortable="false" filterable="false">
														<div align="center">${itemCliente.scoreLegadoAM}</div>
													</jmesa:htmlColumn>										                
									             	<jmesa:htmlColumn property="fechaUpdateAM" titleKey="riesgoCrediticio.grilla.fechaUpdateAM" sortable="false" filterable="false">
														<div align="center">${itemCliente.fechaUpdateAM}</div>
													</jmesa:htmlColumn>										             			            
									                <jmesa:htmlColumn property="scoreManual" titleKey="riesgoCrediticio.grilla.scoreManual" sortable="false" filterable="false">
														<div align="center">${itemCliente.scoreManual}</div>
													</jmesa:htmlColumn>										                
									             	<jmesa:htmlColumn property="fechaCargaScoreManual" titleKey="riesgoCrediticio.grilla.fechaCargaScoreManual" sortable="false" filterable="false">
														<div align="center">${itemCliente.fechaCargaScoreManual}</div>
													</jmesa:htmlColumn>										             			            
									                <jmesa:htmlColumn property="usuarioCargaCambio" titleKey="riesgoCrediticio.grilla.usuarioCargaCambio" sortable="false" filterable="false">
														<div align="center">${itemCliente.usuarioCargaCambio}</div>
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
		</div>
	</form:form>
	<form:form id="formRegistro" commandName="registroClienteBean" action="${pageContext.request.contextPath}/editarRegistro.do">
		<form:hidden id="varTipoDoc" path="tipoDoc"/>
		<form:hidden id="varTipoDocAMDOCS" path="tipoDocAMDOCS"/>
		<form:hidden id="varNroDoc" path="nroDoc"/>
		<form:hidden id="varGenero" path="genero"/>
		<form:hidden id="varScoreCalculadoIN" path="scoreCalculadoIN"/>	
		<form:hidden id="varFechaCargaScoreIN" path="fechaCargaScoreIN"/>	
		<form:hidden id="varScoreLegadoAM" path="scoreLegadoAM"/>	
		<form:hidden id="varFechaUpdateAM" path="fechaUpdateAM"/>	
		<form:hidden id="varScoreManual" path="scoreManual"/>
		<form:hidden id="varFechaCargaScoreManual" path="fechaCargaScoreManual"/>
		<form:hidden id="varUsuarioCargaCambio" path="usuarioCargaCambio"/>
		<input id="linkEditarRegistro" type="submit" style="display: none"/>		
	</form:form>		
<script type="text/javascript">

jQuery.fn.modificarDatos = function(tipoDoc, tipoDocAMDOCS, nroDoc, genero, scoreCalculadoIN, fechaCargaScoreIN, scoreLegadoAM, fechaUpdateAM,
		scoreManual, fechaCargaScoreManual, usuarioCargaCambio) {

	$('#varTipoDoc')[0].value=tipoDoc;
	$('#varTipoDocAMDOCS')[0].value=tipoDocAMDOCS;
	$('#varNroDoc')[0].value=nroDoc;
	$('#varGenero')[0].value=genero;							           			
	$('#varScoreCalculadoIN')[0].value=scoreCalculadoIN;
	$('#varFechaCargaScoreIN')[0].value=fechaCargaScoreIN;
	$('#varScoreLegadoAM')[0].value=scoreLegadoAM;
	$('#varFechaUpdateAM')[0].value=fechaUpdateAM;
	$('#varScoreManual')[0].value=scoreManual;
	$('#varFechaCargaScoreManual')[0].value=fechaCargaScoreManual;
	$('#varUsuarioCargaCambio')[0].value=usuarioCargaCambio;

	if ($('#varTipoDoc')[0].value=='' || $('#varNroDoc')[0].value==''){
		alert("Solo se pueden modificar registros que tengan tipo y nro de documento");
	}else{
		$('#linkEditarRegistro').click();
	}
};

</script>
		
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "consultar",
			formId: "formRiesgoCrediticio",		
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "limpiarFormulario",	
			formId: "formRiesgoCrediticio",	
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>

<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "linkEditarRegistro",
			formId: "formRegistro",		
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "gridSubmit",
			formId: "formRiesgoCrediticio",
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
		$('#linkEditarRegistro').click(function(){
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

function validarFechas(){
  	
  	flag=true;
  	fechaDesde = document.getElementById("fechaDesde").value;
	fechaHasta = document.getElementById("fechaHasta").value;

	//Verifico que fechaDesde no este vacio
	if (fechaDesde == ""){
		flag = false;
		mensajeError = document.getElementById("mensajeErrorFechaDesde");
		mensajeError.innerHTML = "se debe especificar fecha Desde";
		mensajeError.style.color = "#FF0000";
		mensajeError.style.visibility = "visible";
		
	}else{
		mensajeError = document.getElementById("mensajeErrorFechaDesde");
		mensajeError.style.visibility = "hidden";
	}
	
	//Verifico que fechaHasta no este vacio
	if(fechaHasta == ""){
		flag = false;
		mensajeError = document.getElementById("mensajeErrorFechaHasta");
		mensajeError.innerHTML = "se debe especificar fecha Hasta";
		mensajeError.style.color = "#FF0000";
		mensajeError.style.visibility = "visible";
	}else{
		mensajeError = document.getElementById("mensajeErrorFechaHasta");
		mensajeError.style.visibility = "hidden";
	}
	
	//Verifico que fechaDesde no sea mayor a fechaHasta
	if(flag == true){
		//Convierto las fechas a Date y hago la resta para sacar la diferencia en dias
	  	var dateFechaDesde = new Date();
	  	var dateFechaHasta = new Date();
		var splFechaDesde = fechaDesde.split("/");
		var splFechaHasta = fechaHasta.split("/");
		
		dateFechaDesde.setFullYear(splFechaDesde[2], splFechaDesde[1]-1, splFechaDesde[0]);
		dateFechaHasta.setFullYear(splFechaHasta[2], splFechaHasta[1]-1, splFechaHasta[0]);
		diferenciaDias = (dateFechaHasta.getTime() - dateFechaDesde.getTime()) / (3600*24*1000);
		
		//Si diferenciaDias es negativo, quiere decir que fecha hasta es menor que fecha desde
		if(diferenciaDias<0){
			flag = false;
			mensajeError = document.getElementById("mensajeErrorFechaDesde");
			mensajeError.innerHTML = "La fecha desde no debe de ser mayor a la fecha hasta";
			mensajeError.style.color = "#FF0000";
			mensajeError.style.visibility = "visible";
			mensajeError = document.getElementById("mensajeErrorFechaHasta");
			mensajeError.innerHTML = "La fecha hasta no debe de ser menor a la fecha desde";
			mensajeError.style.color = "#FF0000";
			mensajeError.style.visibility = "visible";
		}else{
		
			//Si diferenciaDias es mayor a 92, supera el rango permitido
			if(diferenciaDias>92){
				flag = false;
				mensajeError = document.getElementById("mensajeErrorFechaDesde");
				mensajeError.innerHTML = "El maximo intervalo entre fechas debe de ser menor a los 92 dias";
				mensajeError.style.color = "#FF0000";
				mensajeError.style.visibility = "visible";
				mensajeError = document.getElementById("mensajeErrorFechaHasta");
				mensajeError.innerHTML = "El maximo intervalo entre fechas debe de ser menor a los 92 dias";
				mensajeError.style.color = "#FF0000";
				mensajeError.style.visibility = "visible";
			}else{
				mensajeError = document.getElementById("mensajeErrorFechaDesde");
				mensajeError.style.visibility = "hidden";
				mensajeError = document.getElementById("mensajeErrorFechaHasta");
				mensajeError.style.visibility = "hidden";
			}
		}
	}

	if(flag){
		document.forms[0].action="${pageContext.request.contextPath}/bajadaReporteManualCSV.do";
		document.forms[0].submit();
	}else{
		return false;
	}
  }

</script>
<script language=javascript type=text/javascript>
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
			document.getElementById("divSelectGenero").innerHTML ="<select name='genero' id='genero' style='width:105px'><option value='F'>F</option><option value='M'>M</option><option value=''> </option></select>";								
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

<script type="text/javascript">

	$(function() {
		$( "#fechaHasta" ).datepicker();
	});

	$(function() {
 		$( "#fechaDesde" ).datepicker();
	});    
</script>
	
</div>	 	 	