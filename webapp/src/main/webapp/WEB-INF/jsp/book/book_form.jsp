<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>

<html>

<%@include file="/WEB-INF/jsp/head/headers.jsp" %>

<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/book_form.css" rel="stylesheet"/>

    <title><spring:message code="add.book"/></title>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            var step = ${step};
            var tabs = UIkit.tab('.uk-tab');
            tabs.show(step - 1);
        });

        function nextSection(index) {
            var tabs = UIkit.tab('.uk-tab');
            tabs.show(index);
        }
    </script>

</head>

<body>

<navbar/>

<c:url var="postUrl" value="/book/create_book"/>

<div class="uk-background-muted" style="margin-bottom: 2%;">
    <div class="uk-container uk-margin-bottom" style="margin-top: 1%">
        <a class="uk-button uk-button-text" href="${pageContext.request.contextPath}/book/book_models">
            <span uk-icon="icon:  chevron-left"></span>
            <spring:message code="add.book.return_home"/>
        </a>
    </div>
    <form:form modelAttribute="bookDetailsForm" action="${postUrl}" method="post" enctype="multipart/form-data">
        <div class="uk-container">
            <ul uk-tab>
                <li class="uk-disabled"><a href="#"><spring:message code="add.book.step1"/></a></li>
                <li><a href="#"><spring:message code="add.book.step2"/></a></li>
            </ul>
            <div class="uk-switcher uk-margin">
                    <%---------------------------------------------------------------- STEP 1 ----------------------------------------------------------------%>
                <div id="step1">
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

                    <div style="text-align: center; font-weight: bold; font-size: 24px;">
                        <c:out value="${book_model.title}"/>
                    </div>

                        <%--BookState--%>

                    <div class="form-group uk-margin-top uk-margin-bottom">
                        <form:label path="bookState">
                            <spring:message code="add.publication.book.state"/>
                        </form:label>
                        <form:select path="bookState" class="uk-input">
                            <c:forEach var="bookStateWrapper" items="${bookStates}">
                                <form:option value="${bookStateWrapper.bookState}"
                                             label="${bookStateWrapper.displayName}"/>
                            </c:forEach>
                        </form:select>
                    </div>

                        <%--Rating--%>

                    <div class="form-group uk-margin-top uk-margin-bottom">
                        <label>
                            <spring:message code="add.publication.rating"/>
                        </label>

                        <div class="star-rating">
                            <form:radiobutton path="rating" value="5" id="star5"/>
                            <label for="star5" title="5 stars">
                                <span uk-icon="icon: star; ratio: 1.5"></span>
                            </label>

                            <form:radiobutton path="rating" value="4" id="star4"/>
                            <label for="star4" title="4 stars">
                                <span uk-icon="icon: star; ratio: 1.5"></span>
                            </label>

                            <form:radiobutton path="rating" value="3" id="star3"/>
                            <label for="star3" title="3 stars">
                                <span uk-icon="icon: star; ratio: 1.5"></span>
                            </label>

                            <form:radiobutton path="rating" value="2" id="star2"/>
                            <label for="star2" title="2 stars">
                                <span uk-icon="icon: star; ratio: 1.5"></span>
                            </label>

                            <form:radiobutton path="rating" value="1" id="star1"/>
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

                    <!-- Location -->
                    <input type="hidden" id="location-error-message" value="<spring:message code='NotBlank.bookForm.location'/>" />

                    <div id="location-q" class="uk-inline" style="display: none;">
                        <label class="form-group">
                            <spring:message code="book.set.location"/>
                            <form:input path="location" type="text" class="uk-input" />
                        </label>
                        <form:errors path="location" element="p" cssStyle="color: red;" />
                        <p id="location-error" style="color: red;"></p>
                    </div>



                    <div style="margin-top: 2%; align-self: auto;">
                        <input type="hidden" name="book_model_id" value="${book_model_id}">
                        <button class="uk-button uk-button-primary" type="submit"><spring:message
                                code="add.publication.upload"/></button>
                    </div>
                </div>
                    <%---------------------------------------------------------------- END STEP 2 ----------------------------------------------------------------%>
            </div>
        </div>
    </form:form>
</div>

<script type="text/javascript">

    document.addEventListener('DOMContentLoaded', function () {
        var previewContainer = document.getElementById('image-preview-container');
        var fileInput = document.getElementById('file-input');
        var coverInput = document.getElementById('cover-input');

        fileInput.addEventListener('change', function (event) {
            const files = event.target.files;

            previewContainer.innerHTML = '';

            for (const file of files) {
                const reader = new FileReader();
                reader.onload = function (e) {
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

                    deleteButton.addEventListener('click', function () {
                        imgWrapper.remove();
                    });

                    const selectCoverButton = document.createElement('button');
                    selectCoverButton.classList.add('uk-button', 'uk-button-primary', 'uk-button-small');
                    selectCoverButton.innerText = 'Select as cover';
                    selectCoverButton.type = 'button';

                    selectCoverButton.addEventListener('click', function () {
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

    document.addEventListener('DOMContentLoaded', function () {
        const publishRadioButtons = document.getElementsByName('publish');
        const locationQuestion = document.getElementById('location-q');

        function toggleLocation() {
            const isChecked = document.querySelector('input[name="publish"]:checked').value === 'true';
            locationQuestion.style.display = isChecked ? 'block' : 'none';
        }

        publishRadioButtons.forEach(function (radio) {
            radio.addEventListener('change', toggleLocation);
        });

        toggleLocation();
    });

    document.addEventListener('DOMContentLoaded', function () {
        const yesRadio = document.querySelector('input[name="publish"][value="true"]');
        const noRadio = document.querySelector('input[name="publish"][value="false"]');
        const locationInputDiv = document.getElementById('location-q');
        const locationField = document.querySelector('input[name="location"]');
        const locationError = document.getElementById('location-error');

        yesRadio.addEventListener('change', function () {
            if (this.checked) {
                locationInputDiv.style.display = 'block';
            }
        });

        noRadio.addEventListener('change', function () {
            if (this.checked) {
                locationInputDiv.style.display = 'none';
                locationField.value = '';
                locationError.innerText = '';
            }
        });

        document.querySelector('form').addEventListener('submit', function (e) {
            if (yesRadio.checked && !locationField.value.trim()) {
                e.preventDefault();
                locationError.innerText = 'Location is required when publishing.';
            } else {
                locationError.innerText = '';
            }
        });
    });


</script>


</body>
</html>