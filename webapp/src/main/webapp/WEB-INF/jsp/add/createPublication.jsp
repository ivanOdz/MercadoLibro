<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>

    <title><spring:message code="add.publication.header"/></title>
	<meta charset="UTF-8">
	<link href="${pageContext.request.contextPath}/css/publicationForm.css" rel="stylesheet"/>
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
	<script type="text/javascript">

		let authorIndex = authors.lenght;

		function addAuthorField() {

			let container = document.getElementById("author-container");
			let newField = document.createElement("div");

			newField.innerHTML = `	<input type="text" name="authors[${authorIndex}]" class="form-input"/>
									<button type="button" onclick="removeAuthorField(this)"> X </button>	`;

			container.appendChild(newField);
			authorIndex++;
		}

		function removeAuthorField(button) {

			var container = document.getElementById("author-container");
			container.removeChild(button.parentNode);
			authorIndex--;
		}
	</script>

</head>

<body>

<c:url var="postUrl" value="/createpublication"/>

<div class="form-container">

	<h1 class="label"><spring:message code="add.publication.header.title"/></h1>
	<c:set var="usernameFieldIsReadOnly" value="${not empty username}" />
	<c:set var="emailFieldIsReadOnly" value="${not empty submited_mail}" />

<%--	<small class="description">--%>
<%--		<spring:message code="add.publication.description.required_fields"/>--%>
<%--	</small>--%>

	<form:form modelAttribute="publicationForm" action="${postUrl}" method="post" enctype="multipart/form-data">

		<h4 class="label"><spring:message code="add.publication.subheader.user"/></h4>

		<div>
			<label class="form-group">
				<spring:message code="add.publication.username"/>
				<form:input path="username" type="text" class="form-input"
							value="${username}"
							 />
			</label>
			<form:errors path="username" element="p" cssStyle="color: red;"/>
		</div>

		<div>
			<label class="form-group">
				<spring:message code="add.publication.mail"/>
				<form:input path="mail" type="text" class="form-input"
							value="${submited_mail}" readonly="${emailFieldIsReadOnly}" />
                <small class="description">
                    <spring:message code="add.publication.username_email.description"/>
                </small>
            </label>
			<form:errors path="mail" element="p" cssStyle="color: red;"/>
		</div>



		<h4 class="label"><spring:message code="add.publication.subheader.book"/></h4>

		<div style="margin-bottom: 10px">
			<label class="form-group">
				<spring:message code="add.publication.isbn"/>
				<form:input path="isbn" type="text" class="form-input"/>
				<small class="description">
					<spring:message code="add.publication.description.isbn"/>
				</small>
			</label>
			<form:errors path="isbn" element="p" cssStyle="color: red;"/>

		</div>



		<div  class="form-field">
			<label class="form-group">
				<spring:message code="add.publication.title"/>
				<form:input path="title" type="text" class="form-input"/>
			</label>
			<form:errors path="title" element="p" cssStyle="color: red;"/>
		</div>

		<div id="author-container" class="form-field">
			<label class="form-group">
				<spring:message code="add.publication.authors"/>
				<c:forEach var="author" items="${publicationForm.authors}" varStatus="status">
					<input type="text" name="authors[${status.index}]" value="${author}" class="form-input"/>
						<c:if test="${status.index > 0}">
							<button type="button" onclick="removeAuthorField(this)"> X </button>
						</c:if>
					</c:forEach>
			</label>
		</div>

		<div class="form-container" style="margin-bottom: 10px">
			<button type="button" onclick="addAuthorField()"><spring:message code="add.publication.add.author"/></button>
			<small class="description">
				<spring:message code="add.publication.description.authors"/>
			</small>
		</div>

	    <div class="form-group">
	    	<form:label path="genre">
	    		<spring:message code="add.publication.genre"/>
	    	</form:label>
	    	<form:select path="genre" class="form-input">
	    		<c:forEach var="genreWrapper" items="${genres}">
	    			<form:option value="${genreWrapper.genre}" label="${genreWrapper.displayName}"/>
	    		</c:forEach>
	    	</form:select>
	    </div>

	    <div class="form-group">
	    	<form:label path="genre">
	    		<spring:message code="add.publication.book.state"/>
	    	</form:label>
	    	<form:select path="bookState" class="form-input">
	    		<c:forEach var="bookStateWrapper" items="${bookStates}">
	    			<form:option value="${bookStateWrapper.bookState}" label="${bookStateWrapper.displayName}" />
	    		</c:forEach>
	    	</form:select>
	    </div>

		<div>
			<label class="form-group">
				<spring:message code="add.publication.editorial"/>
				<form:input path="editorial" type="text" class="form-input"/>
			</label>
			<form:errors path="editorial" element="p" cssStyle="color: red;"/>
		</div>

		<div style="margin-bottom: 10px">
			<label class="form-group">
				<spring:message code="add.publication.description"/>
				<form:input path="description" type="text" class="form-input"/>
			</label>
			<form:errors path="description" element="p" cssStyle="color: red;"/>
			<small class="description">
				<spring:message code="add.publication.description.description"/>
			</small>
		</div>

		<div  style="margin-bottom: 10px">
			<label class="form-group">
				<spring:message code="add.publication.edition"/>
				<form:input path="edition" type="text" class="form-input"/>
			</label>
			<form:errors path="edition" element="p" cssStyle="color: red;"/>
			<small class="description">
				<spring:message code="add.publication.description.edition"/>
			</small>
		</div>

		<div class="form-group">
			<label>
				<spring:message code="add.publication.rating"/>
			</label>

			<div class="star-rating">
				<form:radiobutton path="rating" value="5" id="star5" />
				<label for="star5" title="5 stars">
					<i class="material-icons">star</i>
				</label>

				<form:radiobutton path="rating" value="4" id="star4" />
				<label for="star4" title="4 stars">
					<i class="material-icons">star</i>
				</label>

				<form:radiobutton path="rating" value="3" id="star3" />
				<label for="star3" title="3 stars">
					<i class="material-icons">star</i>
				</label>

				<form:radiobutton path="rating" value="2" id="star2" />
				<label for="star2" title="2 stars">
					<i class="material-icons">star</i>
				</label>

				<form:radiobutton path="rating" value="1" id="star1" />
				<label for="star1" title="1 star">
					<i class="material-icons">star</i>
				</label>
			</div>

			<!-- Mostrar errores de validación -->
			<form:errors path="rating" element="p" cssStyle="color: red;" />
		</div>


		<div>
			<label class="form-group">
				<spring:message code="add.publication.image"/>
				<form:input path="imageFile" type="file" class="form-input"/>
			</label>
			<form:errors path="imageFile" element="p" cssStyle="color: red;"/>
		</div>

		<div>
			<label class="form-group">
				<spring:message code="add.publication.location"/>
				<form:input path="location" type="text" class="form-input"/>
			</label>
			<form:errors path="location" element="p" cssStyle="color: red;"/>
		</div>

		<br>

		<div class="form-container">
			<input type="hidden" name="publication_id" value="${publicationId}">
			<input type="hidden" name="is_for_exchange" value="${isForExchange}">
			<input type="hidden" name="submited_mail" value="${submited_mail}">
			<button type="submit"><spring:message code="add.publication.submit"/></button>
		</div>

	</form:form>

</div>

</body>

</html>
