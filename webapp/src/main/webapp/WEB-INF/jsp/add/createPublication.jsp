<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>

<head>

    <title><spring:message code="add.publication.header"/></title>
    
    <script type="text/javascript">
		
		let authorIndex = 0;
	
		function addAuthorField() {
			
			let container = document.getElementById("author-container");
			let newField = document.createElement("div");
			
			newField.innerHTML = `	<input type="text" name="authors[${authorIndex}]" class="form-input"/>
									<button type="button" onclick="removeAuthorField(this)">Eliminar</button>	`;
			
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

	<div>
		
		<form:form class="form-container" modelAttribute="publicationForm" action="${postUrl}" method="post">
		
			<h1 class="label">Publicar un nuevo libro</h1>
			
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
		        </label>
		        
	            <c:forEach var="author" items="${authors}" varStatus="status">
		            <div>
		            	<input type="text" name="authors[${status.index}]" value="${author}" class="form-input"/>
		            	<c:if test="${status.index > 1}">
		            		<button type="button" onclick="removeAuthorField(this)">Eliminar</button>
		            	</c:if>
		            </div>
	            </c:forEach>
		        
		    </div>
		    
		    <div class="form-group">
	       		<button type="button" onclick="addAuthorField()">Añadir Autor</button>
	    	</div>
	    	
		    <div>
		    	<form:label path="genre">Género:</form:label>
		    	<form:select class="form-group" path="genre">
		    		<c:forEach var="genreWrapper" items="${genres}">
		    			<form:option value="${genreWrapper.genre}" label="${genreWrapper.displayName}" />
		    		</c:forEach>
		    	</form:select>
		    </div>
		    
		    <div>
		    	<form:label path="genre">Estado del Libro:</form:label>
		    	<form:select class="form-group" path="bookState">
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
		        <label  class="form-group">
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
		    
			<div  class="form-group">
				<button type="submit">Publicar</button>
			</div>
		
		</form:form>
		
	</div>
	
</body>

</html>
