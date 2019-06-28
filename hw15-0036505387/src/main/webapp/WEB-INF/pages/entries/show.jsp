<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${entry.title}</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" href="<c:url value="/style.css"/>">
</head>
<body>
<%@ include file="../header.jsp" %>

<div class="contents">
    <h1><c:out value="${entry.title}"/></h1>
    <p><c:out value="${entry.text}"/></p>

    <c:if test="${sessionScope['current.user.nick'].equals(entry.creator.nick)}">
        <a href="<c:url value="/servleti/author/${entry.creator.nick}/${entry.id}/edit"/>">
            Edit this blog entry.
        </a>
    </c:if>

    <h2>Comments:</h2>
    <c:forEach items="${entry.comments}" var="comment">
        <div class="comment">
            <c:out value="${comment.usersEMail}"/>: <c:out value="${comment.message}"/>
        </div>
    </c:forEach>

    <h2>Post a new comment:</h2>
    <c:if test="${form.hasAnyErrors()}">
        <ul>
            <c:forEach var="error" items="${form.getErrors().values()}">
                <li class="error"><c:out value="${error}"/></li>
            </c:forEach>
        </ul>
    </c:if>
    <!-- No action attribute means it will post to the same url. -->
    <form method="POST">
        <c:if test="${sessionScope['current.user.id'] == null}">
            <label for="email">Email address: </label>
            <input type="email" id="email" name="email" value="${form.email}" required size="33">
            <br>
        </c:if>

        <label for="message">Message: </label>
        <textarea name="message" id="message" cols="30" rows="10" required>${form.message}</textarea>
        <br>

        <input type="submit" value="Post comment">
    </form>
</div>
</body>
</html>
