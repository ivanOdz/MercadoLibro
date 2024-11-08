<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>
<%-- <%@ page import="ar.edu.itba.paw.models.utils.PublicationState" %> --%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="es" class="custom-style">
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <link href="<c:url value='/css/navbar.css' />" rel="stylesheet"/>
    <link href="<c:url value='/css/book_home.css' />" rel="stylesheet"/>

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
                       value="<c:out value='${books.metadata.search}'/>">
                <button class="uk-search-icon-flip" uk-search-icon></button>
            </form>
        </div>

        <div class="uk-grid ml-1" uk-grid style="margin-bottom: 64px;">


            <div class="uk-width-1-4@s filter-section uk-border-rounded uk-box-shadow-small mt-1 mb-1">
                <h2><c:out value="${books.metadata.search}"/></h2>


                <!-- Esto tiene que aparecer solo si hay algo buscado -->
                <c:if test="${not empty books.metadata.search}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-book-state-filter-active"
                               value="${books.metadata.isBookStateFilterActive}">
                        <input type="hidden" name="is-genre-filter-active" value=${books.metadata.isGenreFilterActive}>
                        <input type="hidden" name="genre-filter" value=${books.metadata.genreFilter.value}>
                        <input type="hidden" name="book-state-filter" value=${books.metadata.bookStateFilter.value}>
                        <input type="hidden" name="search" value=""/>

                        <button type="submit" class="uk-button uk-button-danger uk-button-small delete-button"
                                title="BookStateRemove">
							<span class="ui-search-filter-name">
								<spring:message code="delete.search"/>
							</span>
                            <span uk-icon="trash"></span>
                        </button>
                    </form>
                </c:if>

                <!-- Esto tiene que aparecer solo si hay un filtro de BookState -->
                <c:if test="${books.metadata.isBookStateFilterActive}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-book-state-filter-active" value="false">
                        <input type="hidden" name="is-genre-filter-active" value=${books.metadata.isGenreFilterActive}>
                        <input type="hidden" name="genre-filter" value=${books.metadata.genreFilter.value}>
                        <input type="hidden" name="search" value="<c:out value='${books.metadata.search}'/>"/>

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
                <c:if test="${books.metadata.isGenreFilterActive}">
                    <form action="<c:url value='' />" method="get">
                        <input type="hidden" name="is-genre-filter-active" value="false">
                        <input type="hidden" name="is-book-state-filter-active"
                               value=${books.metadata.isBookStateFilterActive}>
                        <input type="hidden" name="book-state-filter" value=${books.metadata.bookStateFilter.value}>
                        <input type="hidden" name="search" value="<c:out value='${books.metadata.search}'/>">

                        <button type="submit" class="uk-button uk-button-danger uk-button-small delete-button"
                                title="GenreFilterRemove">
							<span class="ui-search-filter-name">
								<spring:message code="delete.genre.filter"/>
							</span>
                            <span uk-icon="trash"></span>
                        </button>
                    </form>
                </c:if>

                <c:if test="${!books.metadata.isBookStateFilterActive}">
                    <h3><spring:message code="filter.condition"/></h3>
                    <c:if test="${empty bookStateWrapperList}">
                        <p><spring:message code="filter.empty.condition"/></p>
                    </c:if>
                    <c:if test="${not empty bookStateWrapperList}">
                        <ul class="uk-list">
                            <c:forEach var="bookStateWrapper" items="${bookStateWrapperList}">
                                <li class="ui-search-filter-container">
                                    <form action="<c:url value='' />" method="get">
                                        <input type="hidden" name="search"
                                               value="<c:out value='${books.metadata.search}'/>">
                                        <input type="hidden" name="is-book-state-filter-active" value='true'>
                                        <input type="hidden" name="book-state-filter"
                                               value="${bookStateWrapper.bookState.value}">
                                        <input type="hidden" name="is-genre-filter-active"
                                               value="${books.metadata.isGenreFilterActive}">
                                        <input type="hidden" name="genre-filter" value="${books.metadata.genreFilter.value}">
                                        <a href="#" class="uk-inline uk-search-button uk-button-link"
                                           title="BookStateFilterRemove"
                                           onclick="this.closest('form').submit(); return false;">

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
                </c:if>

                <c:if test="${!books.metadata.isGenreFilterActive}">
                    <h3><spring:message code="filter.genre"/></h3>
                    <c:if test="${empty genreWrapperList}">
                        <p><spring:message code="filter.empty.genres"/></p>
                    </c:if>
                    <c:if test="${not empty genreWrapperList}">
                        <ul class="uk-list">
                            <c:forEach var="genreWrapper" items="${genreWrapperList}">
                                <li class="ui-search-filter-container">
                                    <form action="<c:url value='' />" method="get">
                                        <input type="hidden" name="genre-filter" value="${genreWrapper.genre.value}">
                                        <input type="hidden" name="is-genre-filter-active" value="true">
                                        <input type="hidden" name="book-state-filter"
                                               value="${books.metadata.bookStateFilter.value}">
                                        <input type="hidden" name="is-book-state-filter-active"
                                               value="${books.metadata.isBookStateFilterActive}">
                                        <input type="hidden" name="search"
                                               value="<c:out value='${books.metadata.search}'/>"/>

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
                </c:if>
            </div>


            <div class="uk-width-3-4@s col-content">

                <c:if test="${not empty books.data}">
                    <div class="uk-card uk-card-default uk-card-body uk-margin-bottom mt-1 uk-border-rounded uk-border-rounded-medium">
                        <h5 class="uk-text-large"><spring:message code="book.view.title"/></h5>
                        <h6 class="uk-text-muted"><spring:message code="book.list.select"/></h6>
                    </div>
                </c:if>

                <div class="uk-grid-match uk-child-width-1-2@s uk-child-width-1-3@m mb-1" uk-grid
                     uk-height-match="target: > div > .uk-card">
                    <c:if test="${not empty books.data}">
                        <c:forEach var="card" items="${books.data}">
                            <div>
                                <div class="uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded custom-link">
                                    <figure class="uk-margin-bottom">
                                            <c:choose>
                                            <c:when test="${card.images[0] != null && !card.images[0].image.isImageNull}">
                                                <img class="book-image"
                                                     src="<c:url value='/images/${card.images[0].image.imageId}' />"
                                                     alt="bookImage"/>
                                            </c:when>
                                                <c:when test="${!card.bookModel.image.isImageNull}">
                                                    <img class="book-image"
                                                         src="<c:url value='/images/${card.bookModel.image.imageId}' />"
                                                         alt="book"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="book-image"
                                                         src="<c:url value='/images/book.jpg' />"
                                                         alt="book"/>
                                                </c:otherwise>
                                            </c:choose>
                                    </figure>

                                    <div class="uk-flex uk-flex-column uk-flex-column uk-margin-bottom">
                                        <div class="">
                                            <h5 class="uk-card-title custom-link">${card.bookModel.title}</h5>
                                            <p class="small-gray-text custom-link">${card.bookModel.authors[0].authorName}</p>
                                        </div>
                                        <c:choose>
                                            <c:when test="${card.available}">
                                                <a class="uk-button uk-button-default uk-button-primary uk-width-1-1"
                                                   href="#modal-sections-${card.bookId}" uk-toggle>
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
                                            <form action="<c:url value='/createpublication' />"
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
<!-- 	                                                            <span class="uk-form-icon uk-form-icon-flip" uk-icon="icon: location"></span> -->
	                                                            
	                                                            <c:choose>
	                                                            	
	                                                            	<c:when test="${not empty user.userLocations}">
																		<select class="uk-select no-arrow-select" name="locationId" aria-label="Not clickable icon" style="width: 90%">
																		    <c:forEach var="userLocation" items="${user.userLocations}">
																		        <option value="${userLocation.locationId}"
																		        	<c:if test="${userLocation.locationId == user.favoriteLocation.locationId}"/>>
																		            ${userLocation.locationString}
																		        </option>
																		    </c:forEach>
																		</select>
																	</c:when>
																	<c:otherwise>
														            <p>
														                <spring:message code="user.no.locations"/>
														                <a href="<c:url value='/profile'/>"><spring:message code="user.add.location"/></a>
														            </p>
																	</c:otherwise>
																</c:choose>
																
<!--                                                                 <span class="uk-form-icon uk-form-icon-flip" -->
<!--                                                                       uk-icon="icon: location"></span> -->
<!--                                                                 <input class="uk-input" type="text" name="location" -->
<%--                                                                        aria-label="Not clickable icon" value="${user.favoriteLocation}"/> --%>
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
                    </c:if>
                </div>

                <c:if test="${empty books.data and not empty books.metadata.search}">
                    <div style="text-align: left;">
                        <h1><spring:message code="filter.empty.header" /></h1>

                        <script src="https://unpkg.com/@dotlottie/player-component@2.7.12/dist/dotlottie-player.mjs" type="module"></script>
                        <div style="display: flex; justify-content: center;">
                            <dotlottie-player src="https://lottie.host/122aec68-0bc1-46ed-a1bd-c82ca1f4bac6/riZdUUo3Qs.json" background="transparent" speed="1" style="width: 300px; height: 300px;" loop autoplay></dotlottie-player>
                        </div>

                        <ul>
                            <li><h5><spring:message code="recommendations.verifySpelling" /></h5></li>
                            <li><h5><spring:message code="recommendations.tryFullOrPartialTitle" /></h5></li>
                            <li><h5><spring:message code="recommendations.exploreByGenreOrCategory" /></h5></li>
                        </ul>
                    </div>
                </c:if>


                <c:if test="${empty books.data and empty books.metadata.search}">
                    <div class="book-empty">
                        <div style="margin:2%;width: max-content;">
                            <spring:message code="books.empty"/>
                        </div>
                        <a style="margin:2%" class="uk-button uk-button-primary"
                           href="<c:url value='/book/new_book' />">
                            <spring:message code="books.empty.upload"/>
                        </a>
                        <a style="margin:2%" class="uk-button uk-button-primary"
                           href="<c:url value='/book/book_models' />">
                            <spring:message code="books.empty.preloaded"/>
                        </a>

                    </div>
                </c:if>

                <c:if test="${not empty books.data}">
                    <hr class="uk-divider-icon">
                    <nav aria-label="Pagination" class="uk-position-relative uk-margin">
                        <ul class="uk-pagination uk-flex-center uk-position-center">

                            <!-- Botón Previous -->
                            <c:if test="${books.metadata.currentPage > 0}">
                                <li>
                                    <c:url var="prevPageUrl" value="">
                                        <c:param name="page" value="${books.metadata.currentPage - 1}"/>
                                        <c:param name="is-book-state-filter-active"
                                                 value="${books.metadata.isBookStateFilterActive}"/>
                                        <c:param name="is-genre-filter-active"
                                                 value="${books.metadata.isGenreFilterActive}"/>
                                        <c:param name="genre-filter" value="${books.metadata.genreFilter.value}"/>
                                        <c:param name="book-state-filter" value="${books.metadata.bookStateFilter.value}"/>
                                        <c:param name="search" value="${books.metadata.search}"/>
                                        <c:param name="sort-type" value="${books.metadata.sortType}"/>
                                    </c:url>
                                    <a href="${prevPageUrl}">
                                        <span uk-pagination-previous></span>
                                        <spring:message code="publications.pagination.previous"/>
                                    </a>
                                </li>
                            </c:if>

                            <!-- Botón de la primera página -->
                            <c:if test="${books.metadata.currentPage > 1}">
                                <li>
                                    <c:url var="firstPageUrl" value="">
                                        <c:param name="page" value="0"/>
                                        <c:param name="is-book-state-filter-active"
                                                 value="${books.metadata.isBookStateFilterActive}"/>
                                        <c:param name="is-genre-filter-active"
                                                 value="${books.metadata.isGenreFilterActive}"/>
                                        <c:param name="genre-filter" value="${books.metadata.genreFilter.value}"/>
                                        <c:param name="book-state-filter" value="${books.metadata.bookStateFilter.value}"/>
                                        <c:param name="search" value="${books.metadata.search}"/>
                                        <c:param name="sort-type" value="${books.metadata.sortType}"/>
                                    </c:url>
                                    <a href="${firstPageUrl}">1</a>
                                </li>
                            </c:if>

                            <c:if test="${books.metadata.currentPage - 2 > 0}">
                                <li><span>...</span></li>
                            </c:if>

                            <!-- Páginas centrales -->
                            <c:forEach var="i"
                                       begin="${books.metadata.currentPage > 0 ? books.metadata.currentPage - 1 : 0}"
                                       end="${books.metadata.currentPage + 1 <= books.metadata.maxPage ? books.metadata.currentPage + 1 : books.metadata.maxPage}">
                                <li class="${i == books.metadata.currentPage ? 'uk-active' : ''}">
                                    <c:url var="centralPageUrl" value="">
                                        <c:param name="page" value="${i}"/>
                                        <c:param name="is-book-state-filter-active"
                                                 value="${books.metadata.isBookStateFilterActive}"/>
                                        <c:param name="is-genre-filter-active"
                                                 value="${books.metadata.isGenreFilterActive}"/>
                                        <c:param name="genre-filter" value="${books.metadata.genreFilter.value}"/>
                                        <c:param name="book-state-filter" value="${books.metadata.bookStateFilter.value}"/>
                                        <c:param name="search" value="${books.metadata.search}"/>
                                        <c:param name="sort-type" value="${books.metadata.sortType}"/>
                                    </c:url>
                                    <a href="${centralPageUrl}">${i + 1}</a> <!-- Mostrar i + 1 para la numeración -->
                                </li>
                            </c:forEach>

                            <c:if test="${books.metadata.currentPage + 2 < books.metadata.maxPage}">
                                <li><span>...</span></li>
                            </c:if>

                            <!-- Botón de la última página -->
                            <c:if test="${books.metadata.currentPage + 1 < books.metadata.maxPage}">
                                <li>
                                    <c:url var="lastPageUrl" value="">
                                        <c:param name="page" value="${books.metadata.maxPage}"/>
                                        <c:param name="is-book-state-filter-active"
                                                 value="${books.metadata.isBookStateFilterActive}"/>
                                        <c:param name="is-genre-filter-active"
                                                 value="${books.metadata.isGenreFilterActive}"/>
                                        <c:param name="genre-filter" value="${books.metadata.genreFilter.value}"/>
                                        <c:param name="book-state-filter" value="${books.metadata.bookStateFilter.value}"/>
                                        <c:param name="search" value="${books.metadata.search}"/>
                                        <c:param name="sort-type" value="${books.metadata.sortType}"/>
                                    </c:url>
                                    <a href="${lastPageUrl}">${books.metadata.maxPage + 1}</a> <!-- Mostrar maxPage + 1 -->
                                </li>
                            </c:if>

                            <!-- Botón Next -->
                            <c:if test="${books.metadata.currentPage < books.metadata.maxPage}">
                                <li>
                                    <c:url var="nextPageUrl" value="">
                                        <c:param name="page" value="${books.metadata.currentPage + 1}"/>
                                        <c:param name="is-book-state-filter-active"
                                                 value="${books.metadata.isBookStateFilterActive}"/>
                                        <c:param name="is-genre-filter-active"
                                                 value="${books.metadata.isGenreFilterActive}"/>
                                        <c:param name="genre-filter" value="${books.metadata.genreFilter.value}"/>
                                        <c:param name="book-state-filter" value="${books.metadata.bookStateFilter.value}"/>
                                        <c:param name="search" value="${books.metadata.search}"/>
                                        <c:param name="sort-type" value="${books.metadata.sortType}"/>
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
            </div>
        </div>
    </div>
</div>

</body>
</html>

