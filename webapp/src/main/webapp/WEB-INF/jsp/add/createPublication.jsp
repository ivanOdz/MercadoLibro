<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>

<head>

    <title><spring:message code="add.publication.header"/></title>
	<meta charset="UTF-8">
	
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

	<style>

		body {

			font-family: Arial, sans-serif;
			margin: 0;
			padding: 0;
			background-color: #ebe3d5;
		}

		.form-container {

			display: flex;
			flex-direction: column;
			align-items: center;
			max-width: 600px;
			margin: auto;
			padding: 20px;
			background-color: #f8f8e2;
			box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);
		}

		.form-group label {

			width: 100%;
			display: flex;
			flex-direction: column;
			margin-bottom: 15px;
			width: 100%;
			color: #333;
		}

		.form-input {

			width: 100%;
			margin-bottom: 12px;
			padding: 5px;
			border: 2px solid #ccc;
			border-radius: 4px;
		}

		.form-container button[type="submit"] {

			font-size: 18px;
			padding: 10px 20px;
			border: none;
			background-color: #007bff;
			color: white;
			border-radius: 5px;
			margin-top: 20px;
			cursor: pointer;
			transition: background-color 0.3s;
		 }

		.form-container button[type="submit"]:hover {

			background-color: #0056b3;
		}

		.form-container .add-author-button:hover {

			background-color: #218838;
		}

		.form-group button:hover {

			background-color: #0056b3;
		}

		.form-group .form-errors {
			color: red;
			font-size: 14px;
			margin-top: 5px;
		}

		#author-container div {

			margin-bottom: 10px;
		}

		#author-container input {

			display: inline-block;
			width: calc(100% - 110px);
		}

	</style>

</head>

<body>

<c:url var="postUrl" value="/createPublication"/>

<div class="form-container">

	<h1 class="label"><spring:message code="add.publication.header.title"/></h1>
	<c:set var="isReadOnly" value="${not empty username}" />


	<form:form modelAttribute="publicationForm" action="${postUrl}" method="post" enctype="multipart/form-data">

		<div>
			<label class="form-group">
				<spring:message code="add.publication.username"/>
				<form:input path="username" type="text" class="form-input"
							value="${username}"
							readonly="${isReadOnly}" />
			</label>
			<form:errors path="username" element="p" cssStyle="color: red;"/>
		</div>
		<div>
			<label class="form-group">
				<spring:message code="add.publication.mail"/>
				<form:input path="mail" type="text" class="form-input"
							value="${submited_mail}" readonly="true" />
			</label>
			<form:errors path="mail" element="p" cssStyle="color: red;"/>
		</div>

		<div>
			<label class="form-group">
				<spring:message code="add.publication.isbn"/>
				<form:input path="isbn" type="text" class="form-input"/>
			</label>
			<form:errors path="isbn" element="p" cssStyle="color: red;"/>
		</div>

		<div>
			<label class="form-group">
				<spring:message code="add.publication.title"/>
				<form:input path="title" type="text" class="form-input"/>
			</label>
			<form:errors path="title" element="p" cssStyle="color: red;"/>
		</div>

		<div id="author-container">
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

		<div class="form-container">
			<button type="button" onclick="addAuthorField()"><spring:message code="add.publication.add.author"/></button>
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

		<div>
			<label class="form-group">
				<spring:message code="add.publication.description"/>
				<form:input path="description" type="text" class="form-input"/>
			</label>
			<form:errors path="description" element="p" cssStyle="color: red;"/>
		</div>

		<div>
			<label class="form-group">
				<spring:message code="add.publication.edition"/>
				<form:input path="edition" type="text" class="form-input"/>
			</label>
			<form:errors path="edition" element="p" cssStyle="color: red;"/>
		</div>

		<div>
			<label class="form-group">
				<spring:message code="add.publication.rating"/>
				<form:input path="rating" type="text" class="form-input"/>
			</label>
			<form:errors path="rating" element="p" cssStyle="color: red;"/>
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
			<input type="hidden" name="publicationId" value="${publicationId}">
			<input type="hidden" name="isForExchange" value="${isForExchange}">
			<input type="hidden" name="		submited_mail" value="${submited_mail}">
			<button type="submit"><spring:message code="add.publication.submit"/></button>
		</div>

	</form:form>

</div>

</body>

</html>
