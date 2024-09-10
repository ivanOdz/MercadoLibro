
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<body>
<c:url var="postUrl" value="/create"/>
<c:url var="signInUrl" value="/login"/>
<form:form action="${postUrl}" method="post" modelAttribute="userForm">
    <div>
        <label>
            <spring:message code="hwc.create.username"/>
            <form:input path="username" type="text"/>
        </label>
        <form:errors path="username" element="p" cssStyle="color: red;"/>
    </div>
    <div>
        <label>
            <spring:message code="hwc.create.mail"/>
            <form:input path="mail" type="text"/>
        </label>
        <form:errors path="mail" element="p" cssStyle="color: red;"/>
    </div>
    <div>
        <label>
            <spring:message code="hwc.create.password"/>
            <form:input path="password" type="password"/>
        </label>
        <form:errors path="password" element="p" cssStyle="color: red;"/>
    </div>
    <div>
        <label>
            <spring:message code="hwc.create.confirm_password"/>
            <form:input path="repeatedPassword" type="password"/>
        </label>
        <form:errors path="repeatedPassword" element="p" cssStyle="color: red;"/>
    </div>


    <c:if test="${!empty userForm.password && !empty userForm.repeatedPassword && userForm.password != userForm.repeatedPassword}">
        <span class="error" style="color:red"><spring:message code="userForm.passwords.mismatch"/></span>
    </c:if>

    <div>
        <label>
            <input type="submit"/>
        </label>
    </div>


</form:form>

<p><spring:message code="hwc.signin.prompt"/>:</p>
<a href="${signInUrl}">
    <button type="button">
        <spring:message code="hwc.signin.button"/>
    </button>
</a>
</body>
</html>
