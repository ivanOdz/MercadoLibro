<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>




<html lang="en" class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>

<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/publications.css?v=1.0" rel="stylesheet"/>


    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title>Error 404 - Page Not Found</title>
    <style>
        body, html {
            height: 100%;
            margin: 0;
        }

        .error-container {
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #f5f5f5;
        }

        .error-card {
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .error-title {
            font-size: 72px;
            color: #ff5733;
            margin-bottom: 0;
        }

        .error-message {
            font-size: 18px;
            color: #666;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="uk-container error-container">
    <div class="uk-card uk-card-default uk-card-body uk-text-center error-card">
        <h1 class="uk-heading-medium error-title">404</h1>
        <p class="uk-text-lead error-message">Oops! La página que estás buscando no se pudo encontrar.</p>
        <p class="uk-text-meta">Es posible que la página se haya movido o que la URL que ingresaste no sea correcta.</p>
        <div class="uk-margin">
            <a href="/" class="uk-button uk-button-primary uk-margin-small-right">Volver a la página principal</a>
            <a href="javascript:history.back()" class="uk-button uk-button-secondary">Volver atrás</a>
        </div>
    </div>
</div>

<!-- UIkit JS -->
<script src="https://cdn.jsdelivr.net/npm/uikit@3.6.16/dist/js/uikit.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/uikit@3.6.16/dist/js/uikit-icons.min.js"></script>
</body>
</html>
