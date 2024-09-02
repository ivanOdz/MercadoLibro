<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Compare Emails</title>
</head>
<body>
<h1>Email Comparison</h1>

<c:choose>
    <c:when test="${ownerMail != solicitingEmail}">
        <%--Redirigir a la pagina de crear un libro--%>


    </c:when>
    <c:otherwise>
        <p>The email you provided matches the owner's email.</p>
        <%-- Redirigir a pagina con leyenda, no puedes intercambiar contigo mismo, y poner boton para ir a pagina principal --%>
        <form action="<c:url value='/'/>" method="get">
            <button type="submit">Volver a pagina principal</button>
        </form>
    </c:otherwise>
</c:choose>

</body>
</html>
