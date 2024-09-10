<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="exchange.accepted"/></title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/WEB-INF/css/exchange.css" rel="stylesheet">
</head>
<body>
<div class="icon-container">
    <i class="material-icons large icon">check_circle</i>
    <div class="message"><fmt:message key="exchange.accepted"/></div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>
