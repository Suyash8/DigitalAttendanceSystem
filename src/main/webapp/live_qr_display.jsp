<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Security Check --%>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<html>
<head>
    <title>Live QR Attendance</title>
    <style>
        body { font-family: sans-serif; display: flex; flex-direction: column; justify-content: center; align-items: center; height: 100vh; margin: 0; background-color: #333; color: white; }
        .qr-container { text-align: center; }
        h1 { margin-bottom: 20px; }
        #qr-code-image { border: 10px solid white; border-radius: 8px; background-color: #f0f0f0; min-width: 350px; min-height: 350px; }
        .footer { margin-top: 30px; font-size: 1.2em; color: #ccc; }
    </style>
</head>
<body>
    <div id="config"
         data-course-id="${courseId}"
         data-refresh-rate="${refreshRate}"
         data-context-path="${pageContext.request.contextPath}">
    </div>

    <div class="qr-container">
        <h1>Scan to Mark Your Attendance</h1>

        <img id="qr-code-image" src="" alt="Loading QR Code...">

        <div class="footer">
            <a href="${pageContext.request.contextPath}/instructor_dashboard.jsp" style="color: white;">End Session</a>
        </div>
    </div>

    <script>
        // --- Configuration ---
        const configDiv = document.getElementById('config');
        // Now, we read the values from the data attributes using the .dataset property
        const courseId = configDiv.dataset.courseId;
        const refreshRateInSeconds = configDiv.dataset.refreshRate;
        const contextPath = configDiv.dataset.contextPath;

        const qrImageElement = document.getElementById('qr-code-image');

        // This function is unchanged, but will now work correctly
        async function fetchAndUpdateQRCode() {
            try {
                const response = await fetch(contextPath + '/qr-code-data?courseId=' + courseId);
                if (!response.ok) {
                    console.error('Failed to fetch new QR code data');
                    return;
                }
                const data = await response.json();
                const newCode = data.code;
                qrImageElement.src = contextPath + '/qr-image?code=' + newCode;
                qrImageElement.alt = 'QR Code for ' + newCode;
            } catch (error) {
                console.error('Error during QR code fetch:', error);
            }
        }

        // --- Main Execution (Unchanged) ---
        document.addEventListener('DOMContentLoaded', fetchAndUpdateQRCode);
        setInterval(fetchAndUpdateQRCode, refreshRateInSeconds * 1000);
    </script>
</body>
</html>