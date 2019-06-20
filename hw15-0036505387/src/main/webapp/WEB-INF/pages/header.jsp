<style>
    body {
        margin: 0px;
        height: 100%;
        background-color: #fafafa;
    }

    .center {
        text-align: center;
    }

    div.header {
        background-color: #b2dfdb;
        text-align: right;
        padding: 10px;
        -webkit-box-shadow: 0px 0px 5px 0px rgba(0,0,0,0.42);
        -moz-box-shadow: 0px 0px 5px 0px rgba(0,0,0,0.42);
        box-shadow: 0px 0px 5px 0px rgba(0,0,0,0.42);
        margin-bottom: 10px;
    }

    a, a:hover, a:active, a:visited, a:link {
        color: #004d40;
    }

    input[type=submit] {
        text-decoration: none;
        color: #ffffff;
        background-color: #26a69a;
        border: none;
        border-radius: 2px;
        padding: 8px 16px;
        transition-duration: 1s;
    }

    input[type=submit]:hover {
        background-color: #2bbbad;
        transition-duration: 1s;
    }

    input[type=submit]:active {
        background-color: #1d7d74;
        transition-duration: 1s;
    }

    form {
        text-align: right;
        display: inline-block;
    }

    form input {
        margin-bottom: 8px;
    }

    form input[type=submit] {
        position: relative;
        left: -20%;
    }

</style>

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