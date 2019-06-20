<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
    <title>Create new blog entry</title>
  </head>
  <body>
    <%@ include file="../header.jsp" %>

    <!-- No action attribute means it will post to the same url. -->
    <form method="POST">
      <input type="hidden" name="id" value="${form.id}">

      <label for="title">Title: </label>
      <input type="text" id="title" name="title" value="${form.title}" required>
      <br>

      <label for="text">Text: </label>
      <textarea name="text" id="text" cols="30" rows="10" required>${form.text}</textarea>
      <br>

      <input type="submit" value="Create entry">
    </form>
  </body>
</html>
