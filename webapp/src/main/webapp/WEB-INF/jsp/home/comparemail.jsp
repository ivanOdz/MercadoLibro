<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
    <link href="${pageContext.request.contextPath}/css/comparemail.css" rel="stylesheet"/>
    <title><spring:message code="compare.emails.title"/></title>
</head>
<body>

<div class="form-container">

	<h1><spring:message code="compare.emails.header"/></h1>
	
	<c:choose>
	    <c:when test="${ownerMail != submited_mail}">
	        <%-- Redirigir a la página de crear un libro --%>
	        <p><spring:message code="compare.emails.different"/></p>
	        <form action="<c:url value='/createpublication'/>" method="get">
	            <input type="hidden" name="publication_id" value="${publication_id}">
	            <input type="hidden" name="is_for_exchange" value="${is_for_exchange}">
	            <input type="hidden" name="submited_mail" value="${submited_mail}">
	            <button type="submit"><spring:message code="compare.emails.createBookButton"/></button>
	        </form>
	    </c:when>
	    <c:otherwise>
	        <p><spring:message code="compare.emails.same"/></p>
	        <%-- Redirigir a página con leyenda, no podes intercambiar con vs mismo, y poner botón para ir a página principal --%>
	        <form action="<c:url value='/'/>" method="get">
	            <button type="submit"><spring:message code="compare.emails.homeButton"/></button>
	        </form>
	    </c:otherwise>
	</c:choose>
</div>
</body>
</html>
