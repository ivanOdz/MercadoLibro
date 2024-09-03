<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>

<head>

    <title><spring:message code="add.publication.header"/></title>
    
    <script type="text/javascript">
		
		var authorIndex = 0;
	
		function addAuthorField() {
			
			var container = document.getElementById("author-container");
			var newField = document.createElement("div");
			
			newField.innerHTML = `	<input type="text" name="authors[${authorCount}]" />
									<button type="button" onclick="removeAuthorField(this)">Eliminar</button>	`;
			
			container.appendChild(newField);
			authorIndex++;
		}
		
		function removeAuthorField(button) {
			
			if (authorIndex >= 1)
			{
				var container = document.getElementById("author-container");
				container.removeChild(button.parentNode);
			}
		}
			
	</script>
	
	<style>

		.form-container {
		
			display: flex;
			flex-direction: column;
			align-items: center;
		}
    
		.form-group label {
		
			display: flex;
			align-items: center;
			margin-bottom: 15px;
			margin-top: 15px;
			width: 160px;
		}
		
		.form-input {
			
			display: flex;
			align-items: flex-end;
		}
		
		.form-container button[type="submit"] {
		
			font-size: 18px;
			padding: 10px 20px;
			border: none;
			background-color: #007bff;
			color: white;
			border-radius: 5px;
			cursor: pointer;
			transition: background-color 0.3s;
		 }
	
		.form-container button[type="submit"]:hover {
		
			background-color: #0056b3;
		}
	
	</style>
		
</head>

<body>

	<c:url var="postUrl" value="/createPublication"/>

	<div class="form-container">
	
		<h1 class="label">Publicar un nuevo libro</h1>
		
		<form:form modelAttribute="publicationForm" action="${postUrl}" method="post">
		
		    <div>
		    <label class="form-group">
		        <spring:message code="add.publication.username"/>
		        <form:input path="username" type="text" class="form-input"/>
		    </label>
		        <form:errors path="username" element="p" cssStyle="color: red;"/>
		    </div>
		
		    <div>
		        <label class="form-group">
		            <spring:message code="add.publication.mail"/>
		            <form:input path="mail" type="text" class="form-input"/>
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
		            <c:forEach var="i" begin="0" end="${book.authors.size()}">
			            <div>
			            	<form:input path="authors[${i}]"/>
			            	<button type="button" onclick="removeAuthorField(this)">Eliminar</button>
			            </div>
		            </c:forEach>
		        </label>
		    </div>
		    
			<div class="form-container">
		       <button type="button" onclick="addAuthorField()">Añadir Autor</button>
		    </div>
		    
		    <div class="form-group">
		    	<form:label path="genre">Género:</form:label>
		    		<form:select path="genre">
		    		<form:options items="${genres}" />
		    	</form:select>
		    </div>
		    
		   	<div class="form-group">
		    	<form:label path="bookState">Estado del libro:</form:label>
		    		<form:select path="bookState">
		    		<form:options items="${bookStates}" />
		    	</form:select>
		    </div>
		    <br>
		    
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
		            <form:input path="image" type="text" class="form-input"/>
		        </label>
		        <form:errors path="image" element="p" cssStyle="color: red;"/>
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
				<button type="submit">Publicar</button>
			</div>
		
		</form:form>
		
	</div>
	
</body>

</html>