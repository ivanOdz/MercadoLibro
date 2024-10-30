<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="es" class="custom-style">

<%@ include file="/WEB-INF/jsp/head/headers.jsp" %>
<%@ include file="/WEB-INF/jsp/components/navbar_wo_search.jsp" %>

<head>

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons"/>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/css/uikit.min.css" rel="stylesheet"/>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/uikit/3.16.20/js/uikit-icons.min.js"></script>

    <link href="<c:url value='/css/navbar.css' />" rel="stylesheet"/>
    <link href="<c:url value='/css/exchange.css' />" rel="stylesheet"/>

    <title><spring:message code="exchanges.view.title"/></title>

</head>

<body>

<c:url var="submitReview" value="/submitReview"/>


<navbar_wo_search/>

<div class="uk-grid">

    <div class="uk-width-5-6 uk-align-center main-section" style="margin-top: 0; padding-top: 0.5rem;">
        <div>
            <h2 class="uk-h2 title" style="margin: 0; padding: 0;"><spring:message code="exchange.requests.title"/></h2>
            <h3 class="uk-h5" style="margin: 0; padding: 1rem;"><spring:message code="exchange.requests.subtitle"/></h3>
        </div>
        
        <div class="main-content">

            <!-- columna de exchanges -->
            <div class="uk-width-3-5 column-exchanges scrollable-content">
                <div>
                    <ul uk-tab>
                        <li class="uk-active"><a href="#"><spring:message code="exchange.status.pending"/> </a></li>
                        <li><a href="#"><spring:message code="exchange.status.in_progress"/></a></li>
                        <li><a href="#"><spring:message code="exchange.status.terminated"/></a></li>
                        <li><a href="#"><spring:message code="exchange.status.rejected"/></a></li>
                    </ul>
                    <ul class="uk-switcher uk-margin">
                        <!-- Pending -->
                        <li style="min-height: 40%; align-content: center;" class="uk-container uk-align-center">
                            <c:if test="${!empty pending.data}">
                                <c:forEach var="data" items="${pending.data}">
                                
<%--                                     <c:set var="locationsListString" value="" /> --%>
<%-- 									<c:forEach var="location" items="${data.offerer.locations}"> --%>
<%-- 										<c:choose> --%>
<%-- 											<c:when test="${empty locationsListString}"> --%>
<%-- 												<c:set var="locationsListString" value="${location.locationString}" /> --%>
<%-- 											</c:when> --%>
<%-- 											<c:otherwise> --%>
<%-- 												<c:set var="locationsListString" value="${locationsListString}, ${location.locationString}" /> --%>
<%-- 											</c:otherwise> --%>
<%-- 										</c:choose> --%>
<%-- 									</c:forEach> --%>
    								
    								<c:set var="authorsListString" value="" />
									<c:forEach var="author" items="${data.requester.book.bookModel.authors}">
										<c:choose>
											<c:when test="${empty authorsListString}">
												<c:set var="authorsListString" value="${author.authorName}" />
											</c:when>
											<c:otherwise>
												<c:set var="authorsListString" value="${authorsListString}, ${author.authorName}" />
											</c:otherwise>
										</c:choose>
									</c:forEach>
									
                                    <div class="uk-card uk-card-default exchange-card"
                                         onclick="selectCard(this,
                                                 '<c:out value="${data.requester.book.owner.username}"/>',
                                                 '<c:out value="${data.requester.book.owner.mail}"/>',
                                                 '<c:out value="${data.offerer.location.locationString}"/>',
                                                 '<c:out value="${data.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${authorsListString}"/>',
                                                 '<c:out value="${data.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${data.offerer.book.images}"/>',
                                                 '<c:out value="${data.exchangeId}"/>',
                                                 '<c:out value="${data.requester.book.owner.userId}"/>',
                                                 '<c:out value="${data.offerer.book.owner.userId}"/>',
                                                 '${data.isReviewable}')"
                                         uk-grid>


                                        <div style="padding: 0">
                                            <c:choose>
                                                <c:when test="${!empty data.offerer.book.images && !data.offerer.book.images[0].image.isImageNull}">
                                                    <img class="book-image"
                                                         src="<c:url value='/images/${data.offerer.book.images[0].image.imageId}' />"
                                                         alt="bookImage"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="book-image"
                                                         src="<c:url value='/images/book.jpg' />" alt="book"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${data.offerer.book.bookModel.title}"/>
                                                </h3>
                                            </div>
                                        </div>
                                        <div style="width: 40%; display: grid; justify-items: center; padding-left: 0px">
                                            <div class="arrow-icon" style="padding: 0">
                                                <span uk-icon="icon: chevron-double-right; ratio: 2"></span>
                                            </div>
                                            <div style="padding-left: 0;width:40%; margin-bottom: 15px; display: flex; flex-direction: column; align-items: center">
                                                <div class="uk-button-group">
                                                    <a class="uk-button uk-button-default uk-button-small"
                                                       href="#modal-exchange-accepted-${data.acceptCode}"
                                                       onclick="event.stopPropagation()" uk-toggle>
                                                        <spring:message code="exchange.button.accept"/>
                                                    </a>
                                                    <a class="uk-button uk-button-default uk-button-small custom-link"
                                                       href="#modal-exchange-rejected-${data.acceptCode}"
                                                       onclick="event.stopPropagation()" uk-toggle>
                                                        <spring:message code="email.rejectButton"/>
                                                    </a>
                                                </div>
                                                <span style="width: 90%; margin: 5%" class="uk-badge state-pending">
                                                        <spring:message code="exchange.status.pending"/>
                                                </span>
                                                <div style="width: 400%; font-size: 10px; padding: 5%">
                                                    <spring:message code="date.start"/>
                                                    <spring:message code="date.format" var="dateFormat"/>
                                                    <fmt:formatDate value="${data.exchangeStartDate}"
                                                                    pattern="${dateFormat}"/>
                                                </div>
                                            </div>
                                        </div>

                                        <div style="padding: 0">
                                            <c:choose>
                                                <c:when test="${!empty data.requester.book.images && !data.requester.book.images[0].image.isImageNull}">
                                                    <img class="book-image"
                                                         src="<c:url value='/images/${data.requester.book.images[0].image.imageId}' />"
                                                         alt="bookImage"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="book-image"
                                                         src="<c:url value='/images/book.jpg' />" alt="book"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${data.requester.book.bookModel.title}"/>
                                                </h3></div>
                                        </div>


                                        <!-- Exchange Rejected modal -->

                                        <div id="modal-exchange-rejected-${data.acceptCode}"
                                             uk-modal>
                                            <div class="uk-modal-dialog uk-modal-body">
                                                <h3 class="uk-h4"><spring:message
                                                        code="exchange.rejection.title"/></h3>
                                                <p class="uk-text-right">
                                                    <button class="uk-button uk-button-default uk-modal-close"
                                                            type="button"><spring:message
                                                            code="button.cancel"/></button>
                                                    <button class="uk-button uk-button-danger"
                                                            type="button">
                                                        <a href="<c:url value='/createexchange'>
                                                        <c:param name='accept_code' value='${data.acceptCode}'/>
                                                        <c:param name='state' value='false'/>
                                                            </c:url>">
                                                            <spring:message
                                                                    code="button.confirm"/>
                                                        </a>
                                                    </button>
                                                </p>
                                            </div>
                                        </div>

                                        <!-- Exchange Accepted modal -->

                                        <div id="modal-exchange-accepted-${data.acceptCode}"
                                             uk-modal>
                                            <div class="uk-modal-dialog uk-modal-body">
                                                <h3 class="uk-h4"><spring:message
                                                        code="exchange.confirmation.title"/></h3>
                                                <p class="uk-text-right">
                                                    <button class="uk-button uk-button-default uk-modal-close"
                                                            type="button"><spring:message
                                                            code="button.cancel"/></button>
                                                    <a class="uk-button uk-button-primary"
                                                            type="button"
                                                           href="<c:url value='/createexchange'>
                                                                   <c:param name='accept_code' value='${data.acceptCode}'/>
                                                                    <c:param name='state' value='true'/>
                                                                         </c:url>">
                                                            <spring:message
                                                                    code="button.confirm"/>
                                                    </a>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty pending.data}">
                                <div>
                                    <h4 class="uk-h6">
                                        <spring:message code="exchange.pending.empty"/>
                                    </h4>
                                </div>
                            </c:if>
                            <hr class="uk-divider-icon">

                            <nav aria-label="Pagination" class="uk-position-relative uk-margin">
                                <ul class="uk-pagination uk-flex-center uk-position-center">

                                    <%--                                    <!-- Botón Previous -->--%>
                                    <c:if test="${pending.metadata.currentPage > 0}">
                                        <li>
                                            <c:url var="prevPageUrl" value="">
                                                <c:param name="pending-page" value="${pending.metadata.currentPage - 1}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${prevPageUrl}">
                                                <span uk-pagination-previous></span>
                                                <spring:message code="publications.pagination.previous"/>
                                            </a>
                                        </li>
                                    </c:if>

                                    <!-- Botón de la primera página -->
                                    <c:if test="${pending.metadata.currentPage > 1}">
                                        <li>
                                            <c:url var="firstPageUrl" value="">
                                                <c:param name="pending-page" value="0" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${firstPageUrl}"></a>
                                        </li>
                                    </c:if>

                                    <c:if test="${pending.metadata.currentPage - 2 > 0}">
                                        <li><span>...</span></li>
                                    </c:if>

                                    <%--                                    <!-- Páginas centrales -->--%>
                                    <c:forEach var="i" begin="${pending.metadata.currentPage > 0 ? pending.metadata.currentPage - 1 : 0}"
                                               end="${pending.metadata.currentPage + 1 <= pending.metadata.maxPage ? pending.metadata.currentPage + 1 : pending.metadata.maxPage}">
                                        <li class="${i == pending.metadata.currentPage ? 'uk-active' : ''}">
                                            <c:url var="centralPageUrl" value="">
                                                <c:param name="pending-page" value="${i}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${centralPageUrl}">${i + 1}</a> <!-- Mostrar i + 1 para la numeración -->
                                        </li>
                                    </c:forEach>


                                    <c:if test="${pending.metadata.currentPage + 2 < pending.metadata.maxPage}">
                                        <li><span>...</span></li>
                                    </c:if>

                                    <%--                                    <!-- Botón de la última página -->--%>
                                    <c:if test="${pending.metadata.currentPage + 1 < pending.metadata.maxPage}">
                                        <li>
                                            <c:url var="lastPageUrl" value="">
                                                <c:param name="pending-page" value="${pending.metadata.maxPage}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${lastPageUrl}">${pending.metadata.maxPage + 1}</a> <!-- Mostrar maxPage + 1 -->
                                        </li>
                                    </c:if>

                                    <%--                                    <!-- Botón Next -->--%>
                                    <c:if test="${pending.metadata.currentPage < pending.metadata.maxPage}">
                                        <li>
                                            <c:url var="nextPageUrl" value="">
                                                <c:param name="pending-page" value="${pending.metadata.currentPage + 1}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
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

                        </li>

                        <!-- In Progress -->

                        <li style="min-height: 40%; align-content: center;" class="uk-container">
                            <c:if test="${!empty inProgress.data}">
                                <c:forEach var="data" items="${inProgress.data}">
    								
    								<c:set var="authorsListString" value="" />
									<c:forEach var="author" items="${data.requester.book.bookModel.authors}">
										<c:choose>
											<c:when test="${empty authorsListString}">
												<c:set var="authorsListString" value="${author.authorName}" />
											</c:when>
											<c:otherwise>
												<c:set var="authorsListString" value="${authorsListString}, ${author.authorName}" />
											</c:otherwise>
										</c:choose>
									</c:forEach>
									
                                    <div class="uk-card uk-card-default exchange-card"
                                         onclick="selectCard(this,
                                                 '<c:out value="${data.requester.book.owner.username}"/>',
                                                 '<c:out value="${data.requester.book.owner.mail}"/>',
                                                 '<c:out value="${data.offerer.location.locationString}"/>',
                                                 '<c:out value="${data.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${authorsListString}"/>',
                                                 '<c:out value="${data.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${data.offerer.book.images}"/>',
                                                 '<c:out value="${data.exchangeId}"/>',
                                                 '<c:out value="${data.requester.book.owner.userId}"/>',
                                                 '<c:out value="${data.offerer.book.owner.userId}"/>',
                                                 '${data.isReviewable}')"
                                         uk-grid>


                                        <div style="padding: 0">
                                            <c:choose>
                                                <c:when test="${!empty data.offerer.book.images && !data.offerer.book.images[0].image.isImageNull}">
                                                    <img class="book-image"
                                                         src="<c:url value='/images/${data.offerer.book.images[0].image.imageId}' />"
                                                         alt="bookImage"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="book-image"
                                                         src="<c:url value='/images/book.jpg' />" alt="book"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${data.offerer.book.bookModel.title}"/>
                                                </h3>
                                            </div>
                                        </div>
                                        <div style="width: 40%; display: grid; justify-items: center; padding-left: 0px">
                                            <div class="arrow-icon" style="padding: 0">
                                                <span uk-icon="icon: chevron-double-right; ratio: 2"></span>
                                            </div>
                                            <c:if test="${data.offererReceivedBook}">
                                                                <span class="uk-badge state-awaiting">
                                                            <spring:message code="exchange.status.awaiting"/>
													            </span>
                                            </c:if>
                                            <c:if test="${!data.offererReceivedBook}">
                                                <a class="uk-button uk-button-default uk-button-small"
                                                   href="#modal-confirm-exchange-${data.acceptCode}"
                                                   onclick="event.stopPropagation()" uk-toggle>
                                                    <spring:message
                                                            code="exchange.button.confirm.exchange"/>
                                                </a>
                                                <span class="uk-badge state-inprogress">
                                                                    <spring:message code="exchange.status.in_progress"/>
                                                                </span>
                                            </c:if>
                                            <div style="font-size: 10px; padding: 5%">
                                                <spring:message code="date.start"/>
                                                <spring:message code="date.format" var="dateFormat"/>
                                                <fmt:formatDate value="${data.exchangeStartDate}"
                                                                pattern="${dateFormat}"/>
                                            </div>


                                            <!-- Confirm exchange modal -->

                                            <div id="modal-confirm-exchange-${data.acceptCode}"
                                                 uk-modal>
                                                <div class="uk-modal-dialog uk-modal-body">
                                                    <h3 class="uk-h4"><spring:message
                                                            code="exchange.confirm.title"/></h3>
                                                    <p class="uk-text-right">
                                                        <button class="uk-button uk-button-default uk-modal-close"
                                                                type="button"><spring:message
                                                                code="button.cancel"/></button>
                                                        <button class="uk-button uk-button-primary"
                                                                type="button">
                                                            <a class="button-text-accept custom-link"
                                                               href="<c:url value='/confirm_offerer'><c:param name='accept_code' value='${data.acceptCode}'/></c:url>">
                                                                <spring:message code="button.confirm"/>
                                                            </a>
                                                        </button>
                                                    </p>
                                                </div>
                                            </div>
                                        </div>

                                        <div style="padding: 0">
                                            <c:choose>
                                                <c:when test="${!empty data.requester.book.images && !data.requester.book.images[0].image.isImageNull}">
                                                    <img class="book-image"
                                                         src="<c:url value='/images/${data.requester.book.images[0].image.imageId}' />"
                                                         alt="bookImage"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="book-image"
                                                         src="<c:url value='/images/book.jpg' />" alt="book"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${data.requester.book.bookModel.title}"/>
                                                </h3>
                                            </div>
                                        </div>


                                    </div>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty inProgress.data}">
                                <div>
                                    <h4 class="uk-h6">
                                        <spring:message code="exchange.inProgress.empty"/>
                                    </h4>
                                </div>
                            </c:if>
                            <hr class="uk-divider-icon">

                            <nav aria-label="Pagination" class="uk-position-relative uk-margin">
                                <ul class="uk-pagination uk-flex-center uk-position-center">

                                    <%--                                    <!-- Botón Previous -->--%>
                                    <c:if test="${inProgress.metadata.currentPage > 0}">
                                        <li>
                                            <c:url var="prevPageUrl" value="">
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage - 1}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${prevPageUrl}">
                                                <span uk-pagination-previous></span>
                                                <spring:message code="publications.pagination.previous"/>
                                            </a>
                                        </li>
                                    </c:if>

                                    <!-- Botón de la primera página -->
                                    <c:if test="${inProgress.metadata.currentPage > 1}">
                                        <li>
                                            <c:url var="firstPageUrl" value="">
                                                <c:param name="in-progress-page" value="0" />
                                                <c:param name="pending" value="${pending.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${firstPageUrl}"></a>
                                        </li>
                                    </c:if>

                                    <c:if test="${inProgress.metadata.currentPage - 2 > 0}">
                                        <li><span>...</span></li>
                                    </c:if>

                                    <%--                                    <!-- Páginas centrales -->--%>
                                    <c:forEach var="i" begin="${inProgress.metadata.currentPage > 0 ? inProgress.metadata.currentPage - 1 : 0}"
                                               end="${inProgress.metadata.currentPage + 1 <= inProgress.metadata.maxPage ? inProgress.metadata.currentPage + 1 : inProgress.metadata.maxPage}">
                                        <li class="${i == inProgress.metadata.currentPage ? 'uk-active' : ''}">
                                            <c:url var="centralPageUrl" value="">
                                                <c:param name="in-progress-page" value="${i}" />
                                                <c:param name="pending" value="${pending.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${centralPageUrl}">${i + 1}</a> <!-- Mostrar i + 1 para la numeración -->
                                        </li>
                                    </c:forEach>


                                    <c:if test="${inProgress.metadata.currentPage + 2 < inProgress.metadata.maxPage}">
                                        <li><span>...</span></li>
                                    </c:if>

                                    <%--                                    <!-- Botón de la última página -->--%>
                                    <c:if test="${inProgress.metadata.currentPage + 1 < inProgress.metadata.maxPage}">
                                        <li>
                                            <c:url var="lastPageUrl" value="">
                                                <c:param name="in-progress-page" value="${inProgress.metadata.maxPage}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${lastPageUrl}">${inProgress.metadata.maxPage + 1}</a> <!-- Mostrar maxPage + 1 -->
                                        </li>
                                    </c:if>

                                    <%--                                    <!-- Botón Next -->--%>
                                    <c:if test="${inProgress.metadata.currentPage < inProgress.metadata.maxPage}">
                                        <li>
                                            <c:url var="nextPageUrl" value="">
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage + 1}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
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

                        </li>

                        <!-- Completed -->

                        <li style="margin-top: 0;min-height: 40%; align-content: center;" class="uk-container">
                            <c:if test="${!empty completed.data}">
                                <c:forEach var="data" items="${completed.data}">
    								
    								<c:set var="authorsListString" value="" />
									<c:forEach var="author" items="${data.requester.book.bookModel.authors}">
										<c:choose>
											<c:when test="${empty authorsListString}">
												<c:set var="authorsListString" value="${author.authorName}" />
											</c:when>
											<c:otherwise>
												<c:set var="authorsListString" value="${authorsListString}, ${author.authorName}" />
											</c:otherwise>
										</c:choose>
									</c:forEach>
									
                                    <div class="uk-card uk-card-default exchange-card"
                                         onclick="selectCard(this,
                                                 '<c:out value="${data.requester.book.owner.username}"/>',
                                                 '<c:out value="${data.requester.book.owner.mail}"/>',
                                                 '<c:out value="${data.offerer.location.locationString}"/>',
                                                 '<c:out value="${data.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${authorsListString}"/>',
                                                 '<c:out value="${data.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${data.offerer.book.images}"/>',
                                                 '<c:out value="${data.exchangeId}"/>',
                                                 '<c:out value="${data.requester.book.owner.userId}"/>',
                                                 '<c:out value="${data.offerer.book.owner.userId}"/>',
                                                 '${data.isReviewable}')"
                                         uk-grid>

                                        <div style="padding: 0">
                                            <c:choose>
                                                <c:when test="${!empty data.offerer.book.images && !data.offerer.book.images[0].image.isImageNull}">
                                                    <img class="book-image"
                                                         src="<c:url value='/images/${data.offerer.book.images[0].image.imageId}' />"
                                                         alt="bookImage"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="book-image"
                                                         src="<c:url value='/images/book.jpg' />" alt="book"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${data.offerer.book.bookModel.title}"/>
                                                </h3></div>
                                        </div>

                                        <div style="width: 40%; display: grid; justify-items: center; padding-left: 0px">
                                            <div class="arrow-icon" style="padding: 0">
                                                <span uk-icon="icon: chevron-double-right; ratio: 2"></span>
                                            </div>
                                            <span class="uk-badge state-approved"><spring:message
                                                    code="exchange.status.terminated"/></span>
                                            <div style="padding-left: 0;width: 100%; margin-bottom: 25px">
                                                <div style="font-size: 10px; padding: 5%">
                                                    <spring:message code="date.start"/>
                                                    <spring:message code="date.format" var="dateFormat"/>
                                                    <fmt:formatDate value="${data.exchangeStartDate}"
                                                                    pattern="${dateFormat}"/>
                                                </div>
                                                <div style="font-size: 10px; margin-bottom: 25px; font-size: 10px;  padding: 0 5% 5% 5%;">
                                                    <spring:message code="date.end"/>
                                                    <spring:message code="date.format" var="dateFormat"/>
                                                    <fmt:formatDate value="${data.exchangeEndDate}"
                                                                    pattern="${dateFormat}"/>
                                                </div>
                                            </div>
                                        </div>

                                        <div style="padding: 0">
                                            <c:choose>
                                                <c:when test="${!empty data.requester.book.images && !data.requester.book.images[0].image.isImageNull}">
                                                    <img class="book-image"
                                                         src="<c:url value='/images/${data.requester.book.images[0].image.imageId}' />"
                                                         alt="bookImage"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="book-image"
                                                         src="<c:url value='/images/book.jpg' />" alt="book"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${data.requester.book.bookModel.title}"/>
                                                </h3>
                                            </div>
                                        </div>


                                    </div>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty completed.data}">
                                <div>
                                    <h4 class="uk-h6">
                                        <spring:message code="exchange.completed.empty"/>
                                    </h4>
                                </div>
                            </c:if>
                            <hr class="uk-divider-icon">

                            <nav aria-label="Pagination" class="uk-position-relative uk-margin">
                                <ul class="uk-pagination uk-flex-center uk-position-center">

                                    <%--                                    <!-- Botón Previous -->--%>
                                    <c:if test="${completed.metadata.currentPage > 0}">
                                        <li>
                                            <c:url var="prevPageUrl" value="">
                                                <c:param name="completed-page" value="${completed.metadata.currentPage - 1}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${prevPageUrl}">
                                                <span uk-pagination-previous></span>
                                                <spring:message code="publications.pagination.previous"/>
                                            </a>
                                        </li>
                                    </c:if>

                                    <!-- Botón de la primera página -->
                                    <c:if test="${completed.metadata.currentPage > 1}">
                                        <li>
                                            <c:url var="firstPageUrl" value="">
                                                <c:param name="completed-page" value="0" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${firstPageUrl}"></a>
                                        </li>
                                    </c:if>

                                    <c:if test="${completed.metadata.currentPage - 2 > 0}">
                                        <li><span>...</span></li>
                                    </c:if>

                                    <%--                                    <!-- Páginas centrales -->--%>
                                    <c:forEach var="i" begin="${completed.metadata.currentPage > 0 ? completed.metadata.currentPage - 1 : 0}"
                                               end="${completed.metadata.currentPage + 1 <= completed.metadata.maxPage ? completed.metadata.currentPage + 1 : completed.metadata.maxPage}">
                                        <li class="${i == completed.metadata.currentPage ? 'uk-active' : ''}">
                                            <c:url var="centralPageUrl" value="">
                                                <c:param name="completed-page" value="${i}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${centralPageUrl}">${i + 1}</a> <!-- Mostrar i + 1 para la numeración -->
                                        </li>
                                    </c:forEach>


                                    <c:if test="${completed.metadata.currentPage + 2 < completed.metadata.maxPage}">
                                        <li><span>...</span></li>
                                    </c:if>

                                    <%--                                    <!-- Botón de la última página -->--%>
                                    <c:if test="${completed.metadata.currentPage + 1 < completed.metadata.maxPage}">
                                        <li>
                                            <c:url var="lastPageUrl" value="">
                                                <c:param name="completed-page" value="${completed.metadata.maxPage}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${lastPageUrl}">${completed.metadata.maxPage + 1}</a> <!-- Mostrar maxPage + 1 -->
                                        </li>
                                    </c:if>

                                    <%--                                    <!-- Botón Next -->--%>
                                    <c:if test="${completed.metadata.currentPage < completed.metadata.maxPage}">
                                        <li>
                                            <c:url var="nextPageUrl" value="">
                                                <c:param name="completed-page" value="${completed.metadata.currentPage + 1}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage}" />
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

                        </li>

                        <!-- Rejected -->

                        <li style="min-height: 40%; align-content: center" class="uk-container">
                            <c:if test="${!empty rejected.data}">
                                <c:forEach var="data" items="${rejected.data}">
    								
    								<c:set var="authorsListString" value="" />
									<c:forEach var="author" items="${data.requester.book.bookModel.authors}">
										<c:choose>
											<c:when test="${empty authorsListString}">
												<c:set var="authorsListString" value="${author.authorName}" />
											</c:when>
											<c:otherwise>
												<c:set var="authorsListString" value="${authorsListString}, ${author.authorName}" />
											</c:otherwise>
										</c:choose>
									</c:forEach>
									
                                    <div class="uk-card uk-card-default exchange-card"
                                         onclick="selectCard(this,
                                                 '<c:out value="${data.requester.book.owner.username}"/>',
                                                 '<c:out value="${data.requester.book.owner.mail}"/>',
                                                 '<c:out value="${data.offerer.location.locationString}"/>',
                                                 '<c:out value="${data.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${authorsListString}"/>',
                                                 '<c:out value="${data.offerer.book.bookModel.title}"/>',
                                                 '<c:out value="${data.offerer.book.images}"/>',
                                                 '<c:out value="${data.exchangeId}"/>',
                                                 '<c:out value="${data.requester.book.owner.userId}"/>',
                                                 '<c:out value="${data.offerer.book.owner.userId}"/>',
                                                 '${data.isReviewable}')"
                                         uk-grid>

                                        <div style="padding: 0">
                                            <c:choose>
                                                <c:when test="${!empty data.offerer.book.images && !data.offerer.book.images[0].image.isImageNull}">
                                                    <img class="book-image"
                                                         src="<c:url value='/images/${data.offerer.book.images[0].image.imageId}' />"
                                                         alt="bookImage"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="book-image"
                                                         src="<c:url value='/images/book.jpg' />" alt="book"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${data.offerer.book.bookModel.title}"/>
                                                </h3>
                                            </div>
                                        </div>
                                        <div style="width: 40%; display: grid; justify-items: center; padding-left: 0px">

                                            <div class="arrow-icon" style="padding: 0">
                                                <span uk-icon="icon: chevron-double-right; ratio: 2"></span>
                                            </div>

                                            <span class="uk-badge state-rejected"><spring:message
                                                    code="exchange.status.rejected"/></span>
                                            <div style="width: 400%;font-size: 10px; padding: 5%">
                                                <spring:message code="date.start"/>
                                                <spring:message code="date.format" var="dateFormat"/>
                                                <fmt:formatDate value="${data.exchangeStartDate}"
                                                                pattern="${dateFormat}"/>
                                            </div>
                                            <div style="width: 400%; margin-bottom: 25px;font-size: 10px; padding: 0 5% 5% 5%;">
                                                <spring:message code="date.end"/>
                                                <spring:message code="date.format" var="dateFormat"/>
                                                <fmt:formatDate value="${data.exchangeEndDate}"
                                                                pattern="${dateFormat}"/>
                                            </div>
                                        </div>
                                        <div style="padding: 0">
                                            <c:choose>
                                                <c:when test="${!empty data.requester.book.images && !data.requester.book.images[0].image.isImageNull}">
                                                    <img class="book-image"
                                                         src="<c:url value='/images/${data.requester.book.images[0].image.imageId}' />"
                                                         alt="bookImage"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <img class="book-image"
                                                         src="<c:url value='/images/book.jpg' />" alt="book"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <div class="card-text-container">
                                                <h3 class="card-text3">
                                                    <c:out value="${data.requester.book.bookModel.title}"/>
                                                </h3>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty rejected.data}">
                                <div>
                                    <h4 class="uk-h6">
                                        <spring:message code="exchange.rejected.empty"/>
                                    </h4>
                                </div>
                            </c:if>
                            <hr class="uk-divider-icon">

                            <nav aria-label="Pagination" class="uk-position-relative uk-margin">
                                <ul class="uk-pagination uk-flex-center uk-position-center">

                                    <%--                                    <!-- Botón Previous -->--%>
                                    <c:if test="${rejected.metadata.currentPage > 0}">
                                        <li>
                                            <c:url var="prevPageUrl" value="">
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage - 1}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${prevPageUrl}">
                                                <span uk-pagination-previous></span>
                                                <spring:message code="publications.pagination.previous"/>
                                            </a>
                                        </li>
                                    </c:if>

                                    <!-- Botón de la primera página -->
                                    <c:if test="${rejected.metadata.currentPage > 1}">
                                        <li>
                                            <c:url var="firstPageUrl" value="">
                                                <c:param name="rejected-page" value="0" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${firstPageUrl}"></a>
                                        </li>
                                    </c:if>

                                    <c:if test="${rejected.metadata.currentPage - 2 > 0}">
                                        <li><span>...</span></li>
                                    </c:if>

                                    <%--                                    <!-- Páginas centrales -->--%>
                                    <c:forEach var="i" begin="${rejected.metadata.currentPage > 0 ? rejected.metadata.currentPage - 1 : 0}"
                                               end="${rejected.metadata.currentPage + 1 <= rejected.metadata.maxPage ? rejected.metadata.currentPage + 1 : rejected.metadata.maxPage}">
                                        <li class="${i == rejected.metadata.currentPage ? 'uk-active' : ''}">
                                            <c:url var="centralPageUrl" value="">
                                                <c:param name="rejected-page" value="${i}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${centralPageUrl}">${i + 1}</a> <!-- Mostrar i + 1 para la numeración -->
                                        </li>
                                    </c:forEach>


                                    <c:if test="${rejected.metadata.currentPage + 2 < rejected.metadata.maxPage}">
                                        <li><span>...</span></li>
                                    </c:if>

                                    <%--                                    <!-- Botón de la última página -->--%>
                                    <c:if test="${rejected.metadata.currentPage + 1 < rejected.metadata.maxPage}">
                                        <li>
                                            <c:url var="lastPageUrl" value="">
                                                <c:param name="rejected-page" value="${rejected.metadata.maxPage}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
                                            </c:url>
                                            <a href="${lastPageUrl}">${rejected.metadata.maxPage + 1}</a> <!-- Mostrar maxPage + 1 -->
                                        </li>
                                    </c:if>

                                    <%--                                    <!-- Botón Next -->--%>
                                    <c:if test="${rejected.metadata.currentPage < rejected.metadata.maxPage}">
                                        <li>
                                            <c:url var="nextPageUrl" value="">
                                                <c:param name="rejected-page" value="${rejected.metadata.currentPage + 1}" />
                                                <c:param name="in-progress-page" value="${inProgress.metadata.currentPage}" />
                                                <c:param name="completed-page" value="${completed.metadata.currentPage}" />
                                                <c:param name="pending-page" value="${pending.metadata.currentPage}" />
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

                        </li>
                    </ul>
                </div>
            </div>


            <!-- contenedor derecho donde se ve la info del exchange -->
            <div class="uk-width-2-5" uk-sticky>
                <div class="uk-container">
                    <div class="uk-grid uk-card uk-card-default uk-card-body exchange-info-container">
                        <div style="padding-left: 0" id="no-selection-message" class="uk-h6">
                            <h4 class="uk-h6">
                                <spring:message code="exchange.choose.message"/>
                            </h4>
                        </div>

                        <div id="exchange-details" style="display: none; padding: 5%;">
                            <h3 id="info-requester-username"><spring:message code="exchange.with"/></h3>
                            <p id="info-requester-mail"><spring:message code="exchange.with_email"/></p>
                            <p id="info-requester-location"><spring:message code="exchange.location"/></p>

                            <h4><spring:message code="exchange.your_book"/></h4>
                            <p id="info-offered-book-title"><spring:message code="exchange.book.title"/></p>
                            <p id="info-offered-book-authors"><spring:message code="exchange.book.authors"/></p>
                            <p id="info-offered-book-edition"><spring:message code="exchange.book.edition"/></p>

                            <div id="info-offered-book-images" uk-grid></div>

                            <div>
                                <button style="width: 100%" id="add-review-button" class="uk-button uk-button-primary"
                                        uk-toggle="target: #modal-add-review">
                                    <spring:message code="exchange.button.add_review"/>
                                </button>
                            </div>

                            <div id="modal-add-review" uk-modal>
                                <div class="uk-modal-dialog uk-modal-body">
                                    <h2 class="uk-modal-title"><spring:message
                                            code="exchange.add_review.title"/></h2>

                                    <form:form action="${submitReview}" method="post" modelAttribute="userReviewForm">

                                        <div class="form-group uk-margin-top uk-margin-bottom">
                                            <label><spring:message code="review.rating.label"/></label>

                                            <div class="star-rating">

                                                <form:radiobutton path="userReviewRating" value="5" id="star5"/>
                                                <label for="star5" title="5 stars">
                                                    <span uk-icon="icon: star; ratio: 1.5"></span>
                                                </label>

                                                <form:radiobutton path="userReviewRating" value="4" id="star4"/>
                                                <label for="star4" title="4 stars">
                                                    <span uk-icon="icon: star; ratio: 1.5"></span>
                                                </label>

                                                <form:radiobutton path="userReviewRating" value="3" id="star3"/>
                                                <label for="star3" title="3 stars">
                                                    <span uk-icon="icon: star; ratio: 1.5"></span>
                                                </label>

                                                <form:radiobutton path="userReviewRating" value="2" id="star2"/>
                                                <label for="star2" title="2 stars">
                                                    <span uk-icon="icon: star; ratio: 1.5"></span>
                                                </label>

                                                <form:radiobutton path="userReviewRating" value="1" id="star1"/>
                                                <label for="star1" title="1 star">
                                                    <span uk-icon="icon: star; ratio: 1.5"></span>
                                                </label>

                                            </div>
                                        </div>

                                        <div class="uk-margin">
                                            <label for="reviewDescription"><spring:message
                                                    code="review.comments.label"/></label>
                                            <form:textarea path="reviewDescription" rows="4" class="uk-textarea"/>
                                        </div>

                                        <form:hidden path="exchangeId"/>

                                        <p class="uk-text-right">
                                            <button class="uk-button uk-button-default uk-modal-close" type="button">
                                                <spring:message code="exchange.button.cancel"/>
                                            </button>
                                            <button class="uk-button uk-button-primary" type="submit">
                                                <spring:message code="exchange.button.accept"/>
                                            </button>
                                        </p>

                                    </form:form>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


</body>

<script>
    function selectCard(card, requesterUsername, requesterMail, requesterLocation, offeredBookTitle, offeredBookAuthors, offeredBookEdition, offeredBookImages, exchangeId, reviewerId, subjectId, isReviewable) {
        // Remover la clase 'selected-card' de todas las tarjetas
        document.querySelectorAll('.exchange-card').forEach(function (el) {
            el.classList.remove('selected-card');
        });

        // Agregar la clase 'selected-card' a la tarjeta clickeada
        card.classList.add('selected-card');

        // Mostrar la sección de detalles y ocultar el mensaje de selección
        document.getElementById('no-selection-message').style.display = 'none';
        document.getElementById('exchange-details').style.display = 'block';

        // Actualizar la información en la columna izquierda
        document.getElementById('info-requester-username').textContent = "<spring:message code="exchange.with"/>" + " " + requesterUsername;
        document.getElementById('info-requester-mail').textContent = "<spring:message code="exchange.with_email"/>" + " " + requesterMail;
        document.getElementById('info-requester-location').textContent = "<spring:message code="exchange.location"/>" + requesterLocation;
        document.getElementById('info-offered-book-title').textContent = "<spring:message code="exchange.book.title"/>" + " " + offeredBookTitle;
        document.getElementById('info-offered-book-authors').textContent = "<spring:message code="exchange.book.authors"/>" + " " + offeredBookAuthors;
        document.getElementById('info-offered-book-edition').textContent = "<spring:message code="exchange.book.edition"/>" + " " + offeredBookEdition;

        if (isReviewable === 'true') {
            document.getElementById('add-review-button').style.display = 'block';
        } else {
            document.getElementById('add-review-button').style.display = 'none';
        }

        // Actualizar los campos ocultos del formulario de reseña
        document.querySelector('input[name="exchangeId"]').value = exchangeId;
        document.querySelector('input[name="reviewerId"]').value = reviewerId;
        document.querySelector('input[name="subjectId"]').value = subjectId;

        // Limpiar imágenes anteriores
        const imageContainer = document.getElementById('info-offered-book-images');
        imageContainer.innerHTML = '';

        // Añadir imágenes del libro ofertado
        offeredBookImages.forEach(function (imageUrl) {
            const imgElement = document.createElement('img');
            imgElement.src = imageUrl;
            imgElement.className = 'uk-border-rounded';
            imgElement.alt = 'Imagen del libro';
            imgElement.style.width = '100%'; // Asegúrate de que las imágenes se ajusten bien
            const divElement = document.createElement('div');
            divElement.className = 'uk-width-1-4';
            divElement.appendChild(imgElement);
            imageContainer.appendChild(divElement);
        });
    }

    // Inicialmente, mostrar el mensaje de selección
    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('no-selection-message').style.display = 'block';
        document.getElementById('exchange-details').style.display = 'none';
        document.getElementById('add-review-button').style.display = 'block';
        document.getElementById('add-review-button').style.display = 'none';
    });
</script>

</html>

