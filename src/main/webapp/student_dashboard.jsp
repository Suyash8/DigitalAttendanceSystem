<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Security Check --%>
<%
    if (session.getAttribute("user") == null || !"student".equals(((com.college.attendance.model.User)session.getAttribute("user")).getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<html>
<head>
    <title>Student Dashboard</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        .container { max-width: 600px; margin: 40px auto; text-align: center; }
        .form-container { background: #f9f9f9; padding: 20px; border-radius: 8px; border: 1px solid #ddd; }
        input[type="text"] { width: 80%; padding: 12px; font-size: 1.5em; text-align: center; text-transform: uppercase; border: 1px solid #ccc; border-radius: 4px; }
        button { padding: 12px 25px; font-size: 1em; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; margin-top: 15px; }
        .feedback { padding: 10px; margin-bottom: 15px; border-radius: 4px; }
        .success { color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; }
        .error { color: #721c24; background-color: #f8d7da; border: 1px solid #f5c6cb; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome, ${sessionScope.user.firstName}!</h1>
        <p>Enter the code from the screen to mark your attendance.</p>
        <br>

        <div class="form-container">
            <%-- Display feedback messages from the servlet --%>
            <c:if test="${not empty param.message}">
                <div class="feedback success"><c:out value="${param.message}"/></div>
            </c:if>
            <c:if test="${not empty requestScope.errorMessage}">
                <div class="feedback error"><c:out value="${requestScope.errorMessage}"/></div>
            </c:if>

            <form action="${pageContext.request.contextPath}/student-checkin" method="post">
                <input type="text" name="attendanceCode" placeholder="ABC-123" required maxlength="6">
                <br>
                <button type="submit">Check In</button>
            </form>
        </div>

        <br><br>
        <a href="#">View My Attendance History</a> (Coming Soon)
        <br><br>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
</body>
</html>