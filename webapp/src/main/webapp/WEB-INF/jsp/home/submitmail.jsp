<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="submitmail" method="post">
    <label for="email">Por favor ingrese su mail:</label>
    <input type="email" id="email" name="email" required>
    <input type="hidden" name="publicationId" value="${publicationId}">
    <button type="submit">Enviar</button>
</form>
</body>
</html>
