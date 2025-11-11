<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/templates/header.jsp">
    <jsp:param name="title" value="Manage Courses"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="h2">Course Management</h1>
    <a href="${pageContext.request.contextPath}/admin/manage-courses?action=new" class="btn btn-primary">Add New Course</a>
</div>

<div class="card custom-card">
    <div class="card-body">
        <table class="table">
            <thead><tr><th>Course Name</th><th>Code</th><th>Instructor</th><th>Actions</th></tr></thead>
            <tbody>
                <c:forEach var="course" items="${courseList}">
                    <tr>
                        <td>${course.courseName}</td>
                        <td>${course.courseCode}</td>
                        <td>${course.instructorName}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/manage-courses?action=edit&courseId=${course.courseId}" class="btn btn-sm btn-outline-secondary">Edit</a>
                            <a href="${pageContext.request.contextPath}/admin/manage-courses?action=enroll&courseId=${course.courseId}" class="btn btn-sm btn-outline-info">Manage Enrollments</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/templates/footer.jsp"/>