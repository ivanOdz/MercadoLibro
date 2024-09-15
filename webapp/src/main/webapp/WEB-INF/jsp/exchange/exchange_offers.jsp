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


<div class="uk-grid">
    <div class="uk-width-5-6 uk-align-center main-section">
        <div>
            <h2 class="uk-h2 title"><spring:message code="exchange.offers.title"/></h2>
            <h3 class="uk-h5"><spring:message code="exchange.offers.subtitle"/></h3>
        </div>

        <c:if test="${!(exchanges.size() eq 0)}">
            <div class="main-content">
                <!-- columna de exchanges -->
                <div class="uk-width-3-5 column-exchanges scrollable-content">
                    <c:forEach var="exchange" items="${exchanges}">
                        <div class="uk-card uk-card-default uk-grid-collapse uk-child-width-1-4@s uk-margin exchange-card"
                             onclick="selectCard(this, '${exchange.requesterUsername}', '${exchange.requesterMail}', '${exchange.requesterLocation}', '${exchange.offererBookModel.title}', '${exchange.offererBookAuthor}', '${exchange.offererBookModel.edition}', '${exchange.offererBookImages}')"
                             uk-grid>
                            <div class="uk-card-media-left uk-cover-container">
                                <img class="book-image"
                                     src="${pageContext.request.contextPath}/images/${exchange.requesterBookImages[0].imageId}"
                                     alt="bookImage"/>
                            </div>
                            <div>
                                <div class="uk-card-body">
                                    <h3 class="uk-card-title">${exchange.requesterBookModel.title}</h3>
                                    <p class="uk-text-meta"><fmt:formatDate
                                            value="${exchange.exchange.exchangeStartDate}" pattern="dd/MM/yyyy"/></p>
                                    <c:choose>
                                        <c:when test="${exchange.exchange.exchangeState == 'ACCEPTED'}">
                                            <span class="uk-badge state-approved">Approved</span>
                                        </c:when>
                                        <c:when test="${exchange.exchange.exchangeState == 'REJECTED'}">
                                            <span class="uk-badge state-rejected">Rejected</span>
                                        </c:when>
                                        <c:when test="${exchange.exchange.exchangeState == 'PENDING'}">
                                            <div class="uk-button-group">
                                                <a class="uk-button uk-button-default"
                                                   href="<c:url value='/createexchange'>
                                            <c:param name='accept_code' value='${exchange.exchange.acceptCode}'/>
                                            <c:param name='state' value='true'/>
                                        </c:url>">Accept</a>
                                                <a class="uk-button uk-button-default"
                                                   href="<c:url value='/createexchange'>
                                            <c:param name='accept_code' value='${exchange.exchange.acceptCode}'/>
                                            <c:param name='state' value='false'/>
                                        </c:url>">Reject</a>
                                            </div>
                                            <span class="uk-badge state-pending">Pending</span>
                                        </c:when>
                                        <c:when test="${exchange.exchange.exchangeState == 'TERMINATED'}">
                                            <span class="uk-badge state-inprogress">In Progress</span>
                                        </c:when>
                                    </c:choose>

                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>


                <!-- contenedor derecho donde se ve la info del exchange -->
                <div class="uk-width-2-5" uk-sticky>
                    <div class="uk-container">
                        <div class="uk-card uk-card-default uk-card-body exchange-info-container">
                            <div id="no-selection-message" class="uk-h6">
                                <h4 class="uk-h6">
                                    <spring:message code="exchange.choose.message"/>
                                </h4>
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
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</div>


</body>

<script>
    function selectCard(card, requesterUsername, requesterMail, requesterLocation, offeredBookTitle, offeredBookAuthors, offeredBookEdition, offeredBookImages) {
        // Remover la clase 'selected-card' de todas las tarjetas
        document.querySelectorAll('.exchange-card').forEach(function (el) {
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
        offeredBookImages.forEach(function (imageUrl) {
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
    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('no-selection-message').style.display = 'block';
        document.getElementById('exchange-details').style.display = 'none';
    });
</script>
</html>
