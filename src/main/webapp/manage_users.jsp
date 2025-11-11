<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/templates/header.jsp">
    <jsp:param name="title" value="Manage Users"/>
</jsp:include>

<div class="d-flex justify-content-between align-items-center mb-4">
    <h1 class="h2">User Management</h1>
    <div>
        <a href="${pageContext.request.contextPath}/admin_dashboard.jsp" class="btn btn-secondary">Back to Dashboard</a>
        <a href="${pageContext.request.contextPath}/user_form.jsp" class="btn btn-primary">Add New User</a>
    </div>
</div>

<div class="card custom-card">
    <div class="card-body">
        <table class="table">
            <thead>
                <tr><th>Name</th><th>Email</th><th>Role</th><th>Actions</th></tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${userList}">
                    <tr>
                        <td>${user.lastName}, ${user.firstName}</td>
                        <td>${user.email}</td>
                        <td><span class="badge text-bg-secondary">${user.role}</span></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/manage-users?action=edit&userId=${user.userId}" class="btn btn-sm btn-outline-secondary">Edit</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="/templates/footer.jsp"/>