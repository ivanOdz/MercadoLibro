<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>


<html class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
    <%@include file="/WEB-INF/jsp/head/headers.jsp"%>
    <link href="${pageContext.request.contextPath}/css/book_form.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/navbar.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <title><spring:message code="add.book"/></title>

</head>

<script type="text/javascript">

    document.addEventListener('DOMContentLoaded', function() {
        var previewContainer = document.getElementById('image-preview-container');
        var fileInput = document.getElementById('file-input');

        fileInput.addEventListener('change', function(event) {
            const files = event.target.files;

            previewContainer.innerHTML = '';

            for (const file of files) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const imgWrapper = document.createElement('div');
                    imgWrapper.classList.add('image-wrapper');

                    const img = document.createElement('img');
                    img.src = e.target.result;
                    img.classList.add('uk-margin-small-right');
                    img.style.maxWidth = '200px';
                    img.style.maxHeight = '200px';

                    const deleteButton = document.createElement('button');
                    deleteButton.classList.add('uk-button', 'uk-button-danger', 'uk-button-small');
                    deleteButton.innerHTML = '<span uk-icon="icon: trash"></span>';
                    deleteButton.classList.add('delete-button');

                    deleteButton.addEventListener('click', function() {
                        imgWrapper.remove();
                    });

                    imgWrapper.appendChild(img);
                    imgWrapper.appendChild(deleteButton);
                    previewContainer.appendChild(imgWrapper);
                };
                reader.readAsDataURL(file);
            }
        });
    });



</script>

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
                            <a class="uk-button uk-button-text" href="${pageContext.request.contextPath}/book/book_models">
                                <span uk-icon="icon:  chevron-left"></span>
                                <spring:message code="add.book.return_book_models"/>
                            </a>
                        </div>
                        <div>
                            <span class="uk-text-large uk-text-bold">
                            <c:out value="${book_model.title}"/> </span>
                        </div>

                        <%--BookState--%>

                        <div class="form-group uk-margin-top uk-margin-bottom">
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

                        <div class="form-group uk-margin-top uk-margin-bottom">
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
                        <div class="uk-placeholder uk-text-center uk-margin-top uk-margin-bottom">
                            <span uk-icon="icon: cloud-upload"></span>
                            <span class="uk-text-middle"><spring:message code="add.publication.add.images"/></span>
                            <div uk-form-custom>
                                <input type="file" id="file-input" name="imageFiles" accept="image/*" multiple>
                                <span class="uk-link"><spring:message code="add.publication.add.image.here"/></span>
                            </div>
                        </div>

                        <progress id="js-progressbar" class="uk-progress" value="0" max="100" hidden></progress>

                        <div id="image-preview-container" class="uk-margin-top"></div>

                        <%--button--%>
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
