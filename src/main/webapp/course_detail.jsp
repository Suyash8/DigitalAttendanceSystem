<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/templates/header.jsp">
    <jsp:param name="title" value="Course Details"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h1 class="h2">${course.courseName}</h1>
        <h2 class="h5 text-muted">${course.courseCode}</h2>
    </div>
    <a href="${pageContext.request.contextPath}/instructor-dashboard" class="btn btn-secondary">Back to Dashboard</a>
</div>

<div class="card custom-card">
    <div class="card-body">
        <h5 class="card-title">Attendance Grid</h5>
        <div class="table-responsive">
            <table class="table table-bordered table-hover text-center">
                <thead>
                    <tr>
                        <th class="text-start">Student Name</th>
                        <%-- Loop through the unique dates to create the table headers --%>
                        <c:forEach var="lectureDate" items="${dates}">
                            <th>
                                <fmt:formatDate value="${lectureDate}" pattern="MMM dd"/>
                            </th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <%-- Loop through each student to create a row --%>
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td class="text-start">${student.lastName}, ${student.firstName}</td>

                            <%-- For each student, loop through the dates again to create the cells --%>
                            <c:forEach var="lectureDate" items="${dates}">
                                <%-- Construct the map key: "studentId-yyyy-MM-dd" --%>
                                <c:set var="mapKey" value="${student.userId}-${lectureDate}"/>
                                <c:set var="status" value="${attendanceMap[mapKey]}"/>

                                <c:choose>
                                    <c:when test="${status == 'Present'}">
                                        <td class="bg-success-subtle text-success-emphasis">P</td>
                                    </c:when>
                                    <c:when test="${status == 'Absent'}">
                                        <td class="bg-danger-subtle text-danger-emphasis">A</td>
                                    </c:when>
                                    <c:otherwise>
                                        <%-- If no record exists for that student on that day --%>
                                        <td>-</td>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="/templates/footer.jsp"/>