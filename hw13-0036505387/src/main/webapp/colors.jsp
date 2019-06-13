<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Background color chooser</title>

    <%-- We do not have to worry about the context path if we use this --%>
    <c:url value="/style.jsp" var="styleUrl" />
    <link rel="stylesheet" href="${styleUrl}">
</head>
<body>
    <a href="setcolor?pickedBgCol=FFFFFF">WHITE</a>
    <a href="setcolor?pickedBgCol=FF0000">RED</a>
    <a href="setcolor?pickedBgCol=00FF00">GREEN</a>
    <a href="setcolor?pickedBgCol=00FFFF">CYAN</a>
</body>
</html>
