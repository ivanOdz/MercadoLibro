<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html lang="es">
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
  <title><spring:message code="publication.details.title"/></title>
</head>
<body>
<div style="border: 1px solid #000; padding: 10px; margin: 10px; display: inline-block; width: 200px; vertical-align: top;">
  <h2><spring:message code="publication.details.publicationId"/>: ${publication.publicationId}</h2>
  <p><strong><spring:message code="publication.details.bookId"/>:</strong> ${publication.bookId}</p>
  <p><strong><spring:message code="publication.details.userId"/>:</strong> ${publication.userId}</p>
  <p><strong><spring:message code="publication.details.publicationState"/>:</strong> ${publication.publicationState}</p>
  <p><strong><spring:message code="publication.details.location"/>:</strong> ${publication.locationId}</p>
</div>
</body>
</html>


<%--Por favor ingrese su mail:--%>
<%--<BUTTON>SUMBIT</BUTTON>--%>
<%-- Cuando haga post, envio el mail ingresado. Eso lo tomo, y llamo a otro metodo para que le pase a otra vista--%>
<%-- el mail ingresado, y el mail asociado al al libro de la publicacion que fue seleccionada.--%>
<%-- Ahi lo que hago es un c:if, si mailIngresado != mailLibro -> me voy a crear un libro --%>
<%-- Si mailIngresado = mailLibro -> rechazo la operacion y hago un redirect a pagina principal /   --%>


<%-- if(mail(libro(publicacion id))) = mail ingresado){--%>
<%--  no puede intercambiar libro con usted mismo  --%>
<%--}--%>
<%--else{--%>
<%--sumbit del libro--%>
<%--  enviar mail(libro(publicacion id)))--%>
<%--}--%>
