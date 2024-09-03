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
		<div style="margin-top: 3vh;" class="row">
			<div class="col s4 align-content">
				<a href="${pageContext.request.contextPath}/" class="brand-logo brown-text darken-4-text s4">BookSwap</a>
			</div>
			<div class="s4 col">
				<div class="row">
					<form class="col s12" action="${pageContext.request.contextPath}/" method="get">
						<div class="row inline-form">
							<input type="text" id="search" name="search">
							<i style="color: black" class="material-icons tiny suffix search-button">search</i>
						</div>
					</form>
				</div>
			</div>
		</div>

			<div style="height: 100%" class="s4 col">

			</div>
	</nav>
	<!-- Contenido de la página-->
	<div class="background-primary">
		<div class="row main-background ">
			<div class="col s12">
				<div class="main-container">
					<div class="row">
						<h5 class="text">Publicaciones disponibles</h5>
						<h8 class="text">Seleccione el libro con el que desea intercambian</h8>
					</div>
					<c:forEach var="publication" items="${publications.publications}">
						<div class="col s12 m6 l3">
							<a href="<c:url value='submitmail'>
										<c:param name='publicationId' value='${publication.publicationId}'/>
										</c:url>" class="card-link">
								<div class="card hoverable">
									<div class="card-image waves-effect waves-block waves-light">
										<img class="activator custom-image" src="images/book.jpg" alt="book">
									</div>
									<div class="card-content">
										<h5 class="card-text">Publication ID: ${publication.publicationId}</h5>
										<p class="card-text"><strong>Book ID:</strong> ${publication.bookId}</p>
										<p class="card-text"><strong>User ID:</strong> ${publication.userId}</p>
										<p class="card-text"><strong>Publication State:</strong> ${publication.publicationState}</p>
										<p class="card-text"><strong>Location:</strong> ${publication.location}</p>
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
