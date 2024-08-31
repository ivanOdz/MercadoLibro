<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<!-- TODO: -->
<!-- 1) Recuperar lista -->
<!-- 2) Recorrer la lista con el forEach -->

<html lang="es">

<head>
	<title>Publications List</title>
</head>

<body>

	<h1>Publications</h1>

	<c:if test="${not empty publications.publications}">
		<table border="1">
			<thead>
			<tr>
 					<th>Publication ID</th>
					<th>Book ID</th>
					<th>User ID</th>
					<th>Publication State</th>
					<th>Location</th>
                </tr>
			</thead>
			<tbody>
				<c:forEach var="publication" items="${publications.publications}">
					<tr>
						<td>${publication.publicationId}</td>
						<td>${publication.bookId}</td>
						<td>${publication.userId}</td>
						<td>${publication.publicationState}</td>
						<td>${publication.location}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

	<c:if test="${empty publications.publications}">
		<p>No publications available.</p>
	</c:if>
	
</body>

</html>