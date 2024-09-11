<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="es">
<head>
    <link href="${pageContext.request.contextPath}/css/publications.css?v=1.0" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <title><spring:message code="book.view.title"/></title>
</head>
<body>
<c:url var="exchangeUrl" value="/exchange"/>
<c:url var="booksUrl" value="/book"/>
<c:url var="profileUrl" value="/profile"/>
<c:url var="newBookFromScratch" value="/book/book_form"/>
<c:url var="uploadNewPrecharged" value="/book/book_form"/>

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
                    <%--					<li><a class="pl-1 pr-1" href="<c:url value="${exchangeUrl}"/>"><spring:message code="home.exchange.view"/></a></li>--%>
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

                    <%--					<li><a class="pl-1 pr-1" href="<c:url value="${profileUrl}"/>"><spring:message code="home.profile.view"/></a></li>--%>
                </ul>
            </div>
        </div>
    </div>
</nav>


<div class="uk-background-muted">
    <div class="uk-container">
        <div class="uk-grid ml-1 uk-margin-top" uk-grid>
<%--            <div class="uk-width-1-4@s filter-section uk-border-rounded uk-box-shadow-small mt-1 mb-1">--%>
<%--                <ul uk-accordion="multiple: true">--%>
<%--                    <li class="uk-open">--%>
<%--                        <a class="uk-accordion-title">--%>
<%--                            <spring:message code="filter.genre"/>--%>
<%--                        </a>--%>
<%--                        <div class="uk-accordion-content">--%>
<%--                            <c:forEach var="genreWrapper" items="${genres}">--%>
<%--                                <div class="uk-margin">--%>
<%--                                    <input class="uk-checkbox" type="checkbox" checked="checked" name="genre" value="${genreWrapper.genre}" />--%>
<%--                                    <label>${genreWrapper.displayName}</label>--%>
<%--                                </div>--%>
<%--                            </c:forEach>--%>
<%--                        </div>--%>
<%--                    </li>--%>
<%--                    <li>--%>
<%--                        <a class="uk-accordion-title"><spring:message code="filter.condition"/></a>--%>
<%--                        <div class="uk-accordion-content">--%>
<%--                            <c:forEach var="bookStateWrapper" items="${bookStates}">--%>
<%--                                <div class="uk-margin">--%>
<%--                                    <input class="uk-checkbox" type="checkbox" checked="checked" name="bookState" value="${bookStateWrapper.bookState}" />--%>
<%--                                    <label>${bookStateWrapper.displayName}</label>--%>
<%--                                </div>--%>
<%--                            </c:forEach>--%>
<%--                        </div>--%>
<%--                    </li>--%>
<%--                    <li>--%>
<%--                        <a class="uk-accordion-title"><spring:message code="filter.location"/></a>--%>
<%--                        <div class="uk-accordion-content">--%>
<%--                            <div class="uk-margin">--%>
<%--                                <label>--%>
<%--                                    <input class="uk-checkbox" type="checkbox" checked="checked" />--%>
<%--                                    Ivan�s House--%>
<%--                                </label>--%>
<%--                            </div>--%>
<%--                            <div class="uk-margin">--%>
<%--                                <label>--%>
<%--                                    <input class="uk-checkbox" type="checkbox" />--%>
<%--                                    Option 2--%>
<%--                                </label>--%>
<%--                            </div>--%>
<%--                            <div class="uk-margin">--%>
<%--                                <label>--%>
<%--                                    <input class="uk-checkbox" type="checkbox" />--%>
<%--                                    Option 3--%>
<%--                                </label>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </li>--%>
<%--                </ul>--%>
<%--            </div>--%>

            <div class="uk-width-3-4@s col-content">
                <div class="uk-card uk-card-default uk-card-body uk-margin-bottom mt-1 uk-border-rounded uk-border-rounded-medium">
                    <h5 class="uk-text-large"><spring:message code="book.view.title"/></h5>
                    <h6 class="uk-text-muted"><spring:message code="book.list.select"/></h6>
                </div>

                <div class="uk-grid-match uk-child-width-1-2@s uk-child-width-1-3@m mb-1" uk-grid>
                    <c:forEach var="card" items="${cardBookList}">
                        <div>
                            <div class="uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded custom-link">
                                <figure class="uk-margin-bottom">
                                    <c:choose>
                                        <c:when test="${card.image != null}">
                                            <img class="book-image" src="${pageContext.request.contextPath}/images/${card.image.imageId}" alt="bookImage"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img class="book-image" src="${pageContext.request.contextPath}/images/book.jpg" alt="book"/>
                                        </c:otherwise>
                                    </c:choose>
                                </figure>
                                <h5 class="uk-card-title custom-link">${card.bookModel.title}</h5>
                                <p class="small-gray-text custom-link">${card.authorsString}</p>

                                <c:choose>
                                    <c:when test="${card.canPublish}">

                                        <a class="uk-button uk-button-default uk-button-primary uk-width-1-1" href="#modal-sections" uk-toggle><spring:message code="book.publish.button"/></a>

                                        <div id="modal-sections" uk-modal>
                                            <div class="uk-modal-dialog">
                                                <button class="uk-modal-close-default" type="button" uk-close></button>
                                                <div class="uk-modal-header">
                                                    <h5><spring:message code="book.set.location"/></h5>

                                                    <div class="uk-margin">
                                                        <div class="uk-inline">
                                                            <span class="uk-form-icon uk-form-icon-flip" uk-icon="icon: location"></span>
                                                            <input class="uk-input" type="text" aria-label="Not clickable icon">
                                                        </div>
                                                    </div>

                                                    <label class="form-group">
                                                        <spring:message code="add.publication.location"/>
                                                        <form:input path="location" type="text" class="form-input"/>
                                                    </label>

                                                    <div class="uk-flex uk-flex-between uk-flex-middle uk-text-right uk-margin-small-top">
                                                        <button class="uk-button uk-button-default uk-modal-close uk-width-auto" type="button"><spring:message code="button.cancel"/></button>

<%--                                                        <form action="${pageContext.request.contextPath}/createpublication" method="post" class="uk-form-stacked">--%>
<%--                                                            <input type="hidden" name="bookId" value="${card.book.bookId}"/>--%>
<%--                                                            <input type="hidden" name="location" value="${location}"/>--%>
<%--                                                                <button type="submit" class="uk-button uk-button-primary uk-width-auto"><spring:message code="book.publish.button"/></button>--%>
<%--                                                        </form>--%>
                                                        <form action="${pageContext.request.contextPath}/createpublication" method="post" class="uk-grid-large uk-grid" style="justify-content: center;">
                                                            <div class="uk-margin" style="justify-content: center">
                                                                <div class="uk-width-1-1" >
                                                                <div class="uk-width-1-1 uk-margin-top">
                                                                    <div>
                                                                        <label>
                                                                            <spring:message code="book.set.location"/>
                                                                        </label>
                                                                    </div>
                                                                    <div class="uk-inline">
                                                                        <span class="uk-form-icon uk-form-icon-flip" uk-icon="icon: location"></span>
                                                                        <input class="uk-input" type="text" name="location" aria-label="Not clickable icon"/>
                                                                    </div>
                                                                    <input class="uk-input" type="text" name="bookId" aria-label="Not clickable icon"/>
                                                                </div>
                                                                    <div class="uk-margin-top uk-button-group" style="margin-left: 50px;">
                                                                        <button class="uk-button uk-button-primary"> <spring:message code="hwc.login.submit"/> </button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </form>

                                                    </div>
                                                </div>
                                            </div>
                                        </div>


                                    </c:when>

                                    <c:otherwise>
                                        <button class="uk-button uk-button-primary uk-width-1-1" disabled><spring:message code="book.published.button"/></button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
