<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>

	<head>
	    <meta charset="UTF-8">
	    <title><spring:message code="hwc.login.title"/></title>
	    
	    <style>
	    
	        body {
	            font-family: Arial, sans-serif;
	            background-color: #f4f4f4;
	            display: flex;
	            justify-content: center;
	            align-items: center;
	            margin-top: 20px;
	        }
	        
	        .container {
	            background: #fff;
	            padding: 20px;
	            border-radius: 8px;
	            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	            width: 100%;
	            max-width: 400px;
	        }
	        
	        h2 {
	            text-align: center;
	            color: #333;
	            margin-bottom: 40px;
	        }
	        
	        .form-group {
	            margin-bottom: 15px;
	        }
	        
	        label {
	            display: block;
	            margin-bottom: 5px;
	            font-weight: bold;
	        }
	        
	        .form-group .checkbox-label {
                display: inline-flex;
                align-items: flex-start;
            }
            
	        input[type="text"],
	        input[type="password"],
	        input[type="checkbox"] {
	            width: calc(100% - 20px);
	            padding: 10px;
	            margin: 4px;
	            border: 1px solid #ccc;
	            border-radius: 4px;
	        }
	        
	        input[type="submit"] {
	            width: 100%;
	            padding: 10px;
	            border: none;
	            border-radius: 4px;
	            background-color: #007bff;
	            color: #fff;
	            font-size: 16px;
	            cursor: pointer;
	        }
	        
	        input[type="submit"]:hover {
	            background-color: #0056b3;
	        }
	        
	        .signup {
	            text-align: center;
	            margin-top: 20px;
	        }
	        
	        .signup button {
	            padding: 10px 20px;
	            border: none;
	            border-radius: 4px;
	            background-color: #28a745;
	            color: #fff;
	            font-size: 16px;
	            cursor: pointer;
	        }
	        
	        .signup button:hover {
	            background-color: #218838;
	        }
	        
	    </style>
	   
	</head>
	
	<body>
	
	    <div class="container">
	        <h2><spring:message code="hwc.login.title"/></h2>
	        <form action="${loginUrl}" method="post">
	            <div class="form-group">
	                <label>
	                    <spring:message code="hwc.login.username"/>
	                    <input type="text" name="username"/>
	                </label>
	            </div>
	            <div class="form-group">
	                <label>
	                    <spring:message code="hwc.login.password"/>
	                    <input type="password" name="password"/>
	                </label>
	            </div>
	            <div class="form-group">
	                <label class="checkbox-label">
	                	<spring:message code="hwc.login.remember_me"/>
	                    <input type="checkbox" name="remember_me"/>
	                </label>
	            </div>
	            <div class="form-group">
	                <input type="submit" value="<spring:message code='hwc.login.submit'/>"/>
	            </div>
	        </form>
	        <div class="signup">
	            <p><spring:message code="hwc.signup.prompt"/>:</p>
	            <a href="${signUpUrl}">
	                <button type="button">
	                    <spring:message code="hwc.signup.button"/>
	                </button>
	            </a>
	        </div>
	    </div>
	    
	</body>

</html>