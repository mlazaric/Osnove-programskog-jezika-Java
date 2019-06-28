<div class="header">
    <c:choose>
        <c:when test="${sessionScope['current.user.id'] == null}">
            You are not logged in.

            <a href="<c:url value="/servleti/main"/>">Log in</a>
            <a href="<c:url value="/servleti/register"/>">Register</a>
        </c:when>
        <c:otherwise>
            ${sessionScope['current.user.firstName']} ${sessionScope['current.user.lastName']}

            <a href="<c:url value="/servleti/logout"/>">Log out</a>
        </c:otherwise>
    </c:choose>
</div>