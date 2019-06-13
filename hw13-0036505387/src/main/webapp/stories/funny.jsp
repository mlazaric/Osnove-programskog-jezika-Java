<%@ page import="java.util.Random" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%!
    private Random random = new Random();
    private List<String> colors = List.of(
            "#FFFFFF", "#C0C0C0", "#808080", "#000000",
            "#FF0000", "#800000", "#FFFF00", "#808000",
            "#00FF00", "#008000", "#00FFFF", "#008080",
            "#0000FF", "#000080", "#FF00FF", "#800080");

    private String randomColor() {
        int index = random.nextInt(colors.size());

        return colors.get(index);
    }
%>

<html>
<head>
    <title>A funny story</title>

    <%-- We do not have to worry about the context path if we use this --%>
    <c:url value="/style.jsp" var="styleUrl" />
    <link rel="stylesheet" href="${styleUrl}">
</head>
<body style="color: <%= randomColor() %>">
    <p>A man takes his Rottweiler to the vet and asks, “My dog’s cross-eyed... is there anything you can do for him?”</p>

    <p>“Well,” says the vet, “let’s have a look at him.” So, he picks the dog up and examines his eyes, then checks his
        teeth.</p>

    <p>Finally, he says “I’m going to have to put him down.”</p>

    <p>“Why? Because he’s cross-eyed?”</p>

    <p>“No, because he’s really heavy.”</p>

    Source: <a href="http://www.inspire21.com/stories/humorstories/DogsandCats">http://www.inspire21.com/stories/humorstories/DogsandCats</a>.
</body>
</html>
