<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Create new or update existing blog entry</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" href="<c:url value="/style.css"/>">
</head>
<body>
<%@ include file="../header.jsp" %>

<div class="center">
    <c:if test="${form.hasAnyErrors()}">
        <ul>
            <c:forEach var="error" items="${form.getErrors().values()}">
                <li class="error"><c:out value="${error}"/></li>
            </c:forEach>
        </ul>
    </c:if>
    <!-- No action attribute means it will post to the same url. -->
    <form method="POST">
        <input type="hidden" name="id" value="${form.id}">

        <label for="title">Title: </label>
        <input type="text" id="title" name="title" value="${form.title}" required size="33">
        <br>

        <label for="text">Text: </label>
        <textarea name="text" id="text" cols="30" rows="10" required>${form.text}</textarea>
        <br>

        <c:choose>
            <c:when test="${form.id == null}">
                <input type="submit" value="Create entry">
            </c:when>
            <c:otherwise>
                <input type="submit" value="Update entry">
            </c:otherwise>
        </c:choose>
    </form>
</div>
</body>
</html>
