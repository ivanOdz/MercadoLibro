<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>

<html>

<%@include file="/WEB-INF/jsp/head/headers.jsp" %>

<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="<c:url value='/css/navbar.css' />" rel="stylesheet"/>
    <link href="<c:url value='/css/publicationDetail.css?v=1.0' />" rel="stylesheet"/>

    <title><spring:message code="solicit.exchange"/></title>
</head>

<c:url var="postUrl" value="/exchange/initializeexchange"/>

<div class="uk-container uk-margin-top">
    <form:form modelAttribute="exchangeForm" action="${postUrl}" method="post" enctype="multipart/form-data">
        <h2 class="uk-heading-line">
            <spring:message code="solicit.exchange.title"/>
        </h2>
        <h4 class="uk-article-meta">
            <spring:message code="solicit.exchange.description"/>
        </h4>

        <!-- Line -->
        <div style="margin-top: 2%;">
            <hr style="width: 50lh;"/>
        </div>

        <div class="uk-container">
            <div class="row-container">
                <h3 style="margin-right: 10%;"><spring:message code="exchange.book"/></h3>

                <c:choose>
                    <c:when test="${!empty publication.book.images && !publication.book.images[0].image.isImageNull}">
                        <img src="images/${publication.book.images[0].image.imageId}" alt="Book Image"
                             style="margin-right: 3%; width:13%; height:5%;"/>
                    </c:when>
                    <c:otherwise>
                        <img class="book-image"
                             style="margin-right: 3%; width:13%; height:5%;"
                             src="<c:url value='/images/book.jpg' />" alt="book"/>
                    </c:otherwise>
                </c:choose>


                <div class="column-container">
                    <p><spring:message code="exchange.book.title"/> ${publication.book.bookModel.title} </p>
                    <c:forEach var="author" items="${publication.book.bookModel.authors}">
                        <p><spring:message code="exchange.book.authors"/> ${author.authorName} </p>
                    </c:forEach>
                    <p><spring:message code="exchange.book.editorial"/> ${publication.book.bookModel.editorial} </p>
                    <p><spring:message code="exchange.book.edition"/> ${publication.book.bookModel.edition} </p>
                </div>
            </div>
        </div>

        <!-- Line -->
        <div style="margin-top: 2%;">
            <hr style="width: 50lh;"/>
        </div>

        <div class="uk-margin">
            <spring:message code="exchange.book.yours"/>
            <div class="uk-form-controls">
                <ul class="uk-list uk-list-divider custom-select">
                    <c:forEach var="availableBook" items="${availableBooks}">
                        <li>
                            <label>
                                <div class="uk-grid-small uk-flex-middle" uk-grid>
                                    <div><!-- ? -->
                                        <form:radiobutton path="bookId" value="${availableBook.bookId}" />
                                    </div>
                                    <div class="uk-width-auto">
                                        <c:choose>
                                            <c:when test="${!availableBook.images[0].image.isImageNull}">
                                                <img src="<c:url value='/images/${availableBook.images[0].image.imageId}' />"
                                                     alt="Book Image" class="uk-border-circle" width="40" height="40">
                                            </c:when>
                                            <c:otherwise>
                                                <img class="uk-border-circle" width="40" height="40"
                                                     src="<c:url value='/images/book.jpg' />" alt="book"/>
                                            </c:otherwise>
                                        </c:choose>

                                    </div>
                                    <div class="uk-width-expand">
                                        <div class="uk-text-bold">${availableBook.bookModel.title}</div>
                                        <c:forEach var="author" items="${availableBook.bookModel.authors}">
                                        <div class="uk-text-small">${author.authorName}</div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </label>
                        </li>
                    </c:forEach>
                </ul>
            </div>

        </div>

        <div class="uk-inline">
            <label class="form-group">
                <form:input type="hidden" path="bookId" value="${availableBook.bookId}" class="uk-input"/>
                
                <spring:message code="book.set.location"/>
				<form:select path="locationId" class="uk-select no-arrow-select" aria-label="Not clickable icon" style="width: 90%">
				    <c:forEach var="userLocation" items="${user.userLocations}">
						<form:options items="${user.userLocations}" itemValue="locationId" itemLabel="locationString"/>
				    </c:forEach>
				</form:select>
            </label>
            <form:errors path="bookId" element="p" cssStyle="color: red;"/>
        </div>

        <div class="form-container" style="margin-top: 5%; margin-left: 35%; margin-bottom: 2%;">
            <form:input path="publicationId" type="hidden" value="${publication.publicationId}"/>
            <button type="submit" class="uk-button uk-button-primary">
                <spring:message code="add.exchange.submit"/>
            </button>
        </div>
    </form:form>
</div>
</html>