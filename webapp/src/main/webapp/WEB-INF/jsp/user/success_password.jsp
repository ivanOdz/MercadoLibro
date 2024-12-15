<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<head>
    <link href="<c:url value='/css/background.css' />" rel="stylesheet"/>

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <meta charset="UTF-8">

    <title><spring:message code="success"/></title>
</head>
<body>
<c:url var="okUrl" value="/"/>

<div class="uk-background-center-center">
    <div class="uk-position-center uk-card uk-card-default uk-card-body uk-width-1-2@m">
        <h3 class="uk-card-title">
            <spring:message code="hwc.change_password.success"/>
        </h3>
        <a href="${okUrl}">
            <button class="uk-button uk-button-primary uk-align-center">
                <spring:message code="hwc.registration.ok"/>
            </button>
        </a>
    </div>
</div>
</body>
</html>