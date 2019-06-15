<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Rezultati glasanja</title>
    <style type="text/css">
        table.rez td {text-align: center;}
    </style>
</head>
<body>
    <h1>Rezultati glasanja</h1>
    <p>Ovo su rezultati glasanja.</p>
    <table border="1" cellspacing="0" class="rez">
        <thead><tr><th>Opcija</th><th>Broj glasova</th></tr></thead>
        <tbody>
            <c:forEach var="option" items="${options}">
                <tr><td>${option.title}</td><td>${option.voteCount}</td></tr>
            </c:forEach>
        </tbody>
    </table>

    <h2>Grafički prikaz rezultata</h2>
    <img alt="Pie-chart" src="glasanje-grafika?pollID=${id}" width="400" height="400" />

    <h2>Rezultati u XLS formatu</h2>
    <p>Rezultati u XLS formatu dostupni su <a href="glasanje-xls?pollID=${id}">ovdje</a></p>

    <h2>Razno</h2>
    <p>Opcije s najviše glasova:</p>
    <ul>
        <c:forEach var="option" items="${bestVoted}">
            <li><a href="${option.link}" target="_blank">${option.title}</a></li>
        </c:forEach>
    </ul>
</body>
</html>