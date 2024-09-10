<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="es">
<head>
    <link href="${pageContext.request.contextPath}/css/publications.css?v=1.0" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>
    <link href="${pageContext.request.contextPath}/css/exchange.css ?v=1.0" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <title><spring:message code="profile.view.title"/></title>
</head>
<body>
<c:url var="exchangeUrl" value="/exchange"/>
<c:url var="booksUrl" value="/book"/>
<c:url var="profileUrl" value="/profile"/>
<%--NavBar--%>

<nav class="uk-navbar-container uk-background-primary uk-box-shadow-small" uk-sticky>
    <div class="uk-container">
        <div  uk-navbar>
            <div class="uk-navbar-left">
                <ul class="uk-navbar-nav">
                    <li>
                        <a href="${pageContext.request.contextPath}/">
                            <img src="${pageContext.request.contextPath}/images/mercado_libro.webp" alt="Logo Icon" class="icon-style">
                        </a>
                    </li>
                    <li>
                        <a class="uk-navbar-item uk-logo" href="${pageContext.request.contextPath}/">
                            <strong>
                                <spring:message code="publications.list.brand.logo"/>
                            </strong>
                        </a>
                    </li>
                </ul>
            </div>

            <div class="uk-navbar-center">
                <ul class="uk-navbar-nav">
                    <li>
                        <form class="uk-search uk-search-default custom-search-form" method="get" action="${pageContext.request.contextPath}">
                            <input class="uk-search-input" type="search"
                                   placeholder="<spring:message code='home.search.text'/>"
                                   aria-label="Search"
                                   name="search"
                                   id="search"
                                   value="${param.search != null ? param.search : ''}">
                            <button class="uk-search-icon-flip" uk-search-icon></button>
                        </form>
                    </li>
                </ul>
            </div>
            <div class="uk-navbar-right">
                <ul class="uk-navbar-nav">
                    <li><a class="pl-1 pr-1" href="<c:url value="${exchangeUrl}"/>"><spring:message code="home.exchange.view"/></a></li>
                    <li><a class="pl-1 pr-1" href="<c:url value="${booksUrl}"/>"><spring:message code="home.book.view"/></a></li>
                    <li><a class="pl-1 pr-1" href="<c:url value="${profileUrl}"/>"><spring:message code="home.profile.view"/></a></li>
                </ul>
            </div>
        </div>
    </div>
</nav>

<%--Content--%>
<div class="uk-section uk-background-muted">
    <div class="uk-align-center uk-container">
        <p class="uk-text-lead uk-align-center">
            <spring:message code="profile.title"/>
        </p>
    </div>
    <div class="uk-container uk-margin-top">
        <div class="uk-grid ml-1" uk-grid>
            <div class="uk-width-1-3@s exchange-information-section uk-border-rounded uk-box-shadow-small mt-1 mb-1 uk-height-viewport" uk-height-viewport="offset-top: true">
            </div>

            <div class="uk-width-expand uk-margin-top">
                <p class="uk-text-lead">
                    <c:out value="${loggedUser.username}"/>
                </p>
                <c:forEach var="review" items="${reviews}">
                    <div class="uk-card uk-card-default uk-card-body uk-border-rounded uk-box-shadow-small mb-1">
<%--                        <p>${review.reviewDescription}</p>--%>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
</body>
</html>
