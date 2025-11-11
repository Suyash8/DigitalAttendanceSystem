<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/templates/header.jsp">
    <jsp:param name="title" value="Manage Enrollments"/>
</jsp:include>

<h1 class="h2 mb-4">Manage Enrollments for ${course.courseName}</h1>
<div class="card custom-card">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/admin/manage-courses" method="post">
            <input type="hidden" name="action" value="updateEnrollment">
            <input type="hidden" name="courseId" value="${course.courseId}">

            <p>Select the students to enroll in this course:</p>
            <div class="row">
                <c:forEach var="student" items="${allStudents}">
                    <div class="col-md-4">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" name="studentIds" value="${student.userId}" id="student_${student.userId}"
                                <c:forEach var="enrolled" items="${enrolledStudents}">
                                    ${enrolled.userId == student.userId ? 'checked' : ''}
                                </c:forEach>
                            >
                            <label class="form-check-label" for="student_${student.userId}">
                                ${student.lastName}, ${student.firstName}
                            </label>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="mt-4">
                <button type="submit" class="btn btn-primary">Save Enrollments</button>
                <a href="${pageContext.request.contextPath}/admin/manage-courses" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/templates/footer.jsp"/>