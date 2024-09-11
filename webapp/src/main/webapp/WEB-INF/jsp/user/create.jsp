<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
    <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/background.css" rel="stylesheet"/>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <title><spring:message code="register.head.title"/></title>
</head>

<body>
<c:url var="postUrl" value="/create"/>
<c:url var="signInUrl" value="/login"/>
<div style="display: flex; place-items: center; height: 100%;">
    <div class="uk-container container">
        <h3 style="justify-content: center; margin-left: 30px;">
            <spring:message code="register.title"/>
        </h3>

        <div style="justify-content: center;">
            <form:form action="${postUrl}" method="post" modelAttribute="userForm" class="uk-grid-large uk-grid" style="justify-content: center;">
                <div class="uk-margin" style="justify-content: center">



                    <div class="uk-width-1-1">
                        <div>
                            <label>
                                <spring:message code="hwc.create.username"/>
                            </label>
                        </div>
                        <div class="uk-inline">
                            <span class="uk-form-icon" uk-icon="icon: user"></span>
                            <input class="uk-input" type="text" name="username" aria-label="Not clickable icon"/>
                        </div>
                        <form:errors path="username" element="p" cssStyle="color: red;"/>
                    </div>

                    <div class="uk-width-1-1 uk-margin-top">
                        <div>
                            <label>
                                <spring:message code="hwc.create.mail"/>
                            </label>
                        </div>
                        <div class="uk-inline">
                            <span class="uk-form-icon" uk-icon="icon: mail"></span>
                            <input class="uk-input" type="text" name="mail" aria-label="Not clickable icon"/>
                        </div>
                        <div class="uk-container uk-align-center">
                            <form:errors class="uk-text-small" path="mail" element="p" cssStyle="color: red;"/>
                        </div>
                    </div>

                    <div class="uk-width-1-1 uk-margin-top">
                        <div>
                            <label>
                                <spring:message code="hwc.login.password"/>
                            </label>
                        </div>
                        <div class="uk-inline">
                            <span class="uk-form-icon uk-form-icon-flip" uk-icon="icon: lock"></span>
                            <input class="uk-input" type="password" name="password" aria-label="Not clickable icon"/>
                        </div>
                        <form:errors path="password" element="p" cssStyle="color: red;"/>
                    </div>

                    <div class="uk-width-1-1 uk-margin-top">
                        <div>
                            <label>
                                <spring:message code="hwc.create.confirm_password"/>
                            </label>
                        </div>
                        <div class="uk-inline">
                            <span class="uk-form-icon uk-form-icon-flip" uk-icon="icon: lock"></span>
                            <input class="uk-input" type="password" name="repeatedPassword" aria-label="Not clickable icon"/>
                        </div>
                        <form:errors path="repeatedPassword" element="p" cssStyle="color: red;"/>
                    </div>

                    <c:if test="${!empty userForm.password && !empty userForm.repeatedPassword && userForm.password != userForm.repeatedPassword}">
                        <span class="error" style="color:red">
                            <spring:message code="userForm.passwords.mismatch"/>
                        </span>
                    </c:if>




                    <div>
                        <div class="uk-margin-top uk-button-group" style="margin-left: 50px;">
                            <button class="uk-button uk-button-primary">
                                <spring:message code="hwc.create.submit"/>
                            </button>
                        </div>
                    </div>

                </div>
            </form:form>
        </div>

        <p class="uk-margin" style="text-align: center;">
            <spring:message code="hwc.signin.prompt"/>
        <a href="${signInUrl}" class="uk-link-text link-text"><spring:message code="hwc.signin.button"/></a>
        </p>
    </div>
</div>
</body>
</html>
