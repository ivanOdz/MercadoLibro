<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <title><spring:message code="form.title"/></title>
    
    <style type="text/css">
    
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
	
	.form-container button[type="submit"] {
	
		font-size: 18px;
		padding: 10px 20px;
		border: none;
		background-color: #aaaaaa;
		color: white;
		border-radius: 5px;
		margin-top: 20px;
		cursor: pointer;
		transition: background-color 0.3s;
	 }
 
    </style>
</head>
<body>

<div class="form-container">

	<form action="submitmail" method="post">
	    <label for="email"><spring:message code="form.label.email"/></label>
	    <input type="email" id="email" name="email" required>
	    <input type="hidden" name="publicationId" value="${publicationId}">
	</form>
	<button type="submit"><spring:message code="form.button.submit"/></button>
</div>
</body>
</html>
