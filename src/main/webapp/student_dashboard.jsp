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
        const scannedCodes = new Set(); // Use a Set to automatically handle duplicates
        const feedbackContainer = document.getElementById('feedback-container');
        const scanProgressElement = document.getElementById('scan-progress');

        // This function is called every time a QR code is successfully scanned
        function onScanSuccess(decodedText, decodedResult) {
            // Add the newly scanned code to our set.
            // If it's already in the set, the set's size will not change.
            scannedCodes.add(decodedText);

            // Update UI feedback
            const currentCount = scannedCodes.size;
            scanProgressElement.innerHTML = 'Code ' + currentCount + ' of ' + REQUIRED_SCANS + ' scanned successfully.';
            scanProgressElement.style.display = 'block';

            // Check if we have collected enough unique codes
            if (currentCount >= REQUIRED_SCANS) {
                console.log('Collected required number of codes. Submitting...');

                // Stop the scanner
                html5QrcodeScanner.clear().catch(error => {
                    console.error("Failed to clear html5QrcodeScanner.", error);
                });

                // Join the codes from the Set into a comma-separated string
                document.getElementById('attendanceCodes').value = Array.from(scannedCodes).join(',');

                // Submit the form
                document.getElementById('hidden-form').submit();
            }
        }

        function onScanFailure(error) {
            // This function is called when a scan attempt fails (e.g., no QR code in view)
            // We can ignore this to keep the UI clean.
            // console.warn(`Code scan error = ${error}`);
        }

        // --- Main Execution ---
        // Only start the scanner if we haven't already been marked present (i.e., no success message)
        const successMessage = document.querySelector('.feedback.success');
        if (!successMessage) {
            let html5QrcodeScanner = new Html5QrcodeScanner(
                "qr-reader",
                { fps: 10, qrbox: { width: 250, height: 250 } },
                /* verbose= */ false);

            html5QrcodeScanner.render(onScanSuccess, onScanFailure);
        } else {
             // If attendance is already marked, hide the scanner box
            document.getElementById('qr-reader').style.display = 'none';
        }
    </script>
</body>
</html>