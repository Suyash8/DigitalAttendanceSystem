<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/templates/header.jsp">
    <jsp:param name="title" value="Student Dashboard"/>
</jsp:include>

<div class="row">
    <!-- QR Scanner Column -->
    <div class="col-lg-5 mb-4">
        <div class="card custom-card">
            <div class="card-body text-center">
                <h5 class="card-title">Live Attendance Check-in</h5>
                <p class="text-muted">Point your camera at the screen in class.</p>
                <div id="qr-reader" style="width: 100%; border-radius: 8px;"></div>
                <div id="scan-progress" class="alert alert-info mt-3" style="display: none;"></div>
            </div>
        </div>
    </div>

    <!-- My Courses Column -->
    <div class="col-lg-7">
        <h1 class="h2 mb-4">My Courses</h1>
        <c:forEach var="course" items="${courses}">
            <div class="card custom-card mb-3">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="card-title mb-0">${course.courseName}</h5>
                            <small class="text-muted">${course.courseCode}</small>
                        </div>
                        <a href="${pageContext.request.contextPath}/student-log?courseId=${course.courseId}" class="btn btn-outline-primary btn-sm">View Log</a>
                    </div>
                    <p class="mb-1 mt-3">Your Attendance:</p>
                    <div class="progress" style="height: 20px;">
                        <div class="progress-bar" role="progressbar" style="width: ${course.overallAttendance}%;" aria-valuenow="${course.overallAttendance}" aria-valuemin="0" aria-valuemax="100">
                            <fmt:formatNumber value="${course.overallAttendance}" maxFractionDigits="1"/>%
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<%-- Hidden form and scanner script remain the same --%>
<form id="hidden-form" action="${pageContext.request.contextPath}/student-checkin" method="post" style="display: none;">
    <input type="hidden" id="attendanceCodes" name="attendanceCodes">
</form>
<script src="https://unpkg.com/html5-qrcode" type="text/javascript"></script>
<script>
    // The robust async/await scanner script from before goes here.
    // No changes are needed in the script itself.
    const REQUIRED_SCANS = 3;
    const scannedCodes = new Set();
    const scanProgressElement = document.getElementById('scan-progress');
    let isSubmitting = false;
    let html5QrcodeScanner;
    async function onScanSuccess(decodedText, decodedResult) {
        if (isSubmitting) { return; }
        scannedCodes.add(decodedText);
        const currentCount = scannedCodes.size;
        scanProgressElement.innerHTML = 'Code ' + currentCount + ' of ' + REQUIRED_SCANS + ' scanned.';
        scanProgressElement.style.display = 'block';
        if (currentCount >= REQUIRED_SCANS) {
            isSubmitting = true;
            scanProgressElement.innerHTML = 'Submitting...';
            try {
                await html5QrcodeScanner.clear();
                document.getElementById('attendanceCodes').value = Array.from(scannedCodes).join(',');
                document.getElementById('hidden-form').submit();
            } catch (error) {
                console.error("Scanner clear failed, submitting anyway.", error);
                document.getElementById('attendanceCodes').value = Array.from(scannedCodes).join(',');
                document.getElementById('hidden-form').submit();
            }
        }
    }
    function onScanFailure(error) {}
    html5QrcodeScanner = new Html5QrcodeScanner("qr-reader",{ fps: 10, qrbox: { width: 250, height: 250 } },false);
    html5QrcodeScanner.render(onScanSuccess, onScanFailure);
</script>

<jsp:include page="/templates/footer.jsp"/>