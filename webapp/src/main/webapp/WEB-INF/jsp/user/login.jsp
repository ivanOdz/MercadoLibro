<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
    <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet"/>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <title><spring:message code="login.head.title"/></title>

</head>
<body>
<c:url value="/login" var="loginUrl"/>
<c:url value="/create" var="signUpUrl"/>
<c:url value="/mail_input" var="changePasswordUrl"/>
<div class="uk-container container">

    <div class="custom-logo">
        <img src="${pageContext.request.contextPath}/images/mercado_libro.jpg" alt="mercado_libro" class="custom-logo-image"/>
    </div>

    <h2 style="justify-content: center; margin-left: 60px;"><spring:message code="login.title"/></h2>
    <div style="justify-content: center;">
    <form action="${loginUrl}" method="post" class="uk-grid-large" uk-grid style="justify-content: center;">
        <div class="uk-margin" style="justify-content: center">
            <div class="uk-width-1-1" >
                <div>
                    <label>
                        <spring:message code="hwc.login.username"/>
                    </label>
                </div>

                <div class="uk-inline">
                    <span class="uk-form-icon" uk-icon="icon: user"></span>
                    <input  class="uk-input" type="text" name="username" aria-label="Not clickable icon"/>
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
            </div>

            <div class="uk-margin-top" style="text-align: center;">
                <label>
                    <spring:message code="hwc.login.remember_me"/>
                    <input class="uk-checkbox" type="checkbox" name="remember_me"/>
                </label>
            </div>
            <div class="uk-margin-top uk-button-group" style="margin-left: 50px;">
                <button class="uk-button uk-button-primary"> <spring:message code="hwc.login.submit"/> </button>
            </div>
        </div>

    </form>
    </div>

    <div class="uk-margin" style="text-align: center;">
        <a class="uk-link-text link-text" href="${changePasswordUrl}">
                <spring:message code="hwc.change_password.button"/>
        </a>
    </div>
    <div class="uk-margin" style="text-align: center;">
        <p><spring:message code="hwc.signup.prompt"/><a class="uk-link-text link-text" href="${signUpUrl}">
            <spring:message code="hwc.signup.button"/>
        </a></p>

    </div>


</div>

</body>
</html>
