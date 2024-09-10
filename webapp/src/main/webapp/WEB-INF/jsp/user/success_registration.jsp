<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>
<body>
<div class="uk-background-primary">



<%--    Por favor revisa tu casilla de correo para verificar tu cuenta.--%>


    <a href="${okUrl}">
        <button type="button">
            <spring:message code="hwc.change_password.button"/>
        </button>
    </a>
</div>
</body>
</html>