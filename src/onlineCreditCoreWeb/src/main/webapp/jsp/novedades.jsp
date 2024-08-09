<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://code.google.com/p/jmesa" prefix="jmesa" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="bodyFragmentChild">
	<form:form id="formNovedades" action="${pageContext.request.contextPath}/paginarNovedades.do">
		<table class="title">
			<tr><td class="	title">
				<spring:message code="novedades.title"/>
			</td></tr>
		</table>
		<br>		
		<c:choose>
			<c:when test="${empty listaNovedades}">
				<table>
					<tr>
						<td><spring:message code="novedades.results.empty"/></td>
					</tr>
				</table>
			</c:when>
			<c:otherwise>
				<c:if test="${not empty listaNovedades}">
					<table BORDER="0" CELLSPACING="9" CELLPADDING="0" WIDTH=570>
						<tbody>
							<tr>
								<td>
									<div class="scroll" >
										<jmesa:tableModel id="tablaNovedades" toolbar="ar.com.latam.jmesa.view.CustomToolBar" items="${listaNovedades}" var="itemNovedad">
									        <jmesa:htmlTable style="width:770px;">               
									            <jmesa:htmlRow>									            					                
													<jmesa:htmlColumn property="tipoDoc" width="90px" titleKey="novedades.grilla.tipoDoc" sortable="false" filterable="false">
														<div align="center">${itemNovedad.tipoDoc}</div>
													</jmesa:htmlColumn>
													<jmesa:htmlColumn property="nroDocFormato" width="90px" titleKey="novedades.grilla.nroDoc" sortable="false" filterable="false">
														<div align="center">${itemNovedad.nroDocFormato}</div>
													</jmesa:htmlColumn>
													<jmesa:htmlColumn property="genero" width="90px" titleKey="novedades.grilla.genero" sortable="false" filterable="false">
														<div align="center">${itemNovedad.genero}</div>
													</jmesa:htmlColumn>																												            
									                <jmesa:htmlColumn property="scoreEnv" titleKey="novedades.grilla.scoreEnviado" sortable="false" filterable="false">
														<div align="center">${itemNovedad.scoreEnv}</div>
													</jmesa:htmlColumn>
													<jmesa:htmlColumn property="descripcionError" titleKey="novedades.grilla.error" sortable="false" filterable="false">
														<div align="center">${itemNovedad.descripcionError}</div>
													</jmesa:htmlColumn>	
													<jmesa:htmlColumn property="fechaCarga" titleKey="novedades.grilla.fechaCarga" sortable="false" filterable="false">
														<div align="center">${itemNovedad.fechaCarga}</div>
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
<script type="text/javascript">
	dojo.addOnLoad(function(){
		Spring.addDecoration(new Spring.AjaxEventDecoration({
			elementId: "gridSubmit",
			formId: "formNovedades",
			event: "onclick", 
			params: { fragments: "body" }
		}));
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
	document.onkeypress = stopEnterKey;
	function stopEnterKey(evt) {
	var evt = (evt) ? evt : ((event) ? event : null);
	var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
	if ((evt.keyCode == 13) && (node.type=="text")) {return false;}
	}
</script>
</div>	 	 	