<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
  <link rel="icon" href="${pageContext.request.contextPath}/images/favicon.ico" type="image/x-icon"/>
  <meta charset="UTF-8">
</head>
<body>
<c:url var="okUrl" value="/login"/>

<div class="uk-background-center-center">
  <div class="uk-position-center uk-card uk-card-default uk-card-body uk-width-1-2@m">
    <h3 class="uk-card-title">
      <spring:message code="hwc.mail_input.message"/>
    </h3>
    <a href="${okUrl}">
      <button class="uk-button uk-button-primary uk-align-center">
        <spring:message code="hwc.registration.ok"/>
      </button>
    </a>
  </div>
</div>
</body>
</html>