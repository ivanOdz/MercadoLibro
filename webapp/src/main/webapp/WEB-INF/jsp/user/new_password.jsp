<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<%@include file="/WEB-INF/jsp/head/headers.jsp"%>
<head>
    <link href="<c:url value='/css/login.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/css/background.css' />" rel="stylesheet"/>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <title><spring:message code="new.password.title"/></title>

</head>
<body>
<c:url var="changePasswordUrl" value="/change_password"/>
<div style="display: flex; place-items: center; height: 100%;">
    <div class="uk-container container uk-align-center" style="text-align: center;">
        <h2><spring:message code="new.password.title2"/></h2>
        <h4><spring:message code="new.password.description2"/></h4>
        <div style="justify-content: center;">
        <form action="${changePasswordUrl}" modelAttribute="passwordForm" method="post" class="uk-grid-large uk-grid" style="justify-content: center;">
            <div class="uk-margin" style="justify-content: center">
                <div class="uk-width-1-1 uk-margin-top">
                    <div>
                    <label>
                        <spring:message code="hwc.create.new.password"/>
                    </label>
                    </div>
                    <div class="uk-inline">
                        <span class="uk-form-icon uk-form-icon-flip" uk-icon="icon: lock"></span>
                        <input class="uk-input" type="password" name="password" aria-label="Not clickable icon"/>
                    </div>
                </div>
                <div class="uk-width-1-1 uk-margin-top">
                    <div>
                        <label>
                            <spring:message code="hwc.create.confirm_password"/>
                        </label>
                    </div>
                    <div class="uk-inline">
                        <span class="uk-form-icon uk-form-icon-flip" uk-icon="icon: lock"></span>
                        <input class="uk-input" type="password" name="confirmPassword" aria-label="Not clickable icon"/>
                    </div>
                </div>

                <div>
                    <c:if test="${!empty passwordForm.password && !empty passwordForm.confirmPassword && passwordForm.password != passwordForm.confirmPassword}">
                            <span class="error" style="color:red"><spring:message code="passwordForm.passwords.mismatch"/></span>
                    </c:if>
                </div>


                <div class="uk-margin-top uk-button-group">
                    <input type="hidden" name="verification_code" value="${verification_code}">
                    <button class="uk-button uk-button-primary"> <spring:message code="hwc.change_password.confirm"/> </button>
                </div>

            </div>
        </form>
        </div>
    </div>
</div>

</body>
</html>
