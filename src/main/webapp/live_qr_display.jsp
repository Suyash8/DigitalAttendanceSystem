<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Security Check --%>
<%
        if (session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
%>

<html>
<head>
        <title>Live QR Attendance</title>
        <style>
        body { font-family: sans-serif; display: flex; flex-direction: column; justify-content: center; align-items: center; height: 100vh; margin: 0; background-color: #333; color: white; }
        .qr-container { text-align: center; }
        h1 { margin-bottom: 20px; }
        img { border: 10px solid white; border-radius: 8px; }
        .footer { margin-top: 30px; font-size: 1.2em; color: #ccc; }
    </style>

    <%-- JavaScript to automatically refresh the page --%>
    <script>
        // The refresh rate is passed from the servlet
        const refreshRateInSeconds = ${refreshRate};

        setTimeout(function() {
            // Reload the page with the same courseId parameter
            window.location.href = "${pageContext.request.contextPath}/qr-session?courseId=${courseId}";
        }, refreshRateInSeconds * 1000); // setTimeout uses milliseconds
    </script>
</head>
<body>

    <div class="qr-container">
        <h1>Scan to Mark Your Attendance</h1>

        <%--
        The image source points to our servlet's SECOND URL pattern.
        We pass the unique code so the servlet knows what to generate.
        --%>
        <img src="${pageContext.request.contextPath}/qr-image?code=${uniqueCode}" alt="QR Code">

        <div class="footer">
        This code will refresh automatically.
        </div>
    </div>

</body>
</html>