<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Values of trigonometric functions</title>

    <%-- We do not have to worry about the context path if we use this --%>
    <c:url value="/style.jsp" var="styleUrl" />
    <link rel="stylesheet" href="${styleUrl}">
</head>
<body>
    <table>
        <c:forEach var="value" items="${values}">
            <tr>
                <td>${value.angle}</td>
                <td>${value.sin}</td>
                <td>${value.cos}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
