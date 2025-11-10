<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- For formatting numbers --%>

<jsp:include page="/templates/header.jsp">
    <jsp:param name="title" value="Instructor Dashboard"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="h2">My Courses</h1>
    <%-- A button to create new courses could go here in a future phase --%>
</div>

<div class="row">
    <c:choose>
        <c:when test="${not empty courses}">
            <c:forEach var="course" items="${courses}">
                <div class="col-md-6 col-lg-4 mb-4">
                    <div class="card custom-card h-100">
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title">${course.courseName}</h5>
                            <h6 class="card-subtitle mb-2 text-muted">${course.courseCode}</h6>
                            <div class="mt-auto">
                                <p class="mb-2">Overall Attendance:</p>
                                <div class="progress" style="height: 20px;">
                                    <div class="progress-bar" role="progressbar" style="width: ${course.overallAttendance}%;" aria-valuenow="${course.overallAttendance}" aria-valuemin="0" aria-valuemax="100">
                                        <fmt:formatNumber value="${course.overallAttendance}" maxFractionDigits="1"/>%
                                    </div>
                                </div>
                                <div class="mt-4">
                                    <%-- In a future step, this will link to the detailed view --%>
                                    <a href="${pageContext.request.contextPath}/course-detail?courseId=${course.courseId}" class="btn btn-outline-primary btn-sm">View Details</a>
                                    <a href="${pageContext.request.contextPath}/manual-attendance?courseId=${course.courseId}" class="btn btn-outline-secondary btn-sm">Manual Entry</a>
                                    <a href="${pageContext.request.contextPath}/qr-session?courseId=${course.courseId}" class="btn btn-outline-secondary btn-sm">Start QR Session</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="col-12">
                <div class="alert alert-info">You are not currently assigned to any courses.</div>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/templates/footer.jsp"/>