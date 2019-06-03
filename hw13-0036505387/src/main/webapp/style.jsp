<%@ page contentType="text/css;charset=UTF-8" pageEncoding="UTF-8" language="java" session="true" %>

body {
    <%
        String pickedBgCol = "FFFFFF";

        if (session.getAttribute("pickedBgCol") != null) {
            pickedBgCol = (String) session.getAttribute("pickedBgCol");
        }
    %>

    background: #<%= pickedBgCol %>;
}
