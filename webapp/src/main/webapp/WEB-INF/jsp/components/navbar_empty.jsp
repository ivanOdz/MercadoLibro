<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
    <link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
</head>

<nav class="uk-navbar-container uk-box-shadow-small" uk-sticky>
    <div class="uk-container uk-width-1-1 nav-background">
        <div class="uk-width-1-1 nav-empty-container" uk-navbar>
            <div class="uk-navbar-left">
                <ul class="uk-navbar-nav uk-flex uk-flex-middle">
                    <li class="uk-flex uk-flex-middle">
                        <a href="${pageContext.request.contextPath}/">
                            <img src="<c:url value='/images/mercado_libro.webp'/>" alt="Logo Icon" style="max-width: 80px; margin-left: 32px;">
                        </a>
                    </li>
                    <li class="uk-flex uk-flex-middle">
                        <a class="uk-navbar-item uk-logo" href="${pageContext.request.contextPath}/">
                            <strong class="button-text-wo" style="margin-left: 10px;">
                                <spring:message code="publications.list.brand.logo"/>
                            </strong>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</nav>
