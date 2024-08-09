<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<script>
	$.ajaxSetup({
		 cache: false
		
	 });
</script>

<div class="menuContainer">

 <div class="menu">

	<ul id="mainUL">
		<!-- TIPOS DOCUMENTO -->
		<sec:authorize ifAnyGranted="ROLE_ADMIN_TDOC" >
			<li>
				<a href="#" onclick="return false;"><spring:message code="tipos.documento.title"/></a> 
				<ul> 
					<sec:authorize ifAnyGranted="ROLE_ADMIN_TDOC">
						<li>
							<a id="conversionAnchor"  href="#" 
							tabindex="-1"><spring:message code="conversion.title"/></a>
						</li> 
					</sec:authorize>
				</ul> 
			</li> 
		</sec:authorize>
		
		<!-- RIESGO CREDITICIO -->
		<sec:authorize ifAnyGranted="ROLE_CRED_MODIF,ROLE_CRED_CONS,ROLE_CRED_ALTA" >
			<li>
				<a href="#" onclick="return false;"><spring:message code="riesgo.crediticio.title"/></a> 
				<ul> 
					<sec:authorize ifAnyGranted="ROLE_CRED_ALTA">
						<li>
							<a id="altaAnchor"  href="#" 
							tabindex="-1"><spring:message code="alta.dni.title"/></a>
						</li> 
					</sec:authorize>
					<sec:authorize ifAnyGranted="ROLE_CRED_CONS,ROLE_CRED_MODIF">
						<li>
							<a id="consultaModifAnchor"  href="#" 
							tabindex="-1"><spring:message code="modificacion.score.title"/></a>
						</li> 
					</sec:authorize>
					<sec:authorize ifAnyGranted="ROLE_CRED_CONS,ROLE_CRED_MODIF">
						<li>
							<a id="consultaHistAnchor"  href="#" 
							tabindex="-1"><spring:message code="consultaHist.score.title"/></a>
						</li> 
					</sec:authorize>										
				</ul>				 
			</li> 
		</sec:authorize>		

		<!-- NOVEDADES -->
		<sec:authorize ifAnyGranted="ROLE_NOVEDAD" >
			<li>
				<a href="#" onclick="return false;"><spring:message code="novedades.title"/></a> 
				<ul> 
					<sec:authorize ifAnyGranted="ROLE_NOVEDAD">
						<li>
							<a id="consultaNovedadesAnchor"  href="#" 
							tabindex="-1"><spring:message code="consulta.title"/></a>
						</li> 
					</sec:authorize>
				</ul> 
			</li> 
		</sec:authorize>
	</ul>		
</div>
	
	
	
</div>
<script type="text/javascript">
	var isIE 	= $.browser.msie,
	isIE6	= isIE && $.browser.version < 7;
	if(isIE6){
		ul = document.getElementById("mainUL");
		ul.className="dropmenu";
		dojo.addOnLoad(function(){
			$('.dropmenu').dropmenu({
					openAnimation: "size",
					closeAnimation: "slide",
					openSpeed: 300,
					closeSpeed: 200,
					closeDelay: 400,
					zindex: 100,
					openMenuClass: 'open',
					autoAddArrowElements: true
				});
		});
	}else{
		dojo.addOnLoad(function(){
		$('.menu').lksMenu();
		});
	}
	
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#conversionAnchor').click(function(){
			$("#bodyFragment").load("rc/tiposDocumento.do");
		});
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#altaAnchor').click(function(){
			$("#bodyFragment").load("rc/formAltaRiesgoCrediticio.do");
		});
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#consultaModifAnchor').click(function(){
			$("#bodyFragment").load("rc/formConsultaModifRiesgoCrediticio.do");
		});
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#consultaHistAnchor').click(function(){
			$("#bodyFragment").load("rc/formConsultaRiesgoCrediticioHist.do");
		});
	});
</script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#consultaNovedadesAnchor').click(function(){
			$("#bodyFragment").load("rc/novedades.do");
		});
	});
</script>
<br>
