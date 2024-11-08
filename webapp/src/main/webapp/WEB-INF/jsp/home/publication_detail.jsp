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


            <div style="display: grid; justify-content: center; grid-template-columns: repeat(4, 1fr);">
                <!-- Book State -->
                <div class="column-container" style="place-items: center; margin-right: 15%;">
                    <div>
                        <p style="display: flex; align-items: center; justify-content: center;">
                            <i class="material-icons" style="margin-right: 8px;">book</i>
                            <spring:message code="publication.details.bookState"/>
                        </p>
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
                    </div>
                </div>

                <!-- Location -->
                <div class="column-container" style="place-items: center; display: flex;">
                    <div>
                        <p style="display: flex; align-items: center; justify-content: center;">
                            <span uk-icon="icon: location" style="margin-right: 8px;"></span>
                            <spring:message code="publication.details.location"/>
                        </p>
                        <p style="text-align: center;">
                            <strong><c:out value="${publication.location.locationString}"/></strong>
                        </p>
                    </div>
                </div>

                <!-- Publication date -->
                <div class="column-container" style="place-items: center; display: flex;">
                    <div>
                        <p style="display: flex; align-items: center; justify-content: center;">
                            <i class="material-icons" style="margin-right: 8px;">history</i>
                            <spring:message code="publication.details.date"/>
                        </p>
                        <p style="text-align: center;">
                            <strong>${publication.publicationDatetime}</strong>
                        </p>
                    </div>
                </div>

                <!-- Editorial -->
                <div class="column-container" style="place-items: center; display: flex;">
                    <div>
                        <p style="display: flex; align-items: center; justify-content: center;">
                            <i class="material-icons" style="margin-right: 8px;">file-edit</i>
                            <spring:message code="publication.details.editorial"/>
                        </p>
                        <p style="text-align: center;">
                            <strong><c:out value="${publication.book.bookModel.editorial}"/></strong>
                        </p>
                    </div>
                </div>
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

</script>

</body>

</html>