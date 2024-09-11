<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
  <link href="${pageContext.request.contextPath}/css/publications.css?v=1.0" rel="stylesheet"/>
  <link href="${pageContext.request.contextPath}/css/publicationDetail.css?v=1.0" rel="stylesheet"/>
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

  <title><spring:message code="publication.details.title"/></title>
</head>
<body>
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

          <li><a class="pl-1 pr-1" href="<c:url value="${profileUrl}"/>"><spring:message code="home.profile.view"/></a></li>
        </ul>
      </div>
    </div>
  </div>
</nav>

<div class="uk-background-muted">
  <div class="uk-container">
  <div class="my-image">
    <figure class="uk-margin-bottom">
      <c:choose>
        <c:when test="${not empty card.bookImages}">
          <img class="book-image" src="${pageContext.request.contextPath}/images/${card.bookImages[0].imageId}" alt="bookImage" style="width: 300px; height: auto; margin-left: 20px"/>

          <div class="uk-position-relative uk-visible-toggle uk-light" tabindex="-1" uk-slideshow>

            <div class="uk-slideshow-items">
              <c:forEach var="image" items="${card.bookImages}">
              <div>
                <img src="${pageContext.request.contextPath}/images/${image.imageId}" alt="bookImage" uk-cover>
              </div>
              </c:forEach>
            </div>

            <a class="uk-position-center-left uk-position-small uk-hidden-hover"  uk-slidenav-previous uk-slideshow-item="previous"></a>
            <a class="uk-position-center-right uk-position-small uk-hidden-hover" uk-slidenav-next uk-slideshow-item="next"></a>

          </div>

        </c:when>
        <c:otherwise>
          <img class="book-image" src="${pageContext.request.contextPath}/images/book.jpg" alt="book" style="width: 300px; height: auto;"/>
        </c:otherwise>
      </c:choose>
    </figure>
  </div>

  <div class="column-container">
    <!-- Title -->
    <div>
      <h1 class="uk-text-large uk-text-bold" style="font-size: 36px;">${card.bookModel.title}</h1>
    </div>

    <!-- Author -->
    <div>
      <p class="small-gray-text custom-link">${card.authorsString}</p>
    </div>

    <!-- Line -->
    <hr style="width: 40lh;"/>

    <!-- Description -->
    <div style="max-width: 40lh; text-align: justify;">
      <p>${card.bookModel.description}</p>
    </div>

    <!-- Line -->
    <hr style="width: 40lh;"/>
    <div>
      <div class="row-container" style="margin-left: 50px;">

        <!-- Book State -->
        <div class="column-container" style="place-items: center; display: flex; margin-right: 90px;">
          <div>
            <p style="text-align: center"><spring:message code="publication.details.bookState"/></p>
            <i class="material-icons" style="margin-left: 40px;">book</i>
            <c:choose>
              <c:when test="${card.book.bookState == 'NEW'}">
                <spring:message code="bookState.new"/>
              </c:when>
              <c:when test="${card.book.bookState == 'LIKE_NEW'}">
                <spring:message code="bookState.like.new"/>
              </c:when>
              <c:when test="${card.book.bookState == 'VERY_GOOD'}">
                <spring:message code="bookState.very.good"/>
              </c:when>
              <c:when test="${card.book.bookState == 'GOOD'}">
                <spring:message code="bookState.good"/>
              </c:when>
              <c:when test="${card.book.bookState == 'ACCEPTABLE'}">
                <spring:message code="bookState.acceptable"/>
              </c:when>
              <c:when test="${card.book.bookState == 'WORN'}">
                <spring:message code="bookState.worn"/>
              </c:when>
            </c:choose>
          </div>
        </div>

        <!-- Publication date -->
        <div class="column-container" style="place-items: center; display: flex;">
          <div>
            <p style="text-align: center"><spring:message code="publication.details.date"/></p>
            <i class="material-icons" style="margin-left: 60px;">history</i>
            <p style="text-align: center"><strong>${card.publication.publicationDatetime}</strong></p>
          </div>
        </div>

        <!-- Editorial -->
        <div class="column-container" style="place-items: center; display: flex;">
          <div>
            <p style="text-align: center"><spring:message code="publication.details.editorial"/></p>
            <i class="material-icons" style="margin-right: 90px;">file-edit</i>
            <p style="text-align: center"><strong>${card.bookModel.editorial}</strong></p>
          </div>

          <!-- Location -->
          <div class="column-container" style="place-items: center; display: flex;">
            <div>
              <p style="text-align: center"><spring:message code="publication.details.location"/></p>
                <span uk-icon="icon: check"></span>
              <p style="text-align: center"><strong>${card.location}</strong></p>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>

  <div class="column-container" style="margin-left: 2lh; place-items: center; display: flex;">
    <div>
      <p class="uk-text-medium" style="font-size: 25px; text-align: center; max-width: 9lh; margin-left: 1lh;"><spring:message code="exchange.description"/></p>
    </div>
    <div>
      <p class="uk-text-medium" style="font-size: 25px; max-width: 9lh; text-align: center; margin-left: 1lh;"><spring:message code="exchange.description2"/></p>
    </div>

    <div class="uk-margin">
      <label class="uk-form-label" for="form-horizontal-select">Elija un libro de su biblioteca</label>
      <div class="uk-form-controls">
        <select class="uk-select" id="form-horizontal-select">
          <c:forEach var="book" items="${books}">
            <option>
                  <a href="<c:url value='publications/${card.publication.publicationId}'>
                                    <c:param name='publication_id' value='${card.publication.publicationId}'/>
                                    </c:url>" class="uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded custom-link">
                  <figure class="uk-margin-bottom">
                    <c:choose>
                      <c:when test="${card.bookImages != null}">
                        <img class="book-image" src="${pageContext.request.contextPath}/images/${card.bookImages[0].imageId}" alt="bookImage"/>
                      </c:when>
                      <c:otherwise>
                        <img class="book-image" src="${pageContext.request.contextPath}/images/book.jpg" alt="book"/>
                      </c:otherwise>
                    </c:choose>
                  </figure>

                  <h5 class="uk-card-title custom-link">${card.bookModel.title}</h5>
                  <p class="small-gray-text custom-link">${card.authorsString}</p>
                </a>
            </option>
          </c:forEach>
        </select>
      </div>
    </div>
    <div class="uk-margin-top uk-button-group" style="margin-left: 50px;">
      <button class="uk-button uk-button-primary"> <spring:message code="exchange.button.description"/> </button>
    </div>
  </div>

</div>
</div>
</body>
</html>
