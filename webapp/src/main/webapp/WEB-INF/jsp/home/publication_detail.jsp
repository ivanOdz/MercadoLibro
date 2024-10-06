<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>


<html class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/publicationDetail.css?v=1.0" rel="stylesheet"/>
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
    <div class="uk-container" style="margin-top: 1%">
        <a class="uk-button uk-button-text" href="${pageContext.request.contextPath}/">
            <span uk-icon="icon:  chevron-left"></span>
            <spring:message code="add.book.return_home"/>
        </a>
    </div>
    <div class="uk-containter" style="margin-bottom: 2%;">
        <div class="row-container">

            <!-- Images -->
            <div class="column-container" style="margin-left: 2%; margin-top: 2%; max-width: 20%;">
                <c:choose>
                    <c:when test="${not empty publication.book.images}">
                        <div class="column-container">
                            <img id="currentImage"
                                 src="<c:url value='${pageContext.request.contextPath}/images/${publication.book.images[0]}'/>"
                                 alt="Book Image"/>
                            <div class="row-container">
                                <button id="prevBtn" class="slider-button" style="margin-left: 30%; margin-right: 5%;">
                                    <span uk-icon="icon: chevron-left"></span>
                                </button>
                                <button id="nextBtn" class="slider-button">
                                    <span uk-icon="icon: chevron-right"></span>
                                </button>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <img class="book-image" src="${pageContext.request.contextPath}/images/book.jpg" width="300"
                             height="400" alt="book"/>
                    </c:otherwise>
                </c:choose>
            </div>


            <div class="column-container" style="margin-top: 2%; margin-left: 7%;">
                <div class="row-container">
                    <div class="column-container">
                        <!-- Title -->
                        <div>
                            <h1 class="uk-text-large uk-text-bold" style="font-size: 36px;">
                                <c:out value="${publication.book.bookModel.title}"/>
                            </h1>
                        </div>

                        <!-- Author -->
                        <div>
                            <p class="small-gray-text custom-link">
                                <c:out value="${publication.book.bookModel.authors}"/>
                            </p>
                        </div>
                    </div>


                    <div class="column-container" style="margin-top: 2%; margin-left: 35%;">
                        <!-- Genre -->
                        <div>
                            <p class="small-gray-text custom-link">
                                <c:forEach var="genreWrapper" items="${genres}">
                                    <c:if test="${genreWrapper.genre == publication.book.bookModel.genre}">
                                        <c:out value="${genreWrapper.displayName}"/>
                                    </c:if>
                                </c:forEach>
                            </p>
                        </div>

                        <!-- Ranking -->
                        <div class="row-container">
                            <div style="margin-right: 5px;">
                                <p class="small-gray-text custom-link">
                                    <c:out value="${publication.book.bookModel.rating.rating}"/>
                                </p>
                            </div>
                            <div class="star-rating">
                                <c:forEach var="i" begin="1" end="5">
                                    <c:choose>
                                        <c:when test="${i <= publication.book.bookModel.rating.rating}">
                                            <!-- Estrella llena -->
                                            <span uk-icon="icon: star; ratio: 1.5" style="color: gold;"></span>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Estrella vacía -->
                                            <span uk-icon="icon: star; ratio: 1.5" style="color: lightgray;"></span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                            <div style="margin-left: 5px;">
                                <p class="small-gray-text custom-link">(<c:out
                                        value="${publication.book.bookModel.rating.ratingCount}"/>)</p>
                            </div>
                        </div>
                    </div>
                </div>


                <!-- Line -->
                <hr style="width: 55lh;"/>

                <!-- Description -->
                <div style="max-width: 55lh; text-align: justify;">
                    <p>
                        <c:out value="${publication.book.bookModel.description}"/>
                    </p>
                </div>

                <!-- Line -->
                <hr style="width: 55lh;"/>


                <div>
                    <div class="row-container"
                         style="margin-left: 50px; align-items: center; justify-content: space-between;">

                        <!-- Book State -->
                        <div class="column-container" style="place-items: center; display: flex;">
                            <div>
                                <p style="text-align: center"><spring:message code="publication.details.bookState"/></p>
                                <i class="material-icons" style="margin-left: 40px;">book</i>
                                <div>
                                    <c:choose>
                                        <c:when test="${publication.book.bookState == 'NEW'}">
                                            <div style="margin-left: 20px;">
                                                <strong><spring:message code="bookState.new"/></strong>
                                            </div>
                                        </c:when>
                                        <c:when test="${publication.book.bookState == 'LIKE_NEW'}">
                                            <div style="margin-left: 20px;">
                                                <strong><spring:message code="bookState.like.new"/></strong>
                                            </div>
                                        </c:when>
                                        <c:when test="${publication.book.bookState == 'VERY_GOOD'}">
                                            <div style="margin-left: 20px;">
                                                <strong><spring:message code="bookState.very.good"/></strong>
                                            </div>
                                        </c:when>
                                        <c:when test="${publication.book.bookState == 'GOOD'}">
                                            <div style="margin-left: 20px;">
                                                <strong><spring:message code="bookState.good"/></strong>
                                            </div>
                                        </c:when>
                                        <c:when test="${publication.book.bookState == 'ACCEPTABLE'}">
                                            <div style="margin-left: 20px;">
                                                <strong><spring:message code="bookState.acceptable"/></strong>
                                            </div>
                                        </c:when>
                                        <c:when test="${publication.book.bookState == 'WORN'}">
                                            <div style="margin-left: 20px;">
                                                <strong><spring:message code="bookState.worn"/></strong>
                                            </div>
                                        </c:when>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                        <!-- Location -->
                        <div class="column-container" style="place-items: center; display: flex;">
                            <div>
                                <p style="text-align: center"><spring:message code="publication.details.location"/></p>
                                <span uk-icon="icon: location" style="margin-left: 30px;"></span>
                                <p style="text-align: center">
                                    <strong>
                                        <c:out value="${publication.location.locationString}"/>
                                    </strong>
                                </p>
                            </div>
                        </div>

                        <!-- Publication date -->
                        <div class="column-container" style="place-items: center; display: flex;">
                            <div>
                                <p style="text-align: center"><spring:message code="publication.details.date"/></p>
                                <i class="material-icons" style="margin-left: 65px;">history</i>
                                <p style="text-align: center">
                                    <strong>${publication.publicationDatetime}</strong></p>
                            </div>
                        </div>

                        <!-- Editorial -->
                        <div class="column-container" style="place-items: center; display: flex;">
                            <div>
                                <p style="text-align: center"><spring:message code="publication.details.editorial"/></p>
                                <i class="material-icons" style="margin-right: 90px;">file-edit</i>
                                <p style="text-align: center"><strong>
                                    <c:out value="${publication.book.bookModel.editorial}"/></strong></p>
                            </div>


                        </div>
                    </div>
                </div>
            </div>

        </div>


        <!-- Line -->
        <div style="margin-top: 2%; margin-left: 10%;">
            <hr style="width: 50lh;"/>
        </div>

        <div class="row-container" style="margin-left: 2lh; place-items: center; display: flex;">
            <div>
                <p class="uk-text-medium"
                   style="font-size: 25px; text-align: center; max-width: 9lh; margin-left: 1lh;">
                    <spring:message code="exchange.description"/></p>
            </div>
            <div>
                <p class="uk-text-medium"
                   style="font-size: 25px; max-width: 9lh; text-align: center; margin-left: 2lh;">
                    <spring:message code="exchange.description2"/></p>
            </div>
            <div>
                <%-- if they dont have books than open the modal --%>
                <c:if test="${empty availableBooks}">
                    <button class="uk-button uk-button-primary" uk-toggle="target: #exchange-modal"
                            style="margin-left: 3lh;">
                        <spring:message code="add.exchange.submit"/>
                    </button>
                </c:if>
                <c:if test="${not empty availableBooks}">
                    <button type="submit" class="uk-button uk-button-primary" style="margin-left: 3lh;">
                        <a class="button-text" href="${pageContext.request.contextPath}/start_exchange?publication_id=${publication.publicationId}">
                            <spring:message code="add.exchange.submit"/>
                        </a>
                    </button>
                </c:if>
            </div>

            <div id="exchange-modal" uk-modal>
                <div class="uk-modal-dialog uk-modal-body">
                    <button class="uk-modal-close-default" type="button" uk-close></button>

                    <label class="form-group" style="margin-bottom: 2%;">
                        <spring:message code="book.set.book"/>
                    </label>

                    <button class="uk-button uk-button-primary">
                        <a class="button-text" href="${pageContext.request.contextPath}/book/book_models">
                            <spring:message code="add.book.missing"/>
                        </a>
                    </button>

                </div>
            </div>
        </div>
    </div>
</div>
</div>

<script>
    const images = [
        <c:forEach var="image" items="${publication.book.images}" varStatus="loop">
        '<c:url value="${pageContext.request.contextPath}/images/${image}"/>'<c:if test="${!loop.last}">, </c:if>
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