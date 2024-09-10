<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<script>
    /*function showExchangeInfo(exchangeId) {
        // Simulación de datos; reemplazar con datos reales del servidor si es necesario
        const exchangeData = {
            '1': 'Detalles del intercambio 1: Este libro fue intercambiado el 15 de agosto.',
            '2': 'Detalles del intercambio 2: Este libro está en proceso de ser intercambiado.',
            '3': 'Detalles del intercambio 3: Este libro fue intercambiado exitosamente.'
        };

        // Actualizar el contenido de la sección izquierda con los detalles del intercambio
        document.getElementById('exchange-info-content').innerText = exchangeData[exchangeId] || 'No hay detalles disponibles para este intercambio.';
    }*/
</script>

<html lang="es">
<head>
    <link href="${pageContext.request.contextPath}/css/publications.css?v=1.0" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/exchange.css ?v=1.0" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <title><spring:message code="exchanges.view.title"/></title>

</head>

<body>
<c:url var="exchangeUrl" value="/exchange"/>
<c:url var="booksUrl" value="/book"/>
<c:url var="profileUrl" value="/profile"/>

<nav class="uk-navbar-container uk-background-primary uk-box-shadow-small" uk-sticky>
    <div class="uk-container">
        <div  uk-navbar>
            <div class="uk-navbar-left">
                <ul class="uk-navbar-nav">
                    <li>
                        <a href="${pageContext.request.contextPath}/">
                            <img src="${pageContext.request.contextPath}/images/mercado_libro.webp" alt="Logo Icon" class="icon-style">
                        </a>
                    </li>
                    <li>
                        <a class="uk-navbar-item uk-logo" href="${pageContext.request.contextPath}/">
                            <strong>
                                <spring:message code="publications.list.brand.logo"/>
                            </strong>
                        </a>
                    </li>
                </ul>
            </div>

            <div class="uk-navbar-center">
                <ul class="uk-navbar-nav">
                    <li>
                        <form class="uk-search uk-search-default custom-search-form" method="get" action="${pageContext.request.contextPath}">
                            <input class="uk-search-input" type="search"
                                   placeholder="<spring:message code='home.search.text'/>"
                                   aria-label="Search"
                                   name="search"
                                   id="search"
                                   value="${param.search != null ? param.search : ''}">
                            <button class="uk-search-icon-flip" uk-search-icon></button>
                        </form>
                    </li>
                </ul>
            </div>
            <div class="uk-navbar-right">
                <ul class="uk-navbar-nav">
                    <li><a class="pl-1 pr-1" href="<c:url value="${exchangeUrl}"/>"><spring:message code="home.exchange.view"/></a></li>
                    <li>
                        <a class="pl-1 pr-1"href="<c:url value="${booksUrl}"/>"><spring:message code="home.book.view"/></a>
                        <div class="uk-navbar-dropdown">
                            <ul class="uk-nav uk-navbar-dropdown-nav">
                                <li class="uk-active uk-margin-small-top">
                                    <a href="<c:url value='${booksUrl}'/>">
                                        <spring:message code="home.book.view.books"/>
                                    </a>
                                </li>
                                <li class="uk-margin-small-top">
                                    <a href="<c:url value='${newBookFromScratch}'/>">
                                        <spring:message code="home.book.view.uploadnew"/>
                                    </a>
                                </li>
                                <li class="uk-margin-small-top">
                                    <a href="<c:url value='${uploadNewPrecharged}'/>">
                                        <spring:message code="home.book.view.uploadnewprecharged"/>
                                    </a>
                                </li>
                            </ul>
                        </div>

                    </li>
                    <li><a class="pl-1 pr-1" href="<c:url value="${profileUrl}"/>"><spring:message code="home.profile.view"/></a></li>
                </ul>
            </div>
        </div>
    </div>
</nav>
<div class="uk-background-muted">
    <div class="uk-container">
        <div class="uk-grid ml-1 uk-margin-top" uk-grid>
            <div class="uk-width-1-3@s exchange-information-section uk-border-rounded uk-box-shadow-small uk-margin-top mb-1 uk-height-viewport" uk-height-viewport="offset-top: true">
                        <%--  TODO: CARD DONDE SE MUESTRA MAS INFORMACION DEL INTERCAMBIO--%>

            </div>

            <div class="uk-width-expand uk-margin-top">
                <c:forEach var="exchange" items="${exchangeWrapperList}">
                    <div class="uk-card uk-card-default uk-card-body uk-border-rounded uk-box-shadow-small mb-1">
                        <!-- Mostrar información del solicitante -->
                        <h3>Intercambio con: ${exchange.requesterUsername}</h3>
                        <p>Email: ${exchange.requesterMail}</p>
                        <p>Ubicación: ${exchange.requesterLocation}</p>

                        <!-- Información del libro del ofertante -->
                        <h4>Libro ofertado:</h4>
                        <p>Título: ${exchange.offererBookModel.title}</p>
                        <p>Autor(es):
                            <c:forEach var="author" items="${exchange.offererBookAuthor}">
                                ${author.name}<c:if test="${!author.last}">, </c:if>
                            </c:forEach>
                        </p>
                        <p>Edición: ${exchange.offererBookModel.edition}</p>

                        <!-- Mostrar imágenes del libro ofertado -->
                        <div uk-grid>
                            <c:forEach var="image" items="${exchange.offererBookImages}">
                                <div class="uk-width-1-4">
                                    <img src="${imageService.getImageUrl(image.imageId)}" class="uk-border-rounded" alt="Imagen del libro">
                                </div>
                            </c:forEach>
                        </div>

                        <!-- Información del libro solicitado -->
                        <h4>Libro solicitado:</h4>
                        <p>Título: ${exchange.requesterBookModel.title}</p>
                        <p>Autor(es):
                            <c:forEach var="author" items="${exchange.requesterBookAuthor}">
                                ${author.name}<c:if test="${!author.last}">, </c:if>
                            </c:forEach>
                        </p>
                        <p>Edición: ${exchange.requesterBookModel.edition}</p>

                        <!-- Mostrar imágenes del libro solicitado -->
                        <div uk-grid>
                            <c:forEach var="image" items="${exchange.requesterBookImages}">
                                <div class="uk-width-1-4">
                                    <img src="${imageService.getImageUrl(image.imageId)}" class="uk-border-rounded" alt="Imagen del libro">
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>

            </div>
            </div>
        </div>
    </div>
</body>
</html>
