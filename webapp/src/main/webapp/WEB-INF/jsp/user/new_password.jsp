<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<body>
<c:url value="/change_password" var="changePasswordUrl"/>
<form action="${changePasswordUrl}" method="post">
    <div>
        <label>
            <spring:message code="hwc.create.password"/>
            <input type="password" name="password"/>
        </label>
    </div>
    <div>
        <label>
            <spring:message code="hwc.create.confirm_password"/>
            <input type="password" name="confirmPassword"/>
        </label>
    </div>

    <div>
        <label>
            <input type="hidden" name="verification_code" value="${verification_code}">
            <input type="submit" value="<spring:message code="hwc.change_password.confirm"/>"/>
        </label>
    </div>
</form>

</body>
</html>
