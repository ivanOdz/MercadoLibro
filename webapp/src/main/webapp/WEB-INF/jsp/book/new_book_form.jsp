<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>

<html>

<%@include file="/WEB-INF/jsp/head/headers.jsp"%>

<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/book_form.css" rel="stylesheet"/>

    <title><spring:message code="add.book"/></title>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            var step = ${step};
            var tabs = UIkit.tab('.uk-tab');
            tabs.show(step-1);
        });

        function nextSection(index) {
            var tabs = UIkit.tab('.uk-tab');
            tabs.show(index);
        }
    </script>

</head>

<body>

<navbar/>

<c:url var="postUrl" value="/book/create_new_book"/>

<div class="uk-background-muted" style="margin-bottom: 2%;">
    <div class="uk-container uk-margin-bottom" style="margin-top: 1%">
        <a class="uk-button uk-button-text" href="${pageContext.request.contextPath}/book">
            <span uk-icon="icon:  chevron-left"></span>
            <spring:message code="add.book.return_home"/>
        </a>
    </div>
    <form:form modelAttribute="bookForm" action="${postUrl}" method="post" enctype="multipart/form-data">
    <div class="uk-container">
        <ul uk-tab>
            <li><a href="#"><spring:message code="add.book.step1"/></a></li>
            <li><a href="#"><spring:message code="add.book.step2"/></a></li>
        </ul>
        <div class="uk-switcher uk-margin">
            <%---------------------------------------------------------------- STEP 1 ----------------------------------------------------------------%>

            <div id="step1">
                    <div class="uk-container">
                        <h2 class="uk-heading-line">
                            <spring:message code="add.book.title"/>
                        </h2>
                        <h4 class="uk-article-meta">
                            <spring:message code="add.book.description"/>
                        </h4>
                    </div>

                    <hr style="width: 55lh;"/>


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
                            <c:forEach var="author" items="${bookForm.authors}" varStatus="status">
                                <input class="uk-input" type="text" name="authors[${status.index}]" value="${author}"/>
                                <c:if test="${status.index > 0}">
                                    <button class="uk-button" type="button" onclick="removeAuthorField(this)">
                                        <span uk-icon="icon: close"></span>
                                    </button>
                                </c:if>
                                <c:if test="${status.index == 0}">
                                    <form:errors path="authors" element="p" cssStyle="color: red;"/>
                                </c:if>
                            </c:forEach>
                        </label>
                    </div>

                    <div class="form-container uk-margin-top uk-margin-bottom" style="margin-bottom: 10px">
                        <button class="uk-button uk-margin-right" type="button" onclick="addAuthorField()"><spring:message code="add.publication.add.author"/></button>
                        <small class="description">
                            <spring:message code="add.publication.description.authors"/>
                        </small>
                        <form:errors path="authors" element="p" cssStyle="color: red;"/>
                    </div>

                        <%--Género--%>

                    <div class="form-group uk-margin-top uk-margin-bottom">
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

                    <div class="form-group uk-margin-top uk-margin-bottom">
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
                    <div class="form-group uk-margin-top uk-margin-bottom">
                        <label for="publicationYear">
                            <spring:message code="add.publication.book.year"/>
                        </label>
                        <select id="publicationYear" name="publicationYear">
                            <c:forEach var="year" begin="1800" end="${currentYear}">
                                <option value="${year}" ${bookForm.publicationYear == year ? 'selected' : ''}>${year}</option>
                            </c:forEach>
                        </select>
                    </div>

                        <%--ISBN--%>

                    <div class="uk-margin-top uk-margin-bottom" style="margin-bottom: 10px">
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

                    <div class="uk-margin-top uk-margin-bottom">
                        <label class="form-group">
                            <spring:message code="add.publication.editorial"/>
                            <form:input path="editorial" type="text" class="uk-input"/>
                        </label>
                        <form:errors path="editorial" element="p" cssStyle="color: red;"/>
                    </div>

                        <%--Descripción--%>

                    <div class="uk-margin-top uk-margin-bottom" style="margin-bottom: 10px">
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

                    <div class="uk-margin-top uk-margin-bottom" style="margin-bottom: 10px">
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

                    <div class="uk-margin-top uk-margin-bottom">
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

                    <div class="form-group uk-margin-top uk-margin-bottom">
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

                    <div class="uk-margin-top uk-margin-bottom">
                        <label class="form-group">
                            <spring:message code="add.book.weight"/>
                            <form:input path="weight" type="text" class="uk-input" placeholder="300"/>
                        </label>
                        <form:errors path="weight" element="p" cssStyle="color: red;"/>
                        <small class="description">
                            <spring:message code="add.book.description.weight"/>
                        </small>
                    </div>

                        <%--Checkboxes--%>

                    <div class="uk-margin uk-grid-small uk-child-width-auto uk-grid">
                        <!-- Pocket Edition -->
                        <label class="mr-1">
                            <spring:message code="add.book.pocket"/>
                            <form:checkbox path="isPocketEdition" value="true" class="uk-checkbox" />
                        </label>

                        <!-- Hardcover -->
                        <label class="mr-1">
                            <spring:message code="add.book.hardcover"/>
                            <form:checkbox path="isHardcover" value="true" class="uk-checkbox" />
                        </label>
                    </div>

                    <%--Button--%>
                    <button class="uk-button uk-button-primary" type="button" onclick="nextSection(1)"><spring:message code="add.publication.next"/></button>
            </div>
                <%---------------------------------------------------------------- END STEP 1 ----------------------------------------------------------------%>


                <%---------------------------------------------------------------- STEP 2 ----------------------------------------------------------------%>
            <div id="step2">
                    <div class="uk-container">
                        <h2 class="uk-heading-line">
                            <spring:message code="add.book.title2"/>
                        </h2>
                        <h4 class="uk-article-meta">
                            <spring:message code="add.book.description2"/>
                        </h4>
                    </div>

                    <hr style="width: 55lh;"/>

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
                        <form:errors path="rating" element="p" cssStyle="color: red;"/>
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

<%--                <input type="hidden" id="cover-input" name="bookCover">--%>

                <!-- Publish -->
                <div class="uk-margin uk-grid-small uk-child-width-auto uk-grid" style="margin-top: 2%;">
                    <label>
                        <spring:message code="add.publication"/>
                    </label>
                    <label class="mr-1">
                        <spring:message code="yes"/>
                        <form:radiobutton path="publish" value="true" class="uk-radiobutton" name="publish"/>
                    </label>

                    <label class="mr-1">
                        <spring:message code="no"/>
                        <form:radiobutton path="publish" value="false" class="uk-radiobutton" name="publish"/>
                    </label>
                </div>

                <div id="location-q" class="uk-inline" style="display: none;">
                    <label class="form-group">
                        <spring:message code="book.set.location"/>
                        <form:input path="location" type="text" class="uk-input"/>
                    </label>
                </div>

                <div style="margin-top: 2%; align-self: auto;">
                    <button class="uk-button uk-button-primary" type="submit"><spring:message code="add.publication.upload"/></button>
                </div>
            </div>
                <%---------------------------------------------------------------- END STEP 2 ----------------------------------------------------------------%>
        </div>
    </div>
    </form:form>
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
        var coverInput = document.getElementById('cover-input');

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

                    const selectCoverButton = document.createElement('button');
                    selectCoverButton.classList.add('uk-button', 'uk-button-primary', 'uk-button-small');
                    selectCoverButton.innerText = 'Select as cover';
                    selectCoverButton.type = 'button';

                    selectCoverButton.addEventListener('click', function() {
                        document.querySelectorAll('.image-wrapper').forEach(wrapper => {
                            wrapper.style.border = 'none';
                        });
                        imgWrapper.style.border = '3px solid blue';

                        coverInput.value = index;
                    });

                    imgWrapper.appendChild(img);
                    imgWrapper.appendChild(selectCoverButton);
                    imgWrapper.appendChild(deleteButton);
                    previewContainer.appendChild(imgWrapper);
                };
                reader.readAsDataURL(file);
            }
        });
    });

    document.addEventListener('DOMContentLoaded', function() {
        const publishRadioButtons = document.getElementsByName('publish');
        const locationQuestion = document.getElementById('location-q');

        // Función para mostrar u ocultar el campo "Location"
        function toggleLocation() {
            const isChecked = document.querySelector('input[name="publish"]:checked').value === 'true';
            locationQuestion.style.display = isChecked ? 'block' : 'none';
        }

        // Añade listeners a los radiobuttons
        publishRadioButtons.forEach(function(radio) {
            radio.addEventListener('change', toggleLocation);
        });

        // Llama a la función una vez al cargar la página
        toggleLocation();
    });



</script>


</body>
</html>