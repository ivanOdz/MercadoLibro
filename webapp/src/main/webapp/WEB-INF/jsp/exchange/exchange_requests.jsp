<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>

<!DOCTYPE html>
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html lang="es" class="custom-style">
<head>
    <link href="${pageContext.request.contextPath}/css/exchange.css?v=1.0" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/exchange.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/navbar.css" rel="stylesheet"/>

    <title><spring:message code="exchanges.view.title"/></title>

</head>

<body>
<navbar/>

<div class="uk-grid">
    <div class="uk-width-5-6 uk-align-center main-section">
<%--        <div>--%>
<%--            <h2 class="uk-h2 title"><spring:message code="exchange.requests.title"/></h2>--%>
<%--            <h3 class="uk-h5"><spring:message code="exchange.requests.subtitle"/></h3>--%>
<%--        </div>--%>

        <c:if test="${!(exchanges.size() eq 0)}">
            <div class="main-content">
                <!-- columna de exchanges -->
                <div class="uk-width-3-5 column-exchanges scrollable-content">
                    <c:forEach var="exchange" items="${exchanges}">
                        <div class="uk-card uk-card-default uk-grid-collapse uk-child-width-1-4@s exchange-card"
                             onclick="selectCard(this, '<c:out value="${exchange.requester.book.owner.username}"/>', '<c:out value="${exchange.requester.book.owner.mail}"/>', '<c:out value="${exchange.requester.location}"/>', '<c:out value="${exchange.offerer.book.bookModel.title}"/>', '<c:out value="${exchange.offerer.book.bookModel.authors}"/>', '<c:out value="${exchange.offerer.book.bookModel.title}"/>', '<c:out value="${exchange.offerer.book.images[0]}"/>')"
                             uk-grid>
                            <div class="uk-card-media-left">
                                <img class="book-image"
                                     src="${pageContext.request.contextPath}/images/${exchange.requester.book.bookModel.imageId}"
                                     alt="bookImage"/>
                            </div>
                            <div>
                                <div class="card-text">
                                    <div class="card-text-left">
                                        <h3 class="uk-h6">
                                                <c:out value="${exchange.requester.book.bookModel.title}"/>
                                        </h3>

                                    </div>
                                    <div class="uk-align-right card-text-right">
                                        <c:choose>

                                            <c:when test="${exchangeState == 'ACCEPTED'
                                                    && exchange.offererReceivedBook == true}">
                                                <span class="uk-badge state-awaiting"><spring:message
                                                        code="exchange.status.awaiting"/></span>
                                            </c:when>

                                            <c:when test="${exchangeState == 'ACCEPTED'}">
                                                <a class="uk-button uk-button-default uk-button-small uk-margin-right"
                                                   href="#modal-confirm-exchange-${exchange.acceptCode}"
                                                   onclick="event.stopPropagation()" uk-toggle>
                                                    <spring:message code="exchange.button.confirm.exchange"/>
                                                </a>
                                                <span class="uk-badge state-inprogress"><spring:message
                                                        code="exchange.status.in_progress"/></span>

                                                <!-- Confirm exchange modal -->

                                                <div id="modal-confirm-exchange-${exchange.acceptCode}" uk-modal>
                                                    <div class="uk-modal-dialog uk-modal-body">
                                                        <h3 class="uk-h4"><spring:message code="exchange.confirm.title"/> </h3>
                                                        <p class="uk-text-right">
                                                            <button class="uk-button uk-button-default uk-modal-close" type="button"><spring:message code="button.cancel"/></button>
                                                            <button class="uk-button uk-button-primary" type="button">
                                                                <a class="button-text-accept custom-link" href="<c:url value='/confirm_offerer'>
                                                                            <c:param name='accept_code' value='${exchange.acceptCode}'/>
                                                                            </c:url>">
                                                                    <spring:message code="button.confirm"/>
                                                                </a>
                                                            </button>
                                                        </p>
                                                    </div>
                                                </div>

                                            </c:when>


                                            <c:when test="${exchangeState == 'REJECTED'}">
                                                <span class="uk-badge state-rejected"><spring:message
                                                        code="exchange.status.rejected"/></span>
                                            </c:when>

                                            <c:when test="${exchangeState == 'PENDING'}">
                                                <div class="uk-button-group">
                                                    <a class="uk-button uk-button-default uk-button-small"
                                                       href="#modal-exchange-accepted-${exchange.acceptCode}"
                                                       onclick="event.stopPropagation()" uk-toggle>
                                                        <spring:message code="exchange.button.accept"/>
                                                    </a>
                                                    <a class="uk-button uk-button-default uk-button-small uk-margin-right custom-link"
                                                       href="#modal-exchange-rejected-${exchange.acceptCode}"
                                                       onclick="event.stopPropagation()" uk-toggle>
                                                        <spring:message code="email.rejectButton"/>
                                                    </a>

                                                    <!-- Exchange Rejected modal -->

                                                    <div id="modal-exchange-rejected-${exchange.acceptCode}" uk-modal>
                                                        <div class="uk-modal-dialog uk-modal-body">
                                                            <h3 class="uk-h4"><spring:message code="exchange.rejection.title"/> </h3>
                                                            <p class="uk-text-right">
                                                                <button class="uk-button uk-button-default uk-modal-close" type="button"><spring:message code="button.cancel"/></button>
                                                                <button class="uk-button uk-button-danger" type="button">
                                                                    <a href="<c:url value='/createexchange'>
                                                        <c:param name='accept_code' value='${exchange.acceptCode}'/>
                                                        <c:param name='state' value='false'/>
                                                            </c:url>">
                                                                        <spring:message code="button.confirm"/>
                                                                    </a>
                                                                </button>
                                                            </p>
                                                        </div>
                                                    </div>


                                                    <!-- Exchange Accepted modal -->

                                                    <div id="modal-exchange-accepted-${exchange.acceptCode}" uk-modal>
                                                        <div class="uk-modal-dialog uk-modal-body">
                                                            <h3 class="uk-h4"><spring:message code="exchange.confirmation.title"/> </h3>
                                                            <p class="uk-text-right">
                                                                <button class="uk-button uk-button-default uk-modal-close" type="button"><spring:message code="button.cancel"/></button>
                                                                <button class="uk-button uk-button-primary" type="button">
                                                                    <a class="button-text-accept custom-link" href="<c:url value='/createexchange'>
                                                                   <c:param name='accept_code' value='${exchange.acceptCode}'/>
                                                                    <c:param name='state' value='true'/>
                                                                         </c:url>">
                                                                        <spring:message code="button.confirm"/>
                                                                    </a>
                                                                </button>
                                                            </p>
                                                        </div>
                                                    </div>

                                                </div>
                                                <span class="uk-badge state-pending"><spring:message
                                                        code="exchange.status.pending"/></span>

                                            </c:when>

                                            <c:when test="${exchangeState == 'TERMINATED'}">
                                                <span class="uk-badge state-approved"><spring:message
                                                        code="exchange.status.terminated"/></span>
                                            </c:when>
                                        </c:choose>
                                    </div>


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

				            <div id="exchange-details" style="display: none;">
				                <h3 id="info-requester-username"><spring:message code="exchange.with"/></h3>
				                <p id="info-requester-mail"><spring:message code="exchange.with_email"/></p>
				                <p id="info-requester-location"><spring:message code="exchange.location"/></p>

				                <h4><spring:message code="exchange.your_book"/></h4>
				                <p id="info-offered-book-title"><spring:message code="exchange.book.title"/></p>
				                <p id="info-offered-book-authors"><spring:message code="exchange.book.authors"/></p>
				                <p id="info-offered-book-edition"><spring:message code="exchange.book.edition"/></p>

				                <div id="info-offered-book-images" uk-grid></div>

							    <button id="add-review-button" class="uk-button uk-button-primary" uk-toggle="target: #modal-add-review">
							        <spring:message code="exchange.button.add_review"/>
							    </button>

								<div id="modal-add-review" uk-modal>
								    <div class="uk-modal-dialog uk-modal-body">
								        <h2 class="uk-modal-title"><spring:message code="exchange.add_review.title"/></h2>

								        <form:form action="/submitReview" method="post" modelAttribute="userReview">

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
								                <label for="reviewDescription"><spring:message code="review.comments.label"/></label>
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
        </c:if>
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

