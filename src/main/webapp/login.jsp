<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/templates/header.jsp">
    <jsp:param name="title" value="Login"/>
</jsp:include>

<div class="row justify-content-center mt-5">
    <div class="col-md-6 col-lg-5">
        <div class="card custom-card p-3"> <%-- Applying our custom class --%>
            <div class="card-body">
                <h2 class="card-title text-center mb-4" style="font-weight: 700;">Sign In</h2>

                <c:if test="${not empty requestScope.errorMessage}">
                    <div class="alert alert-danger" role="alert">
                        <c:out value="${requestScope.errorMessage}"/>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/login" method="post">
                    <div class="mb-3">
                        <label for="email" class="form-label fw-medium">Email Address</label>
                        <input type="email" class="form-control" id="email" name="email" required autofocus>
                    </div>
                    <div class="mb-4"> <%-- Increased margin bottom for more space --%>
                        <label for="password" class="form-label fw-medium">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary">Sign In</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/templates/footer.jsp"/>