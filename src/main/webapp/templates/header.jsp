<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.title} - Digital Attendance</title>

    <!-- Bootstrap 5 CSS (for its grid and utilities, not for its look) -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">

    <!-- Google Fonts: Inter -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Our Custom Stylesheet -->
    <style>
        /* Define CSS variables for our color palette */
        :root {
            --primary-color: #6a5acd; /* A modern slate blue/purple */
            --background-color: #f4f7f6;
            --text-color: #333;
            --card-background: #ffffff;
            --border-color: #e9ecef;
        }

        body {
            font-family: 'Inter', sans-serif;
            background-color: var(--background-color);
            color: var(--text-color);
        }

        /* Custom Navbar Style */
        .navbar {
            background-color: var(--card-background) !important;
            border-bottom: 1px solid var(--border-color);
        }
        .navbar-brand, .nav-link {
            color: var(--text-color) !important;
            font-weight: 500;
        }
        .navbar-brand {
            font-weight: 700;
        }

        /* Custom Card Style */
        .custom-card {
            background-color: var(--card-background);
            border: 1px solid var(--border-color);
            border-radius: 12px; /* Softer, more modern corners */
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            transition: box-shadow 0.3s ease;
        }
        .custom-card:hover {
            box-shadow: 0 8px 12px rgba(0, 0, 0, 0.08);
        }

        /* Custom Button Style */
        .btn-primary {
            background-color: var(--primary-color);
            border: none;
            border-radius: 8px;
            font-weight: 600;
            padding: 10px 20px;
        }
        .btn-primary:hover {
            background-color: #594ab5; /* A slightly darker shade for hover */
        }
        .form-control:focus {
             border-color: var(--primary-color);
             box-shadow: 0 0 0 0.25rem rgba(106, 90, 205, 0.25);
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light mb-4">
    <div class="container"> <%-- Changed to fixed-width container for a more centered look on desktop --%>
        <c:choose>
    <c:when test="${sessionScope.user.role == 'instructor'}">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/instructor-dashboard">Digital Attendance</a>
    </c:when>
    <c:when test="${sessionScope.user.role == 'student'}">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/student-dashboard">Digital Attendance</a>
    </c:when>
    <c:when test="${sessionScope.user.role == 'admin'}">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/admin_dashboard.jsp">Digital Attendance</a>
    </c:when>
    <c:otherwise>
        <a class="navbar-brand" href="#">Digital Attendance</a>
    </c:otherwise>
</c:choose>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                 <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item">
                            <span class="navbar-text me-3">
                                Welcome, ${sessionScope.user.firstName}
                            </span>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login.jsp">Login</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<main class="container">