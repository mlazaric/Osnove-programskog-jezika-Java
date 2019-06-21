<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Entries by ${nick}</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" href="<c:url value="/style.css"/>">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="contents">
    <h1>List of blog entries by ${nick}:</h1>
    <ul>
        <c:forEach items="${entries}" var="entry">
            <li>
                <a href="<c:url value="/servleti/author/${nick}/${entry.id}"/>">
                        ${entry.title}
                </a>
            </li>
        </c:forEach>
    </ul>

    <c:if test="${sessionScope['current.user.nick'].equals(nick)}">
        <a href="<c:url value="/servleti/author/${nick}/new"/>">
            Write a new blog entry.
        </a>
    </c:if>
</div>
</body>
</html>
