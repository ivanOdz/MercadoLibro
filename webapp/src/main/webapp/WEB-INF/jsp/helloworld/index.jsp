<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Tu Aplicacion</title>
        <!-- Fuente iconos de google -->
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

        <!-- Materialize CSS -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    </head>
    <body>
        <h2>Hello <c:out value="${username}"/>!</h2>
        <h5>Your user id is <c:out value="${userId}"/></h5>
        <h5>Your logged user <c:out value="${loggedUser.id}"/>, <c:out value="${loggedUser.username}"/></h5>
        <button class="btn waves-effect waves-light" type="submit" name="action">Submit
            <i class="material-icons right">send</i>
        </button>
    </body>

    <!-- Materialize JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</html>
