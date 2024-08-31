<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html lang="es">
  <head>
    <title>Title</title>
  </head>
  <body>
    <div style="border: 1px solid #000; padding: 10px; margin: 10px; display: inline-block; width: 200px; vertical-align: top;">
      <h2>Publication ID: ${publication.publicationId}</h2>
      <p><strong>Book ID:</strong> ${publication.bookId}</p>
      <p><strong>User ID:</strong> ${publication.userId}</p>
      <p><strong>Publication State:</strong> ${publication.publicationState}</p>
      <p><strong>Location:</strong> ${publication.location}</p>
  </div>
  </body>
</html>
