<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>


<html class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/publicationDetail.css?v=1.0" rel="stylesheet"/>

    <title><spring:message code="publication.details.title"/></title>
</head>
<body>
<navbar/>


<div class="uk-background-muted">
    <div class="uk-container main uk-align-center" style="margin-bottom: 2%;">
    <div class="row-container">

        <!-- Images -->
        <div class="column-container">
            <c:choose>
                <c:when test="${not empty pd.images}">
                    <div class="uk-visible-toggle uk-light images" tabindex="-1" uk-slider>
                        <div class="uk-slider-items uk-grid">
                            <c:forEach var="bookImage" items="${pd.images}">
                                <div>
                                    <div class="uk-panel">
                                        <img src="${pageContext.request.contextPath}/images/${bookImage.imageId}"
                                             width="200" height="450" alt="bookImage">
                                    </div>
                                </div>
                            </c:forEach>
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
                            <c:out value="${pd.bookModel.title}"/>
                        </h1>
                    </div>

                    <!-- Author -->
                    <div>
                        <p class="small-gray-text custom-link">
                            <c:out value="${card.authorsString}"/>
                        </p>
                    </div>
                </div>


                <div class="column-container" style="margin-top: 2%; margin-left: 5%">
                    <!-- Genre -->
                    <div>
                        <p class="small-gray-text custom-link">
                            <c:out value="${card.bookModel.genre}"/>
                        </p>
                    </div>

                    <!-- Ranking -->
                    <div class="row-container">
                        <div style="margin-right: 5px;">
                            <p class="small-gray-text custom-link">
                                <c:out value="${pd.rating.rating}"/>
                            </p>
                        </div>
                        <div class="star-rating">
                            <c:forEach var="i" begin="1" end="5">
                                <c:choose>
                                    <c:when test="${i <= pd.rating.rating}">
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
                            <p class="small-gray-text custom-link">(<c:out value="${pd.rating.ratingCount}"/>)</p>
                        </div>
                    </div>
                </div>
            </div>


            <!-- Line -->
            <hr style="width: 55lh;"/>

            <!-- Description -->
            <div style="max-width: 55lh; text-align: justify;">
                <p>
                    <c:out value="${card.bookModel.description}"/>
                </p>
            </div>

            <!-- Line -->
            <hr style="width: 55lh;"/>


            <div class="uk-width-2-3">
                <div class="row-container"
                     style="margin-left: 50px; align-items: center; justify-content: space-between;">

                    <!-- Book State -->
                    <div class="column-container" style="place-items: center; display: flex;">
                        <div>
                            <p style="text-align: center"><spring:message code="publication.details.bookState"/></p>
                            <i class="material-icons" style="margin-left: 40px;">book</i>
                            <div>
                                <c:choose>
                                    <c:when test="${card.book.bookState == 'NEW'}">
                                        <div style="margin-left: 20px;">
                                            <strong><spring:message code="bookState.new"/></strong>
                                        </div>
                                    </c:when>
                                    <c:when test="${card.book.bookState == 'LIKE_NEW'}">
                                        <div style="margin-left: 20px;">
                                            <strong><spring:message code="bookState.like.new"/></strong>
                                        </div>
                                    </c:when>
                                    <c:when test="${card.book.bookState == 'VERY_GOOD'}">
                                        <div style="margin-left: 20px;">
                                            <strong><spring:message code="bookState.very.good"/></strong>
                                        </div>
                                    </c:when>
                                    <c:when test="${card.book.bookState == 'GOOD'}">
                                        <div style="margin-left: 20px;">
                                            <strong><spring:message code="bookState.good"/></strong>
                                        </div>
                                    </c:when>
                                    <c:when test="${card.book.bookState == 'ACCEPTABLE'}">
                                        <div style="margin-left: 20px;">
                                            <strong><spring:message code="bookState.acceptable"/></strong>
                                        </div>
                                    </c:when>
                                    <c:when test="${card.book.bookState == 'WORN'}">
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
                            <span uk-icon="icon: location" style="margin-left: 25px;"></span>
                            <p style="text-align: center">
                                <strong>
                                    <c:out value="${card.location}"/>
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
                                <strong>${card.publication.publicationDatetime}</strong></p>
                        </div>
                    </div>

                    <!-- Editorial -->
                    <div class="column-container" style="place-items: center; display: flex;">
                        <div>
                            <p style="text-align: center"><spring:message code="publication.details.editorial"/></p>
                            <i class="material-icons" style="margin-right: 90px;">file-edit</i>
                            <p style="text-align: center"><strong>
                                <c:out value="${card.bookModel.editorial}"/></strong></p>
                        </div>


                    </div>
                </div>
            </div>
        </div>

    </div>


    <!-- Line -->
    <div style="margin-top: 5%; margin-left: 10%;">
        <hr style="width: 50lh;"/>
    </div>

    <div class="row-container" style="margin-left: 2lh; place-items: center; display: flex;">
        <div>
            <p class="uk-text-medium" style="font-size: 25px; text-align: center; max-width: 9lh; margin-left: 1lh;">
                <spring:message code="exchange.description"/></p>
        </div>
        <div>
            <p class="uk-text-medium" style="font-size: 25px; max-width: 9lh; text-align: center; margin-left: 1lh;">
                <spring:message code="exchange.description2"/></p>
        </div>

        <div class="column-container" style="margin-left: 10%;">
            <label class="form-group" style="margin-left: 10px;">
                <spring:message code="book.set.book"/>
            </label>
            <c:if test="${!(completeBooks.size() eq 0)}">
                <div class="uk-container uk-margin-top">
                    <form:form action="${pageContext.request.contextPath}/exchange/initializeexchange" method="post"
                               modelAttribute="completeBookParam" enctype="multipart/form-data">
                        <div class="uk-margin">
                            <div class="uk-form-controls">
                                <form:select path="selectedBookId" cssClass="uk-select">
                                    <c:forEach var="completeBook" items="${completeBooks}">
                                        <form:option value="${completeBook.book.bookId}">
                                            <c:out value='${completeBook.bookModel.title}'/>
                                        </form:option>
                                    </c:forEach>
                                </form:select>

                            </div>
                        </div>
                        <div class="uk-inline">
                            <label class="form-group">
                                <spring:message code="book.set.location"/>
                                <form:input path="location" type="text" class="uk-input"/>
                            </label>
                        </div>

                        <div class="form-container" style="margin-top: 5%; margin-left: 35%;">
                            <input type="hidden" name="publication_id" value="${publication_id}">
                            <button type="submit" class="uk-button uk-button-primary">
                                <spring:message code="add.exchange.submit"/>
                            </button>
                        </div>
                    </form:form>
                </div>
            </c:if>
            <c:if test="${completeBooks.size() eq 0}">
                <button class="uk-button uk-button-primary">
                    <a class="button-text" href="${pageContext.request.contextPath}/book/book_models">
                        <spring:message code="add.book.missing"/>
                    </a>
                </button>
            </c:if>
        </div>
    </div>
</div>
</div>
</div>
</body>
</html>