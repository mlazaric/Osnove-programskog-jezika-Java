<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Error</title>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <link rel="stylesheet" href="<c:url value="/style.css"/>">
</head>
<body>
<%@ include file="header.jsp" %>

<div class="contents">
    <h1>Error</h1>
    <p><c:out value="${message}"/></p>
</div>
</body>
</html>
