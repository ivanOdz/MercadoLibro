<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>

<html lang="es" class="custom-style">

<head>

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <link href="<c:url value='/css/navbar.css?v=1.0' />" rel="stylesheet"/>
    <link href="<c:url value='/css/profile.css?v=1.0' />" rel="stylesheet"/>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <title><spring:message code="profile.view.title"/></title>

</head>

<body style="background-color: #f8f8f8;">

<navbar_wo_search></navbar_wo_search>

<div class="main-grid-container">

    <h1 class="title-profile"><spring:message code="profile.title"/></h1>

    <div class="user-data-grid-container">

        <div class="user-data-grid-element-left">

            <c:choose>
                <c:when test="${loggedUser.imageId != null}">
                    <img class="profile-pic" src="<c:url value='/images/${loggedUser.imageId}' />" alt="Profile Image"/>
                </c:when>
                <c:otherwise>
                    <img class="profile-pic" src="<c:url value='/images/profile.png' />" alt="Default Image"/>
                </c:otherwise>
            </c:choose>
            <!-- Acá agregar boton para editar imagen de perfil o clickeando sobre la imagen... -->
        </div>

        <div class="user-data-grid-element-right">

            <h4 id="username-display" style="display: inline;"><spring:message code="profile.name"/><c:out
                    value="${loggedUser.username}"/></h4>

            <button class="edit-button" id="change-username-btn" onclick="showEditForm()"
                    style="display: inline; margin-left: 2%;">
                <svg class="edit-svgIcon" viewBox="0 0 512 512">
                    <path d="M410.3 231l11.3-11.3-33.9-33.9-62.1-62.1L291.7 89.8l-11.3 11.3-22.6 22.6L58.6 322.9c-10.4 10.4-18 23.3-22.2 37.4L1 480.7c-2.5 8.4-.2 17.5 6.1 23.7s15.3 8.5 23.7 6.1l120.3-35.4c14.1-4.2 27-11.8 37.4-22.2L387.7 253.7 410.3 231zM160 399.4l-9.1 22.7c-4 3.1-8.5 5.4-13.3 6.9L59.4 452l23-78.1c1.4-4.9 3.8-9.4 6.9-13.3l22.7-9.1v32c0 8.8 7.2 16 16 16h32zM362.7 18.7L348.3 33.2 325.7 55.8 314.3 67.1l33.9 33.9 62.1 62.1 33.9 33.9 11.3-11.3 22.6-22.6 14.5-14.5c25-25 25-65.5 0-90.5L453.3 18.7c-25-25-65.5-25-90.5 0zm-47.4 168l-144 144c-6.2 6.2-16.4 6.2-22.6 0s-6.2-16.4 0-22.6l144-144c6.2-6.2 16.4-6.2 22.6 0s6.2 16.4 0 22.6z"></path>
                </svg>
            </button>
            <form id="change-username-form" action="<c:url value='/changeUsername' />" method="post"
                  style="display:none; align-items: center;">
                <input type="text" name="newUsername" id="new_username" class="uk-input"
                       value="<c:out value='${loggedUser.username}'/>" required>
                <input type="hidden" name="loggedUserId" value="${loggedUser.userId}">
                <!-- Podria sacarse desde el controlador y seria mas seguro -->
                <button type="button" onclick="cancelEdit()" class="btn-cancel uk-button"><spring:message
                        code="button.cancel"/></button>
                <button type="submit" class="btn-confirm uk-button uk-button-primary"><spring:message
                        code="hwc.change_password.confirm"/></button>
            </form>

            <h4><spring:message code="profile.mail"/><c:out value="${loggedUser.mail}"/></h4>

            <button style="margin: 5% 0 5% 0;" class="uk-button uk-button-default" type="button"><spring:message
                    code="language"/></button>
            <div uk-dropdown>
                <ul class="uk-nav uk-dropdown-nav ">
                    <li><a href="<c:url value='/language?lang=en' />"><spring:message code="language.english"/></a></li>
                    <li><a href="<c:url value='/language?lang=es' />"><spring:message code="language.spanish"/></a></li>
                </ul>
            </div>

        </div>

    </div>

    <div class="profile-content uk-align-center" style="width: auto">
        <h4>
            <spring:message code="profile.rating.earned"/>
            <span><fmt:formatNumber value="${userRating.rating}" maxFractionDigits="1"/></span>
        </h4>


        <!--             <div class="stars"> -->
        <%--                 <c:forEach var="i" begin="1" end="5"> --%>
        <%--                     <c:choose> --%>
        <%--                         <c:when test="${i <= userRating.rating}"> --%>
        <!--                             <i class="material-icons yellow-text">star</i> -->
        <%--                         </c:when> --%>
        <%--                         <c:when test="${i - 0.5 <= userRating.rating && userRating.rating < i}"> --%>
        <!--                             <i class="material-icons yellow-text">star_half</i> -->
        <%--                         </c:when> --%>
        <%--                         <c:otherwise> --%>
        <!--                             <i class="material-icons grey-text">star_border</i> -->
        <%--                         </c:otherwise> --%>
        <%--                     </c:choose> --%>
        <%--                 </c:forEach> --%>

        <!--             </div> -->

        <!-- Yo diria de hacer una barra de mensajes -->
        <%--             <c:choose> --%>
        <%--                 <c:when test="${not empty message}"> --%>
        <!--                     <div class="alert alert-success"> -->
        <!--                         <button class="close-btn" onclick="this.parentElement.style.display='none';"> -->
        <!--                             &times; -->
        <!--                         </button> -->
        <%--                         <c:out value="${message}"/> --%>
        <!--                     </div> -->
        <%--                 </c:when> --%>
        <%--                 <c:when test="${not empty errorMessage}"> --%>
        <!--                     <div class="alert alert-danger"> -->
        <!--                         <button class="close-btn" onclick="this.parentElement.style.display='none';"> -->
        <!--                             &times; -->
        <!--                         </button> -->
        <%--                         <c:out value="${errorMessage}"/> --%>
        <!--                     </div> -->
        <%--                 </c:when> --%>
        <%--             </c:choose> --%>

        <!--<div> -->
        <!-- 	                	<a href="change_mail_solicited" title="Change Mail"> -->
        <!-- 				            <i class="material-icons edit-icon">edit</i> -->
        <!-- 				        </a> -->
        <!--</div> -->

        <div>
            <h3 class="uk-h5"><c:out value="${loggedUser.favoriteLocation}"/></h3>
        </div>

        <c:forEach var="userLocation" items="${loggedUser.userLocations}">
            <div class="location-item">
                <form class="remove-location-form" action="<c:url value='/user/removeLocation' />" method="post"
                      style="display: flex;">
                    <input type="hidden" name="userId" value="${loggedUser.userId}"/>
                    <input type="hidden" name="locationId" value="${userLocation.locationId}"/>
                    <input style="border: none" class="uk-input" type="text" name="locationString"
                           value="<c:out value='${userLocation.locationString}'/>" readonly/>
                    <button type="submit" class="uk-button remove-location-btn" title="Remove Location">
                        <span uk-icon="icon: close"></span>
                    </button>
                </form>
            </div>
        </c:forEach>

        <form id="add-location-form" action="<c:url value='/user/addLocation' />" method="post">
            <input type="hidden" name="userId" value="${loggedUser.userId}"/>
            <input id="new-location-input" type="text" name="locationString" style="margin-top: 3%;" class="uk-input"
                   placeholder="<spring:message code='user.new.location.placeholder'/>" required/>
            <button id="add-location-btn" type="submit" style="margin-top: 2%;" class="uk-button uk-button-primary">
                <spring:message code="user.add.location.button"/></button>
        </form>


        <!-- 					<div class="changePasswordButton"> -->
        <!-- 					    <a href="change_password_solicited" class="btn-red" title="Cambiar Contraseña"> -->
        <!-- 					        Password change -->
        <!-- 					    </a> -->
        <!-- 					</div> -->

        <hr class="uk-divider-icon">
    </div>

    <div>
        <h2 class="uk-h4 subtitles-profile"><spring:message code="review.title"/></h2>
    </div>

    <c:if test="${not empty reviews.data}">
        <c:forEach var="review" items="${reviews.data}">

            <div class="uk-card uk-card-body uk-border-rounded uk-box-shadow-small uk-width-expand" style="background-color: #f7f7f7">
                <div>
                    <p class="date-text"><fmt:formatDate value="${review.reviewDate}" pattern="dd/MM/yyyy"/></p>
                    <p><c:out value='${review.reviewer.username}'/>: <c:out value='${review.reviewDescription}'/></p>
                </div>

                <div class="reviewStars" style="margin-left: 7%;">
                    <c:forEach var="i" begin="1" end="5">
                        <c:choose>
                            <c:when test="${i <= review.reviewRating}">
                                <i class="material-icons yellow-text">star</i>
                            </c:when>
                            <c:otherwise>
                                <i class="material-icons grey-text">star_border</i>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>

            </div>

        </c:forEach>
        <hr class="uk-divider-icon">
        <nav aria-label="Pagination" class="uk-position-relative uk-margin">
            <ul class="uk-pagination uk-flex-center uk-position-center">

                <!-- Botón Previous -->
                <c:if test="${reviews.metadata.currentPage > 0}">
                    <li>
                        <c:url var="prevPageUrl" value="">
                            <c:param name="page" value="${reviews.metadata.currentPage - 1}"/>
                        </c:url>
                        <a href="${prevPageUrl}">
                            <span uk-pagination-previous></span>
                            <spring:message code="publications.pagination.previous"/>
                        </a>
                    </li>
                </c:if>

                <!-- Botón de la primera página -->
                <c:if test="${reviews.metadata.currentPage > 1}">
                    <li>
                        <c:url var="firstPageUrl" value="">
                            <c:param name="page" value="0"/>
                        </c:url>
                        <a href="${firstPageUrl}">1</a>
                    </li>
                </c:if>

                <c:if test="${reviews.metadata.currentPage - 2 > 0}">
                    <li><span>...</span></li>
                </c:if>

                <!-- Páginas centrales -->
                <c:forEach var="i" begin="${reviews.metadata.currentPage > 0 ? reviews.metadata.currentPage - 1 : 0}"
                           end="${reviews.metadata.currentPage + 1 <= reviews.metadata.maxPage ? reviews.metadata.currentPage + 1 : reviews.metadata.maxPage}">
                    <li class="${i == reviews.metadata.currentPage ? 'uk-active' : ''}">
                        <c:url var="centralPageUrl" value="">
                            <c:param name="page" value="${i}"/>
                        </c:url>
                        <a href="${centralPageUrl}">${i + 1}</a> <!-- Mostrar i + 1 para la numeración -->
                    </li>
                </c:forEach>

                <c:if test="${reviews.metadata.currentPage + 2 < reviews.metadata.maxPage}">
                    <li><span>...</span></li>
                </c:if>

                <!-- Botón de la última página -->
                <c:if test="${reviews.metadata.currentPage + 1 < reviews.metadata.maxPage}">
                    <li>
                        <c:url var="lastPageUrl" value="">
                            <c:param name="page" value="${reviews.metadata.maxPage}"/>
                        </c:url>
                        <a href="${lastPageUrl}">${reviews.metadata.maxPage + 1}</a> <!-- Mostrar maxPage + 1 -->
                    </li>
                </c:if>

                <!-- Botón Next -->
                <c:if test="${reviews.metadata.currentPage < reviews.metadata.maxPage}">
                    <li>
                        <c:url var="nextPageUrl" value="">
                            <c:param name="page" value="${reviews.metadata.currentPage + 1}"/>
                        </c:url>
                        <a href="${nextPageUrl}">
                            <spring:message code="publications.pagination.next"/>
                            <span uk-pagination-next></span>
                        </a>
                    </li>
                </c:if>
            </ul>

            <!-- Botón "Ir al inicio" alineado a la derecha -->
            <a href="" uk-totop uk-scroll class="uk-position-right uk-margin-right">
                <spring:message code="publications.pagination.totop"/>
            </a>
        </nav>
    </c:if>
    <c:if test="${empty reviews.data}">
        <div class="review-empty">
            <div style="margin:2%;width: max-content;">
                <spring:message code="reviews.empty"/>
            </div>
        </div>
    </c:if>
</div>

</body>

<script type="text/javascript">

    function showEditForm() {

        document.getElementById('username-display').style.display = 'none';
        document.getElementById('change-username-btn').style.display = 'none';
        document.getElementById('change-username-form').style.display = 'inline-block';
    }

    function cancelEdit() {

        document.getElementById('username-display').style.display = 'block';
        document.getElementById('change-username-btn').style.display = 'inline-block';
        document.getElementById('change-username-form').style.display = 'none';
    }

</script>

</html>
