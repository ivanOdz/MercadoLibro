<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="exchangeRequestsUrl" value="/requests"/>
<c:url var="exchangeOffersUrl" value="/offers"/>
<c:url var="booksUrl" value="/book"/>
<c:url var="profileUrl" value="/profile"/>
<c:url var="newBookFromScratch" value="/book/new_book"/>
<c:url var="uploadNewPrecharged" value="/book/book_models"/>
<c:url var="logout" value="/logout"/>

<nav class="uk-navbar-container uk-box-shadow-small " style="height: 5rem;" uk-sticky>
    <div class="uk-container uk-width-1-1  nav-background" style="height: 100%;">
        <div class="uk-width-1-1 nav-container" uk-navbar>
            <div class="uk-navbar-left">
                <ul class="uk-navbar-nav">
                    <li>
                        <a class="image-hover" href="<c:url value='/' /> ">
                            <img src="<c:url value='/images/mercado_libro.webp' />" alt="Logo Icon"
                                 class="icon-style">
                        </a>
                    </li>
                    <li>
                        <a class="uk-navbar-item uk-logo" href="<c:url value='/' /> ">
                            <strong class="button-text-wo">
                                <spring:message code="publications.list.brand.logo"/>
                            </strong>
                        </a>
                    </li>

                </ul>
            </div>

            <div class="uk-navbar-right">
                <ul class="uk-navbar-nav">
                    <li>
                        <a class="right-icons-nav pl-1 pr-1" href="${exchangeRequestsUrl}"><spring:message
                                code="home.exchange.view"/></a>
                        <div class="uk-navbar-dropdown">
                            <ul class="uk-nav uk-navbar-dropdown-nav column">
                                <li class="uk-active uk-margin-small-top">
                                    <a class="dropdown-wo-buttons" href="${exchangeRequestsUrl}">
                                        <spring:message code="home.exchange.requests"/>
                                    </a>
                                </li>
                                <li class="uk-margin-small-top">
                                    <a class="dropdown-wo-buttons" href="${exchangeOffersUrl}">
                                        <spring:message code="home.exchange.offers"/>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </li>

                    <li>
                        <a class="pl-1 pr-1 right-icons-nav" href="${booksUrl}">
                            <spring:message code="home.book.view"/>
                        </a>
                        <div class="uk-navbar-dropdown">
                            <ul class="uk-nav uk-navbar-dropdown-nav column">
                                <li class="uk-active uk-margin-small-top">
                                    <a class="dropdown-wo-buttons" href="${booksUrl}">
                                        <spring:message code="home.book.view.books"/>
                                    </a>
                                </li>
                                <li class="uk-margin-small-top">
                                    <a class="dropdown-wo-buttons" href="${newBookFromScratch}">
                                        <spring:message code="home.book.view.uploadnew"/>
                                    </a>
                                </li>
                                <li class="uk-margin-small-top">
                                    <a class="dropdown-wo-buttons" href="${uploadNewPrecharged}">
                                        <spring:message code="home.book.view.uploadnewprecharged"/>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </li>
                    <li>
                        <a class="pl-1 pr-1 right-icons-nav" href="${profileUrl}"><spring:message
                                code="home.profile.view"/></a>
                        <div class="uk-navbar-dropdown">
                            <ul class="uk-nav uk-navbar-dropdown-nav column">
                                <li class="uk-active uk-margin-small-top">
                                    <a class="dropdown-wo-buttons" href="${profileUrl}">
                                        <spring:message code="home.profile.view"/>
                                    </a>
                                </li>
                                <li class="uk-margin-small-top">
                                    <a class="dropdown-wo-buttons" href="#modal-logout" uk-toggle><spring:message
                                            code="home.profile.logout"/></a>
                                </li>
                            </ul>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</nav>

<div id="modal-logout" uk-modal>
    <div class="uk-modal-dialog uk-modal-body">
        <h3 class="uk-h3"><spring:message code="logout.confirmation.title"/></h3>
        <p class="uk-text-right">
            <button class="uk-button uk-button-default uk-modal-close" type="button"><spring:message
                    code="button.cancel"/></button>
            <button class="uk-button uk-button-danger" type="button">
                <a href="${logout}">
                    <spring:message code="home.profile.logout"/>
                </a>
            </button>
        </p>
    </div>
</div>