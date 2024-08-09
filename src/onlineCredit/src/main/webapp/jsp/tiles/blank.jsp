<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
		<sec:authorize ifAnyGranted="WITHOUT_ROL">
			<spring:message code="noRole"/>
		</sec:authorize>
		
		