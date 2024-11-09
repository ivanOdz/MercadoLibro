<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>
<%@ include file="/WEB-INF/jsp/head/headers.jsp" %>

<html class="custom-style">

<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="<c:url value='/css/navbar.css?v=1.0' />" rel="stylesheet"/>
    <link href="<c:url value='/css/publications.css?v=1.0' />" rel="stylesheet"/>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

    <title><spring:message code="publication.details.title"/></title>
</head>

<style>
    .slider-button {
        background: none;
        border: none;
        cursor: pointer;
        outline: none;
        box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
        border-radius: 50%;
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: box-shadow 0.3s ease;
    }

    .slider-button:hover {
        box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.2);
    }

    .slider-button span {
        font-size: 24px;
        color: #000000;
    }
</style>

<body>

<navbar/>

<div class="uk-background-muted">
    <div class="uk-container" style="max-width: 90%">
        <div class="uk-container uk-card uk-card-default"
             style="align-content: center; border-radius: 1%; padding: 5%; justify-content: center; margin-top:5%; margin-bottom:5%; max-width:125%">
            <div style="margin-bottom: 5%">
                <a class="uk-button uk-button-text" href="<c:url value='/' />">
                    <span uk-icon="icon:  chevron-left"></span>
                    <spring:message code="add.book.return_home"/>
                </a>
            </div>
            <div style="display: grid; grid-template-columns: repeat(3, 1fr);">
                <div style="grid-column: 1 / 2; display: grid; justify-content: center; margin-left:2rem;">
                    <c:choose>
                        <c:when test="${!publication.book.images[0].image.isImageNull}">
                            <img id="currentImage"
                                 src="<c:url value='/images/${publication.book.images[0].image.imageId}'/>"
                                 alt="Book Image"/>
                            <div class="uk-button-group" style="justify-content:center">
                                <button id="prevBtn" class="slider-button">
                                    <span uk-icon="icon: chevron-left"></span>
                                </button>
                                <button id="nextBtn" class="slider-button">
                                    <span uk-icon="icon: chevron-right"></span>
                                </button>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <img class="book-image" src="<c:url value='/images/book.jpg' />" width="300"
                                 height="400" alt="book"/>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div style="grid-column: 2 / 4; display: grid;justify-content: left; margin-left: 5%;">
                    <div>
                        <!-- Title -->
                        <div>
                            <h1 class="uk-text-large uk-text-bold" style="font-size: 36px;">
                                <c:out value="${publication.book.bookModel.title}"/>
                            </h1>
                        </div>

                        <!-- Author -->
                        <div>
                            <c:forEach var="author" items="${publication.book.bookModel.authors}">
                                <p class="small-gray-text custom-link">
                                    <c:out value="${author.authorName}"/>
                                </p>
                            </c:forEach>
                        </div>
                    </div>


                    <div style="margin-top: 2%;">
                        <!-- Genre -->
                        <div>
                            <p class="small-gray-text custom-link">
                                <c:forEach var="genreWrapper" items="${genres}">
                                    <c:if test="${genreWrapper.value == publication.book.bookModel.genre.value}">
                                        <c:set var="genre" value="${genreWrapper.value}" />
                                        <span class="ui-search-filter-name">
                                            <spring:message code="${genre}"/>
                                        </span>
                                    </c:if>
                                </c:forEach>
                            </p>
                        </div>

                        <div style="display: grid; justify-content: center; grid-template-columns: repeat(4, 1fr);">
                            <hr style="grid-column: 1 / 5; margin: 5%"/>
                        </div>

                        <div style="display: grid; justify-content: center; grid-template-columns: repeat(7, 1fr);">

                            <div style="grid-column: 1 / 7">
                                <p>
                                    <c:out value="${publication.book.bookModel.description}"/>
                                </p>
                            </div>
                        </div>

                        <!-- Ranking -->
                        <div class="row-container">
                            <div class="star-rating uk-flex uk-flex-middle">
                                <p class="small-gray-text custom-link"
                                   style="display: inline; margin-bottom: 0; margin-right:1rem;">
                                    <c:out value="${publication.book.bookModel.averageRating}"/>
                                </p>
                                <c:forEach var="i" begin="1" end="5">
                                    <c:choose>
                                        <c:when test="${i <= publication.book.bookModel.averageRating}">
                                            <!-- Estrella llena -->
                                            <i class="material-icons yellow-text">star</i>
                                        </c:when>
                                        <c:when test="${i - 0.5 <= publication.book.bookModel.averageRating && publication.book.bookModel.averageRating < i}">
                                            <i class="material-icons yellow-text">star_half</i>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Estrella vacía -->
                                            <i class="material-icons grey-text">star_border</i>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                <p class="small-gray-text custom-link" style="display: inline; margin-left:1rem;">
                                    (<c:out
                                        value="${publication.book.bookModel.ratingCount}"/>)</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <div style="display: grid; justify-content: center; grid-template-columns: repeat(4, 1fr);">
                <hr style="grid-column: 1 / 5; margin: 5%"/>
            </div>

            <div style="display: grid; justify-content: center; grid-template-columns: repeat(4, 1fr); gap: 20px;">
                <!-- Fila de Títulos -->
                <div style="display: flex; align-items: center; justify-content: center;">
                    <i class="material-icons" style="margin-right: 8px;">book</i>
                    <spring:message code="publication.details.bookState"/>
                </div>
                <div style="display: flex; align-items: center; justify-content: center;">
                    <span uk-icon="icon: location" style="margin-right: 8px;"></span>
                    <spring:message code="publication.details.location"/>
                </div>
                <div style="display: flex; align-items: center; justify-content: center;">
                    <i class="material-icons" style="margin-right: 8px;">history</i>
                    <spring:message code="publication.details.date"/>
                </div>
                <div style="display: flex; align-items: center; justify-content: center;">
                    <i class="material-icons" style="margin-right: 8px;">edit</i>
                    <spring:message code="publication.details.editorial"/>
                </div>

                <!-- Fila de Contenidos -->
                <div style="text-align: center;">
                    <c:choose>
                        <c:when test="${publication.book.bookState == 'NEW'}">
                            <strong><spring:message code="bookstate.new"/></strong>
                        </c:when>
                        <c:when test="${publication.book.bookState == 'LIKE_NEW'}">
                            <strong><spring:message code="bookstate.like.new"/></strong>
                        </c:when>
                        <c:when test="${publication.book.bookState == 'VERY_GOOD'}">
                            <strong><spring:message code="bookstate.very.good"/></strong>
                        </c:when>
                        <c:when test="${publication.book.bookState == 'GOOD'}">
                            <strong><spring:message code="bookstate.good"/></strong>
                        </c:when>
                        <c:when test="${publication.book.bookState == 'ACCEPTABLE'}">
                            <strong><spring:message code="bookstate.acceptable"/></strong>
                        </c:when>
                        <c:when test="${publication.book.bookState == 'WORN'}">
                            <strong><spring:message code="bookstate.worn"/></strong>
                        </c:when>
                    </c:choose>
                </div>
                <div style="text-align: center;">
                    <strong>
                        <c:forEach var="location" items="${publication.locations}" varStatus="status">
                            <c:out value="${location.locationString}"/>
                            <c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    </strong>
                </div>
                <div style="text-align: center;">
                    <strong>${publication.publicationDatetime}</strong>
                </div>
                <div style="text-align: center;">
                    <strong><c:out value="${publication.book.bookModel.editorial}"/></strong>
                </div>

                <c:if test="${loggedUser.userId == publication.user.userId}">
                    <div style="text-align: center;display: flex;justify-content: center; align-items: center;grid-column: 2/3">
                        <a class="edit-button" type="button" href="#new-location-modal" uk-toggle>
                            <svg class="edit-svgIcon" viewBox="0 0 512 512">
                                <path d="M410.3 231l11.3-11.3-33.9-33.9-62.1-62.1L291.7 89.8l-11.3 11.3-22.6 22.6L58.6 322.9c-10.4 10.4-18 23.3-22.2 37.4L1 480.7c-2.5 8.4-.2 17.5 6.1 23.7s15.3 8.5 23.7 6.1l120.3-35.4c14.1-4.2 27-11.8 37.4-22.2L387.7 253.7 410.3 231zM160 399.4l-9.1 22.7c-4 3.1-8.5 5.4-13.3 6.9L59.4 452l23-78.1c1.4-4.9 3.8-9.4 6.9-13.3l22.7-9.1v32c0 8.8 7.2 16 16 16h32zM362.7 18.7L348.3 33.2 325.7 55.8 314.3 67.1l33.9 33.9 62.1 62.1 33.9 33.9 11.3-11.3 22.6-22.6 14.5-14.5c25-25 25-65.5 0-90.5L453.3 18.7c-25-25-65.5-25-90.5 0zm-47.4 168l-144 144c-6.2 6.2-16.4 6.2-22.6 0s-6.2-16.4 0-22.6l144-144c6.2-6.2 16.4-6.2 22.6 0s6.2 16.4 0 22.6z"></path>
                            </svg>
                        </a>
                    </div>
                </c:if>
            </div>



            <div style="display: grid; justify-content: center; grid-template-columns: repeat(4, 1fr);">
                <hr style="grid-column: 1 / 5; margin: 5%"/>
            </div>

            <div style="display: grid; justify-content: center; grid-template-columns: repeat(2, 1fr);">

                <c:if test="${user.userId != publication.book.owner.userId}">
                    <div style="grid-column: span 1">
                        <p class="uk-text-medium"
                           style="font-size: 25px; text-align: center;">
                            <spring:message code="exchange.description"/></p>
                        <p class="uk-text-medium"
                           style="font-size: 25px; max-width: 9lh; text-align: center; margin-left: 2lh;">
                            <spring:message code="exchange.description2"/></p>
                    </div>
                    <div style="grid-column: span 1; align-content: center; margin-left: 10%;">
                            <%-- if they dont have books than open the modal --%>
                        <c:if test="${empty availableBooks}">
                            <a class="uk-button uk-button-primary" uk-toggle="target: #exchange-modal">
                                <spring:message code="add.exchange.submit"/>
                            </a>
                        </c:if>
                        <c:if test="${not empty availableBooks}">
                            <a class="uk-button uk-button-primary"
                               href="<c:url value='/start_exchange?publication_id=${publication.publicationId}' />">
                                <spring:message code="add.exchange.submit"/>
                            </a>
                        </c:if>
                    </div>

                    <div id="exchange-modal" uk-modal>
                        <div class="uk-modal-dialog uk-modal-body">
                            <button class="uk-modal-close-default" type="button" uk-close></button>

                            <label class="form-group" style="margin-bottom: 2%;">
                                <spring:message code="book.set.book"/>
                            </label>
                            <a class="uk-button uk-button-primary"
                               href="<c:url value='/book/book_models' />">
                                <spring:message code="add.book.missing"/>
                            </a>
                        </div>
                    </div>
                </c:if>
            </div>

            <c:url var="postUrl" value='/publication/add_location' />
            <div id="new-location-modal" class="uk-flex-top" uk-modal>
                <div class="uk-modal-dialog">
                    <button class="uk-modal-close-default" type="button" uk-close></button>
                    <div class="uk-modal-header">
                        <h2 class="uk-modal-title"><spring:message code="publication.add.location"/></h2>
                    </div>
                    <form:form modelAttribute="locationForm" action="${postUrl}" method="post" enctype="multipart/form-data">
                        <div style="padding: 5%">
                            <p><spring:message code="publication.add.location.description"/></p>
                            <form:select path="locationId" class="uk-select no-arrow-select" aria-label="Not clickable icon" style="width: 90%">
                                <form:options items="${user.userLocations}" itemValue="locationId" itemLabel="locationString" />
                            </form:select>
                        </div>
                        <form:hidden path="publicationId" value="${publication.publicationId}"/>
                        <div class="uk-modal-footer uk-text-right">
                            <button class="uk-button uk-button-default uk-modal-close" type="button"><spring:message code="exchange.button.cancel"/></button>
                            <button class="uk-button uk-button-primary" type="submit"><spring:message code="button.confirm"/></button>
                        </div>
                    </form:form>
                </div>
            </div>

        </div>
    </div>
</div>

<script>
    const images = [
        <c:forEach var="image" items="${publication.book.images}" varStatus="loop">
        '<c:url value="/images/${image.image.imageId}"/>'<c:if test="${!loop.last}">, </c:if>
        </c:forEach>
    ];


    let currentIndex = 0;
    const imgElement = document.getElementById('currentImage');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    function showImage(index) {
        if (index < 0) {
            currentIndex = images.length - 1;
        } else if (index >= images.length) {
            currentIndex = 0;
        } else {
            currentIndex = index;
        }
        imgElement.src = images[currentIndex];
    }

    prevBtn.addEventListener('click', () => showImage(currentIndex - 1));
    nextBtn.addEventListener('click', () => showImage(currentIndex + 1));


    document.addEventListener('DOMContentLoaded', function () {
        const editButton = document.querySelector('.edit-button::before');
        if (editButton) {
            editButton.style.content = '"<spring:message code="publications.edit"/>"';
        }
    });

</script>

</body>

</html>