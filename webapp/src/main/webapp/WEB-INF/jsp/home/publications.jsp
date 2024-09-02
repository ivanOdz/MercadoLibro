<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html lang="es">

<head>
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/publications.css" rel="stylesheet">
	<link href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css" rel="stylesheet">
	<title>Publications List</title>
</head>

<body>
	<nav style="background-color: #F7E7DC">
		<div class="container">
			<a href="#" class="brand-logo">BookSwap</a>
			<ul id="nav-mobile" class="right hide-on-med-and-down">
				<li><a href="#profile" class="nav-bar-text">Profile</a></li>
			</ul>
		</div>
	</nav>
<%--	<nav style="background-color: #EBE3D5;">--%>
<%--		<div class="nav-bar">--%>
<%--			<a href="#" class="brand-logo">BookExchange</a>--%>
<%--			<form action="/" method="get">--%>
<%--				<input type="text" id="search" name="search">--%>
<%--				<button type="submit">Buscar</button>--%>
<%--			</form>--%>
<%--			<ul id="nav-mobile" class="right hide-on-med-and-down">--%>
<%--			</ul>--%>
<%--		</div>--%>
<%--	</nav>--%>
<div class="row">
	<div class="col s3 background-primary">
		<p>hole</p>
	</div>

	<div class="col s9 background-secondary">
		<!-- Page content with #EBE3D5 -->
		<p>holi</p>
	</div>

</div>

</body>

</html>



<%--<h1>Publications</h1>--%>



<%--<c:if test="${not empty publications.publications}">--%>
<%--	<div>--%>
<%--		<c:forEach var="publication" items="${publications.publications}">--%>
<%--			<div style="border: 1px solid #000; padding: 10px; margin: 10px; display: inline-block; width: 200px; vertical-align: top;">--%>
<%--				<h2>Publication ID: ${publication.publicationId}</h2>--%>
<%--				<p><strong>Book ID:</strong> ${publication.bookId}</p>--%>
<%--				<p><strong>User ID:</strong> ${publication.userId}</p>--%>
<%--				<p><strong>Publication State:</strong> ${publication.publicationState}</p>--%>
<%--				<p><strong>Location:</strong> ${publication.location}</p>--%>

<%--				<c:url var="publicationUrl" value="submitmail">--%>
<%--				<c:param name="publicationId" value="${publication.publicationId}" />--%>
<%--				</c:url>--%>
<%--				<a href="${publicationUrl}">--%>
<%--					<button>Lo quiero!</button>--%>
<%--				</a>--%>
<%--			</div>--%>
<%--		</c:forEach>--%>
<%--	</div>--%>
<%--</c:if>--%>

<%--<c:if test="${empty publications.publications}">--%>
<%--	<p>No publications available.</p>--%>
<%--</c:if>--%>
