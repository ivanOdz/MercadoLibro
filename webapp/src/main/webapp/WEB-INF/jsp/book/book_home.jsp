<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>


<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="es" class="custom-style">
<head>
    <link href="${pageContext.request.contextPath}/css/book_home.css?v=1.0" rel="stylesheet"/>

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>

    <title><spring:message code="book.view.title"/></title>
</head>
<body>
<navbar></navbar>


<div class="uk-background-muted">
    <div class="uk-container">
        <div class="uk-grid ml-1 uk-margin-top" uk-grid>
            <div class="uk-width-1-4@s filter-section uk-border-rounded uk-box-shadow-small mt-1 mb-1">
                <h2>${param.search}</h2>

                <!-- Esto tiene que aparecer solo si hay un filtro de BookState -->
                <c:if test="${bookStateFilter != '6'}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="book-state-filter" value="">
                        <input type="hidden" name="genre-filter" value=${genreFilter}>
                        <input type="hidden" name="search" value='<c:out value="${param.search}"/>'/>

                        <button type="submit" class="ui-search-button uk-button uk-button-default uk-button-small" title="BookStateRemove">
							<span class="ui-search-filter-name">
								<spring:message code="delete.bookstate.filter"/>

							</span>
                            <span uk-icon="close"></span>

                        </button>
                    </form>
                </c:if>

                <!-- Esto tiene que aparecer solo si hay un filtro de Genero -->
                <c:if test="${genreFilter != '32'}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="book-state-filter" value=${bookStateFilter}>
                        <input type="hidden" name="genre-filter" value="">
                        <input type="hidden" name="search" value='<c:out value="${param.search}"/>'/>

                        <button type="submit" class="ui-search-button uk-button uk-button-default uk-button-small" title="GenreFilterRemove">
							<span class="ui-search-filter-name">
								<spring:message code="delete.genre.filter"/>
							</span>
                            <span uk-icon="close"></span>

                        </button>
                    </form>
                </c:if>

                <c:if test="${bookStateFilter == '6'}">
                    <h3><spring:message code="filter.condition"/></h3>
                    <ul class="uk-list">
                        <c:forEach var="bookStateWrapper" items="${bookStates}">
                            <li class="ui-search-filter-container">
                                <form action="<c:url value='' />" method="get">
                                    <input type="hidden" name="book-state-filter"
                                           value="${bookStateWrapper.bookState.value}">
                                    <input type="hidden" name="genre-filter" value="${genreFilter}">
                                    <input type="hidden" name="search" value='<c:out value="${param.search}"/>'/>

                                    <button type="submit" class="ui-search-button uk-button uk-button-default uk-button-small"
                                            title="${bookStateWrapper.displayName}">
                                        <span class="ui-search-filter-name">${bookStateWrapper.displayName}</span>
                                    </button>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>

                <c:if test="${genreFilter == '32'}">
                    <h3><spring:message code="filter.genre"/></h3>
                    <ul class="uk-list">
                        <c:forEach var="genreWrapper" items="${genres}">
                            <li class="ui-search-filter-container">
                                <form action="<c:url value='' />" method="get">
                                    <input type="hidden" name="genre-filter" value="${genreWrapper.genre.value}">
                                    <input type="hidden" name="book-state-filter" value="${bookStateFilter}">
                                    <input type="hidden" name="search" value='<c:out value="${param.search}"/>'/>

                                    <button type="submit" class="ui-search-button uk-button uk-button-default uk-button-small" title="${genreWrapper.displayName}">
                                        <span class="ui-search-filter-name">${genreWrapper.displayName}</span>
                                    </button>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
            </div>


            <div class="uk-width-3-4@s col-content">
                <div class="uk-card uk-card-default uk-card-body uk-margin-bottom mt-1 uk-border-rounded uk-border-rounded-medium">
                    <h5 class="uk-text-large"><spring:message code="book.view.title"/></h5>
                    <h6 class="uk-text-muted"><spring:message code="book.list.select"/></h6>
                </div>

                <div class="uk-grid-match uk-child-width-1-2@s uk-child-width-1-3@m mb-1" uk-grid
                     uk-height-match="target: > div > .uk-card">
                    <c:forEach var="card" items="${cardBookList}">
                    <c:set var="cardClass"
                           value="${card.canPublish ? 'uk-card uk-card-default uk-card-hover' : 'uk-card uk-card-default'}"/>
                    <c:choose>
                    <c:when test="${card.canPublish}">
                    <a href="#modal-sections-${card.book.bookId}" uk-toggle class="uk-display-block custom-link">
                        </c:when>
                        <c:otherwise>

                        </c:otherwise>
                        </c:choose>
                        <div>
                            <div class="${cardClass} uk-card-body uk-border-rounded custom-link">
                                <figure class="uk-margin-bottom">
                                    <c:choose>
                                        <c:when test="${card.image != null}">
                                            <img class="book-image"
                                                 src="${pageContext.request.contextPath}/images/${card.image}"
                                                 alt="bookImage"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img class="book-image"
                                                 src="${pageContext.request.contextPath}/images/book.jpg" alt="book"/>
                                        </c:otherwise>
                                    </c:choose>
                                </figure>
                                <h5 class="uk-card-title m-0 custom-link">
                                    <c:out value="${card.bookModel.title}"/>
                                </h5>
                                <p class="small-gray-text m-0 custom-link">
                                    <c:out value="${card.authorsString}"/>
                                </p>

                                <div id="modal-sections-${card.book.bookId}" uk-modal>
                                    <div class="uk-modal-dialog">
                                        <button class="uk-modal-close-default" type="button" uk-close></button>
                                        <div class="uk-modal-header">

                                            <form action="${pageContext.request.contextPath}/createpublication"
                                                  method="post" class="uk-grid-large uk-grid"
                                                  style="justify-content: center;">
                                                <div class="uk-margin" style="justify-content: center">
                                                    <div class="uk-width-1-1">
                                                        <div class="uk-width-1-1 uk-margin-top">
                                                            <div class="uk-margin-bottom">
                                                                <label class="uk-margin">
                                                                    <spring:message code="book.set.location"/>
                                                                </label>
                                                            </div>
                                                            <div class="uk-inline">
                                                                <span class="uk-form-icon uk-form-icon-flip"
                                                                      uk-icon="icon: location"></span>
                                                                <input class="uk-input" type="text" name="location"
                                                                       aria-label="Not clickable icon"/>
                                                            </div>
                                                            <input class="uk-input" type="hidden"
                                                                   value="${card.book.bookId}" name="bookId"
                                                                   aria-label="Not clickable icon"/>
                                                        </div>
                                                        <div class="uk-margin-top uk-button-group"
                                                             style="margin-left: 50px;">
                                                            <button class="uk-button uk-button-primary"><spring:message
                                                                    code="book.publish.button"/></button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>

                                    </div>
                                </div>
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

