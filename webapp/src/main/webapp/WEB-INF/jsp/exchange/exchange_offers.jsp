<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html lang="es" class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/exchange.css" rel="stylesheet"/>

    <title><spring:message code="publications.list.brand.logo"/></title>

</head>
<body class="main">
<navbar/>



</body>
</html>
