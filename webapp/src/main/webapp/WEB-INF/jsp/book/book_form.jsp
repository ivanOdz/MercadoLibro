<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
    <%@include file="/WEB-INF/jsp/head/headers.jsp"%>
    <link href="${pageContext.request.contextPath}/css/book_form.css" rel="stylesheet"/>
    <link href="${pageContext.request.contextPath}/css/publications.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

</head>

<body>

<c:url var="exchangeUrl" value="/exchange"/>
<c:url var="booksUrl" value="/book"/>
<c:url var="profileUrl" value="/profile"/>
<c:url var="postUrl" value="/book/upload_book"/>

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
<%--                    <li><a class="pl-1 pr-1" href="<c:url value="${exchangeUrl}"/>"><spring:message code="home.exchange.view"/></a></li>--%>
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

<%--                    <li><a class="pl-1 pr-1" href="<c:url value="${profileUrl}"/>"><spring:message code="home.profile.view"/></a></li>--%>
                </ul>
            </div>
        </div>
    </div>
</nav>

<div class="uk-background-muted">
    <div class="uk-container">
        <div class="uk-grid ml-1 uk-margin-top" uk-grid>

            <div class="uk-section uk-align-center">
                <div class="uk-container uk-margin-top">
                    <form:form modelAttribute="bookForm" action="${postUrl}" method="post" enctype="multipart/form-data">

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
                                <c:forEach var="author" items="${bookForm.authors}" varStatus="status">
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

                        <%--BookState--%>

                        <div class="form-group">
                            <form:label path="genre">
                                <spring:message code="add.publication.book.state"/>
                            </form:label>
                            <form:select path="bookState" class="uk-input">
                                <c:forEach var="bookStateWrapper" items="${bookStates}">
                                    <form:option value="${bookStateWrapper.bookState}" label="${bookStateWrapper.displayName}" />
                                </c:forEach>
                            </form:select>
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

                        <%--Image--%>

                        <div id="image-container" class="uk-container uk-margin-top uk-margin-bottom">
                            <label class="form-group">
                                <spring:message code="add.publication.image"/>
                                <form:input path="imageFile" type="file" class="form-input"/>

                                <c:forEach var="image" items="${bookForm.imageFile}" varStatus="imageStatus">
                                    <input class="uk-input" type="file" name="imageFile[${imageStatus.index}]" value="${image}"/>
                                    <c:if test="${imageStatus.index > 0}">
                                        <button class="uk-button" type="button" onclick="removeImageField(this)">
                                            <span uk-icon="icon:  close"></span>
                                        </button>
                                    </c:if>
                                </c:forEach>
                            </label>
                        </div>

                        <div class="form-container" style="margin-bottom: 10px">
                            <button class="uk-button uk-margin-right" type="button" onclick="addImageField()"><spring:message code="add.publication.add.author"/></button>
                            <small class="description">
                                    <spring:message code="add.publication.image"/>
                            </small>
                        </div>



                        <div class="uk-container uk-margin-top">

                            <div class="uk-inline">

                                <div class="uk-position-right">
                                    <button type="submit" class="uk-button uk-button-default uk-background-primary uk-light uk-panel"><spring:message code="add.publication.submit"/></button>
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

<%--Script--%>

<script type="text/javascript">

    let authorIndex = authors.length;
    let imageIndex = imageFile.length;

    function addAuthorField() {

        let container = document.getElementById("author-container");
        let newField = document.createElement("div");

        newField.innerHTML = `	<input type="text" name="authors[${authorIndex}]" class="uk-input"/>
									<button type="button" onclick="removeAuthorField(this)"> X </button>	`;

        container.appendChild(newField);
        authorIndex++;
    }


    function removeAuthorField(button) {

        var container = document.getElementById("author-container");
        container.removeChild(button.parentNode);
        authorIndex--;
    }

    function addImageField() {

        let container = document.getElementById("image-container");
        let newField = document.createElement("div");

        newField.innerHTML = `	<input type="file" name="imageFile[${imageIndex}]" class="uk-input"/>
									<button type="button" onclick="removeImageField(this)"> X </button>	`;

        container.appendChild(newField);
        imageIndex++;
    }

    function removeImageField(button) {

        var container = document.getElementById("image-container");
        container.removeChild(button.parentNode);
        imageIndex--;
    }

</script>

</html>
