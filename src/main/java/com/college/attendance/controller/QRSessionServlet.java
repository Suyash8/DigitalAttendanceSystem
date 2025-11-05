package com.college.attendance.controller;

import com.college.attendance.dao.AttendanceSessionDao;
import com.college.attendance.model.AttendanceSession;
import com.college.attendance.util.QRCodeUtil;
import com.google.zxing.WriterException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

// This servlet handles TWO URL patterns
@WebServlet(urlPatterns = {"/qr-session", "/qr-image"})
public class QRSessionServlet extends HttpServlet {

    private static final int SESSION_DURATION_SECONDS = 60;
    private AttendanceSessionDao sessionDao;

    @Override
    public void init() {
        sessionDao = new AttendanceSessionDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        // Router: Check which URL was called
        if ("/qr-session".equals(servletPath)) {
            handleSessionPage(req, resp);
        } else if ("/qr-image".equals(servletPath)) {
            handleImageRequest(req, resp);
        }
    }

    /**
     * Handles the request for the main QR display page.
     * It creates a new session and forwards to the JSP.
     */
    private void handleSessionPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));

        // 1. Generate a unique code
        String uniqueCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        // 2. Create session object with expiration time
        Instant now = Instant.now();
        Timestamp createdAt = Timestamp.from(now);
        Timestamp expiresAt = Timestamp.from(now.plus(SESSION_DURATION_SECONDS, ChronoUnit.SECONDS));

        AttendanceSession session = new AttendanceSession();
        session.setCourseId(courseId);
        session.setSessionCode(uniqueCode);
        session.setCreatedAt(createdAt);
        session.setExpiresAt(expiresAt);

        // 3. Save the session to the database
        sessionDao.createSession(session);

        // 4. Set attributes for the JSP
        req.setAttribute("courseId", courseId);
        req.setAttribute("uniqueCode", uniqueCode); // Pass the code for the image URL
        req.setAttribute("refreshRate", SESSION_DURATION_SECONDS);

        req.getRequestDispatcher("/live_qr_display.jsp").forward(req, resp);
    }

    /**
     * Handles the direct request for the QR code image.
     * It generates the image and writes it to the response output stream.
     */
    private void handleImageRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getParameter("code");
        if (code == null || code.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Code parameter is missing.");
            return;
        }

        try {
            byte[] qrImage = QRCodeUtil.generateQRCodeImage(code, 350, 350);

            resp.setContentType("image/png");
            resp.setContentLength(qrImage.length);

            OutputStream os = resp.getOutputStream();
            os.write(qrImage);

        } catch (WriterException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not generate QR code image.");
        }
    }
}