<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>List of JVD files:</title>
</head>
<body>
    <h1>The following JVD files have been uploaded:</h1>
    <ul>
        <c:forEach var="image" items="${images}">
            <li><a href="show?image=${image}">${image}</a></li>
        </c:forEach>
    </ul>

    <form method="POST">
        <label for="name">Name of the JVD file (must end with .jvd):</label>
        <input type="text" name="name" id="name"> <br>

        <label for="contents">Contents of the JVD file: </label>
        <textarea name="contents" id="contents" cols="30" rows="10"></textarea> <br>

        <input type="submit" value="Upload">
    </form>
</body>
</html>
