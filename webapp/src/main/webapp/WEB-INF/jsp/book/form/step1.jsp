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

    <title>STEP1</title>

</head>

<body>

<navbar/>

<c:url var="postUrl" value="/book/upload_book_model"/>

<div class="uk-background-muted">
    <div class="uk-container">
        <div class="uk-grid ml-1 uk-margin-top" uk-grid>

            <div class="uk-section uk-align-center">
                <div class="uk-container uk-margin-top">
                    <form:form modelAttribute="modelBookForm" action="${postUrl}" method="post" enctype="multipart/form-data">

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

                        <%--Titulo--%>

                        <div  class="uk-container uk-margin-top uk-margin-bottom">
                            <label class="form-group">
                                <spring:message code="add.publication.title"/>
                                <form:input path="title" type="text" class="uk-input"/>
                            </label>
                            <form:errors path="title" element="p" cssStyle="color: red;"/>
                        </div>

                        <%--Autores--%>
                        <div id="author-container" class="uk-container uk-margin-top uk-margin-bottom">
                            <label class="form-group">
                                <spring:message code="add.publication.authors"/>
                                <c:forEach var="author" items="${modelBookForm.authors}" varStatus="status">
                                    <input class="uk-input" type="text" name="authors[${status.index}]" value="${author}"/>
                                    <c:if test="${status.index > 0}">
                                        <button class="uk-button" type="button" onclick="removeAuthorField(this)">
                                            <span uk-icon="icon:  close"></span>
                                        </button>
                                    </c:if>
                                </c:forEach>
                            </label>
                        </div>

                        <div class="form-container" style="margin-bottom: 10px">
                            <button class="uk-button uk-margin-right" type="button" onclick="addAuthorField()"><spring:message code="add.publication.add.author"/></button>
                            <small class="description">
                                <spring:message code="add.publication.description.authors"/>
                            </small>
                        </div>

                        <%--Género--%>

                        <div class="form-group">
                            <form:label path="genre">
                                <spring:message code="add.publication.genre"/>
                            </form:label>
                            <form:select path="genre" class="uk-input">
                                <c:forEach var="genreWrapper" items="${genres}">
                                    <form:option value="${genreWrapper.genre}" label="${genreWrapper.displayName}"/>
                                </c:forEach>
                            </form:select>
                        </div>

                        <%--Languages--%>

                        <div class="form-group">
                            <form:label path="language">
                                <spring:message code="add.publication.book.language"/>
                            </form:label>
                            <form:select path="language" class="uk-input">
                                <c:forEach var="languageWrapper" items="${languages}">
                                    <form:option value="${languageWrapper.language}" label="${languageWrapper.displayName}" />
                                </c:forEach>
                            </form:select>
                        </div>

                        <%--Publication Year--%>
                        <div class="input-field">
                            <label for="publicationYear">
                                <spring:message code="add.publication.book.year"/>
                            </label>
                            <select id="publicationYear" name="publicationYear">
                                <c:forEach var="year" begin="1900" end="${currentYear}">
                                    <option value="${year}" ${bookForm.publicationYear == year ? 'selected' : ''}>${year}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <%--ISBN--%>

                        <div style="margin-bottom: 10px">
                            <label class="form-group">
                                <spring:message code="add.publication.isbn"/>
                                <form:input path="isbn" type="text" class="uk-input"/>
                                <small class="description">
                                    <spring:message code="add.publication.description.isbn"/>
                                </small>
                            </label>
                            <form:errors path="isbn" element="p" cssStyle="color: red;"/>

                        </div>

                        <%--Editorial--%>

                        <div>
                            <label class="form-group">
                                <spring:message code="add.publication.editorial"/>
                                <form:input path="editorial" type="text" class="uk-input"/>
                            </label>
                            <form:errors path="editorial" element="p" cssStyle="color: red;"/>
                        </div>

                        <%--Descripción--%>

                        <div style="margin-bottom: 10px">
                            <label class="form-group">
                                <spring:message code="add.publication.description"/>
                                <form:textarea path="description" class="uk-textarea uk-height-small"/>
                            </label>
                            <form:errors path="description" element="p" cssStyle="color: red;"/>
                            <small class="description">
                                <spring:message code="add.publication.description.description"/>
                            </small>
                        </div>

                        <%--Edicion--%>

                        <div  style="margin-bottom: 10px">
                            <label class="form-group">
                                <spring:message code="add.publication.edition"/>
                                <form:input id="edition" autocomplete="false" placeholder="1" path="edition" type="text" class="uk-input"/>
                            </label>
                            <form:errors path="edition" element="p" cssStyle="color: red;"/>
                            <small class="description">
                                <spring:message code="add.publication.description.edition"/>
                            </small>
                        </div>

                        <%--Pages--%>

                        <div>
                            <label class="form-group">
                                <spring:message code="add.book.pages"/>
                                <form:input path="pages" type="text" class="uk-input" placeholder="300"/>
                            </label>
                            <form:errors path="pages" element="p" cssStyle="color: red;"/>
                            <small class="description">
                                <spring:message code="add.book.description.pages"/>
                            </small>
                        </div>

                        <%--Dimension--%>

                        <div class="form-group">
                            <form:label path="dimension">
                                <spring:message code="add.publication.book.dimension"/>
                            </form:label>
                            <form:select path="dimension" class="uk-input">
                                <c:forEach var="dimensionWrapper" items="${dimensions}">
                                    <form:option value="${dimensionWrapper.dimension}" label="${dimensionWrapper.displayName}" />
                                </c:forEach>
                            </form:select>
                        </div>
                        <%--Weight--%>

                        <div>
                            <label class="form-group">
                                <spring:message code="add.book.weight"/>
                                <form:input path="weight" type="text" class="uk-input" placeholder="300"/>
                            </label>
                            <form:errors path="weight" element="p" cssStyle="color: red;"/>
                            <small class="description">
                                <spring:message code="add.book.description.weight"/>
                            </small>
                        </div>

                        <%--Ckeckboxes--%>

                        <div class="uk-margin uk-grid-small uk-child-width-auto uk-grid">
                            <!-- Pocket Edition -->
                            <label class="mr-1">
                                <spring:message code="add.book.pocket"/>
                                <form:checkbox path="isPocketEdition" class="uk-checkbox" />
                            </label>

                            <!-- Hardcover -->
                            <label class="mr-1">
                                <spring:message code="add.book.hardcover"/>
                                <form:checkbox path="isHardcover" class="uk-checkbox" />
                            </label>
                        </div>

                        <div class="uk-container uk-margin-top">
                            <div class="uk-inline">
                                <div class="uk-position-right">
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

<script type="text/javascript">

    var authors = ${bookForm.authors};

    let authorIndex = authors.length;

    function addAuthorField() {


        let container = document.getElementById("author-container");
        let newField = document.createElement("div");

        newField.innerHTML = `	<input type="text" name="authors[${authorIndex}]" class="uk-input"/>
									<button class ="uk-button uk-button-danger uk-button-small" type="button" onclick="removeAuthorField(this)"><span uk-icon="icon: trash"></span></button>`



        container.appendChild(newField);
        authorIndex++;
    }


    function removeAuthorField(button) {

        var container = document.getElementById("author-container");
        container.removeChild(button.parentNode);
        authorIndex--;
    }


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
</body>

</html>
