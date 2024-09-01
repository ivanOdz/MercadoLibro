<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html lang="es">

<head>
	<title>Publications List</title>
</head>

<body>

<h1>Publications</h1>

<c:if test="${not empty publications.publications}">
	<div>
		<c:forEach var="publication" items="${publications.publications}">
			<div style="border: 1px solid #000; padding: 10px; margin: 10px; display: inline-block; width: 200px; vertical-align: top;">
				<h2>Publication ID: ${publication.publicationId}</h2>
				<p><strong>Book ID:</strong> ${publication.bookId}</p>
				<p><strong>User ID:</strong> ${publication.userId}</p>
				<p><strong>Publication State:</strong> ${publication.publicationState}</p>
				<p><strong>Location:</strong> ${publication.location}</p>

				<c:url var="publicationUrl" value="submitmail">
				<c:param name="publicationId" value="${publication.publicationId}" />
				</c:url>
				<a href="${publicationUrl}">
					<button>Lo quiero!</button>
				</a>
			</div>
		</c:forEach>
	</div>
</c:if>

<c:if test="${empty publications.publications}">
	<p>No publications available.</p>
</c:if>

</body>

</html>
