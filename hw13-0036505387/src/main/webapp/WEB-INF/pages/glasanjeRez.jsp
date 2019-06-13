<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Rezultati glasanja</title>

    <%-- We do not have to worry about the context path if we use this --%>
    <c:url value="/style.jsp" var="styleUrl" />
    <link rel="stylesheet" href="${styleUrl}">
    <style type="text/css">
        table.rez td {text-align: center;}
    </style>
</head>
<body>
    <h1>Rezultati glasanja</h1>
    <p>Ovo su rezultati glasanja.</p>
    <table border="1" cellspacing="0" class="rez">
        <thead><tr><th>Bend</th><th>Broj glasova</th></tr></thead>
        <tbody>
            <c:forEach var="bandVoteCount" items="${bandVoteCounts}">
                <tr><td>${bandVoteCount.band.name}</td><td>${bandVoteCount.voteCount}</td></tr>
            </c:forEach>
        </tbody>
    </table>

    <h2>Grafički prikaz rezultata</h2>
    <img alt="Pie-chart" src="glasanje-grafika" width="400" height="400" />

    <h2>Rezultati u XLS formatu</h2>
    <p>Rezultati u XLS formatu dostupni su <a href="glasanje-xls">ovdje</a></p>

    <h2>Razno</h2>
    <p>Primjeri pjesama pobjedničkih bendova:</p>
    <ul>
        <c:forEach var="bandVoteCount" items="${bestVoted}">
            <li><a href="${bandVoteCount.band.song}" target="_blank">${bandVoteCount.band.name}</a></li>
        </c:forEach>
    </ul>
</body>
</html>