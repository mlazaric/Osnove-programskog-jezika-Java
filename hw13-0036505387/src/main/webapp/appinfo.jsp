<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%!
    private final static long SEC_IN_MS = 1000;
    private final static long MIN_IN_MS = 60 * SEC_IN_MS;
    private final static long HOUR_IN_MS = 60 * MIN_IN_MS;
    private final static long DAY_IN_MS = 24 * HOUR_IN_MS;

    private long appendPart(long difference, String nameOfPart, long timePartInMs, StringBuilder sb) {
        if (difference >= timePartInMs) {
            long numberOfParts = difference / timePartInMs;

            sb.append(numberOfParts);

            if (numberOfParts == 1) {
                sb.append(' ').append(nameOfPart).append(' ');
            }
            else {
                sb.append(' ').append(nameOfPart).append("s ");
            }
        }

        return difference % timePartInMs;
    }

    private String printFormattedTime() {
        long startTime = (Long) getServletConfig().getServletContext().getAttribute("startTime");
        long currentTime = System.currentTimeMillis();

        long difference = currentTime - startTime;

        StringBuilder sb = new StringBuilder();

        difference = appendPart(difference, "day", DAY_IN_MS, sb);
        difference = appendPart(difference, "hour", HOUR_IN_MS, sb);
        difference = appendPart(difference, "minute", MIN_IN_MS, sb);
        difference = appendPart(difference, "second", SEC_IN_MS, sb);
        difference = appendPart(difference, "millisecond", 1, sb);

        return sb.toString();
    }
%>

<html>
<head>
    <title>Running time</title>

    <%-- We do not have to worry about the context path if we use this --%>
    <c:url value="/style.jsp" var="styleUrl" />
    <link rel="stylesheet" href="${styleUrl}">
</head>
<body>
    <h4>The server has been running for <%= printFormattedTime() %></h4>
</body>
</html>
