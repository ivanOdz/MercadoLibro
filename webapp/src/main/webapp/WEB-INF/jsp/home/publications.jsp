<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html lang="es" class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/publications.css?v=1.0" rel="stylesheet"/>

    <title><spring:message code="publications.list.brand.logo"/></title>

</head>
<body class="main">
<navbar></navbar>

<div class="uk-background-muted">
    <div class="uk-container">
        <div class="uk-grid ml-1 uk-margin-top" uk-grid>
            <div class="uk-width-1-4@s filter-section uk-border-rounded uk-box-shadow-small mt-1 mb-1">
                <h2>
                    <c:out value='${publications.pageInfo.search}'/>
                </h2><h2>
                    <c:out value='${publications.pageInfo.totalResults}'/>
                </h2>

                <!-- Esto tiene que aparecer solo si hay algo buscado -->
                <c:if test="${not empty publications.pageInfo.search}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-book-state-filter-active" value="${publications.pageInfo.isBookStateFilterActive}">
                        <input type="hidden" name="is-genre-filter-active" value=${publications.pageInfo.isGenreFilterActive}>
                        <input type="hidden" name="genre-filter" value=${publications.pageInfo.genreFilter}>
                        <input type="hidden" name="book-state-filter" value=${publications.pageInfo.bookStateFilter}>
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
                <c:if test="${publications.pageInfo.isBookStateFilterActive}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-book-state-filter-active" value="false">
                        <input type="hidden" name="is-genre-filter-active" value=${publications.pageInfo.isGenreFilterActive}>
                        <input type="hidden" name="genre-filter" value=${publications.pageInfo.genreFilter}>
                        <input type="hidden" name="search" value="<c:out value='${publications.pageInfo.search}'/>"/>

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
                <c:if test="${publications.pageInfo.isGenreFilterActive}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-genre-filter-active" value="false">
                        <input type="hidden" name="is-book-state-filter-active" value=${publications.pageInfo.isBookStateFilterActive}>
                        <input type="hidden" name="book-state-filter" value=${publications.pageInfo.bookStateFilter}>
                        <input type="hidden" name="search" value="<c:out value='${publications.pageInfo.search}'/>">

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

                <c:if test="${!publications.pageInfo.isBookStateFilterActive}">
                    <h3><spring:message code="filter.condition"/></h3>
                    <ul class="uk-list">
                        <c:forEach var="bookStateWrapper" items="${publications.pageInfo.bookStateWrapperList}">
                            <li class="ui-search-filter-container">
                                <form action="<c:url value='' />" method="get">
                                    <input type="hidden" name="search" value="<c:out value='${publications.pageInfo.search}'/>">
                                    <input type="hidden" name="is-book-state-filter-active" value='true'>
                                    <input type="hidden" name="book-state-filter"
                                           value="${bookStateWrapper.bookState}">
                                    <input type="hidden" name="is-genre-filter-active" value="${publications.pageInfo.isGenreFilterActive}">
                                    <input type="hidden" name="genre-filter" value="${publications.pageInfo.genreFilter}">


                                    <button type="submit"
                                            class="ui-search-button uk-button uk-button-default uk-button-small"
                                            title="${bookStateWrapper.displayName}">
                                        <span class="ui-search-filter-name">${bookStateWrapper.displayName}</span>
                                        <span class="ui-search-filter-name">(${bookStateWrapper.resultByState})</span>
                                    </button>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>

                <c:if test="${!publications.pageInfo.isGenreFilterActive}">
                    <h3><spring:message code="filter.genre"/></h3>
                    <ul class="uk-list">
                        <c:forEach var="genreWrapper" items="${publications.pageInfo.genreWrapperList}">
                            <li class="ui-search-filter-container">
                                <form action="<c:url value='' />" method="get">
                                    <input type="hidden" name="genre-filter" value="${genreWrapper.genre}">
                                    <input type="hidden" name="is-genre-filter-active" value="true">
                                    <input type="hidden" name="book-state-filter" value="${publications.pageInfo.bookStateFilter}">
                                    <input type="hidden" name="is-book-state-filter-active" value="${publications.pageInfo.isBookStateFilterActive}">
                                    <input type="hidden" name="search" value="<c:out value='${param.search}'/>"/>

                                    <button type="submit"
                                            class="ui-search-button uk-button uk-button-default uk-button-small"
                                            title="${genreWrapper.displayName}">
                                        <span class="ui-search-filter-name">${genreWrapper.displayName}</span>
                                        <span class="ui-search-filter-name">(${genreWrapper.resultByGenre})</span>
                                    </button>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
            </div>

            <div class="uk-width-3-4@s col-content">
                <div class="uk-card uk-card-default uk-card-body uk-margin-bottom mt-1 uk-border-rounded uk-border-rounded-medium">
                    <h5 class="uk-text-large"><spring:message code="publications.list.available"/></h5>
                    <h6 class="uk-text-muted"><spring:message code="publications.list.select"/></h6>
                </div>

                <div class="uk-grid-match uk-child-width-1-2@s uk-child-width-1-3@m mb-1" uk-grid>
                    <c:forEach var="card" items="${publications.data}">
                        <div>
                            <a href="<c:url value='publications/${card.publicationId}'>
								</c:url>"
                               class="uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded custom-link">
                                <figure class="uk-margin-bottom">
                                    <c:choose>
                                        <c:when test="${card.book.images[0] != null}">
                                            <img class="book-image"
                                                 src="${pageContext.request.contextPath}/images/${card.book.images[0]}"
                                                 alt="bookImage"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img class="book-image"
                                                 src="${pageContext.request.contextPath}/images/book.jpg" alt="book"/>
                                        </c:otherwise>
                                    </c:choose>
                                </figure>

                                <h5 class="uk-card-title custom-link">
                                    <c:out value='${card.book.bookModel.title}'/>
                                </h5>
                                <p class="small-gray-text custom-link">
                                    <c:out value='${card.book.bookModel.authors}'/>
                                </p>
                            </a>
                        </div>
                    </c:forEach>
                </div>
                <nav aria-label="Pagination">
                    <ul class="uk-pagination uk-flex-center" uk-margin>
                        <!-- Botón Previous (solo mostrar si currentPage > 0) -->
                        <c:if test="${publications.pageInfo.currentPage > 0}">
                            <li>
                                <a href="?page=${publications.pageInfo.currentPage - 1}">
                                    <span uk-pagination-previous></span>
                                </a>
                            </li>
                        </c:if>

                        <!-- Página anterior (mostrar si currentPage > 0) -->
                        <c:if test="${publications.pageInfo.currentPage > 0}">
                            <li>
                                <a href="?page=${publications.pageInfo.currentPage - 1}">
                                        ${publications.pageInfo.currentPage} <!-- Mostrar página anterior -->
                                </a>
                            </li>
                        </c:if>

                        <!-- Página actual (siempre visible y centrada) -->
                        <li class="uk-active">
                            <span aria-current="page">${publications.pageInfo.currentPage + 1}</span>
                        </li>

                        <!-- Página siguiente (mostrar si currentPage < maxPage) -->
                        <c:if test="${publications.pageInfo.currentPage < publications.pageInfo.maxPage}">
                            <li>
                                <a href="?page=${publications.pageInfo.currentPage + 1}">
                                        ${publications.pageInfo.currentPage + 2} <!-- Mostrar página siguiente -->
                                </a>
                            </li>
                        </c:if>

                        <!-- Botón Next (solo mostrar si currentPage < maxPage) -->
                        <c:if test="${publications.pageInfo.currentPage < publications.pageInfo.maxPage}">
                            <li>
                                <a href="?page=${publications.pageInfo.currentPage + 1}">
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
