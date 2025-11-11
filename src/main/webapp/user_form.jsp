<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/templates/header.jsp">
    <jsp:param name="title" value="${empty userToEdit ? 'Add User' : 'Edit User'}"/>
</jsp:include>

<h1 class="h2 mb-4">${empty userToEdit ? 'Add New User' : 'Edit User'}</h1>

<div class="card custom-card">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/admin/manage-users" method="post">
            <%-- Use a hidden field to determine if this is a create or update action --%>
            <input type="hidden" name="action" value="${empty userToEdit ? 'create' : 'update'}">
            <c:if test="${not empty userToEdit}">
                <input type="hidden" name="userId" value="${userToEdit.userId}">
            </c:if>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="firstName" class="form-label">First Name</label>
                    <input type="text" class="form-control" id="firstName" name="firstName" value="${userToEdit.firstName}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label for="lastName" class="form-label">Last Name</label>
                    <input type="text" class="form-control" id="lastName" name="lastName" value="${userToEdit.lastName}" required>
                </div>
            </div>
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" value="${userToEdit.email}" required>
            </div>
            <c:if test="${empty userToEdit}">
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                </div>
            </c:if>
            <div class="mb-3">
                <label for="role" class="form-label">Role</label>
                <select class="form-select" id="role" name="role" required>
                    <option value="student" ${userToEdit.role == 'student' ? 'selected' : ''}>Student</option>
                    <option value="instructor" ${userToEdit.role == 'instructor' ? 'selected' : ''}>Instructor</option>
                    <option value="admin" ${userToEdit.role == 'admin' ? 'selected' : ''}>Admin</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Save Changes</button>
            <a href="${pageContext.request.contextPath}/admin/manage-users" class="btn btn-secondary">Cancel</a>
        </form>
    </div>
</div>

<jsp:include page="/templates/footer.jsp"/>