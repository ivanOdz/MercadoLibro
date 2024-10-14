<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="/WEB-INF/jsp/components/navbar_empty.jsp" %>


<html class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="<c:url value='/css/navbar.css?v=1.0' />" rel="stylesheet"/>
    <link href="<c:url value='/css/login.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/css/background.css' />" rel="stylesheet"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

    <title><spring:message code="publication.details.title"/></title>
</head>

<body>
<navbar/>

<c:url var="registerUrl" value="/create"/>
<c:url var="signInUrl" value="/login"/>

<div>
    <div style="margin-top: 1%; margin-left: 1%;">
        <a class="uk-button uk-button-text" href="<c:url value='/' />">
            <span uk-icon="icon:  chevron-left"></span>
            <spring:message code="add.book.return_home"/>
        </a>
    </div>
    <div class="uk-container container uk-align-center" style="max-width: 25%;">
        <h2 style="text-align: center; margin-bottom: 10%;"><spring:message code="must.login.title"/></h2>

        <form action="${registerUrl}" method="post">
            <div>
                <div class="uk-margin-top uk-button-group">
                    <button class="uk-button uk-button-primary">
                        <spring:message code="hwc.create.submit"/>
                    </button>
                </div>
            </div>
        </form>

        <p class="uk-margin" style="text-align: center;">
            <a href="${signInUrl}" class="uk-link-text link-text"><spring:message code="hwc.signin.button"/></a>
        </p>

    </div>
</div>
</div>


</body>
</html>
