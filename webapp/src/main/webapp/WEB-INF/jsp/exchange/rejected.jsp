<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>

<!DOCTYPE html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="confirmation.page.title"/></title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>
</head>
<body>
<navbar/>

<div class="uk-container uk-margin-large-top uk-margin-large-bottom">
    <div class="uk-text-center">
        <h1 class="uk-heading-large"><spring:message code="rejected.title"/></h1>
        <p class="uk-text-lead"><spring:message code="rejected.message"/></p>

        <p>
            <a class="uk-button uk-button-primary" href="${pageContext.request.contextPath}/">
                <spring:message code="confirmation.home.button"/>
            </a>
        </p>
    </div>
</div>

</body>
</html>
