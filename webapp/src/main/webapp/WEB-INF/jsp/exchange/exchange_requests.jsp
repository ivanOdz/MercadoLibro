<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>


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

    <title><spring:message code="exchanges.view.title"/></title>

</head>

<body>
<navbar></navbar>

<div class="uk-grid">
    <div class="uk-width-5-6 uk-align-center main-section">
        <div>
            <h2 class="uk-h2 title"><spring:message code="exchange.requests.title"/></h2>
            <h3 class="uk-h5"><spring:message code="exchange.requests.subtitle"/></h3>
        </div>

        <div class="main-content">

            <!-- columna de exchanges -->
            <div class="uk-width-3-5 column-exchanges scrollable-content">
                <div>
                    <ul uk-tab>
                        <li class="uk-active"><a href="#"><spring:message code="exchange.status.pending"/> </a></li>
                        <li><a href="#"><spring:message code="exchange.status.in_progress"/></a></li>
                        <li><a href="#"><spring:message code="exchange.status.terminated"/></a></li>
                        <li><a href="#"><spring:message code="exchange.status.rejected"/></a></li>
                    </ul>
                    <ul class="uk-switcher uk-margin">
                        <!-- Pending -->
                        <li class="uk-container uk-align-center">
                            <c:if test="${!empty pending}">
                                <c:forEach var="pending" items="${pending}">
                                    <div class="uk-card uk-card-default exchange-card"
                                         onclick="selectCard(this,
                                                 '<c:out value="${pending.requester.book.owner.username}"/>',
                                                 '<c:out value="${pending.requester.book.owner.mail}"/>',
                                                 '<c:out value="${pending.requester.location}"/>',
                                                 '<c:out value="${pending.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${pending.offerer.book.bookModel.authors}"/>',
                                                 '<c:out value="${pending.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${pending.offerer.book.images}"/>',
                                                 '<c:out value="${pending.exchangeId}"/>',
                                                 '<c:out value="${pending.requester.book.owner.userId}"/>',
                                                 '<c:out value="${pending.offerer.book.owner.userId}"/>',
                                                 '${pending.isReviewable}')"
                                         uk-grid>


                                        <div style="padding: 0">
                                            <img class="book-image"
                                                 src="${pageContext.request.contextPath}/images/${pending.offerer.book.images[0]}"
                                                 alt="bookImage"/>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${pending.offerer.book.bookModel.title}"/>
                                                </h3>
                                            </div>
                                        </div>
                                        <div class="arrow-icon" style="padding: 0">
                                            <span uk-icon="icon: chevron-double-right; ratio: 2"></span>
                                        </div>
                                        <div style="padding: 0">
                                            <img class="book-image"
                                                 src="${pageContext.request.contextPath}/images/${pending.requester.book.images[0]}"
                                                 alt="bookImage"/>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${pending.requester.book.bookModel.title}"/>
                                                </h3></div>
                                        </div>

                                        <div style="padding-left: 0;width:40%; margin-bottom: 15px; display: flex; flex-direction: column; align-items: center">
                                            <div class="uk-button-group">
                                                <a class="uk-button uk-button-default uk-button-small"
                                                   href="#modal-exchange-accepted-${pending.acceptCode}"
                                                   onclick="event.stopPropagation()" uk-toggle>
                                                    <spring:message code="exchange.button.accept"/>
                                                </a>
                                                <a class="uk-button uk-button-default uk-button-small custom-link"
                                                   href="#modal-exchange-rejected-${pending.acceptCode}"
                                                   onclick="event.stopPropagation()" uk-toggle>
                                                    <spring:message code="email.rejectButton"/>
                                                </a>


                                            </div>
                                            <span class="uk-badge state-pending">
                                                        <spring:message code="exchange.status.pending"/>
                                                    </span>
                                            <fmt:formatDate
                                                    value="${pending.exchangeStartDate}"
                                                    pattern="dd/MM/yyyy"/>
                                        </div>


                                        <!-- Exchange Rejected modal -->

                                        <div id="modal-exchange-rejected-${pending.acceptCode}"
                                             uk-modal>
                                            <div class="uk-modal-dialog uk-modal-body">
                                                <h3 class="uk-h4"><spring:message
                                                        code="exchange.rejection.title"/></h3>
                                                <p class="uk-text-right">
                                                    <button class="uk-button uk-button-default uk-modal-close"
                                                            type="button"><spring:message
                                                            code="button.cancel"/></button>
                                                    <button class="uk-button uk-button-danger"
                                                            type="button">
                                                        <a href="<c:url value='/createexchange'>
                                                        <c:param name='accept_code' value='${pending.acceptCode}'/>
                                                        <c:param name='state' value='false'/>
                                                            </c:url>">
                                                            <spring:message
                                                                    code="button.confirm"/>
                                                        </a>
                                                    </button>
                                                </p>
                                            </div>
                                        </div>


                                        <!-- Exchange Accepted modal -->

                                        <div id="modal-exchange-accepted-${pending.acceptCode}"
                                             uk-modal>
                                            <div class="uk-modal-dialog uk-modal-body">
                                                <h3 class="uk-h4"><spring:message
                                                        code="exchange.confirmation.title"/></h3>
                                                <p class="uk-text-right">
                                                    <button class="uk-button uk-button-default uk-modal-close"
                                                            type="button"><spring:message
                                                            code="button.cancel"/></button>
                                                    <button class="uk-button uk-button-primary"
                                                            type="button">
                                                        <a class="button-text-accept custom-link"
                                                           href="<c:url value='/createexchange'>
                                                                   <c:param name='accept_code' value='${pending.acceptCode}'/>
                                                                    <c:param name='state' value='true'/>
                                                                         </c:url>">
                                                            <spring:message
                                                                    code="button.confirm"/>
                                                        </a>
                                                    </button>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:if>
                        </li>

                        <!-- In Progress -->

                        <li class="uk-container">
                            <c:if test="${!empty inProgress}">
                                <c:forEach var="inProgress" items="${inProgress}">
                                    <div class="uk-card uk-card-default exchange-card"
                                         onclick="selectCard(this,
                                                 '<c:out value="${inProgress.requester.book.owner.username}"/>',
                                                 '<c:out value="${inProgress.requester.book.owner.mail}"/>',
                                                 '<c:out value="${inProgress.requester.location}"/>',
                                                 '<c:out value="${inProgress.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${inProgress.offerer.book.bookModel.authors}"/>',
                                                 '<c:out value="${inProgress.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${inProgress.offerer.book.images}"/>',
                                                 '<c:out value="${inProgress.exchangeId}"/>',
                                                 '<c:out value="${inProgress.requester.book.owner.userId}"/>',
                                                 '<c:out value="${inProgress.offerer.book.owner.userId}"/>',
                                                 '${inProgress.isReviewable}')"
                                         uk-grid>


                                        <div style="padding: 0">
                                            <img class="book-image"
                                                 src="${pageContext.request.contextPath}/images/${inProgress.offerer.book.images[0]}"
                                                 alt="bookImage"/>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${inProgress.offerer.book.bookModel.title}"/>
                                                </h3>
                                            </div>
                                        </div>
                                        <div class="arrow-icon" style="padding: 0">
                                            <span uk-icon="icon: chevron-double-right; ratio: 2"></span>
                                        </div>
                                        <div style="padding: 0">
                                            <img class="book-image"
                                                 src="${pageContext.request.contextPath}/images/${inProgress.requester.book.images[0]}"
                                                 alt="bookImage"/>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${inProgress.requester.book.bookModel.title}"/>
                                                </h3>
                                            </div>
                                        </div>

                                        <div style="padding-left: 0; width:40%; margin-bottom: 15px; display: flex; flex-direction: column; align-items: center">
                                            <c:if test="${inProgress.offererReceivedBook}">
                                                                <span class="uk-badge state-awaiting">
                                                            <spring:message code="exchange.status.awaiting"/>
													            </span>
                                            </c:if>
                                            <c:if test="${!inProgress.offererReceivedBook}">
                                                <a class="uk-button uk-button-default uk-button-small"
                                                   href="#modal-confirm-exchange-${inProgress.acceptCode}"
                                                   onclick="event.stopPropagation()" uk-toggle>
                                                    <spring:message
                                                            code="exchange.button.confirm.exchange"/>
                                                </a>
                                                <span class="uk-badge state-inprogress">
                                                                    <spring:message code="exchange.status.in_progress"/>
                                                                </span>
                                            </c:if>

                                            <fmt:formatDate
                                                    value="${inProgress.exchangeStartDate}"
                                                    pattern="dd/MM/yyyy"/>

                                            <!-- Confirm exchange modal -->

                                            <div id="modal-confirm-exchange-${inProgress.acceptCode}"
                                                 uk-modal>
                                                <div class="uk-modal-dialog uk-modal-body">
                                                    <h3 class="uk-h4"><spring:message
                                                            code="exchange.confirm.title"/></h3>
                                                    <p class="uk-text-right">
                                                        <button class="uk-button uk-button-default uk-modal-close"
                                                                type="button"><spring:message
                                                                code="button.cancel"/></button>
                                                        <button class="uk-button uk-button-primary"
                                                                type="button">
                                                            <a class="button-text-accept custom-link"
                                                               href="<c:url value='/confirm_offerer'><c:param name='accept_code' value='${inProgress.acceptCode}'/></c:url>">
                                                                <spring:message code="button.confirm"/>
                                                            </a>
                                                        </button>
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:if>
                        </li>

                        <!-- Completed -->

                        <li style="margin-top: 0" class="uk-container">
                            <c:if test="${!empty completed}">
                            <c:forEach var="completed" items="${completed}">
                            <div class="uk-card uk-card-default exchange-card"
                                 onclick="selectCard(this,
                                         '<c:out value="${completed.requester.book.owner.username}"/>',
                                         '<c:out value="${completed.requester.book.owner.mail}"/>',
                                         '<c:out value="${completed.requester.location}"/>',
                                         '<c:out value="${completed.offerer.book.bookModel.title}"/>',
                                         '<c:out value="${completed.offerer.book.bookModel.authors}"/>',
                                         '<c:out value="${completed.offerer.book.bookModel.title}"/>',
                                         '<c:out value="${completed.offerer.book.images}"/>',
                                         '<c:out value="${completed.exchangeId}"/>',
                                         '<c:out value="${completed.requester.book.owner.userId}"/>',
                                         '<c:out value="${completed.offerer.book.owner.userId}"/>',
                                         '${completed.isReviewable}')"
                                 uk-grid>

                                <div style="padding: 0">
                                    <img class="book-image"
                                         src="${pageContext.request.contextPath}/images/${completed.offerer.book.images[0]}"
                                         alt="bookImage"/>
                                    <div class="card-text-container">
                                        <h3 class="card-text3">
                                            <c:out value="${completed.offerer.book.bookModel.title}"/>
                                        </h3></div>
                                </div>
                                <div class="arrow-icon" style="padding: 0">
                                    <span uk-icon="icon: chevron-double-right; ratio: 2"></span>
                                </div>
                                <div style="padding: 0">
                                    <img class="book-image"
                                         src="${pageContext.request.contextPath}/images/${completed.requester.book.images[0]}"
                                         alt="bookImage"/>
                                    <div class="card-text-container">
                                        <h3 class="card-text3">
                                            <c:out value="${completed.requester.book.bookModel.title}"/>
                                        </h3>
                                    </div>
                                </div>

                                <div style="padding-left: 0;width: 40%; margin-bottom: 25px">
                                                 <span class="uk-badge state-approved"><spring:message
                                                         code="exchange.status.terminated"/></span>
                                    <fmt:formatDate
                                            value="${completed.exchangeStartDate}"
                                            pattern="dd/MM/yyyy"/>
                                </div>

                </div>
                </c:forEach>
                </c:if>
                </li>

                <!-- Rejected -->

                <li class="uk-container">
                    <c:if test="${!empty rejected}">
                        <c:forEach var="rejected" items="${rejected}">
                            <div class="uk-card uk-card-default exchange-card"
                                 onclick="selectCard(this,
                                         '<c:out value="${rejected.requester.book.owner.username}"/>',
                                         '<c:out value="${rejected.requester.book.owner.mail}"/>',
                                         '<c:out value="${rejected.requester.location}"/>',
                                         '<c:out value="${rejected.offerer.book.bookModel.title}"/>',
                                         '<c:out value="${rejected.offerer.book.bookModel.authors}"/>',
                                         '<c:out value="${rejected.offerer.book.bookModel.title}"/>',
                                         '<c:out value="${rejected.offerer.book.images}"/>',
                                         '<c:out value="${rejected.exchangeId}"/>',
                                         '<c:out value="${rejected.requester.book.owner.userId}"/>',
                                         '<c:out value="${rejected.offerer.book.owner.userId}"/>',
                                         '${rejected.isReviewable}')"
                                 uk-grid>


                                <div style="padding: 0">
                                    <img class="book-image"
                                         src="${pageContext.request.contextPath}/images/${rejected.offerer.book.images[0]}"
                                         alt="bookImage"/>
                                    <div class="card-text-container">
                                        <h3 class="card-text3">
                                            <c:out value="${rejected.offerer.book.bookModel.title}"/>
                                        </h3>
                                    </div>
                                </div>
                                <div class="arrow-icon" style="padding: 0">
                                    <span uk-icon="icon: chevron-double-right; ratio: 2"></span>
                                </div>
                                <div style="padding: 0">
                                    <img class="book-image"
                                         src="${pageContext.request.contextPath}/images/${rejected.requester.book.images[0]}"
                                         alt="bookImage"/>
                                    <div class="card-text-container">
                                        <h3 class="card-text3">
                                            <c:out value="${rejected.requester.book.bookModel.title}"/>
                                        </h3>
                                    </div>
                                </div>

                                <div style="padding-left: 0;width: 40%; margin-bottom: 25px">
                                            <span class="uk-badge state-rejected"><spring:message
                                                    code="exchange.status.rejected"/></span>
                                    <fmt:formatDate
                                            value="${rejected.exchangeStartDate}"
                                            pattern="dd/MM/yyyy"/>
                                </div>


                            </div>
                        </c:forEach>
                    </c:if>
                </li>
                </ul>
            </div>
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

                    <div id="exchange-details" style="display: none;">
                        <h3 id="info-requester-username"><spring:message code="exchange.with"/></h3>
                        <p id="info-requester-mail"><spring:message code="exchange.with_email"/></p>
                        <p id="info-requester-location"><spring:message code="exchange.location"/></p>

                        <h4><spring:message code="exchange.your_book"/></h4>
                        <p id="info-offered-book-title"><spring:message code="exchange.book.title"/></p>
                        <p id="info-offered-book-authors"><spring:message code="exchange.book.authors"/></p>
                        <p id="info-offered-book-edition"><spring:message code="exchange.book.edition"/></p>

                        <div id="info-offered-book-images" uk-grid></div>

                        <button id="add-review-button" class="uk-button uk-button-primary"
                                uk-toggle="target: #modal-add-review">
                            <spring:message code="exchange.button.add_review"/>
                        </button>

                        <div id="modal-add-review" uk-modal>
                            <div class="uk-modal-dialog uk-modal-body">
                                <h2 class="uk-modal-title"><spring:message
                                        code="exchange.add_review.title"/></h2>

                                <form:form action="/submitReview" method="post" modelAttribute="review">

                                    <div class="form-group uk-margin-top uk-margin-bottom">
                                        <label><spring:message code="review.rating.label"/></label>

                                        <div class="star-rating">

                                            <form:radiobutton path="userReviewRating" value="5" id="star5"/>
                                            <label for="star5" title="5 stars">
                                                <span uk-icon="icon: star; ratio: 1.5"></span>
                                            </label>

                                            <form:radiobutton path="userReviewRating" value="4" id="star4"/>
                                            <label for="star4" title="4 stars">
                                                <span uk-icon="icon: star; ratio: 1.5"></span>
                                            </label>

                                            <form:radiobutton path="userReviewRating" value="3" id="star3"/>
                                            <label for="star3" title="3 stars">
                                                <span uk-icon="icon: star; ratio: 1.5"></span>
                                            </label>

                                            <form:radiobutton path="userReviewRating" value="2" id="star2"/>
                                            <label for="star2" title="2 stars">
                                                <span uk-icon="icon: star; ratio: 1.5"></span>
                                            </label>

                                            <form:radiobutton path="userReviewRating" value="1" id="star1"/>
                                            <label for="star1" title="1 star">
                                                <span uk-icon="icon: star; ratio: 1.5"></span>
                                            </label>

                                        </div>
                                    </div>

                                    <div class="uk-margin">
                                        <label for="reviewDescription"><spring:message
                                                code="review.comments.label"/></label>
                                        <form:textarea path="reviewDescription" rows="4" class="uk-textarea"/>
                                    </div>

                                    <form:hidden path="exchangeId"/>
                                    <form:hidden path="reviewerId"/>
                                    <form:hidden path="subjectId"/>

                                    <p class="uk-text-right">
                                        <button class="uk-button uk-button-default uk-modal-close" type="button">
                                            <spring:message code="exchange.button.cancel"/>
                                        </button>
                                        <button class="uk-button uk-button-primary" type="submit">
                                            <spring:message code="exchange.button.accept"/>
                                        </button>
                                    </p>

                                </form:form>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>


</body>

<script>
    function selectCard(card, requesterUsername, requesterMail, requesterLocation, offeredBookTitle, offeredBookAuthors, offeredBookEdition, offeredBookImages, exchangeId, reviewerId, subjectId, isReviewable) {
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
        document.getElementById('info-requester-username').textContent = "<spring:message code="exchange.with"/>" + " " + requesterUsername;
        document.getElementById('info-requester-mail').textContent = "<spring:message code="exchange.with_email"/>" + " " + requesterMail;
        document.getElementById('info-requester-location').textContent = "<spring:message code="exchange.location"/>" + requesterLocation;
        document.getElementById('info-offered-book-title').textContent = "<spring:message code="exchange.book.title"/>" + " " + offeredBookTitle;
        document.getElementById('info-offered-book-authors').textContent = "<spring:message code="exchange.book.authors"/>" + " " + offeredBookAuthors;
        document.getElementById('info-offered-book-edition').textContent = "<spring:message code="exchange.book.edition"/>" + " " + offeredBookEdition;

        if (isReviewable === 'true') {
            document.getElementById('add-review-button').style.display = 'block';
        } else {
            document.getElementById('add-review-button').style.display = 'none';
        }

        // Actualizar los campos ocultos del formulario de reseña
        document.querySelector('input[name="exchangeId"]').value = exchangeId;
        document.querySelector('input[name="reviewerId"]').value = reviewerId;
        document.querySelector('input[name="subjectId"]').value = subjectId;

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
        document.getElementById('add-review-button').style.display = 'block';
        document.getElementById('add-review-button').style.display = 'none';
    });
</script>

</html>

