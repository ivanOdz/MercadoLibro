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
