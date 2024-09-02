<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<head>
    <title><spring:message code="add.publication.header"/></title>
</head>
<body>
<c:url var="postUrl" value="/singlePublication"/>
<form:form action="${postUrl}" method="post" modelAttribute="publicationForm">
    <div>
    <label>
        <spring:message code="add.publication.username"/>
        <form:input path="username" type="text"/>
    </label>
        <form:errors path="username" element="p" cssStyle="color: red;"/>
    </div>

    <div>
        <label>
            <spring:message code="add.publication.mail"/>
            <form:input path="mail" type="text"/>
        </label>
        <form:errors path="mail" element="p" cssStyle="color: red;"/>
    </div>

    <div>
        <label>
            <spring:message code="add.publication.isbn"/>
            <form:input path="isbn" type="text"/>
        </label>
        <form:errors path="isbn" element="p" cssStyle="color: red;"/>
    </div>

    <div>
        <label>
            <spring:message code="add.publication.title"/>
            <form:input path="title" type="text"/>
        </label>
        <form:errors path="title" element="p" cssStyle="color: red;"/>
    </div>

    <div>
        <label>
            <spring:message code="add.publication.author"/>
            <form:input path="author" type="text"/>
        </label>
        <form:errors path="author" element="p" cssStyle="color: red;"/>
    </div>

    <div>
        <label>
            <spring:message code="add.publication.editorial"/>
            <form:input path="editorial" type="text"/>
        </label>
        <form:errors path="editorial" element="p" cssStyle="color: red;"/>
    </div>

    <div>
        <label>
            <spring:message code="add.publication.description"/>
            <form:input path="description" type="text"/>
        </label>
        <form:errors path="description" element="p" cssStyle="color: red;"/>
    </div>

    <div>
        <label>
            <spring:message code="add.publication.edition"/>
            <form:input path="edition" type="text"/>
        </label>
        <form:errors path="edition" element="p" cssStyle="color: red;"/>
    </div>

    <div>
        <label>
            <spring:message code="add.publication.rating"/>
            <form:input path="rating" type="text"/>
        </label>
        <form:errors path="rating" element="p" cssStyle="color: red;"/>
    </div>

    <div>
        <label>
            <spring:message code="add.publication.image"/>
            <form:input path="image" type="text"/>
        </label>
        <form:errors path="image" element="p" cssStyle="color: red;"/>
    </div>

    <div>
        <label>
            <spring:message code="add.publication.location"/>
            <form:input path="location" type="text"/>
        </label>
        <form:errors path="location" element="p" cssStyle="color: red;"/>
    </div>
    <div>
        <input type="submit"/>
    </div>

</form:form>
</body>
</html>