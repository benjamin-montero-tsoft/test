<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="bodyFragmentChild">
	<form:form id="formTiposDoc" action="${pageContext.request.contextPath}/paginarTiposDoc.do">
		<table class="title">
			<tr><td class="	title">
				<spring:message code="tiposDocumento.title"/>
			</td></tr>
		</table>
		<table>
			<tr>
				<td>
					<br>
					&nbsp;&nbsp;<input id="irAlta" type="submit" value="<spring:message code="button.irAlta"/>" onclick="$('#formTiposDoc')[0].action='${pageContext.request.contextPath}/formAltaTipoDoc.do'"/>
				</td>
			</tr>
		</table>	
		<c:choose>
			<c:when test="${empty listaTiposDoc}">
				<table>
					<tr>
						<td><spring:message code="tiposDocumento.results.empty"/></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<c:if test="${not empty listaTiposDoc}">
					<table BORDER="0" CELLSPACING="9" CELLPADDING="0" WIDTH=570>
						<tbody>
							<tr>
								<td>
									<div class="scroll" >
										<jmesa:tableModel id="tablaTiposDoc" toolbar="ar.com.latam.jmesa.view.CustomToolBar" items="${listaTiposDoc}" var="itemTiposDoc">
									        <jmesa:htmlTable style="width: 360px;">               
									            <jmesa:htmlRow>									            
									                <jmesa:htmlColumn titleKey="" sortable="false" filterable="false">
									               		<table>
									               			<tr>
									               				<td nowrap="nowrap"> 
											               			<a href="#" onclick="$(this).modificarDatos('${itemTiposDoc.tipoDocDW}','${itemTiposDoc.tipoDocDWBackup}',
																			'${itemTiposDoc.tipoDocAmdoc}','${itemTiposDoc.tipoDocAmdocBackup}','${itemTiposDoc.cargaGenero}','${itemTiposDoc.fechaCarga}');">
													            		<img src="${pageContext.request.contextPath}/images/editar2.gif" 
													            			 title="EDITAR REGISTRO" 
													            			 style="border:0px;width:15px;height:15px;"/></a>
											               			<a href="#" onclick="$(this).eliminarDatos('${itemTiposDoc.tipoDocDW}','${itemTiposDoc.tipoDocAmdoc}');">
													            		<img src="${pageContext.request.contextPath}/images/equis.png" 
													            			 title="ELIMINAR REGISTRO" 
													            			 style="border:0px;width:15px;height:15px;"/></a>
											            		</td>
											            	</tr>
											            </table>												            			 
									                </jmesa:htmlColumn>									                
													<jmesa:htmlColumn property="tipoDocDW" width="90px" titleKey="tiposDocumento.grilla.tipoDocDW" sortable="false" filterable="false">
														<div align="center">${itemTiposDoc.tipoDocDW}</div>
													</jmesa:htmlColumn>
													<jmesa:htmlColumn property="tipoDocAmdoc" width="90px" titleKey="tiposDocumento.grilla.tipoDocAmdoc" sortable="false" filterable="false">
														<div align="center">${itemTiposDoc.tipoDocAmdoc}</div>
													</jmesa:htmlColumn>
													<jmesa:htmlColumn property="cargaGenero" width="90px" titleKey="tiposDocumento.grilla.cargaGenero" sortable="false" filterable="false">
														<div align="center">${itemTiposDoc.cargaGenero}</div>
													</jmesa:htmlColumn>																											            
									                <jmesa:htmlColumn property="fechaCarga" titleKey="tiposDocumento.grilla.fechaCarga" sortable="false" filterable="false">
														<div align="center">${itemTiposDoc.fechaCarga}</div>
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
	</form:form>
	<form:form id="formRegistro" commandName="registroTipoDocBean">
		<form:hidden id="varTipoDocDW" path="tipoDocDW"/>
		<form:hidden id="varTipoDocDWBackup" path="tipoDocDWBackup"/>
		<form:hidden id="varTipoDocAmdoc" path="tipoDocAmdoc"/>
		<form:hidden id="varTipoDocAmdocBackup" path="tipoDocAmdocBackup"/>
		<form:hidden id="varCargaGenero" path="cargaGenero"/>
		<form:hidden id="varFechaCarga" path="fechaCarga"/>
		<input id="linkEliminarRegistro" type="submit" style="display: none" onclick="$('#formRegistro')[0].action='${pageContext.request.contextPath}/eliminarRegistroTipoDoc.do'"/>
		<input id="linkEditarRegistro" type="submit" style="display: none" onclick="$('#formRegistro')[0].action='${pageContext.request.contextPath}/editarRegistroTipoDoc.do'"/>		
	</form:form>		
<script type="text/javascript">
jQuery.fn.modificarDatos = function(tipoDocDW, tipoDocDWBackup, tipoDocAmdoc, tipoDocAmdocBackup, cargaGenero, fechaCarga) {

	$('#varTipoDocDW')[0].value=tipoDocDW;
	$('#varTipoDocDWBackup')[0].value=tipoDocDWBackup;
	$('#varTipoDocAmdoc')[0].value=tipoDocAmdoc;
	$('#varTipoDocAmdocBackup')[0].value=tipoDocAmdocBackup;
	$('#varCargaGenero')[0].value=cargaGenero;
	$('#varFechaCarga')[0].value=fechaCarga;

	$('#linkEditarRegistro').click();
};
</script>
<script type="text/javascript">
jQuery.fn.eliminarDatos = function(tipoDocDW, tipoDocAmdoc) {

	$('#varTipoDocDW')[0].value=tipoDocDW;
	$('#varTipoDocAmdoc')[0].value=tipoDocAmdoc;
	
	var state = confirm("CONFIRMAR ELIMINACION");
	if (state) {
		$('#linkEliminarRegistro').click();
	}
};
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
			elementId: "linkEliminarRegistro",
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
			formId: "formTiposDoc",
			event: "onclick", 
			params: { fragments: "body" }
		}));
	});
</script>	
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "irAlta",
			formId: "formTiposDoc",
			event: "onclick", 
			params: { fragments: "body" }
		}));
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
		$('#linkEliminarRegistro').click(function(){
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
<script type="text/javascript">
	$(document).ready(function() {
		$('#irAlta').click(function(){
			$('#inline').trigger('click');
		});
	});
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