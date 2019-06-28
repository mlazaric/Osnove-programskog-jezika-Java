<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Register</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" href="<c:url value="/style.css"/>">
</head>
<body class="center">
<%@ include file="header.jsp" %>

<h1>Register</h1>
<c:if test="${form.hasAnyErrors()}">
    <ul>
        <c:forEach var="error" items="${form.getErrors().values()}">
            <li class="error"><c:out value="${error}"/></li>
        </c:forEach>
    </ul>
</c:if>
<form action="<c:url value="/servleti/register"/>" method="POST">
    <label for="firstName">First name:</label>
    <input type="text" name="firstName" id="firstName" required maxlength="30"
           value="<c:out value="${form.firstName}" />">
    <br>

    <label for="lastName">Last name:</label>
    <input type="text" name="lastName" id="lastName" required maxlength="30" value="<c:out value="${form.lastName}" />">
    <br>

    <label for="nick">Nickname:</label>
    <input type="text" name="nick" id="nick" required maxlength="30" value="<c:out value="${form.nick}" />">
    <br>

    <label for="email">Email address:</label>
    <input type="email" name="email" id="email" required maxlength="30" value="<c:out value="${form.email}" />">
    <br>

    <label for="password">Password:</label>
    <input type="password" name="password" id="password" required>
    <br>

    <input type="submit" value="Register">
</form>
</body>
</html>
