<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <script src="https://unpkg.com/@dotlottie/player-component@2.7.12/dist/dotlottie-player.mjs" type="module"></script>
    <link href="<c:url value='/css/navbar.css?v=1.0' />" rel="stylesheet"/>

    <title><spring:message code="confirmation.page.title"/></title>
</head>
<body>

<navbar/>

<div style="display: flex; flex-direction: column; align-items: center; text-align: center; height: 100vh; justify-content: center;">
    <h1><spring:message code="accepted.title"/></h1>
    <p class="uk-text-lead"><spring:message code="accepted.message"/></p>

    <dotlottie-player src="https://lottie.host/d04f2d1e-b1c1-4e83-9599-12173714af0f/MlWnatYjoD.json" background="transparent" speed="1" style="width: 300px; height: 300px;" loop autoplay></dotlottie-player>
    <p>
        <a class="uk-button uk-button-primary" href="<c:url value='/' />">
            <spring:message code="confirmation.home.button"/>
        </a>
    </p>
</div>

</body>
</html>
