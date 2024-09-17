<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html lang="es" class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>
<head>
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
	<link href="${pageContext.request.contextPath}/css/publications.css?v=1.0" rel="stylesheet"/>

	<link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
	<link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>

	<title><spring:message code="publications.list.brand.logo"/></title>

</head>
<body class="main">
<navbar></navbar>

<div class="uk-background-muted">
	<div class="uk-container">
		<div class="uk-grid ml-1 uk-margin-top" uk-grid>
			<div class="uk-width-1-4@s filter-section uk-border-rounded uk-box-shadow-small mt-1 mb-1">
				<h2>${param.search}</h2>

				<!-- Esto tiene que aparecer solo si hay un filtro de BookState -->
				<c:if test="${bookStateFilter != ''}">
					<form action="<c:url value='/' />" method="get">
						<input type="hidden" name="bookStateFilter" value="">
						<input type="hidden" name="genreFilter" value=${genreFilter}>
						<input type="hidden" name="search" value="${param.search}">

						<button type="submit" class="ui-search-button" title="BookStateRemove">
							<span class="ui-search-filter-name">Borrar filtro estado del libro</span>
						</button>
					</form>
				</c:if>

				<!-- Esto tiene que aparecer solo si hay un filtro de Genero -->
				<c:if test="${genreFilter != ''}">
					<form action="<c:url value='/' />" method="get">
						<input type="hidden" name="bookStateFilter" value=${bookStateFilter}>
						<input type="hidden" name="genreFilter" value="">
						<input type="hidden" name="search" value="${param.search}">

						<button type="submit" class="ui-search-button" title="GenreFilterRemove">
							<span class="ui-search-filter-name">Borrar filtro genero</span>
						</button>
					</form>
				</c:if>

				<c:if test="${bookStateFilter == ''}">
					<h3><spring:message code="filter.condition"/></h3>
					<ul class="uk-list">
						<c:forEach var="bookStateWrapper" items="${bookStates}">
							<li class="ui-search-filter-container">
								<form action="<c:url value='/' />" method="get">
									<input type="hidden" name="bookStateFilter" value="${bookStateWrapper.bookState}">
									<input type="hidden" name="genreFilter" value="${genreFilter}">
									<input type="hidden" name="search" value="${param.search}">

									<button type="submit" class="ui-search-button" title="${bookStateWrapper.displayName}">
										<span class="ui-search-filter-name">${bookStateWrapper.displayName}</span>
									</button>
								</form>
							</li>
						</c:forEach>
					</ul>
				</c:if>

				<c:if test="${genreFilter == ''}">
					<h3><spring:message code="filter.genre"/></h3>
					<ul class="uk-list">
						<c:forEach var="genreWrapper" items="${genres}">
	<%--							<input class="uk-checkbox" type="checkbox" checked="checked" name="genre" value="${genreWrapper.genre}" />--%>
	<%--							<li>${genreWrapper.displayName}</li>--%>

							<li class="ui-search-filter-container">
								<form action="<c:url value='/' />" method="get">
									<input type="hidden" name="genreFilter" value="${genreWrapper.genre}">
									<input type="hidden" name="bookStateFilter" value="${bookStateFilter}">
									<input type="hidden" name="search" value="${param.search}">

									<button type="submit" class="ui-search-button" title="${genreWrapper.displayName}">
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
					<h5 class="uk-text-large"><spring:message code="publications.list.available"/></h5>
					<h6 class="uk-text-muted"><spring:message code="publications.list.select"/></h6>
				</div>

				<div class="uk-grid-match uk-child-width-1-2@s uk-child-width-1-3@m mb-1" uk-grid>
					<c:forEach var="card" items="${publications}">
						<div>
							<a href="<c:url value='publications/${card.publication.publicationId}'>
								<c:param name='publication_id' value='${card.publication.publicationId}'/>
								</c:url>" class="uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded custom-link">
								<figure class="uk-margin-bottom">
									<c:choose>
										<c:when test="${card.bookImages != null}">
											<img class="book-image" src="${pageContext.request.contextPath}/images/${card.bookImages[0].imageId}" alt="bookImage"/>
										</c:when>
										<c:otherwise>
											<img class="book-image" src="${pageContext.request.contextPath}/images/book.jpg" alt="book"/>
										</c:otherwise>
									</c:choose>
								</figure>

								<h5 class="uk-card-title custom-link">${card.bookModel.title}</h5>
								<p class="small-gray-text custom-link">${card.authorsString}</p>
							</a>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
