<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>

<html>
<head>
  <link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
  <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

  <title><spring:message code="library.title"/></title>
</head>
<body>
<navbar/>


<div class="uk-background-muted">
  <div class="uk-container">
    <div class="uk-grid ml-1 uk-margin-top" uk-grid>
      <div class="uk-width-1-4@s filter-section uk-border-rounded uk-box-shadow-small mt-1 mb-1">
        <h2>${param.search}</h2>

        <!-- Esto tiene que aparecer solo si hay un filtro de Genero -->
        <c:if test="${genreFilter != '32'}">
          <form action="<c:url value='' />" method="get">
            <input type="hidden" name="genre-filter" value="">
            <input type="hidden" name="search" value="${param.search}">

            <button type="submit" class="ui-search-button" title="GenreFilterRemove">
							<span class="ui-search-filter-name">
								<spring:message code="delete.genre.filter"/>
							</span>
            </button>
          </form>
        </c:if>

        <c:if test="${genreFilter == '32'}">
          <h3><spring:message code="filter.genre"/></h3>
          <ul class="uk-list">
            <c:forEach var="genreWrapper" items="${genres}">
              <li class="ui-search-filter-container">
                <form action="<c:url value='' />" method="get">
                  <input type="hidden" name="genre-filter" value="${genreWrapper.genre.value}">
                  <input type="hidden" name="search" value="${param.search}">

                  <button type="submit" class="ui-search-button" title="${genreWrapper.displayName}">
                    <span class="ui-search-filter-name">${genreWrapper.displayName}</span>
                  </button>
                </form>
              </li>
            </c:forEach>
          </ul>
        </c:if>
      </div>

      <div class="uk-width-3-4@s col-content">
        <div class="uk-card uk-card-default uk-card-body uk-margin-bottom mt-1 uk-border-rounded uk-border-rounded-medium">
          <h5 class="uk-text-large"><spring:message code="book.model.view.title"/></h5>
          <h6 class="uk-text-muted"><spring:message code="book.model.list.select"/></h6>
        </div>

        <div class="uk-grid-match uk-child-width-1-2@s uk-child-width-1-3@m mb-1" uk-grid>
          <c:forEach var="card" items="${cardBookList}">
            <div>
              <div class="uk-card uk-card-default uk-card-hover uk-card-body uk-border-rounded custom-link">
                <figure class="uk-margin-bottom">
                  <c:choose>
                    <c:when test="${card.image != null}">
                      <img class="book-image" src="${pageContext.request.contextPath}/images/${card.image}" alt="bookImage"/>
                    </c:when>
                    <c:otherwise>
                      <img class="book-image" src="${pageContext.request.contextPath}/images/book.jpg" alt="book"/>
                    </c:otherwise>
                  </c:choose>
                </figure>
                <h5 class="uk-card-title custom-link">${card.bookModel.title}</h5>
                <p class="small-gray-text custom-link">${card.authorsString}</p>

                    <a class="uk-button uk-button-default uk-button-primary uk-width-1-1" href="#modal-sections-${card.bookModel.bookModelId}" uk-toggle>
                      <spring:message code="book.add.button"/>
                    </a>

                    <div id="modal-sections-${card.bookModel.bookModelId}" uk-modal>
                      <div class="uk-modal-dialog">
                        <div class="uk-modal-header">
                          <h5 class="uk-card-title custom-link">${card.bookModel.title}</h5>
                          <p class="small-gray-text custom-link">${card.authorsString}</p>
                          <p>${card.bookModel.genre}</p>
                          <p>${card.bookModel.editorial}</p>
                          <p>${card.bookModel.description}</p>

                            <div class="uk-margin" style="justify-content: center">
                              <div class="uk-width-1-1" >
                                <div class="uk-margin-top uk-button-group" style="margin-left: 50px;">
                                  <a href="${pageContext.request.contextPath}/book/form_step2?book_model_id=${card.bookModel.bookModelId}" type="submit" class="uk-button uk-button-primary"> <spring:message code="book.model.view.button"/></a>
                                </div>
                              </div>
                            </div>
                        </div>
                      </div>
                    </div>

              </div>
            </div>
          </c:forEach>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
