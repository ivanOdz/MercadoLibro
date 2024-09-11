<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<body>
<h1><spring:message code="hwc.profile.title"/></h1>
<h2><spring:message code="hwc.profile.greeting" arguments="${username}" htmlEscape="true"/></h2>
<h5><spring:message code="hwc.profile.user_id" arguments="${userId}" htmlEscape="true"/></h5>
</body>
</html>
