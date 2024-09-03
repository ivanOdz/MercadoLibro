<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html lang="es">

<head>
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/publications.css" rel="stylesheet">
	<link href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css" rel="stylesheet">
	<title>Publications List</title>
</head>

<body>
	<nav class="background-nav">
		<div class="nav-wrapper">
			<a href="${pageContext.request.contextPath}/" class="brand-logo brown-text darken-4-text">BookSwap</a>
			<div class="right inline-form">
				<form class="input-field search-form" action="${pageContext.request.contextPath}/" method="get">
					<div class="search-container">
						<input type="text" id="search" name="search" class="search-input">
					</div>
					<button class="btn-flat search-button"><i class="material-icons tiny">search</i></button>
				</form>

				<ul id="nav-mobile" class="right hide-on-med-and-down">
					<li><a href="#profile" class="nav-bar-text">Profile</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<!-- Contenido de la página-->
	<div class="row">
		<div class="col s12 background-primary">
			<div class="main-container">
				<c:forEach var="publication" items="${publications.publications}">
					<div class="col s12 m6 l3"> <!-- Columna para cuadrícula -->
						<a href="<c:url value='submitmail'>
                    <c:param name='publicationId' value='${publication.publicationId}'/>
                    </c:url>" class="card-link">
							<div class="card">
								<div class="card-image waves-effect waves-block waves-light">
									<img class="activator custom-image" src="images/book.jpg" alt="book">
								</div>
								<div class="card-content">
									<h5>Publication ID: ${publication.publicationId}</h5>
									<p><strong>Book ID:</strong> ${publication.bookId}</p>
									<p><strong>User ID:</strong> ${publication.userId}</p>
									<p><strong>Publication State:</strong> ${publication.publicationState}</p>
									<p><strong>Location:</strong> ${publication.location}</p>
								</div>
								<div class="card-action">
									<a href="${publicationUrl}">
										<button class="btn">Lo quiero!</button>
									</a>
								</div>
							</div>
						</a>
					</div>
				</c:forEach>
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
