<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
  <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet"/>
  <link href="${pageContext.request.contextPath}/css/background.css" rel="stylesheet"/>

  <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

  <title><spring:message code="new.password.title"/></title>

</head>
<body>
<c:url value="/change_password_solicited" var="changePasswordUrl"/>
<div style="display: flex; place-items: center; height: 100%;">
<div class="uk-container container">
  <h2 style="justify-content: center; display: flex; place-items: center;"><spring:message code="new.password.title"/></h2>
  <h4><spring:message code="new.password.description"/></h4>
  <div style="justify-content: center;">
  <form action="${changePasswordUrl}" method="post" class="uk-grid-large uk-grid" style="justify-content: center;">
    <div class="uk-margin" style="justify-content: center">

      <div class="uk-width-1-1 uk-margin-top">
        <div>
          <label>
            <spring:message code="hwc.create.mail"/>
          </label>
        </div>
        <div class="uk-inline">
          <span class="uk-form-icon" uk-icon="icon: mail"></span>
          <input  class="uk-input" type="text" name="email" aria-label="Not clickable icon"/>
        </div>
        <form:errors path="email" element="p" cssStyle="color: red;"/>
      </div>

      <div class="uk-margin-top uk-button-group" style="margin-left: 50px;">
        <button class="uk-button uk-button-primary"> <spring:message code="hwc.change_password.confirm"/> </button>
      </div>

    </div>

  </form>
  </div>
</div>
</div>

</body>
</html>
