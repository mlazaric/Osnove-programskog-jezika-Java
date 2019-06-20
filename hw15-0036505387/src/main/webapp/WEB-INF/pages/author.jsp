<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
    <title>Entries by ${nick}</title>
  </head>
  <body>
    <%@ include file="header.jsp" %>

    <h5>List of blog entry by ${nick}:</h5>
    <ul>
      <c:forEach items="${entries}" var="entry">
        <li>
          <a href="<c:url value="/servleti/author/${nick}/${entry.id}"/>">
            ${entry.title}
          </a>
        </li>
      </c:forEach>
    </ul>

    <c:if test="${sessionScope['current.user.nick'].equals(nick)}">
      <a href="<c:url value="/servleti/author/${nick}/new"/>">
          Write a new blog entry.
      </a>
    </c:if>
  </body>
</html>
