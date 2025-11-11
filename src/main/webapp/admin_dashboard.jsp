<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/templates/header.jsp">
    <jsp:param name="title" value="Admin Dashboard"/>
</jsp:include>

<h1 class="h2 mb-4">Admin Dashboard</h1>
<div class="row">
    <div class="col-md-6 mb-4">
        <div class="card custom-card">
            <div class="card-body">
                <h5 class="card-title">User Management</h5>
                <p class="card-text">Create, edit, and manage student and instructor accounts.</p>
                <a href="${pageContext.request.contextPath}/admin/manage-users" class="btn btn-primary">Manage Users</a>
            </div>
        </div>
    </div>
    <div class="col-md-6 mb-4">
        <div class="card custom-card">
            <div class="card-body">
                <h5 class="card-title">Course Management</h5>
                <p class="card-text">Create courses and manage student enrollments.</p>
                <a href="${pageContext.request.contextPath}/admin/manage-courses" class="btn btn-primary">Manage Courses</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/templates/footer.jsp"/>