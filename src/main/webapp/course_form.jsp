<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/templates/header.jsp">
    <jsp:param name="title" value="${empty courseToEdit ? 'Add Course' : 'Edit Course'}"/>
</jsp:include>

<h1 class="h2 mb-4">${empty courseToEdit ? 'Add New Course' : 'Edit Course'}</h1>
<div class="card custom-card">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/admin/manage-courses" method="post">
            <input type="hidden" name="action" value="create">
            <c:if test="${not empty courseToEdit}"><input type="hidden" name="courseId" value="${courseToEdit.courseId}"></c:if>

            <div class="mb-3">
                <label for="courseName" class="form-label">Course Name</label>
                <input type="text" class="form-control" id="courseName" name="courseName" value="${courseToEdit.courseName}" required>
            </div>
            <div class="mb-3">
                <label for="courseCode" class="form-label">Course Code</label>
                <input type="text" class="form-control" id="courseCode" name="courseCode" value="${courseToEdit.courseCode}" required>
            </div>
            <div class="mb-3">
                <label for="instructorId" class="form-label">Instructor</label>
                <select class="form-select" id="instructorId" name="instructorId" required>
                    <c:forEach var="instructor" items="${instructors}">
                        <option value="${instructor.userId}" ${courseToEdit.instructorId == instructor.userId ? 'selected' : ''}>
                            ${instructor.lastName}, ${instructor.firstName}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Save Course</button>
            <a href="${pageContext.request.contextPath}/admin/manage-courses" class="btn btn-secondary">Cancel</a>
        </form>
    </div>
</div>

<jsp:include page="/templates/footer.jsp"/>