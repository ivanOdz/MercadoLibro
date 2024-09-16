<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="es" class="custom-style">
<%@include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ include file="/WEB-INF/jsp/components/navbar.jsp" %>
<head>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/navbar.css?v=1.0" rel="stylesheet"/>

    <link href="${pageContext.request.contextPath}/css/profile.css" rel="stylesheet"/>
    <title><spring:message code="profile.view.title"/></title>
</head>
<body>
<navbar/>

<div class="uk-grid">
    <div class="uk-width-1-2 main-margin uk-align-center">
        <div class="uk-card uk-card-default uk-card-body card-profile">
            <h1 class="uk-h1 title-profile"><spring:message code="profile.title"/></h1>
            <div class="profile-content">
                <!-- Profile picture-->
<%--                <h3 class="uk-h5"><c:out value="${loggedUser.imageId}"/></h3>--%>
                <img src="images/profile-default.jpg" alt="default-profile-pic" class="circle"/>
                <!-- Profile data-->
                <h3 class="uk-h5"><c:out value="${loggedUser.username}"/></h3>
                <h3 class="uk-h5"><c:out value="${loggedUser.mail}"/></h3>

                <hr class="uk-divider-icon">


                <h2 class="uk-h4 subtitles-profile"><spring:message code="review.title"/></h2>

                <c:forEach var="review" items="${reviews}">
                    <div class="uk-card uk-card-default uk-card-body uk-border-rounded uk-box-shadow-small mb-1">
                                                    <p>${review.reviewDescription}</p>
                    </div>
                </c:forEach>
            </div>
        </div>

    </div>
</div>

</div>


<%--<div class="uk-section uk-background-muted">--%>
<%--    <div class="uk-align-center uk-container">--%>
<%--        <p class="uk-text-lead uk-align-center">--%>
<%--            <spring:message code="profile.title"/>--%>
<%--        </p>--%>
<%--    </div>--%>
<%--    <div class="uk-container uk-margin-top">--%>
<%--        <div class="uk-grid ml-1" uk-grid>--%>
<%--            <div class="uk-width-1-3@s exchange-information-section uk-border-rounded uk-box-shadow-small mt-1 mb-1 uk-height-viewport"--%>
<%--                 uk-height-viewport="offset-top: true">--%>
<%--            </div>--%>

<%--            <div class="uk-width-expand uk-margin-top">--%>
<%--                <p class="uk-text-lead">--%>
<%--                    <c:out value="${loggedUser.username}"/>--%>
<%--                </p>--%>
                <c:forEach var="review" items="${reviews}">
                    <div class="uk-card uk-card-default uk-card-body uk-border-rounded uk-box-shadow-small mb-1">
                            <%--                        <p>${review.reviewDescription}</p>--%>
                    </div>
                </c:forEach>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

</div>
</body>
</html>
