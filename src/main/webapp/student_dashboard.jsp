<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- Security Check --%>
<%
    if (session.getAttribute("user") == null || !"student".equals(((com.college.attendance.model.User)session.getAttribute("user")).getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<html>
<head>
    <title>Scan Attendance Code</title>
    <style>
        body { font-family: sans-serif; padding: 20px; }
        .container { max-width: 500px; margin: 20px auto; text-align: center; }
        #qr-reader { width: 100%; border: 2px solid #ddd; border-radius: 8px; }
        #qr-reader__dashboard_section_csr button { background-color: #007bff !important; color: white !important; }
        .feedback { padding: 12px; margin: 15px 0; border-radius: 4px; font-size: 1.1em; }
        .success { color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; }
        .error { color: #721c24; background-color: #f8d7da; border: 1px solid #f5c6cb; }
        .info { color: #0c5460; background-color: #d1ecf1; border: 1px solid #bee5eb; }
        #hidden-form { display: none; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Point Camera at QR Code</h1>

        <div id="qr-reader"></div>

        <div id="feedback-container">
            <%-- Display feedback messages --%>
            <c:if test="${not empty param.message}">
                <div class="feedback success"><c:out value="${param.message}"/></div>
            </c:if>
            <c:if test="${not empty requestScope.errorMessage}">
                <div class="feedback error"><c:out value="${requestScope.errorMessage}"/></div>
            </c:if>
            <c:if test="${not empty requestScope.infoMessage}">
                <div class="feedback info"><c:out value="${requestScope.infoMessage}"/></div>
            </c:if>
            <%-- A placeholder for dynamic JS feedback --%>
            <div id="scan-progress" class="feedback info" style="display: none;"></div>
        </div>

        <form id="hidden-form" action="${pageContext.request.contextPath}/student-checkin" method="post">
            <input type="hidden" id="attendanceCodes" name="attendanceCodes">
        </form>

        <br>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>

    <script src="https://unpkg.com/html5-qrcode" type="text/javascript"></script>

    <script>
        // --- Configuration ---
        const REQUIRED_SCANS = 3;
        const scannedCodes = new Set();
        const scanProgressElement = document.getElementById('scan-progress');
        let isSubmitting = false;
        let html5QrcodeScanner;

        // CRITICAL CHANGE: The function is now declared as 'async'
        async function onScanSuccess(decodedText, decodedResult) {
            if (isSubmitting) {
                return;
            }

            scannedCodes.add(decodedText);

            const currentCount = scannedCodes.size;
            scanProgressElement.innerHTML = 'Code ' + currentCount + ' of ' + REQUIRED_SCANS + ' scanned successfully.';
            scanProgressElement.style.display = 'block';

            if (currentCount >= REQUIRED_SCANS) {
                isSubmitting = true;
                scanProgressElement.innerHTML = 'Requirement met. Stopping scanner and submitting...';
                console.log('Collected required codes. Attempting to stop scanner...');

                try {
                    // CRITICAL FIX: We 'await' the result of the clear() promise.
                    // The code will PAUSE here until the scanner is confirmed to be off.
                    await html5QrcodeScanner.clear();
                    console.log("Scanner stopped successfully.");

                    // This code will only run AFTER the scanner is off.
                    document.getElementById('attendanceCodes').value = Array.from(scannedCodes).join(',');
                    document.getElementById('hidden-form').submit();

                } catch (error) {
                    console.error("Failed to clear html5QrcodeScanner.", error);
                    // Even if clearing fails, we can still try to submit as a fallback.
                    document.getElementById('attendanceCodes').value = Array.from(scannedCodes).join(',');
                    document.getElementById('hidden-form').submit();
                }
            }
        }

        function onScanFailure(error) {
            // Ignore scan failures.
        }

        // --- Main Execution ---
        const successMessage = document.querySelector('.feedback.success');
        if (!successMessage) {
            html5QrcodeScanner = new Html5QrcodeScanner(
                "qr-reader",
                { fps: 10, qrbox: { width: 250, height: 250 } },
                false);
            
            html5QrcodeScanner.render(onScanSuccess, onScanFailure);
        } else {
            document.getElementById('qr-reader').style.display = 'none';
        }
    </script>
</body>
</html>