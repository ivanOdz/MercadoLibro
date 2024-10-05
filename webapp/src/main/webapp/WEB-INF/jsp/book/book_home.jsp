<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>
<%@ page import="ar.edu.itba.paw.models.utils.PublicationState" %>


<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="es" class="custom-style">
<head>


    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/book_home.css?v=1.0" rel="stylesheet"/>


    <title><spring:message code="book.view.title"/></title>
</head>
<body>
<navbar_wo_search></navbar_wo_search>


<div class="uk-background-muted">
    <div class="uk-container main-content">
        <div class="uk-container book-search-section">
            <form class="uk-search uk-search-default custom-search-form book-search" method="get" action="">
                <input class="uk-search-input button-text " type="search"
                       placeholder="<spring:message code='home.search.text'/>"
                       aria-label="Search"
                       name="search"
                       id="search"
                       value="<c:out value='${books.pageInfo.search}'/>">
                <button class="uk-search-icon-flip" uk-search-icon></button>
            </form>
        </div>

        <div class="uk-grid ml-1" uk-grid>


            <div class="uk-width-1-4@s filter-section uk-border-rounded uk-box-shadow-small mt-1 mb-1">
                <h2><c:out value="${books.pageInfo.search}"/></h2>


                <!-- Esto tiene que aparecer solo si hay algo buscado -->
                <c:if test="${not empty books.pageInfo.search}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-book-state-filter-active" value="${books.pageInfo.isBookStateFilterActive}">
                        <input type="hidden" name="is-genre-filter-active" value=${books.pageInfo.isGenreFilterActive}>
                        <input type="hidden" name="genre-filter" value=${books.pageInfo.genreFilter}>
                        <input type="hidden" name="book-state-filter" value=${books.pageInfo.bookStateFilter}>
                        <input type="hidden" name="search" value=""/>

                        <button type="submit" class="ui-search-button uk-button uk-button-default uk-button-small"
                                title="BookStateRemove" uk-close-icon>
							<span class="ui-search-filter-name">
								<spring:message code="delete.search"/>
							</span>
                            <span uk-icon="close"></span>
                        </button>
                    </form>
                </c:if>

                <!-- Esto tiene que aparecer solo si hay un filtro de BookState -->
                <c:if test="${books.pageInfo.isBookStateFilterActive}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-book-state-filter-active" value="false">
                        <input type="hidden" name="is-genre-filter-active" value=${books.pageInfo.isGenreFilterActive}>
                        <input type="hidden" name="genre-filter" value=${books.pageInfo.genreFilter}>
                        <input type="hidden" name="search" value="<c:out value='${books.pageInfo.search}'/>"/>

                        <button type="submit" class="ui-search-button uk-button uk-button-default uk-button-small"
                                title="BookStateRemove" uk-close-icon>
							<span class="ui-search-filter-name">
								<spring:message code="delete.bookstate.filter"/>
							</span>
                            <span uk-icon="close"></span>
                        </button>
                    </form>
                </c:if>

                <!-- Esto tiene que aparecer solo si hay un filtro de Genero -->
                <c:if test="${books.pageInfo.isGenreFilterActive}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-genre-filter-active" value="false">
                        <input type="hidden" name="is-book-state-filter-active" value=${books.pageInfo.isBookStateFilterActive}>
                        <input type="hidden" name="book-state-filter" value=${books.pageInfo.bookStateFilter}>
                        <input type="hidden" name="search" value="<c:out value='${books.pageInfo.search}'/>">

                        <button type="submit"
                                class="uk-inline uk-search-button uk-button uk-button-default uk-button-small"
                                title="GenreFilterRemove">
							<span class="ui-search-filter-name">
								<spring:message code="delete.genre.filter"/>
							</span>
                            <span uk-icon="close"></span>
                        </button>
                    </form>
                </c:if>

                <c:if test="${!books.pageInfo.isBookStateFilterActive}">
                    <h3><spring:message code="filter.condition"/></h3>
                    <ul class="uk-list">
                        <c:forEach var="bookStateWrapper" items="${books.pageInfo.bookStateWrapperList}">
                            <li class="ui-search-filter-container">
                                <form action="<c:url value='' />" method="get">
                                    <input type="hidden" name="search" value="<c:out value='${books.pageInfo.search}'/>">
                                    <input type="hidden" name="is-book-state-filter-active" value='true'>
                                    <input type="hidden" name="book-state-filter"
                                           value="${bookStateWrapper.bookState}">
                                    <input type="hidden" name="is-genre-filter-active" value="${books.pageInfo.isGenreFilterActive}">
                                    <input type="hidden" name="genre-filter" value="${books.pageInfo.genreFilter}">

<%--                                    <button type="submit"--%>

<%--                                            class="ui-search-button uk-button uk-button-default uk-button-small"--%>
<%--                                            title="${bookStateWrapper.displayName}">--%>
<%--                                        <span class="ui-search-filter-name">${bookStateWrapper.displayName}</span>--%>
<%--                                        <span class="ui-search-filter-name">(${bookStateWrapper.resultByState})</span>--%>
<%--                                    </button>--%>
                                    <a href="#" class="uk-inline uk-search-button uk-button-link" title="BookStateFilterRemove" onclick="this.closest('form').submit(); return false;">
                                        <span class="ui-search-filter-name">
                                                ${bookStateWrapper.displayName} (${bookStateWrapper.resultByState})
                                        </span>
                                    </a>

                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>

                <c:if test="${!books.pageInfo.isGenreFilterActive}">
                    <h3><spring:message code="filter.genre"/></h3>
                    <ul class="uk-list">
                        <c:forEach var="genreWrapper" items="${books.pageInfo.genreWrapperList}">
                            <li class="ui-search-filter-container">
                                <form action="<c:url value='' />" method="get">
                                    <input type="hidden" name="genre-filter" value="${genreWrapper.genre}">
                                    <input type="hidden" name="is-genre-filter-active" value="true">
                                    <input type="hidden" name="book-state-filter" value="${books.pageInfo.bookStateFilter}">
                                    <input type="hidden" name="is-book-state-filter-active" value="${books.pageInfo.isBookStateFilterActive}">
                                    <input type="hidden" name="search" value="<c:out value='${books.pageInfo.search}'/>"/>

                                    <a href="#" class="uk-inline uk-search-button uk-button-link" title="GenreFilterRemove" onclick="this.closest('form').submit(); return false;">
                                        <span class="ui-search-filter-name">
                                                ${genreWrapper.displayName} (${genreWrapper.resultByGenre})
                                        </span>
                                    </a>
<%--                                    <button type="submit"--%>
<%--                                            class="ui-search-button uk-button uk-button-default uk-button-small"--%>
<%--                                            title="${genreWrapper.displayName}">--%>
<%--                                        <span class="ui-search-filter-name">${genreWrapper.displayName}</span>--%>
<%--                                        <span class="ui-search-filter-name">(${genreWrapper.resultByGenre})</span>--%>
<%--                                    </button>--%>
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
                    <c:forEach var="card" items="${books.data}">
                        <div>
                            <div class="uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded custom-link">
                                <figure class="uk-margin-bottom">
                                    <c:choose>
                                        <c:when test="${card.images[0] != null}">
                                            <img class="book-image" src="${pageContext.request.contextPath}/images/${card.images[0]}" alt="bookImage"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img class="book-image" src="${pageContext.request.contextPath}/images/book.jpg" alt="book"/>
                                        </c:otherwise>
                                    </c:choose>
                                </figure>

                                <div class="uk-flex uk-flex-column uk-flex-column uk-margin-bottom">
                                    <div class="">
                                        <h5 class="uk-card-title custom-link">${card.bookModel.title}</h5>
                                        <p class="small-gray-text custom-link">${card.bookModel.authors}</p>
                                    </div>
                                    <c:choose>
                                        <c:when test="${card.available}">
                                            <a class="uk-button uk-button-default uk-button-primary uk-width-1-1" href="#modal-sections-${card.bookId}" uk-toggle>
                                                <spring:message code="book.publish.button"/>
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="uk-button uk-button-primary uk-width-1-1" disabled>
                                                <spring:message code="book.published.button"/>
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <!-- Modal específico para cada card -->
                            <div id="modal-sections-${card.bookId}" uk-modal>
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
                                                            <span class="uk-form-icon uk-form-icon-flip" uk-icon="icon: location"></span>
                                                            <input class="uk-input" type="text" name="location"
                                                                   aria-label="Not clickable icon"/>
                                                        </div>
                                                        <input class="uk-input" type="hidden"
                                                               value="${card.bookId}" name="bookId"
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
                    </c:forEach>
                </div>
                <hr class="uk-divider-icon">

                <nav aria-label="Pagination">
                    <ul class="uk-pagination uk-flex-center" uk-margin>
                        <!-- Botón Previous (solo mostrar si currentPage > 0) -->
                        <c:if test="${books.pageInfo.currentPage > 0}">
                            <li>
                                <a href="?page=${books.pageInfo.currentPage - 1}">
                                    <span uk-pagination-previous></span>
                                </a>
                            </li>
                        </c:if>

                        <!-- Página anterior (mostrar si currentPage > 0) -->
                        <c:if test="${books.pageInfo.currentPage > 0}">
                            <li>
                                <a href="?page=${books.pageInfo.currentPage - 1}">
                                        ${books.pageInfo.currentPage} <!-- Mostrar página anterior -->
                                </a>
                            </li>
                        </c:if>

                        <!-- Página actual (siempre visible y centrada) -->
                        <li class="uk-active">
                            <span aria-current="page">${books.pageInfo.currentPage + 1}</span>
                        </li>

                        <!-- Página siguiente (mostrar si currentPage < maxPage) -->
                        <c:if test="${books.pageInfo.currentPage < books.pageInfo.maxPage}">
                            <li>
                                <a href="?page=${books.pageInfo.currentPage + 1}">
                                        ${books.pageInfo.currentPage + 2} <!-- Mostrar página siguiente -->
                                </a>
                            </li>
                        </c:if>

                        <!-- Botón Next (solo mostrar si currentPage < maxPage) -->
                        <c:if test="${books.pageInfo.currentPage < books.pageInfo.maxPage}">
                            <li>
                                <a href="?page=${books.pageInfo.currentPage + 1}">
                                    <span uk-pagination-next></span>
                                </a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>

</body>
</html>

