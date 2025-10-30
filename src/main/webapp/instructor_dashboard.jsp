<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- This checks if a user is actually logged in. If not, it kicks them back to the login page. --%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
<head>
    <title>Instructor Dashboard</title>
</head>
<body>
    <%-- We can access the user object stored in the session --%>
    <h1>Welcome, ${sessionScope.user.firstName}!</h1>
    <p>You are logged in as an <strong>${sessionScope.user.role}</strong>.</p>
    <hr>
    <h3>Your Actions:</h3>
    <ul>
        <li><a href="${pageContext.request.contextPath}/manual-attendance?courseId=1">Take Manual Attendance for CS101</a></li>
        <li><a href="#">Start QR Code Session</a> (Coming Soon Phase 2)</li>
    </ul>
    <br>
    <a href="${pageContext.request.contextPath}/logout">Logout</a>
</body>
</html>