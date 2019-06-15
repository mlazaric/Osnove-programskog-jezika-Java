<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>${poll.title}</title>
</head>
<body>
    <h1>${poll.title}</h1>
    <p>${poll.message}</p>
    <ol>
        <c:forEach var="option" items="${options}">
            <li><a href="glasanje-glasaj?id=${option.id}">${option.title}</a></li>
        </c:forEach>
    </ol>
</body>
</html>