<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html lang="es">
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
	<link href="${pageContext.request.contextPath}/css/publications.css?v=1.0" rel="stylesheet"/>

	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
	<link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

	<title><spring:message code="publications.list.title"/></title>

</head>
<body>
<nav class="uk-navbar-container uk-background-primary" uk-navbar>
	<div class="uk-navbar-left ml-9">
		<a class="uk-navbar-item uk-logo navbar-logo" href="${pageContext.request.contextPath}/">
			<spring:message code="publications.list.brand.logo"/>
		</a>
	</div>
	<div class="uk-navbar-center uk-flex uk-flex-middle">
		<div>
			<form class="uk-search uk-search-default custom-search-form" method="get" action="${pageContext.request.contextPath}">
				<input class="uk-search-input" type="search"
					   placeholder="Search"
					   aria-label="Search"
					   name="search"
					   id="search"
					   value="${param.search != null ? param.search : ''}">
				<button class="uk-search-icon-flip" uk-search-icon></button>
			</form>

		</div>
	</div>
<%--    <h4><spring:message code="home.profile.welcome"/><c:out value="${loggedUser.username}"/>!</h4>--%>
</nav>

<div class="fixed-action-btn">
	<a href="${pageContext.request.contextPath}/createpublication?publication_id=0&is_for_exchange=false" class="uk-button uk-button-large uk-button-primary">
		<span uk-icon="icon: plus"></span>
	</a>
</div>


<div class="uk-background-muted">
	<div class="uk-container uk-margin-top">
		<div class="uk-grid" uk-grid>
			<div class="uk-width-1-4@s filter-section uk-border-rounded uk-box-shadow-small mt-1 mb-1">
				<ul uk-accordion="multiple: true">
					<li class="uk-open">
						<a class="uk-accordion-title" href><spring:message code="filter.genre"/></a>
						<div class="uk-accordion-content">
							<div class="uk-margin">
								<label>
									<input class="uk-checkbox" type="checkbox" checked="checked" />
									Option 1
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
					<li>
						<a class="uk-accordion-title" href><spring:message code="filter.condition"/></a>
						<div class="uk-accordion-content">
							<div class="uk-margin">
								<label>
									<input class="uk-checkbox" type="checkbox" checked="checked" />
									Option 1
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
					<li>
						<a class="uk-accordion-title" href><spring:message code="filter.location"/></a>
						<div class="uk-accordion-content">
							<div class="uk-margin">
								<label>
									<input class="uk-checkbox" type="checkbox" checked="checked" />
									Option 1
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
							<a href="<c:url value='submitmail'>
								<c:param name='publication_id' value='${card.publication.publicationId}'/>
								</c:url>" class="uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded custom-link">
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
								<h5 class="uk-card-title custom-link">${card.book.title}</h5>
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
