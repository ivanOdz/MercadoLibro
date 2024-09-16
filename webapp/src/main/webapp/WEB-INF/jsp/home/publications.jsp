<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html lang="es" class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>
<head>
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>

	<link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
	<link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>
	<link href="${pageContext.request.contextPath}/css/publications.css" rel="stylesheet"/>

	<title><spring:message code="publications.list.brand.logo"/></title>

</head>
<body class="main">
<navbar/>

<div class="uk-background-muted">
	<div class="uk-container">
		<div class="uk-grid ml-1 uk-margin-top" uk-grid>
			<div class="uk-width-1-4@s filter-section uk-border-rounded uk-box-shadow-small mt-1 mb-1">
				<ul uk-accordion="multiple: true">
					<li class="uk-open">
						<a class="uk-accordion-title">
							<spring:message code="filter.genre"/>
						</a>
						<div class="uk-accordion-content">
							<c:forEach var="genreWrapper" items="${genres}">
								<div class="uk-margin">
								<input class="uk-checkbox" type="checkbox" checked="checked" name="genre" value="${genreWrapper.genre}" />
								<label>${genreWrapper.displayName}</label>
								</div>
							</c:forEach>
						</div>
					</li>
					<li>
						<a class="uk-accordion-title"><spring:message code="filter.condition"/></a>
						<div class="uk-accordion-content">
							<c:forEach var="bookStateWrapper" items="${bookStates}">
							<div class="uk-margin">
								<input class="uk-checkbox" type="checkbox" checked="checked" name="bookState" value="${bookStateWrapper.bookState}" />
								<label>${bookStateWrapper.displayName}</label>
							</div>
							</c:forEach>
						</div>
					</li>
					<li>
						<a class="uk-accordion-title"><spring:message code="filter.location"/></a>
						<div class="uk-accordion-content">
							<div class="uk-margin">
								<label>
									<input class="uk-checkbox" type="checkbox" checked="checked" />
									Ivan�s House
								</label>
							</div>
							<div class="uk-margin">
								<label>
									<input class="uk-checkbox" type="checkbox" />
									Option 2
								</label>
							</div>
							<div class="uk-margin">
								<label>
									<input class="uk-checkbox" type="checkbox" />
									Option 3
								</label>
							</div>
						</div>
					</li>
				</ul>
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
