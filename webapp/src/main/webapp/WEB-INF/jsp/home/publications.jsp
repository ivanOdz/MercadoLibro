<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html lang="es">

<head>
	<link href="${pageContext.request.contextPath}/css/publications.css" rel="stylesheet"/>
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
	<link href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css" rel="stylesheet"/>
	<title><spring:message code="publications.list.title"/></title>
	
	<style>
		.fixed-action-btn {
			position: fixed;
			right: 20px;
			bottom: 20px;
		}
	</style>
	
</head>

<body>
<nav class="background-nav">
	<div style="margin-top: 3vh;" class="row">
		<div class="col s4 align-content">
			<a href="${pageContext.request.contextPath}/" class="brand-logo brown-text darken-4-text s4"><spring:message code="publications.list.brand.logo"/></a>
		</div>
		<div class="s4 col">
			<div class="row">
				<form class="col s12" action="${pageContext.request.contextPath}/" method="get">
					<div class="row inline-form">
						<input type="text" id="search" name="search"/>
						<i style="color: black" class="material-icons tiny suffix search-button">search</i>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div style="height: 100%" class="s4 col">

	</div>
</nav>

<div class="fixed-action-btn">
	<a href="${pageContext.request.contextPath}/createPublication?publicationId=0&isForExchange=false" class="btn-floating btn-large waves-effect waves-light pink">
		<i class="material-icons">add</i>
	</a>
</div>

<div class="background-primary">
	<div class="row main-background ">
		<div class="col s12">
			<div class="main-container">
				<div class="row">
					<h5 class="text"><spring:message code="publications.list.available"/></h5>
					<h8 class="text"><spring:message code="publications.list.select"/></h8>
				</div>
				<c:forEach var="card" items="${publications}">
					<div class="col s12 m6 l3">
						<a href="<c:url value='submitmail'>
							<c:param name='publicationId' value='${card.publication.publicationId}'/>
							</c:url>" class="card-link">
							<div class="card hoverable">
								<div class="card-image waves-effect waves-block waves-light">
									<c:if test="${card.image != null}">
										<img class="activator custom-image" src="${pageContext.request.contextPath}/images/${card.image.imageId}" alt="bookImage"/>
									</c:if>
									<c:if test="${card.image == null}">
										<img class="activator custom-image" src="images/book.jpg" alt="book"/>
									</c:if>
								</div>
								<div class="card-content">
									<h5 class="card-text">${card.book.title}</h5>
									<p class="card-text"><strong><spring:message code="book.description"/></strong>: ${card.book.description}</p>
									<p class="card-text"><strong><spring:message code="publication.location"/></strong>: ${card.publication.location}</p>
								</div>
							</div>
						</a>
					</div>
				</c:forEach>
			</div>
		</div>

	</div>
</div>
</body>
</html>


<%--<c:if test="${not empty publications.publications}">--%>
<%--	<div>--%>

<%--	</div>--%>
<%--</c:if>--%>

<%--<c:if test="${empty publications.publications}">--%>
<%--	<p>No publications available.</p>--%>
<%--</c:if>--%>
