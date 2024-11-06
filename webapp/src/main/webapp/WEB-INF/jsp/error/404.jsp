<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>

<html lang="es" class="custom-style">
<head>
    <meta charset="UTF-8">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <link href="<c:url value='/css/navbar.css?v=1.0' />" rel="stylesheet"/>
    <link href="<c:url value='/css/profile.css?v=1.0' />" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <script src="https://unpkg.com/@dotlottie/player-component@2.7.12/dist/dotlottie-player.mjs" type="module"></script>

    <title>404</title>
</head>

<body style="background-color: #f8f8f8;">

<navbar_wo_search></navbar_wo_search>

<div style="display: flex; flex-direction: column; align-items: center; text-align: center; height: 100vh; justify-content: center;">
    <h1><spring:message code="error.404.header" /></h1>
    <p><spring:message code="error.404.message" /></p>

    <p><spring:message code="error.404.suggestion" arguments="${pageContext.request.contextPath}/" /></p>


    <dotlottie-player src="https://lottie.host/faf7e913-2cc4-4314-ab85-74adc73afeed/YsWXivAusz.json" background="transparent" speed="1" style="width: 500px; height: 500px;" loop autoplay></dotlottie-player>
</div>
</body>
</html>
