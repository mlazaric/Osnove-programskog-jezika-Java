<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Main page</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" href="<c:url value="/style.css"/>">
</head>
<body>
<%@ include file="header.jsp" %>

<c:if test="${sessionScope['current.user.id'] == null}">
    <div class="center">
        <h1>Log in</h1>
        <c:if test="${form.hasAnyErrors()}">
            <ul>
                <c:forEach var="error" items="${form.getErrors().values()}">
                    <li class="error"><c:out value="${error}"/></li>
                </c:forEach>
            </ul>
        </c:if>

        <form action="<c:url value="/servleti/main"/>" method="POST">
            <label for="nick">Nickname:</label>
            <input type="text" name="nick" id="nick" required maxlength="30" value="<c:out value="${form.nick}" />">
            <br>

            <label for="password">Password:</label>
            <input type="password" name="password" id="password" required>
            <br>

            <input type="submit" value="Login">
        </form>
        <br>

        Register here: <a href="<c:url value="/servleti/register"/>">register</a>.
    </div>
</c:if>

<div class="contents">
    <h1>List of authors:</h1>
    <ul>
        <c:forEach items="${authors}" var="author">
            <li>
                <a href="<c:url value="/servleti/author/${author.nick}"/>">
                        ${author.nick} (${author.firstName} ${author.lastName})
                </a>
            </li>
        </c:forEach>
    </ul>
</div>
</body>
</html>
