<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html lang="es" class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/exchange.css" rel="stylesheet"/>

    <title><spring:message code="publications.list.brand.logo"/></title>

</head>
<body class="main">
<navbar/>

<div class="uk-grid">
    <div class="uk-width-1-2 uk-align-center title-section">
        <h2 class="uk-h2 title"><spring:message code="exchange.offers.title"/></h2>
        <h3 class="uk-h5"><spring:message code="exchange.offers.subtitle"/></h3>
        <div>
            <div class="uk-grid-match uk-child-width-1-2@s uk-child-width-1-3@m mb-1" uk-grid>
                <c:forEach var="exchange" items="${exchanges}">
                    <div>
                        <a href="<c:url value='exchange/${exchange.exchange.exchangeId}'>
								<c:param name='exchangeId' value='${exchange.exchange.exchangeId}'/>
								</c:url>"
                           class="uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded custom-link">

<%--                                <h5 class="uk-card-title custom-link">${exchange.exchange.title_offered}</h5>--%>
<%--                                <h5 class="uk-card-title custom-link">${exchange.exchange.title_requested}</h5>--%>
<%--                            <p class="small-gray-text custom-link">${exchange.authorsString}</p>--%>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

</div>


</body>
</html>
