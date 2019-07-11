<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>${name}</title>
</head>
<body>
    <h1>${name}</h1>

    Contains:
    <ul>
        <li>${numberOfLines} lines</li>
        <li>${numberOfCircles} circles</li>
        <li>${numberOfFilledCircles} filled circles</li>
        <li>${numberOfFilledTriangles} filled triangles</li>
    </ul>

    <img src="render?image=${name}" alt="${name}"><br>

    <a href="main">Go back!</a>
</body>
</html>
