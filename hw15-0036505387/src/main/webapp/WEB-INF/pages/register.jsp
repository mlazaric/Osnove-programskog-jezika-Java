<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
    <title>Register</title>

    <style>
      span.error {
        color: darkred;
      }
    </style>
  </head>
  <body class="center">
    <%@ include file="header.jsp" %>

    <form action="<c:url value="/servleti/register"/>" method="POST">
      <label for="firstName">First name:</label>
      <input type="text" name="firstName" id="firstName" required maxlength="30" value="<c:out value="${form.firstName}" />">
      <c:if test="${form.hasErrorForAttribute('firstName')}">
        <span class="error"><c:out value="${form.getErrorForAttribute('firstName')}" /></span>
      </c:if>
      <br>

      <label for="lastName">Last name:</label>
      <input type="text" name="lastName" id="lastName" required maxlength="30" value="<c:out value="${form.lastName}" />">
      <c:if test="${form.hasErrorForAttribute('lastName')}">
        <span class="error"><c:out value="${form.getErrorForAttribute('lastName')}" /></span>
      </c:if>
      <br>

      <label for="nick">Nickname:</label>
      <input type="text" name="nick" id="nick" required maxlength="30" value="<c:out value="${form.nick}" />">
      <c:if test="${form.hasErrorForAttribute('nick')}">
        <span class="error"><c:out value="${form.getErrorForAttribute('nick')}" /></span>
      </c:if>
      <br>

      <label for="email">Email address:</label>
      <input type="email" name="email" id="email" required maxlength="30" value="<c:out value="${form.email}" />">
      <c:if test="${form.hasErrorForAttribute('email')}">
        <span class="error"><c:out value="${form.getErrorForAttribute('email')}" /></span>
      </c:if>
      <br>

      <label for="password">Password:</label>
      <input type="password" name="password" id="password" required>
      <c:if test="${form.hasErrorForAttribute('passwordHash')}">
        <span class="error"><c:out value="${form.getErrorForAttribute('passwordHash')}" /></span>
      </c:if>
      <br>

      <input type="submit" value="Register">
    </form>
  </body>
</html>
