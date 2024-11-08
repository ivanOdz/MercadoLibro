<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html lang="es" class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="<c:url value='/css/navbar.css?v=1.0' />" rel="stylesheet"/>
    <link href="<c:url value='/css/publications.css?v=1.0' />" rel="stylesheet"/>

    <title><spring:message code="publications.list.brand.logo"/></title>

</head>
<body class="main">
<navbar></navbar>

<div class="uk-background-muted">
    <div class="uk-container">
        <div class="uk-grid ml-1 uk-margin-top mb-2" uk-grid>
            <div class="uk-width-1-4@s filter-section uk-border-rounded uk-box-shadow-small mt-1 mb-1">
                <!-- Esto tiene que aparecer solo si hay un filtro de BookState -->
                <c:if test="${publications.metadata.isBookStateFilterActive}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-book-state-filter-active" value="false">
                        <input type="hidden" name="is-genre-filter-active" value=${publications.metadata.isGenreFilterActive}>
                        <input type="hidden" name="genre-filter" value=${publications.metadata.genreFilter.value}>
                        <input type="hidden" name="search" value="<c:out value='${publications.metadata.search}'/>"/>

                        <button type="submit" class="uk-button uk-button-danger uk-button-small delete-button"
                                title="BookStateRemove">
							<span class="ui-search-filter-name">
								<spring:message code="delete.bookstate.filter"/>
							</span>
                            <span uk-icon="trash"></span>
                        </button>
                    </form>

                </c:if>

                <!-- Esto tiene que aparecer solo si hay un filtro de Genero -->
                <c:if test="${publications.metadata.isGenreFilterActive}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-genre-filter-active" value="false">
                        <input type="hidden" name="is-book-state-filter-active" value=${publications.metadata.isBookStateFilterActive}>
                        <input type="hidden" name="book-state-filter" value=${publications.metadata.bookStateFilter.value}>
                        <input type="hidden" name="search" value="<c:out value='${publications.metadata.search}'/>">

                        <button type="submit"
                                class="uk-button uk-button-danger uk-button-small delete-button"
                                title="GenreFilterRemove">
							<span class="ui-search-filter-name">
								<spring:message code="delete.genre.filter"/>
							</span>
                            <span uk-icon="trash"></span>
                        </button>
                    </form>
                </c:if>

                <c:if test="${!publications.metadata.isBookStateFilterActive}">
                    <h3><spring:message code="filter.condition"/></h3>
                    <c:if test="${empty bookStateWrapperList}">
                        <p><spring:message code="filter.empty.condition"/></p>
                    </c:if>
                    <ul class="uk-list">
                        <c:forEach var="bookStateWrapper" items="${bookStateWrapperList}">
                            <li class="ui-search-filter-container">
                                <form action="<c:url value='' />" method="get">
                                    <input type="hidden" name="search" value="<c:out value='${publications.metadata.search}'/>">
                                    <input type="hidden" name="is-book-state-filter-active" value='true'>
                                    <input type="hidden" name="book-state-filter"
                                           value="${bookStateWrapper.bookState.value}">
                                    <input type="hidden" name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}">
                                    <input type="hidden" name="genre-filter" value="${publications.metadata.genreFilter.value}">

                                    <a href="#" class="uk-inline uk-search-button uk-button-link" title="BookStateFilterRemove" onclick="this.closest('form').submit(); return false;">

                                        <c:set var="i18nBookStateKey" value="${bookStateWrapper.bookState.value}" />
                                        <span class="ui-search-filter-name">
                                            <spring:message code="${i18nBookStateKey}"/>
                                        </span>
                                        <span> (${bookStateWrapper.resultByState})</span>
                                    </a>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>

                <c:if test="${!publications.metadata.isGenreFilterActive}">
                    <h3><spring:message code="filter.genre"/></h3>
                    <c:if test="${empty genreWrapperList}">
                        <p><spring:message code="filter.empty.genres"/></p>
                    </c:if>

                    <ul class="uk-list">
                        <c:forEach var="genreWrapper" items="${genreWrapperList}">
                            <li class="ui-search-filter-container">
                                <form action="<c:url value='' />" method="get">
                                    <input type="hidden" name="genre-filter" value="${genreWrapper.genre.value}">
                                    <input type="hidden" name="is-genre-filter-active" value="true">
                                    <input type="hidden" name="book-state-filter" value="${publications.metadata.bookStateFilter.value}">
                                    <input type="hidden" name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}">
                                    <input type="hidden" name="search" value="<c:out value='${publications.metadata.search}'/>"/>

                                    <a href="#" class="uk-inline uk-search-button uk-button-link" title="GenreFilterRemove" onclick="this.closest('form').submit(); return false;">
                                        <c:set var="i18nKey" value="${genreWrapper.genre.value}" />
                                        <span class="ui-search-filter-name">
                                            <spring:message code="${i18nKey}"/>
                                        </span>
                                        <span> (${genreWrapper.resultByGenre})</span>
                                    </a>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
            </div>

            <div class="uk-width-3-4 col-content mb-1">
                <c:if test="${not empty publications.data}">
                    <div  style="display:flex; justify-content: space-between; align-items: center;">
                        <div><h2 class="mt-1">
                                <spring:message code="publications.list.available"/>
                        </div>
                        <c:set var="sortKey" value="${publications.metadata.sortType.value}"/>
                        <div>
                            <button type="button" class="uk-button uk-button-link">
                                <spring:message code="${sortKey}" />
                                <span uk-drop-parent-icon></span>
                            </button>
                            <div class="uk-navbar-dropdown uk-drop" uk-drop="mode: click">
                                <ul class="uk-nav uk-dropdown-nav">
                                    <li>
                                        <form action="<c:url value='' />" method="get">
                                            <input type="hidden" name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}">
                                            <input type="hidden" name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}">
                                            <input type="hidden" name="genre-filter" value="${publications.metadata.genreFilter.value}">
                                            <input type="hidden" name="book-state-filter" value="${publications.metadata.bookStateFilter.value}">
                                            <input type="hidden" name="search" value="${publications.metadata.search}"/>
                                            <input type="hidden" name="order" value="sort.rating.ascending" />
                                            <button type="submit" class="uk-button uk-button-link uk-width-1-1 uk-text-left">
                                                <spring:message code="sort.rating.ascending"/>
                                            </button>
                                        </form>
                                    </li>
                                    <li>
                                        <form action="<c:url value='' />" method="get">
                                            <input type="hidden" name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}">
                                            <input type="hidden" name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}">
                                            <input type="hidden" name="genre-filter" value="${publications.metadata.genreFilter.value}">
                                            <input type="hidden" name="book-state-filter" value="${publications.metadata.bookStateFilter.value}">
                                            <input type="hidden" name="search" value="${publications.metadata.search}"/>
                                            <input type="hidden" name="order" value="sort.rating.descending" />
                                            <button type="submit" class="uk-button uk-button-link uk-width-1-1 uk-text-left">
                                                <spring:message code="sort.rating.descending"/>
                                            </button>
                                        </form>
                                    </li>
                                    <li>
                                        <form action="<c:url value='' />" method="get">
                                            <input type="hidden" name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}">
                                            <input type="hidden" name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}">
                                            <input type="hidden" name="genre-filter" value="${publications.metadata.genreFilter.value}">
                                            <input type="hidden" name="book-state-filter" value="${publications.metadata.bookStateFilter.value}">
                                            <input type="hidden" name="search" value="${publications.metadata.search}"/>
                                            <input type="hidden" name="order" value="sort.publication.date.ascending" />
                                            <button type="submit" class="uk-button uk-button-link uk-width-1-1 uk-text-left">
                                                <spring:message code="sort.publication.date.ascending"/>
                                            </button>
                                        </form>
                                    </li>
                                    <li>
                                        <form action="<c:url value='' />" method="get">
                                            <input type="hidden" name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}">
                                            <input type="hidden" name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}">
                                            <input type="hidden" name="genre-filter" value="${publications.metadata.genreFilter.value}">
                                            <input type="hidden" name="book-state-filter" value="${publications.metadata.bookStateFilter.value}">
                                            <input type="hidden" name="search" value="${publications.metadata.search}"/>
                                            <input type="hidden" name="order" value="sort.publication.date.descending" />
                                            <button type="submit" class="uk-button uk-button-link uk-width-1-1 uk-text-left">
                                                <spring:message code="sort.publication.date.descending"/>
                                            </button>
                                        </form>
                                    </li>
                                    <li>
                                        <form action="<c:url value='' />" method="get">
                                            <input type="hidden" name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}">
                                            <input type="hidden" name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}">
                                            <input type="hidden" name="genre-filter" value="${publications.metadata.genreFilter.value}">
                                            <input type="hidden" name="book-state-filter" value="${publications.metadata.bookStateFilter.value}">
                                            <input type="hidden" name="search" value="${publications.metadata.search}"/>
                                            <input type="hidden" name="order" value="sort.book.name.ascending" />
                                            <button type="submit" class="uk-button uk-button-link uk-width-1-1 uk-text-left">
                                                <spring:message code="sort.book.name.ascending"/>
                                            </button>
                                        </form>
                                    </li>
                                    <li>
                                        <form action="<c:url value='' />" method="get">
                                            <input type="hidden" name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}">
                                            <input type="hidden" name="is-genre-filter-active" value="${publications.metadata.isGenreFilterActive}">
                                            <input type="hidden" name="genre-filter" value="${publications.metadata.genreFilter.value}">
                                            <input type="hidden" name="book-state-filter" value="${publications.metadata.bookStateFilter.value}">
                                            <input type="hidden" name="search" value="${publications.metadata.search}"/>
                                            <input type="hidden" name="order" value="sort.book.name.descending" />
                                            <button type="submit" class="uk-button uk-button-link uk-width-1-1 uk-text-left">
                                                <spring:message code="sort.book.name.descending"/>
                                            </button>
                                        </form>
                                    </li>
                                </ul>
                            </div>

                        </div>
                    </div>
                </c:if>
                <div class="uk-card uk-card-default uk-card-body uk-margin-bottom uk-border-rounded uk-border-rounded-medium" style="display: flex; align-items: center">
                    <h4 style="margin:0;">
                        <spring:message code="publications.totalresults">
                            <spring:argument value="${publications.metadata.totalResults}"/>
                        </spring:message>
                    </h4>
                    <c:if test="${not empty publications.metadata.search}">
                        <hr class="uk-divider-vertical" style="height: 30px; margin: 0 1rem;">
                        <h4 style="margin:0;">
                            <spring:message code="publications.search" />
                            '<c:out value='${publications.metadata.search}'/>'
                        </h4>
                        <c:if test="${not empty publications.metadata.search}">
                            <form action="<c:url value='' />" method="get">
                                <input type="hidden" name="is-book-state-filter-active" value="${publications.metadata.isBookStateFilterActive}">
                                <input type="hidden" name="is-genre-filter-active" value=${publications.metadata.isGenreFilterActive}>
                                <input type="hidden" name="genre-filter" value=${publications.metadata.genreFilter.value}>
                                <input type="hidden" name="book-state-filter" value=${publications.metadata.bookStateFilter.value}>
                                <input type="hidden" name="search" value=""/>

                                <button type="submit" class="uk-button uk-button-danger uk-button-small delete-button" style="margin-left: 1rem;">
                                    <span uk-icon="trash"></span>
                                </button>
                            </form>
                        </c:if>
                    </c:if>
                </div>

                <c:if test="${not empty publications.data}">
                <div class="uk-grid-match" uk-grid>
                    <c:forEach var="card" items="${publications.data}">
                        <div class="uk-width-1-1">
                            <a href="<c:url value='publications/${card.publicationId}' />"
                               class="uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded custom-link uk-flex uk-flex-middle"
                            style="padding: 1rem !important;">

<%--                                <div class="uk-position-top-right"--%>
<%--                                     style="padding: 2rem; display: flex; align-items: center;">--%>
<%--                                    <form action="<c:url value='/like/${card.publicationId}' />" method="post">--%>
<%--                                        <button type="submit" class="transparent"--%>
<%--                                                style="border: none; background: none;">--%>
<%--                                            <c:choose>--%>
<%--                                                <c:when test="${card.likedByUser}">--%>
<%--                                                    <i class="material-icons red-text">favorite</i>--%>
<%--                                                </c:when>--%>
<%--                                                <c:otherwise>--%>
<%--                                                    <i class="material-icons grey-text">favorite_border</i>--%>
<%--                                                </c:otherwise>--%>
<%--                                            </c:choose>--%>
<%--                                        </button>--%>
<%--                                    </form>--%>
<%--                                    <c:if test="${card.likes > 0}">--%>
<%--                                        <p class="small-gray-text custom-link" style="margin: 0 0 0 0.5rem;">(<c:out--%>
<%--                                                value="${card.likes}"/>)</p>--%>
<%--                                    </c:if>--%>
<%--                                </div>--%>

                                <!-- Contenedor de la imagen (Columna izquierda) -->
                                <div class="uk-width-1-4 uk-flex uk-flex-center">
                                    <figure class="uk-margin-remove">
                                        <c:choose>
                                            <c:when test="${!card.book.images[0].image.isImageNull}">
                                                <img class="book-image uk-border-rounded"
                                                     src="<c:url value='/images/${card.book.images[0].image.imageId}' />"
                                                     alt="bookImage"/>
                                            </c:when>
                                            <c:otherwise>
                                                <img class="book-image uk-border-rounded"
                                                     src="<c:url value='/images/book.jpg' />" alt="book"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </figure>
                                </div>

                                <!-- Contenedor del texto (Columna derecha) -->
                                <div class="uk-width-3-4 uk-margin-small-left" style="width:10rem;">
                                    <!-- Título del libro -->
                                    <h5 class="uk-card-title custom-link uk-margin-remove-bottom">
                                        <c:out value="${card.book.bookModel.title}"/>
                                    </h5>

                                    <!-- Autores del libro -->
                                    <c:forEach var="author" items="${card.book.bookModel.authors}">
                                        <p class="small-gray-text custom-link uk-margin-remove-top">
                                            <c:out value="${author.authorName}"/>
                                        </p>
                                    </c:forEach>
                                    <div>
                                        <span class="uk-margin-small-right" uk-icon="location">
                                        </span>

                                        <span>
                                            <c:forEach var="location" items="${card.locations}" varStatus="status">
                                                <c:out value="${location.locationString}"/>
                                                <c:if test="${!status.last}">
                                                    ,
                                                </c:if>
                                            </c:forEach>
                                        </span>
                                    </div>
                                </div>

                                <div class="row-container" style="width:15rem; padding-left:10rem;">
                                    <div class="star-rating uk-flex uk-flex-middle">
                                        <p class="small-gray-text custom-link" style="display: inline; margin-bottom: 0; margin-right:1rem;">
                                            <c:out value="${card.book.bookModel.averageRating}"/>
                                        </p>
                                        <c:forEach var="i" begin="1" end="5">
                                            <c:choose>
                                                <c:when test="${i <= card.book.bookModel.averageRating}">
                                                    <i class="material-icons yellow-text">star</i>
                                                </c:when>
                                                <c:when test="${i - 0.5 <= card.book.bookModel.averageRating && card.book.bookModel.averageRating < i}">
                                                    <i class="material-icons yellow-text">star_half</i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="material-icons grey-text">star_border</i>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>

                                        <p class="small-gray-text custom-link" style="display: inline; margin-left:1rem;">(<c:out
                                                value="${card.book.bookModel.ratingCount}"/>)
                                        </p>
                                    </div>
                                </div>
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
                                        <c:param name="genre-filter" value="${publications.metadata.genreFilter.value}" />
                                        <c:param name="book-state-filter" value="${publications.metadata.bookStateFilter.value}" />
                                        <c:param name="search" value="${publications.metadata.search}" />
                                        <c:param name="order" value="${publications.metadata.sortType.value}" />
                                    </c:url>
                                    <a href="${prevPageUrl}">
                                        <span uk-pagination-previous></span>
                                        <spring:message code="publications.pagination.previous"/>
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
                                        <c:param name="genre-filter" value="${publications.metadata.genreFilter.value}" />
                                        <c:param name="book-state-filter" value="${publications.metadata.bookStateFilter.value}" />
                                        <c:param name="search" value="${publications.metadata.search}" />
                                        <c:param name="order" value="${publications.metadata.sortType.value}" />
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
                                        <c:param name="genre-filter" value="${publications.metadata.genreFilter.value}" />
                                        <c:param name="book-state-filter" value="${publications.metadata.bookStateFilter.value}" />
                                        <c:param name="search" value="${publications.metadata.search}" />
                                        <c:param name="order" value="${publications.metadata.sortType.value}" />
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
                                        <c:param name="genre-filter" value="${publications.metadata.genreFilter.value}" />
                                        <c:param name="book-state-filter" value="${publications.metadata.bookStateFilter.value}" />
                                        <c:param name="search" value="${publications.metadata.search}" />
                                        <c:param name="order" value="${publications.metadata.sortType.value}" />
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
                                        <c:param name="genre-filter" value="${publications.metadata.genreFilter.value}" />
                                        <c:param name="book-state-filter" value="${publications.metadata.bookStateFilter.value}" />
                                        <c:param name="search" value="${publications.metadata.search}" />
                                        <c:param name="order" value="${publications.metadata.sortType.value}" />
                                    </c:url>
                                    <a href="${nextPageUrl}">
                                        <spring:message code="publications.pagination.next"/>
                                        <span uk-pagination-next></span>
                                    </a>
                                </li>
                            </c:if>
                        </ul>

                        <!-- Botón "Ir al inicio" alineado a la derecha -->
                        <a href="" uk-totop uk-scroll class="uk-position-right uk-margin-right">
                            <spring:message code="publications.pagination.totop"/>
                        </a>
                    </nav>
                </c:if>
                <c:if test="${empty publications.data and not empty publications.metadata.search}">
                    <div style="text-align: left;">
                        <h1><spring:message code="publications.filter.empty.header" /></h1>

                        <script src="https://unpkg.com/@dotlottie/player-component@2.7.12/dist/dotlottie-player.mjs" type="module"></script>
                        <div style="display: flex; justify-content: center;">
                            <dotlottie-player src="https://lottie.host/122aec68-0bc1-46ed-a1bd-c82ca1f4bac6/riZdUUo3Qs.json" background="transparent" speed="1" style="width: 300px; height: 300px;" loop autoplay></dotlottie-player>
                        </div>

                        <ul>
                            <li><h5><spring:message code="recommendations.publications.verifySpelling" /></h5></li>
                            <li><h5><spring:message code="recommendations.publications.tryFullOrPartialTitle" /></h5></li>
                            <li><h5><spring:message code="recommendations.publications.checkYourFilters" /></h5></li>
                        </ul>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>
</body>
</html>
