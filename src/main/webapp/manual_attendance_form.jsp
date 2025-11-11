<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- Import the JSTL core library --%>

<%-- Security Check: Ensure user is logged in --%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<html>
<head>
    <title>Manual Attendance - ${course.courseCode}</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        .container { max-width: 800px; margin: 0 auto; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        button { padding: 10px 20px; background-color: #28a745; color: white; border: none; cursor: pointer; border-radius: 4px; }
        button:hover { background-color: #218838; }
    </style>
</head>
<body>

<div class="container">
    <h2>Take Manual Attendance</h2>
    <h3>Course: ${course.courseName} (${course.courseCode})</h3>
    <hr>

    <%-- The form will POST back to the same servlet URL --%>
    <form action="${pageContext.request.contextPath}/manual-attendance" method="post">

        <%-- We need to pass the courseId back to the POST method --%>
        <input type="hidden" name="courseId" value="${course.courseId}">

        <table>
            <thead>
                <tr>
                    <th>Student Name</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="student" items="${studentRoster}">
                    <tr>
                        <td><c:out value="${student.lastName}, ${student.firstName}"/></td>
                        <td>
                            <%-- Get the student's status from the map we passed from the servlet --%>
                            <c:set var="currentStatus" value="${todaysRecords[student.userId]}"/>

                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio"
                                       id="present_${student.userId}" name="status_${student.userId}" value="Present" required
                                       ${(currentStatus == 'Present' or empty currentStatus) ? 'checked' : ''}>
                                <label class="form-check-label" for="present_${student.userId}">Present</label>
                            </div>

                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio"
                                       id="absent_${student.userId}" name="status_${student.userId}" value="Absent" required
                                       ${currentStatus == 'Absent' ? 'checked' : ''}>
                                <label class="form-check-label" for="absent_${student.userId}">Absent</label>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <br>
        <button type="submit">Submit Attendance</button>
    </form>

    <br>
    <a href="${pageContext.request.contextPath}/instructor-dashboard" class="btn btn-secondary mt-3">Back to Dashboard</a>

</div>

</body>
</html>