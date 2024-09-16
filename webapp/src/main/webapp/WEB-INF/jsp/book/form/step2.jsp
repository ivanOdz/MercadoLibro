<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>

<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
    <%@include file="/WEB-INF/jsp/head/headers.jsp"%>
    <link href="${pageContext.request.contextPath}/css/book_form.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/navbar.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <title>STEP2</title>

</head>

<body>

<navbar/>

<c:url var="postUrl" value="/book/upload_book"/>

<div class="uk-background-muted">
    <div class="uk-container">
        <div class="uk-grid ml-1 uk-margin-top" uk-grid>

            <div class="uk-section uk-align-center">
                <div class="uk-container uk-margin-top">
                    <form:form modelAttribute="bookDetailsForm" action="${postUrl}" enctype="multipart/form-data">

                        <div class="uk-container uk-margin-bottom">
                            <a class="uk-button uk-button-text" href="${pageContext.request.contextPath}/">
                                <span uk-icon="icon:  chevron-left"></span>
                                <spring:message code="add.book.return_home"/>
                            </a>
                        </div>

                        <div class="uk-container">
                            <h2 class="uk-heading-line">
                                <spring:message code="add.book.title"/>
                            </h2>
                            <h4 class="uk-article-meta">
                                <spring:message code="add.book.description"/>
                            </h4>
                        </div>


                        <%--BookState--%>

                        <div class="form-group">
                            <form:label path="bookState">
                                <spring:message code="add.publication.book.state"/>
                            </form:label>
                            <form:select path="bookState" class="uk-input">
                                <c:forEach var="bookStateWrapper" items="${bookStates}">
                                    <form:option value="${bookStateWrapper.bookState}" label="${bookStateWrapper.displayName}" />
                                </c:forEach>
                            </form:select>
                        </div>

                        <%--Rating--%>

                        <div class="form-group">
                            <label>
                                <spring:message code="add.publication.rating"/>
                            </label>

                            <div class="star-rating">
                                <form:radiobutton path="rating" value="5" id="star5" />
                                <label for="star5" title="5 stars">
                                    <span uk-icon="icon: star; ratio: 1.5"></span>
                                </label>

                                <form:radiobutton path="rating" value="4" id="star4" />
                                <label for="star4" title="4 stars">
                                    <span uk-icon="icon: star; ratio: 1.5"></span>
                                </label>

                                <form:radiobutton path="rating" value="3" id="star3" />
                                <label for="star3" title="3 stars">
                                    <span uk-icon="icon: star; ratio: 1.5"></span>
                                </label>

                                <form:radiobutton path="rating" value="2" id="star2" />
                                <label for="star2" title="2 stars">
                                    <span uk-icon="icon: star; ratio: 1.5"></span>
                                </label>

                                <form:radiobutton path="rating" value="1" id="star1" />
                                <label for="star1" title="1 star">
                                    <span uk-icon="icon: star; ratio: 1.5"></span>
                                </label>
                            </div>
                        </div>

                        <%--Images--%>
                        <div class="uk-placeholder uk-text-center">
                            <span uk-icon="icon: cloud-upload"></span>
                            <span class="uk-text-middle">Attach binaries by dropping them here or</span>
                            <div uk-form-custom>
                                <input type="file" id="file-input" name="imageFiles" accept="image/*" multiple>
                                <span class="uk-link">selecting one</span>
                            </div>
                        </div>

                        <progress id="js-progressbar" class="uk-progress" value="0" max="100" hidden></progress>

                        <div id="image-preview-container" class="uk-margin-top"></div>


                        <div class="uk-container uk-margin-top">
                            <div class="uk-inline">
                                <div class="uk-position-right">
                                    <input type="hidden" name="book_model_id" value="${book_model.bookModelId}">
                                    <button type="submit" id="upload-button" class="uk-button uk-button-default uk-background-primary uk-light uk-panel">
                                        <spring:message code="add.publication.upload"/>
                                    </button>
                                </div>
                            </div>
                        </div>

                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>

</html>
