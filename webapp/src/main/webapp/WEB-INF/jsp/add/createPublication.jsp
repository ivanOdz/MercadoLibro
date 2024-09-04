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
	
		body {
		
			font-family: Arial, sans-serif;
			margin: 0;
			padding: 0;
			background-color: #f8f9fa;
		}
		
		.form-container {
		
			display: flex;
			flex-direction: column;
			align-items: center;
			max-width: 600px;
			margin: auto;
			padding: 20px;
			background-color: #fff;
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

	<div>
		
		<form:form class="form-container" modelAttribute="publicationForm" action="${postUrl}" method="post">
		
			<h1 class="label">Publicar un nuevo libro</h1>
			
		    <div class="form-group">
			    <label>
			        <spring:message code="add.publication.username"/>
			    </label>
		    	<form:input path="username" type="text" class="form-input"/>
		        <form:errors path="username" element="div" class="form-errors"/>
		    </div>
		
		    <div class="form-group">
		        <label>
		            <spring:message code="add.publication.mail"/>
		        </label>
		        <form:input path="mail" type="text" class="form-input"/>
		        <form:errors path="mail" element="div" class="form-errors"/>
		    </div>
		    
		    <div class="form-group">
		        <label>
		            <spring:message code="add.publication.isbn"/>
		        </label>
		        <form:input path="isbn" type="text" class="form-input"/>
		        <form:errors path="isbn" element="div" class="form-errors"/>
		    </div>
		
		    <div class="form-group">
		        <label>
		            <spring:message code="add.publication.title"/>
		        </label>
		        <form:input path="title" type="text" class="form-input"/>
		        <form:errors path="title" element="div" class="form-errors"/>
		    </div>
		
		    <div id="author-container" class="form-group">
		    
		        <label>
		            <spring:message code="add.publication.authors"/>
		        </label>
		        
	            <c:forEach var="author" items="${authors}" varStatus="status">
	            	<input type="text" name="authors[${status.index}]" value="${author}" class="form-input"/>
	            	<c:if test="${status.index > 1}">
	            		<button type="button" onclick="removeAuthorField(this)">Eliminar</button>
	            	</c:if>
	            </c:forEach>
		    </div>
		    
		    <div class="form-group">
	       		<button type="button" onclick="addAuthorField()">Añadir Autor</button>
	    	</div>
	    	
		    <div class="form-group">
		    	<form:label path="genre">Género:</form:label>
		    	<form:select path="genre" class="form-input">
		    		<c:forEach var="genreWrapper" items="${genres}">
		    			<form:option value="${genreWrapper.genre}" label="${genreWrapper.displayName}"/>
		    		</c:forEach>
		    	</form:select>
		    </div>
		    
		    <div class="form-group">
		    	<form:label path="genre">Estado del Libro:</form:label>
		    	<form:select path="bookState" class="form-input">
		    		<c:forEach var="bookStateWrapper" items="${bookStates}">
		    			<form:option value="${bookStateWrapper.bookState}" label="${bookStateWrapper.displayName}" />
		    		</c:forEach>
		    	</form:select>
		    </div>
		    
		    <div class="form-group">
		        <label>
		            <spring:message code="add.publication.editorial"/>
		        </label>
		        <form:input path="editorial" type="text" class="form-input"/>
		        <form:errors path="editorial" element="div" class="form-errors"/>
		    </div>
		
		    <div class="form-group">
		        <label>
		            <spring:message code="add.publication.description"/>
		        </label>
		        <form:input path="description" type="text" class="form-input"/>
		        <form:errors path="description" element="div" class="form-errors"/>
		    </div>
		
		    <div class="form-group">
		        <label>
		            <spring:message code="add.publication.edition"/>
		        </label>
		        <form:input path="edition" type="text" class="form-input"/>
		        <form:errors path="edition" element="div" class="form-errors"/>
		    </div>
		
		    <div class="form-group">
		        <label>
		            <spring:message code="add.publication.rating"/>
		        </label>
		        <form:input path="rating" type="text" class="form-input"/>
		        <form:errors path="rating" element="div" class="form-errors"/>
		    </div>
		
		    <div class="form-group">
		        <label>
		            <spring:message code="add.publication.image"/>
		        </label>
		        <form:input path="image" type="text" class="form-input"/>
		        <form:errors path="image" element="div" class="form-errors"/>
		    </div>
		
		    <div class="form-group">
		        <label>
		        	<spring:message code="add.publication.location"/>
		        </label>
	            <form:input path="location" type="text" class="form-input"/>
		        <form:errors path="location" element="div" class="form-errors"/>
		    </div>
		    
			<div class="form-group">
				<button type="submit">Publicar</button>
			</div>
		
		</form:form>
		
	</div>
	
</body>

</html>
