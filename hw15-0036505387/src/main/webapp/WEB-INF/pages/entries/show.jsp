<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
    <title>${entry.title}</title>
  </head>
  <body>
    <%@ include file="../header.jsp" %>

    <h1><c:out value="${entry.title}"/></h1>
    <p><c:out value="${entry.text}"/></p>

    <c:if test="${sessionScope['current.user.nick'].equals(entry.creator.nick)}">
      <a href="<c:url value="/servleti/author/${entry.creator.nick}/${entry.id}/edit"/>">
        Edit this blog entry.
      </a>
    </c:if>

    <h3>Comments:</h3>
    <c:forEach items="${entry.comments}" var="comment">
      <div class="comment">
        <c:out value="${comment.usersEMail}"/>: <c:out value="${comment.message}"/>
      </div>
    </c:forEach>

    <!-- No action attribute means it will post to the same url. -->
    <form method="POST">
      <label for="email">Email address: </label>
      <input type="email" id="email" name="email" value="${form.email}" required>
      <br>

      <label for="message">Message: </label>
      <textarea name="message" id="message" cols="30" rows="10" required>
        ${form.message}
      </textarea>
      <br>

      <input type="submit" value="Post comment">
    </form>
  </body>
</html>
