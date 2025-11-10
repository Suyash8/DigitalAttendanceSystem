<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/templates/header.jsp">
    <jsp:param name="title" value="Attendance Log"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-4">
    <div>
        <h1 class="h2">Attendance Log: ${course.courseName}</h1>
        <h2 class="h5 text-muted">${course.courseCode}</h2>
    </div>
    <a href="${pageContext.request.contextPath}/student-dashboard" class="btn btn-secondary">Back to Dashboard</a>
</div>

<div class="card custom-card">
    <div class="card-body">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="record" items="${records}">
                    <tr>
                        <td><fmt:formatDate value="${record.lectureDate}" type="date" dateStyle="long"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${record.status == 'Present'}">
                                    <span class="badge text-bg-success">Present</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge text-bg-danger">Absent</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/templates/footer.jsp"/>