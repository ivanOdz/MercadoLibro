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

<body>

<navbar_wo_search/>

<div class="uk-grid" style="margin: 0rem !important; padding: 0rem !important;">
    <div class="uk-width-2-3 uk-align-center">
    
        <div class="uk-card uk-card-default uk-align-center card-profile" style="margin: 2rem !important; padding: 1rem !important;">
        
            <h1 class="uk-h1 title-profile" style="margin: 0rem !important; padding: 0rem !important;"><spring:message code="profile.title"/></h1>
            
            <div class="profile-content uk-align-center" style="width: auto">
                <h3 class="profile-pic">
                    <c:choose>
                        <c:when test="${loggedUser.imageId != null}">
                            <img class="profile-pic"
                                 src="<c:url value='/images/${loggedUser.imageId}' />"
                                 alt="profileImage"/>
                        </c:when>
                        <c:otherwise>
                            <img class="profile-pic"
                                 src="<c:url value='/images/profile.png' />"
                                 alt="defaultImage"/>
                        </c:otherwise>
                    </c:choose>
                </h3>

                <div class="stars">
                    <c:forEach var="i" begin="1" end="5">
                        <c:choose>
                            <c:when test="${i <= userRating.rating}">
                                <i class="material-icons yellow-text">star</i>
                            </c:when>
                            <c:when test="${i - 0.5 <= userRating.rating && userRating.rating < i}">
                                <i class="material-icons yellow-text">star_half</i>
                            </c:when>
                            <c:otherwise>
                                <i class="material-icons grey-text">star_border</i>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                </div>

                <div class="uk-h5">
                    <h3 class="uk-h5" id="username-display" style="display: inline;"><c:out
                            value="${loggedUser.username}"/></h3>
                    <button type="button" id="change-username-btn" onclick="showEditForm()"
                            style="display: inline;">
                        <i class="material-icons edit-icon">edit</i>
                    </button>

                    <form id="change-username-form" action="<c:url value='/changeUsername' />" method="post"
                          style="display:none;">
                        <input type="text" name="newUsername" id="new_username" value="${loggedUser.username}"
                               required>
                        <input type="hidden" name="loggedUserId" value="${loggedUser.userId}">
                        <button type="submit" class="btn-confirm"><spring:message code="hwc.change_password.confirm"/></button>
                        <button type="button" onclick="cancelEdit()" class="btn-cancel">X</button>
                    </form>

                </div>

                <div id="username-input-section" style="display: none;">
                    <div class="input-field">
                        <input id="new-username" type="text" placeholder="Nuevo nombre de usuario"/>
                        <label for="new-username"><spring:message code="profile.new"/></label>
                    </div>

                    <button id="confirm-username-btn" class="btn green"><spring:message code="hwc.change_password.confirm"/></button>
                    <button id="cancel-username-btn" class="btn red">X</button>
                </div>

                <c:choose>
                    <c:when test="${not empty message}">
                        <div class="alert alert-success">
                            <button class="close-btn" onclick="this.parentElement.style.display='none';">
                                &times;
                            </button>
                            <c:out value="${message}"/>
                        </div>
                    </c:when>
                    <c:when test="${not empty errorMessage}">
                        <div class="alert alert-danger">
                            <button class="close-btn" onclick="this.parentElement.style.display='none';">
                                &times;
                            </button>
                            <c:out value="${errorMessage}"/>
                        </div>
                    </c:when>
                </c:choose>

                <div>
                    <h3 class="uk-h5"><c:out value="${loggedUser.mail}"/></h3>
                    <!-- 	                	<a href="change_mail_solicited" title="Change Mail"> -->
                    <!-- 				            <i class="material-icons edit-icon">edit</i> -->
                    <!-- 				        </a> -->
                </div>
                
                <div>
                    <h3 class="uk-h5"><c:out value="${loggedUser.favoriteLocation}"/></h3>
                </div>
				
				<c:forEach var="userLocation" items="${loggedUser.userLocations}">
					<div class="location-item">
					<form class="remove-location-form" action="<c:url value='/user/removeLocation' />" method="post" style="display: inline;">
						<input type="hidden" name="userId" value="${loggedUser.userId}" />
						<input type="hidden" name="locationId" value="${userLocation.locationId}" />
						<input class="uk-input" type="text" name="locationString" value="${userLocation.locationString}" readonly />
						<button type="submit" class="uk-button remove-location-btn" title="Remove Location">
							<span uk-icon="icon: close"></span>
						</button>
					</form>
					</div>
				</c:forEach>

				<form id="add-location-form" action="<c:url value='/user/addLocation' />" method="post">
					<input type="hidden" name="userId" value="${loggedUser.userId}" />
					<input id="new-location-input" type="text" name="locationString" placeholder="<spring:message code='user.new.location.placeholder'/>" required />
					<button id="add-location-btn" type="submit" class="uk-button uk-button-primary"><spring:message code="user.add.location"/></button>
				</form>

                <button style="margin: 5% 0 5% 0;" class="uk-button uk-button-default" type="button"><spring:message code="language"/></button>
                <div uk-dropdown>
                    <ul class="uk-nav uk-dropdown-nav ">
                        <li><a href="<c:url value='/language?lang=en' />"><spring:message code="language.english"/></a></li>
                        <li><a href="<c:url value='/language?lang=es' />"><spring:message code="language.spanish"/></a></li>
                    </ul>
                </div>

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

                    <div class="uk-card uk-card-body uk-border-rounded uk-box-shadow-small uk-width-expand">
                        <div class="reviewStars">
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

                        <p class="date-text"><fmt:formatDate value="${review.reviewDate}" pattern="dd/MM/yyyy"/></p>
                        <p><c:out value='${review.reviewer.username}'/>: <c:out value='${review.reviewDescription}'/></p>

                    </div>

                </c:forEach>
                <hr class="uk-divider-icon">
                <nav aria-label="Pagination" class="uk-position-relative uk-margin">
                    <ul class="uk-pagination uk-flex-center uk-position-center">

                        <!-- Botón Previous -->
                        <c:if test="${reviews.metadata.currentPage > 0}">
                            <li>
                                <c:url var="prevPageUrl" value="">
                                    <c:param name="page" value="${reviews.metadata.currentPage - 1}" />
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
                                    <c:param name="page" value="0" />
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
                                    <c:param name="page" value="${i}" />
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
                                    <c:param name="page" value="${reviews.metadata.maxPage}" />
                                </c:url>
                                <a href="${lastPageUrl}">${reviews.metadata.maxPage + 1}</a> <!-- Mostrar maxPage + 1 -->
                            </li>
                        </c:if>

                        <!-- Botón Next -->
                        <c:if test="${reviews.metadata.currentPage < reviews.metadata.maxPage}">
                            <li>
                                <c:url var="nextPageUrl" value="">
                                    <c:param name="page" value="${reviews.metadata.currentPage + 1}" />
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
    </div>
</div>

<!-- 	<div> -->
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
<%-- 	                <c:forEach var="review" items="${reviews}"> --%>
<!-- 	                    <div class="uk-card uk-card-default uk-card-body uk-border-rounded uk-box-shadow-small mb-1"> -->
<%--                        <p>${review.reviewDescription}</p>--%>
<!-- 	                    </div> -->
<%--                 	</c:forEach>	 --%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

<!-- 	</div> -->

</body>

<script type="text/javascript">

    function showEditForm() {
    	
        document.getElementById('username-display').style.display = 'none';
        document.getElementById('change-username-btn').style.display = 'none';
        document.getElementById('change-username-form').style.display = 'block';
    }

    function cancelEdit() {
    	
        document.getElementById('username-display').style.display = 'block';
        document.getElementById('change-username-btn').style.display = 'inline-block';
        document.getElementById('change-username-form').style.display = 'none';
    }

// 	document.addEventListener("DOMContentLoaded", function() {
		
// 		const addLocationForm = document.getElementById("add-location-form");
// 		const newLocationInput = document.getElementById("new-location-input");
// 		const addLocationBtn = document.getElementById("add-location-btn");
// 		const removeLocationForms = document.querySelectorAll(".remove-location-form");
		
// 		addLocationForm.addEventListener("submit", function() {
			
// 		newLocationInput.disabled = true;
// 		addLocationBtn.disabled = true;
		
// 		removeLocationForms.forEach(form => {
// 				const removeBtn = form.querySelector(".remove-location-btn");
// 				removeBtn.disabled = true;
// 			});
// 		});
	
// 		removeLocationForms.forEach(form => {
// 			form.addEventListener("submit", function() {
// 				newLocationInput.disabled = true;
// 				addLocationBtn.disabled = true;
				
// 				removeLocationForms.forEach(f => {
// 					const removeBtn = f.querySelector(".remove-location-btn");
// 					removeBtn.disabled = true;
// 				});
// 			});
// 		});
// 	});
    
</script>

</html>
