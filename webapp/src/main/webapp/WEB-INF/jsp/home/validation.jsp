<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="submitMail" method="post">
    <label for="email">Por favor ingrese su mail:</label>
    <input type="email" id="email" name="email" required>
    <button type="submit">Enviar</button>
</form>

    <div style="border: 1px solid #000; padding: 10px; margin: 10px; display: inline-block; width: 200px; vertical-align: top;">
        <h2>Book ID: ${book.bookId}</h2>
        <p><strong>Desc:</strong> ${book.description}</p>
    </div>
</body>
</html>
