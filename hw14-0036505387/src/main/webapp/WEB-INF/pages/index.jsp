<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>List of available polls</title>
</head>
<body>
    <ul>
        <c:forEach items="${polls}" var="poll">
            <li>
                <a href="glasanje?pollID=${poll.id}">${poll.title}</a> <br>
                ${poll.message}
            </li>
        </c:forEach>
    </ul>
</body>
</html>
