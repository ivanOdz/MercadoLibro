<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:url var="exchangeRequestsUrl" value="/requests"/>
<c:url var="exchangeOffersUrl" value="/offers"/>
<c:url var="booksUrl" value="/book"/>
<c:url var="profileUrl" value="/profile"/>
<c:url var="newBookFromScratch" value="/book/new_book"/>
<c:url var="uploadNewPrecharged" value="/book/book_models"/>
<c:url var="logout" value="/logout"/>
<c:url var="loginUrl" value="/login"/>
<c:url var="registerUrl" value="/create"/>
<c:url var="myPublicationsUrl" value="/my_publications"/>
<c:url var="myFavoritesUrl" value="/my_favorites"/>

<nav class="uk-navbar-container uk-box-shadow-small " uk-sticky>
    <div class="uk-container uk-width-1-1 nav-background">
        <div class="uk-width-1-1" style="
             margin: 0;
             padding: 0 3rem 0 2rem;
        " uk-navbar>
            <div class="uk-navbar-left">
                <ul class="uk-navbar-nav">
                    <li>
                        <a href="<c:url value='/' /> ">
                            <img src="<c:url value='/images/mercado_libro.webp' />" alt="Logo Icon"
                                 class="icon-style">
                        </a>
                    </li>
                    <li>
                        <a class="uk-navbar-item uk-logo" href="<c:url value='/' /> ">
                            <strong class="button-text">
                                <spring:message code="publications.list.brand.logo"/>
                            </strong>
                        </a>
                    </li>
                </ul>
            </div>

            <div class="uk-navbar-center">
                <ul class="uk-navbar-nav">
                    <li>
                        <form class="uk-search uk-search-default custom-search-form" method="get" action="">
                            <input class="uk-search-input button-text" type="search"
                                   placeholder="  <spring:message code='home.search.text'/>"
                                   aria-label="Search"
                                   name="search"
                                   id="search"
                                   value="<c:out value='${param.search}'/>">
                            <button class="uk-search-icon-flip" style="color:white;" uk-search-icon></button>
                        </form>
                    </li>
                </ul>
            </div>
            <div class="uk-navbar-right">
                <ul class="uk-navbar-nav" style="display: flex; flex-direction: row;">
                    <c:if test="${loggedUser != null}">
                        <li>
                            <a class="pl-1 pr-1" href="${exchangeRequestsUrl}"><spring:message
                                    code="home.exchange.view"/></a>
                            <div class="uk-navbar-dropdown">
                                <ul  class="uk-nav uk-navbar-dropdown-nav" >
                                    <li class="uk-active uk-margin-small-top">
                                        <a class="button-text-dropdown" href="${exchangeRequestsUrl}">
                                            <spring:message code="home.exchange.requests"/>
                                        </a>
                                    </li>
                                    <li class="uk-margin-small-top">
                                        <a class="button-text-dropdown" href="${exchangeOffersUrl}">
                                            <div class="button-text">
                                                <spring:message code="home.exchange.offers"/></div>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </li>

                        <li>
                            <a class="pl-1 pr-1" href="${booksUrl}">
                                <spring:message code="home.book.view"/>
                            </a>
                            <div class="uk-navbar-dropdown">
                                <ul class="uk-nav uk-navbar-dropdown-nav">
                                    <li class="uk-active uk-margin-small-top">
                                        <a class="button-text-dropdown" href="${booksUrl}">
                                            <spring:message code="home.book.view.books"/>
                                        </a>
                                    </li>
                                    <li class="uk-margin-small-top">
                                        <a class="button-text-dropdown" href="${newBookFromScratch}">
                                            <spring:message code="home.book.view.uploadnew"/>
                                        </a>
                                    </li>
                                    <li class="uk-margin-small-top">
                                        <a class="button-text-dropdown" href="${uploadNewPrecharged}">
                                            <spring:message code="home.book.view.uploadnewprecharged"/>
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </li>
                        <li>
                            <a class="pl-1 pr-1" href="${profileUrl}"><spring:message code="home.profile.view"/></a>
                            <div class="uk-navbar-dropdown">
                                <ul class="uk-nav uk-navbar-dropdown-nav">
                                    <li class="uk-active uk-margin-small-top">
                                        <a class="button-text-dropdown" href="${profileUrl}">
                                            <spring:message code="home.profile.view"/>
                                        </a>
                                    </li>
                                    <li class="uk-margin-small-top">
                                        <a class="button-text-dropdown" href="${myPublicationsUrl}">
                                            <spring:message code="home.profile.publications"/>
                                        </a>
                                    </li>
                                    <li class="uk-margin-small-top">
                                        <a class="button-text-dropdown" href="${myFavoritesUrl}">
                                            <spring:message code="home.profile.favorites"/>
                                        </a>
                                    </li>
                                    <li class="uk-margin-small-top">
                                        <a class="button-text-dropdown" href="#modal-logout" uk-toggle><spring:message
                                                code="home.profile.logout"/></a>
                                    </li>
                                </ul>
                            </div>
                        </li>
                    </c:if>
                    <c:if test="${loggedUser == null}">
                        <li>
                            <a class="uk-button login-button" href="${loginUrl}">
                                        <spring:message code="hwc.login.submit"/>
                            </a>
                        </li>
                        <li>
                            <a class="uk-button register-button" href="${registerUrl}">
                                        <spring:message code="hwc.signup.button"/>
                            </a>
                        </li>
                    </c:if>
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