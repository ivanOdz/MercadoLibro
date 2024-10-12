<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>

<html class="custom-style">
<head>
    <link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/book_home.css?v=1.0" rel="stylesheet"/>


    <title><spring:message code="library.title"/></title>
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
                       value="<c:out value='${books.metadata.search}'/>">
                <button class="uk-search-icon-flip" uk-search-icon></button>
            </form>
        </div>

        <div class="uk-grid ml-1 uk-margin-top" uk-grid style="margin-bottom: 64px;">
            <div class="uk-width-1-4@s filter-section uk-border-rounded uk-box-shadow-small mt-1 mb-1">
                <h2>
                    <c:out value="${modelBooks.metadata.search}"/>
                </h2>

                <!-- Esto tiene que aparecer solo si hay algo buscado -->
                <c:if test="${not empty param.search}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-genre-filter-active" value=${modelBooks.metadata.isGenreFilterActive}>
                        <input type="hidden" name="genre-filter" value=${modelBooks.metadata.genreFilter}>
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

                <c:if test="${modelBooks.metadata.isGenreFilterActive}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-genre-filter-active" value="false">
                        <input type="hidden" name="search" value="<c:out value='${modelBooks.metadata.search}'/>">

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

                <c:if test="${!modelBooks.metadata.isGenreFilterActive}">
                    <h3><spring:message code="filter.genre"/></h3>
                    <ul class="uk-list">
                        <c:forEach var="genreWrapper" items="${modelBooks.metadata.genreWrapperList}">
                            <li class="ui-search-filter-container">
                                <form action="<c:url value='' />" method="get">
                                    <input type="hidden" name="genre-filter" value="${genreWrapper.genre}">
                                    <input type="hidden" name="is-genre-filter-active" value="true">
                                    <input type="hidden" name="search" value="<c:out value='${modelBooks.metadata.search}'/>"/>

                                    <a href="#" class="uk-inline uk-search-button uk-button-link" title="GenreFilterRemove" onclick="this.closest('form').submit(); return false;">
                                        <span class="ui-search-filter-name">
                                                ${genreWrapper.displayName} (${genreWrapper.resultByGenre})
                                        </span>
                                    </a>

<%--                                    <button type="submit"--%>
<%--                                            class="ui-search-button uk-button uk-button-default uk-button-small"--%>
<%--                                            title="${genreWrapper.displayName}">--%>
<%--                                        <span class="ui-search-filter-name">${genreWrapper.displayName}</span>--%>
<%--                                    </button>--%>
                                </form>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
            </div>

            <div class="uk-width-3-4@s col-content">
                <div class="uk-card uk-card-default uk-card-body uk-margin-bottom mt-1 uk-border-rounded uk-border-rounded-medium">
                    <h5 class="uk-text-large"><spring:message code="book.model.view.title"/></h5>
                    <h6 class="uk-text-muted"><spring:message code="book.model.list.select"/></h6>
                </div>

                <div class="uk-grid-match uk-child-width-1-2@s uk-child-width-1-3@m mb-1" uk-grid>
                    <c:forEach var="card" items="${modelBooks.data}">
                        <div>
                            <div class="uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded custom-link">
                                <figure class="uk-margin-bottom">
                                    <c:choose>
                                        <c:when test="${card.imageId != null}">
                                            <img class="book-image"
                                                 src="${pageContext.request.contextPath}/images/${card.imageId}"
                                                 alt="bookImage"/>
                                        </c:when>
                                        <c:otherwise>
                                            <img class="book-image"
                                                 src="${pageContext.request.contextPath}/images/book.jpg" alt="book"/>
                                        </c:otherwise>
                                    </c:choose>
                                </figure>
                                <h5 class="uk-card-title custom-link">
                                    <c:out value="${card.title}"/>
                                </h5>
                                <p class="small-gray-text custom-link">
                                    <c:out value="${card.authors}"/>
                                </p>

                                <a class="uk-button uk-button-default uk-button-primary uk-width-1-1"
                                   href="#modal-sections-${card.bookModelId}" uk-toggle>
                                    <spring:message code="book.add.button"/>
                                </a>

                                <div id="modal-sections-${card.bookModelId}" uk-modal>
                                    <div class="uk-modal-dialog">
                                        <div class="uk-modal-header">
                                            <h5 class="uk-card-title custom-link">
                                                <c:out value="${card.title}"/>
                                            </h5>
                                            <p class="small-gray-text custom-link">
                                                <c:out value="${card.authors}"/>
                                            </p>
                                            <p>
                                                <c:out value="${card.genre}"/>
                                            </p>
                                            <p>
                                                <c:out value="${card.editorial}"/>
                                            </p>
                                            <p>
                                                <c:out value="${card.description}"/>
                                            </p>

                                            <div class="uk-margin" style="justify-content: center">
                                                <div class="uk-width-1-1">
                                                    <div class="uk-margin-top uk-button-group"
                                                         style="margin-left: 50px;">
                                                        <a href="${pageContext.request.contextPath}/book/new_book_model?book_model_id=${card.bookModelId}"
                                                           type="submit" class="uk-button uk-button-primary">
                                                            <spring:message code="book.model.view.button"/></a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </c:forEach>
                </div>
                <hr class="uk-divider-icon">
                <nav aria-label="Pagination" class="uk-position-relative uk-margin">
                    <ul class="uk-pagination uk-flex-center uk-position-center">

                        <!-- Botón Previous -->
                        <c:if test="${modelBooks.metadata.currentPage > 0}">
                            <li>
                                <c:url var="prevPageUrl" value="">
                                    <c:param name="page" value="${modelBooks.metadata.currentPage - 1}" />
                                    <c:param name="is-genre-filter-active" value="${modelBooks.metadata.isGenreFilterActive}" />
                                    <c:param name="genre-filter" value="${modelBooks.metadata.genreFilter}" />
                                    <c:param name="search" value="${modelBooks.metadata.search}" />
                                    <c:param name="sort-type" value="${modelBooks.metadata.sortType}" />
                                </c:url>
                                <a href="${prevPageUrl}">
                                    <span uk-pagination-previous></span>
                                    <spring:message code="publications.pagination.previous"/>
                                </a>
                            </li>
                        </c:if>

                        <!-- Botón de la primera página -->
                        <c:if test="${modelBooks.metadata.currentPage > 1}">
                            <li>
                                <c:url var="firstPageUrl" value="">
                                    <c:param name="page" value="0" />
                                    <c:param name="is-genre-filter-active" value="${modelBooks.metadata.isGenreFilterActive}" />
                                    <c:param name="genre-filter" value="${modelBooks.metadata.genreFilter}" />
                                    <c:param name="search" value="${modelBooks.metadata.search}" />
                                    <c:param name="sort-type" value="${modelBooks.metadata.sortType}" />
                                </c:url>
                                <a href="${firstPageUrl}">1</a>
                            </li>
                        </c:if>

                        <c:if test="${modelBooks.metadata.currentPage - 2 > 0}">
                            <li><span>...</span></li>
                        </c:if>

                        <!-- Páginas centrales -->
                        <c:forEach var="i" begin="${modelBooks.metadata.currentPage > 0 ? modelBooks.metadata.currentPage - 1 : 0}"
                                   end="${modelBooks.metadata.currentPage + 1 <= modelBooks.metadata.maxPage ? modelBooks.metadata.currentPage + 1 : modelBooks.metadata.maxPage}">
                            <li class="${i == modelBooks.metadata.currentPage ? 'uk-active' : ''}">
                                <c:url var="centralPageUrl" value="">
                                    <c:param name="page" value="${i}" />
                                    <c:param name="is-genre-filter-active" value="${modelBooks.metadata.isGenreFilterActive}" />
                                    <c:param name="genre-filter" value="${modelBooks.metadata.genreFilter}" />
                                    <c:param name="search" value="${modelBooks.metadata.search}" />
                                    <c:param name="sort-type" value="${modelBooks.metadata.sortType}" />
                                </c:url>
                                <a href="${centralPageUrl}">${i + 1}</a> <!-- Mostrar i + 1 para la numeración -->
                            </li>
                        </c:forEach>

                        <c:if test="${modelBooks.metadata.currentPage + 2 < modelBooks.metadata.maxPage}">
                            <li><span>...</span></li>
                        </c:if>

                        <!-- Botón de la última página -->
                        <c:if test="${modelBooks.metadata.currentPage + 1 < modelBooks.metadata.maxPage}">
                            <li>
                                <c:url var="lastPageUrl" value="">
                                    <c:param name="page" value="${modelBooks.metadata.maxPage}" />
                                    <c:param name="is-genre-filter-active" value="${modelBooks.metadata.isGenreFilterActive}" />
                                    <c:param name="genre-filter" value="${modelBooks.metadata.genreFilter}" />
                                    <c:param name="search" value="${modelBooks.metadata.search}" />
                                    <c:param name="sort-type" value="${modelBooks.metadata.sortType}" />
                                </c:url>
                                <a href="${lastPageUrl}">${modelBooks.metadata.maxPage + 1}</a> <!-- Mostrar maxPage + 1 -->
                            </li>
                        </c:if>

                        <!-- Botón Next -->
                        <c:if test="${modelBooks.metadata.currentPage < modelBooks.metadata.maxPage}">
                            <li>
                                <c:url var="nextPageUrl" value="">
                                    <c:param name="page" value="${modelBooks.metadata.currentPage + 1}" />
                                    <c:param name="is-genre-filter-active" value="${modelBooks.metadata.isGenreFilterActive}" />
                                    <c:param name="genre-filter" value="${modelBooks.metadata.genreFilter}" />
                                    <c:param name="search" value="${modelBooks.metadata.search}" />
                                    <c:param name="sort-type" value="${modelBooks.metadata.sortType}" />
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
            </div>
        </div>
    </div>
</div>
</body>
</html>
