<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>

<link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>
<body>

<c:url value="/change_password_solicited" var="changePasswordUrl"/>
<form action="${changePasswordUrl}" method="post">
  <div>
    <label>
      <spring:message code="hwc.create.mail"/>
      <input type="text" name="email"/>
    </label>
  </div>

  <div>
    <label>
      <input type="submit" value="<spring:message code="hwc.change_password.confirm"/>"/>
    </label>
  </div>
</form>

</body>
</html>
