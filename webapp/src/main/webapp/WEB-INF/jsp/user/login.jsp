<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<head>
    <link href="<c:url value='/css/login.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/css/background.css' />" rel="stylesheet"/>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <title><spring:message code="login.head.title"/></title>

</head>
<body>
<c:url value="/login" var="loginUrl"/>
<c:url value="/create" var="signUpUrl"/>
<c:url value="/mail_input" var="changePasswordUrl"/>
<div style="display: flex; place-items: center; height: 100%;">
    <div class="uk-container container uk-align-center">
        <div class="mini_container uk-align-center" style="max-width: 30%;">
            <a href="<c:url value='/' />">
                <img src="<c:url value='/images/logo_w_name.png' />" alt="Logo Icon"
                     class="icon-style">
            </a>
        </div>

        <h2 style="text-align: center;"><spring:message code="login.title"/></h2>
        <div style="justify-content: center;">
            <form action="${loginUrl}" method="post" class="uk-grid-large uk-grid" style="justify-content: center;">
                <div class="uk-margin" style="justify-content: center">
                    <div class="uk-width-1-1">
                        <div>
                            <label>
                                <spring:message code="hwc.login.username"/>
                            </label>
                        </div>

                        <div class="uk-inline">
                            <span class="uk-form-icon" uk-icon="icon: user"></span>
                            <input class="uk-input" type="text" name="username" aria-label="Not clickable icon"/>
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

                    <c:if test="${not empty error}">
                        <div class="uk-text-small" style="color:red;">
                                ${error}
                        </div>
                    </c:if>

                    <div class="uk-margin-top uk-button-group" style="margin-left: 50px;">
                        <button class="uk-button uk-button-primary"><spring:message code="hwc.login.submit"/></button>
                    </div>
                </div>

            </form>
        </div>

        <div style="text-align: center;">
            <a class="uk-link-text link-text uk-align-center" href="${changePasswordUrl}">
                <spring:message code="hwc.change_password.button"/>
            </a>
        </div>
        <div class="uk-margin" style="text-align: center;">
            <p><spring:message code="hwc.signup.prompt"/><a class="uk-link-text link-text" href="${signUpUrl}">
                <spring:message code="hwc.signup.button"/>
            </a></p>

        </div>


    </div>
</div>
</body>
</html>