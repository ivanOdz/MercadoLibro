<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
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
    <link href="${pageContext.request.contextPath}/css/exchange.css?v=1.0" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>

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
                        <a class="pl-1 pr-1" href="<c:url value='${booksUrl}'/>">
                            <spring:message code="home.book.view"/>
                        </a>
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
            <!-- Primera columna que ocupa 1/3 del ancho -->
            <div id="exchange-info uk-padding-small" class="uk-width-1-3@s exchange-information-section uk-card uk-card-default uk-border-rounded uk-box-shadow-small uk-margin-top mb-1 uk-height-viewport uk-margin-bottom" uk-height-viewport="offset-top: true">
                <!-- Contenedor para el mensaje cuando no se ha seleccionado ninguna tarjeta -->
                <div id="no-selection-message" class="uk-text-center uk-margin-large-top">
                    <p>Por favor, haga clic en alguna tarjeta para mostrar más información.</p>
                </div>

                <!-- Contenedor para la información del intercambio -->
                <div id="exchange-details" style="display: none;">
                    <h3 id="info-requester-username">Intercambio con: </h3>
                    <p id="info-requester-mail">Email: </p>
                    <p id="info-requester-location">Ubicación: </p>

                    <h4>Libro ofertado:</h4>
                    <p id="info-offered-book-title">Título: </p>
                    <p id="info-offered-book-authors">Autor(es): </p>
                    <p id="info-offered-book-edition">Edición: </p>

                    <div id="info-offered-book-images" uk-grid></div>
                </div>
            </div>

            <!-- Segunda columna que ocupa el resto del espacio disponible -->
            <div class="uk-width-expand uk-margin-top uk-overflow-auto" style="max-height: 80vh;">
                <!-- Contenedor desplazable con altura máxima y ancho completo -->
                <c:forEach var="exchange" items="${exchangeWrapperList}">
                    <div class="uk-width-1-1 uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded uk-box-shadow-small uk-margin-small-bottom exchange-card"
                         onclick="selectCard(this, '${exchange.requesterUsername}', '${exchange.requesterMail}', '${exchange.requesterLocation}', '${exchange.offererBookModel.title}', '${exchange.offererBookAuthor}', '${exchange.offererBookModel.edition}', '${exchange.offererBookImages}')">
                        <h3 class="uk-card-title">Intercambio con: ${exchange.requesterUsername}</h3>
                        <p>Email: ${exchange.requesterMail}</p>
                        <p>Ubicación: ${exchange.requesterLocation}</p>

                        <h4>Libro ofertado:</h4>
                        <p>Título: ${exchange.offererBookModel.title}</p>
                        <p>Autor(es):
                            <c:forEach var="author" items="${exchange.offererBookAuthor}">
                                ${author}
                            </c:forEach>
                        </p>
                        <p>Edición: ${exchange.offererBookModel.edition}</p>

                        <div class="uk-position-relative uk-visible-toggle uk-light" tabindex="-1" uk-slideshow>

                            <div class="uk-slideshow-items">
                                <c:forEach var="image" items="${exchange.offererBookImages}">
                                    <div>
                                        <img src="${pageContext.request.contextPath}/images/${image.imageId}" alt="bookImage" uk-cover>
                                    </div>
                                </c:forEach>
                            </div>

                            <a class="uk-position-center-left uk-position-small uk-hidden-hover"  uk-slidenav-previous uk-slideshow-item="previous"></a>
                            <a class="uk-position-center-right uk-position-small uk-hidden-hover" uk-slidenav-next uk-slideshow-item="next"></a>

                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

</body>

<script>
    function selectCard(card, requesterUsername, requesterMail, requesterLocation, offeredBookTitle, offeredBookAuthors, offeredBookEdition, offeredBookImages) {
        // Remover la clase 'selected-card' de todas las tarjetas
        document.querySelectorAll('.exchange-card').forEach(function(el) {
            el.classList.remove('selected-card');
        });

        // Agregar la clase 'selected-card' a la tarjeta clickeada
        card.classList.add('selected-card');

        // Mostrar la sección de detalles y ocultar el mensaje de selección
        document.getElementById('no-selection-message').style.display = 'none';
        document.getElementById('exchange-details').style.display = 'block';

        // Actualizar la información en la columna izquierda
        document.getElementById('info-requester-username').textContent = 'Intercambio con: ' + requesterUsername;
        document.getElementById('info-requester-mail').textContent = 'Email: ' + requesterMail;
        document.getElementById('info-requester-location').textContent = 'Ubicación: ' + requesterLocation;
        document.getElementById('info-offered-book-title').textContent = 'Título: ' + offeredBookTitle;
        document.getElementById('info-offered-book-authors').textContent = 'Autor(es): ' + offeredBookAuthors;
        document.getElementById('info-offered-book-edition').textContent = 'Edición: ' + offeredBookEdition;

        // Limpiar imágenes anteriores
        const imageContainer = document.getElementById('info-offered-book-images');
        imageContainer.innerHTML = '';

        // Añadir imágenes del libro ofertado
        offeredBookImages.forEach(function(imageUrl) {
            const imgElement = document.createElement('img');
            imgElement.src = imageUrl;
            imgElement.className = 'uk-border-rounded';
            imgElement.alt = 'Imagen del libro';
            imgElement.style.width = '100%'; // Asegúrate de que las imágenes se ajusten bien
            const divElement = document.createElement('div');
            divElement.className = 'uk-width-1-4';
            divElement.appendChild(imgElement);
            imageContainer.appendChild(divElement);
        });
    }

    // Inicialmente, mostrar el mensaje de selección
    document.addEventListener('DOMContentLoaded', function() {
        document.getElementById('no-selection-message').style.display = 'block';
        document.getElementById('exchange-details').style.display = 'none';
    });
</script>

</html>






<!-- Información del libro solicitado -->
<%--                        <h4>Libro solicitado:</h4>--%>
<%--                        <p>Título: ${exchange.requesterBookModel.title}</p>--%>
<%--                        <p>Autor(es):--%>
<%--                            <c:forEach var="author" items="${exchange.requesterBookAuthor}">--%>
<%--                                ${author.authorName}--%>
<%--                            </c:forEach>--%>
<%--                        </p>--%>
<%--                        <p>Edición: ${exchange.requesterBookModel.edition}</p>--%>
<%--    --%>
<!-- Mostrar imágenes del libro solicitado -->
<%--                        <div uk-grid>--%>
<%--                            <c:forEach var="image" items="${exchange.requesterBookImages}">--%>
<%--                                <div class="uk-width-1-4">--%>
<%--                                    <img src="${image.imageId}" class="uk-border-rounded" alt="Imagen del libro">--%>
<%--                                </div>--%>
<%--                            </c:forEach>--%>
<%--                        </div>--%>

