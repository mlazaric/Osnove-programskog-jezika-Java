<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Glasanje za omiljeni bend</title>

    <%-- We do not have to worry about the context path if we use this --%>
    <c:url value="/style.jsp" var="styleUrl" />
    <link rel="stylesheet" href="${styleUrl}">
</head>
<body>
    <h1>Glasanje za omiljeni bend:</h1>
    <p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste
        glasali!</p>
    <ol>
        <c:forEach var="band" items="${bands}">
            <li><a href="glasanje-glasaj?id=${band.id}">${band.name}</a></li>
        </c:forEach>
    </ol>
</body>
</html>