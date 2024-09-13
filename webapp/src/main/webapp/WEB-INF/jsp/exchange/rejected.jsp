<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
    <title><fmt:message key="exchange.accepted"/></title>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <meta charset="UTF-8">
</head>
<body>
<c:url var="okUrl" value="/exchange"/>
<c:url var="logout" value="/logout"/>

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
                    <li>
                        <a class="pl-1 pr-1" href="<c:url value='${booksUrl}'/>">
                            <spring:message code="home.book.view"/>
                        </a>
                        <div class="uk-navbar-dropdown">
                            <ul class="uk-nav uk-navbar-dropdown-nav">
                                <li class="uk-active uk-margin-small-top">
                                    <a href="<c:url value='${booksUrl}'/>">
                                        <spring:message code="home.book.view.books"/>
                                    </a>
                                </li>
                                <li class="uk-margin-small-top">
                                    <a href="<c:url value='${newBookFromScratch}'/>">
                                        <spring:message code="home.book.view.uploadnew"/>
                                    </a>
                                </li>
                                <li class="uk-margin-small-top">
                                    <a href="<c:url value='${uploadNewPrecharged}'/>">
                                        <spring:message code="home.book.view.uploadnewprecharged"/>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </li>
                    <li>
                        <a class="pl-1 pr-1" href="<c:url value="${profileUrl}"/>"><spring:message code="home.profile.view"/></a>
                        <div class="uk-navbar-dropdown">
                            <ul class="uk-nav uk-navbar-dropdown-nav">
                                <li class="uk-active uk-margin-small-top">
                                    <a href="<c:url value='${profileUrl}'/>">
                                        <spring:message code="home.profile.view"/>
                                    </a>
                                </li>
                                <li class="uk-margin-small-top">
                                    <a href="<c:url value='${logout}'/>">
                                        <spring:message code="home.profile.logout"/>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</nav>

<div class="uk-background-center-center">
    <div class="uk-position-center uk-card uk-card-default uk-card-body uk-width-1-2@m">
        <h3 class="uk-card-title">
                <fmt:message key="exchange.rejected"/>
        </h3>
        <a href="${okUrl}">
            <button class="uk-button uk-button-primary uk-align-center">
                <spring:message code="exchange.button.redirect"/>
            </button>
        </a>
    </div>
</div>
</body>
</html>