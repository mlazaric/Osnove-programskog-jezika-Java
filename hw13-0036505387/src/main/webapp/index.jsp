<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Index page</title>

    <%-- We do not have to worry about the context path if we use this --%>
    <c:url value="/style.jsp" var="styleUrl" />
    <link rel="stylesheet" href="${styleUrl}">
</head>
<body>
    <ul>
        <li><a href="colors.jsp">Background color chooser</a></li>
        <li><a href="trigonometric?a=0&b=90">Values of trigonometric functions</a></li>
        <li><a href="stories/funny.jsp">A funny story</a></li>
        <li><a href="report.jsp">OS usage</a></li>
        <li><a href="powers?a=1&b=100&n=3">Spreadsheet of powers</a></li>
        <li><a href="appinfo.jsp">Running time</a></li>
        <li><a href="glasanje">Vote for your favourite band</a></li>
    </ul>

    <form action="trigonometric" method="GET">
        First angle value:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
        Last angle value:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
        <input type="submit" value="Show table"><input type="reset" value="Reset">
    </form>

    <form action="powers" method="GET">
        First number: <input type="number" name="a" min="-100" max="100" step="1" value="1"><br>
        Last number: <input type="number" name="b" min="-100" max="100" step="1" value="100"><br>
        Maximum power: <input type="number" name="n" min="1" max="5" step="1" value="3"><br>
        <input type="submit" value="Download spreadsheet"><input type="reset" value="Reset">
    </form>
</body>
</html>
