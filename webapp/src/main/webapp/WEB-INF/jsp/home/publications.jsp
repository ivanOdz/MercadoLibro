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
                    <c:out value='${publications.metadata.search}'/>
                </h2><h2>
                    <c:out value='${publications.metadata.totalResults}'/>
                </h2>

                <!-- Esto tiene que aparecer solo si hay algo buscado -->
                <c:if test="${not empty publications.metadata.search}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}">
                        <input type="hidden" name="is-genre-filter-active" value=${publications.metadata.isGenreFilterActive}>
                        <input type="hidden" name="genre-filter" value=${publications.metadata.genreFilter}>
                        <input type="hidden" name="book-state-filter" value=${publications.metadata.bookStateFilter}>
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
                <c:if test="${publications.metadata.isBookStateFilterActive}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-book-state-filter-active" value="false">
                        <input type="hidden" name="is-genre-filter-active" value=${publications.metadata.isGenreFilterActive}>
                        <input type="hidden" name="genre-filter" value=${publications.metadata.genreFilter}>
                        <input type="hidden" name="search" value="<c:out value='${publications.metadata.search}'/>"/>

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
                <c:if test="${publications.metadata.isGenreFilterActive}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-genre-filter-active" value="false">
                        <input type="hidden" name="is-book-state-filter-active" value=${publications.metadata.isBookStateFilterActive}>
                        <input type="hidden" name="book-state-filter" value=${publications.metadata.bookStateFilter}>
                        <input type="hidden" name="search" value="<c:out value='${publications.metadata.search}'/>">

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

                <c:if test="${!publications.metadata.isBookStateFilterActive}">
                    <h3><spring:message code="filter.condition"/></h3>
                    <ul class="uk-list">
                        <c:forEach var="bookStateWrapper" items="${publications.metadata.bookStateWrapperList}">
                            <li class="ui-search-filter-container">
                                <form action="<c:url value='' />" method="get">
                                    <input type="hidden" name="search" value="<c:out value='${publications.metadata.search}'/>">
                                    <input type="hidden" name="is-book-state-filter-active" value='true'>
                                    <input type="hidden" name="book-state-filter"
                                           value="${bookStateWrapper.bookState}">
                                    <input type="hidden" name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}">
                                    <input type="hidden" name="genre-filter" value="${publications.metadata.genreFilter}">


                                    <a href="#" class="uk-inline uk-search-button uk-button-link" title="BookStateFilterRemove" onclick="this.closest('form').submit(); return false;">
                                        <span class="ui-search-filter-name">
                                                ${bookStateWrapper.displayName} (${bookStateWrapper.resultByState})
                                        </span>
                                    </a>

                                <%--                                    <button type="submit"--%>
<%--                                            class="ui-search-button uk-button uk-button-default uk-button-small"--%>
<%--                                            title="${bookStateWrapper.displayName}">--%>
<%--                                        <span class="ui-search-filter-name">${bookStateWrapper.displayName}</span>--%>
<%--                                        <span class="ui-search-filter-name">(${bookStateWrapper.resultByState})</span>--%>
<%--                                    </button>--%>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>

                <c:if test="${!publications.metadata.isGenreFilterActive}">
                    <h3><spring:message code="filter.genre"/></h3>
                    <ul class="uk-list">
                        <c:forEach var="genreWrapper" items="${publications.metadata.genreWrapperList}">
                            <li class="ui-search-filter-container">
                                <form action="<c:url value='' />" method="get">
                                    <input type="hidden" name="genre-filter" value="${genreWrapper.genre}">
                                    <input type="hidden" name="is-genre-filter-active" value="true">
                                    <input type="hidden" name="book-state-filter" value="${publications.metadata.bookStateFilter}">
                                    <input type="hidden" name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}">
                                    <input type="hidden" name="search" value="<c:out value='${param.search}'/>"/>

<%--                                    <button type="submit"--%>
<%--                                            class="ui-search-button uk-button uk-button-default uk-button-small"--%>
<%--                                            title="${genreWrapper.displayName}">--%>
<%--                                        <span class="ui-search-filter-name">${genreWrapper.displayName}</span>--%>
<%--                                        <span class="ui-search-filter-name">(${genreWrapper.resultByGenre})</span>--%>
<%--                                    </button>--%>
                                    <a href="#" class="uk-inline uk-search-button uk-button-link" title="GenreFilterRemove" onclick="this.closest('form').submit(); return false;">
                                        <span class="ui-search-filter-name">
                                                ${genreWrapper.displayName} (${genreWrapper.resultByGenre})
                                        </span>
                                    </a>

                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
            </div>

            <div class="uk-width-3-4@s col-content">
                    <h5 class="uk-text-large mt-1"><spring:message code="publications.list.available"/></h5>
                <div class="uk-card uk-card-default uk-card-body uk-margin-bottom mt-1 uk-border-rounded uk-border-rounded-medium">
<%--                    <h6 class="uk-text-muted"><spring:message code="publications.list.select"/></h6>--%>
                </div>

                <div class="uk-grid-match uk-child-width-1-2@s uk-child-width-1-3@m mb-1 mt-1" uk-grid>
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
                <hr class="uk-divider-icon">

                <nav aria-label="Pagination" class="uk-position-relative uk-margin">
                    <ul class="uk-pagination uk-flex-center uk-position-center">

                        <!-- Botón Previous -->
                        <c:if test="${publications.metadata.currentPage > 0}">
                            <li>
                                <c:url var="prevPageUrl" value="">
                                    <c:param name="page" value="${publications.metadata.currentPage - 1}" />
                                    <c:param name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}" />
                                    <c:param name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}" />
                                    <c:param name="genre-filter" value="${publications.metadata.genreFilter}" />
                                    <c:param name="book-state-filter" value="${publications.metadata.bookStateFilter}" />
                                    <c:param name="search" value="${publications.metadata.search}" />
                                    <c:param name="sort-type" value="${publications.metadata.sortType}" />
                                </c:url>
                                <a href="${prevPageUrl}">
                                    <span uk-pagination-previous></span>
                                    Anterior
                                </a>
                            </li>
                        </c:if>

                        <!-- Botón de la primera página -->
                        <c:if test="${publications.metadata.currentPage > 1}">
                            <li>
                                <c:url var="firstPageUrl" value="">
                                    <c:param name="page" value="0" />
                                    <c:param name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}" />
                                    <c:param name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}" />
                                    <c:param name="genre-filter" value="${publications.metadata.genreFilter}" />
                                    <c:param name="book-state-filter" value="${publications.metadata.bookStateFilter}" />
                                    <c:param name="search" value="${publications.metadata.search}" />
                                    <c:param name="sort-type" value="${publications.metadata.sortType}" />
                                </c:url>
                                <a href="${firstPageUrl}">1</a>
                            </li>
                        </c:if>

                        <c:if test="${publications.metadata.currentPage - 2 > 0}">
                            <li><span>...</span></li>
                        </c:if>

                        <!-- Páginas centrales -->
                        <c:forEach var="i" begin="${publications.metadata.currentPage > 0 ? publications.metadata.currentPage - 1 : 0}"
                                   end="${publications.metadata.currentPage + 1 <= publications.metadata.maxPage ? publications.metadata.currentPage + 1 : publications.metadata.maxPage}">
                            <li class="${i == publications.metadata.currentPage ? 'uk-active' : ''}">
                                <c:url var="centralPageUrl" value="">
                                    <c:param name="page" value="${i}" />
                                    <c:param name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}" />
                                    <c:param name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}" />
                                    <c:param name="genre-filter" value="${publications.metadata.genreFilter}" />
                                    <c:param name="book-state-filter" value="${publications.metadata.bookStateFilter}" />
                                    <c:param name="search" value="${publications.metadata.search}" />
                                    <c:param name="sort-type" value="${publications.metadata.sortType}" />
                                </c:url>
                                <a href="${centralPageUrl}">${i + 1}</a> <!-- Mostrar i + 1 para la numeración -->
                            </li>
                        </c:forEach>

                        <c:if test="${publications.metadata.currentPage + 2 < publications.metadata.maxPage}">
                            <li><span>...</span></li>
                        </c:if>

                        <!-- Botón de la última página -->
                        <c:if test="${publications.metadata.currentPage + 1 < publications.metadata.maxPage}">
                            <li>
                                <c:url var="lastPageUrl" value="">
                                    <c:param name="page" value="${publications.metadata.maxPage}" />
                                    <c:param name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}" />
                                    <c:param name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}" />
                                    <c:param name="genre-filter" value="${publications.metadata.genreFilter}" />
                                    <c:param name="book-state-filter" value="${publications.metadata.bookStateFilter}" />
                                    <c:param name="search" value="${publications.metadata.search}" />
                                    <c:param name="sort-type" value="${publications.metadata.sortType}" />
                                </c:url>
                                <a href="${lastPageUrl}">${publications.metadata.maxPage + 1}</a> <!-- Mostrar maxPage + 1 -->
                            </li>
                        </c:if>

                        <!-- Botón Next -->
                        <c:if test="${publications.metadata.currentPage < publications.metadata.maxPage}">
                            <li>
                                <c:url var="nextPageUrl" value="">
                                    <c:param name="page" value="${publications.metadata.currentPage + 1}" />
                                    <c:param name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}" />
                                    <c:param name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}" />
                                    <c:param name="genre-filter" value="${publications.metadata.genreFilter}" />
                                    <c:param name="book-state-filter" value="${publications.metadata.bookStateFilter}" />
                                    <c:param name="search" value="${publications.metadata.search}" />
                                    <c:param name="sort-type" value="${publications.metadata.sortType}" />
                                </c:url>
                                <a href="${nextPageUrl}">
                                    Siguiente
                                    <span uk-pagination-next></span>
                                </a>
                            </li>
                        </c:if>
                    </ul>

                    <!-- Botón "Ir al inicio" alineado a la derecha -->
                    <a href="" uk-totop uk-scroll class="uk-position-right uk-margin-right">
                        Back to top
                    </a>
                </nav>

            </div>
        </div>
    </div>
</div>
</body>
</html>
