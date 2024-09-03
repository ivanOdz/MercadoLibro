<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <title><spring:message code="form.title"/></title>
</head>
<body>
<form action="submitmail" method="post">
    <label for="email"><spring:message code="form.label.email"/></label>
    <input type="email" id="email" name="email" required>
    <input type="hidden" name="publicationId" value="${publicationId}">
    <button type="submit"><spring:message code="form.button.submit"/></button>
</form>
</body>
</html>
