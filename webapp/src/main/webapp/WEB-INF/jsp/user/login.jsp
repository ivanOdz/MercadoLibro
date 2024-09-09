<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>
<body>
<c:url value="/login" var="loginUrl"/>
<c:url value="/create" var="signUpUrl"/>
<c:url value="/mail_input" var="changePasswordUrl"/>
<form action="${loginUrl}" method="post">
    <div>
        <label>
            <spring:message code="hwc.login.username"/>
            <input type="text" name="username"/>
        </label>
    </div>
    <div>
        <label>
            <spring:message code="hwc.login.password"/>
            <input type="password" name="password"/>
        </label>
    </div>
    <div>
        <label>
            <spring:message code="hwc.login.remember_me"/>
            <input type="checkbox" name="remember_me"/>
        </label>
    </div>
    <div>
        <label>
            <input type="submit" value="<spring:message code="hwc.login.submit"/>"/>
        </label>
    </div>
</form>


    <p><spring:message code="hwc.signup.prompt"/>:</p>
    <a href="${signUpUrl}">
        <button type="button">
            <spring:message code="hwc.signup.button"/>
        </button>
    </a>

    <a href="${changePasswordUrl}">
        <button type="button">
            <spring:message code="hwc.change_password.button"/>
        </button>
    </a>

</body>
</html>