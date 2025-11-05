package com.college.attendance.controller;

import com.college.attendance.dao.AttendanceSessionDao;
import com.college.attendance.model.AttendanceSession;
import com.college.attendance.util.QRCodeUtil;
import com.google.gson.Gson; // New import
import com.google.zxing.WriterException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter; // New import
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map; // New import
import java.util.UUID;

// This servlet now handles THREE URL patterns
@WebServlet(urlPatterns = {"/qr-session", "/qr-image", "/qr-code-data"})
public class QRSessionServlet extends HttpServlet {

    // DISPLAY a new code every 2 seconds
    public static final int REFRESH_INTERVAL_SECONDS = 2;
    // But keep each code VALID for 10 seconds
    private static final int SESSION_VALIDITY_SECONDS = 10;

    private AttendanceSessionDao sessionDao;
    private Gson gson; // For JSON conversion

    @Override
    public void init() {
        sessionDao = new AttendanceSessionDao();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();

        // Router: Check which URL was called
        switch (servletPath) {
            case "/qr-session":
                handleSessionPage(req, resp);
                break;
            case "/qr-image":
                handleImageRequest(req, resp);
                break;
            case "/qr-code-data":
                handleCodeDataRequest(req, resp);
                break;
        }
    }

    /**
     * Handles the INITIAL request for the QR display page.
     */
    private void handleSessionPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // This method only sets up the page. The actual code generation is now handled by the API endpoint.
        req.setAttribute("courseId", req.getParameter("courseId"));
        req.setAttribute("refreshRate", REFRESH_INTERVAL_SECONDS);
        req.getRequestDispatcher("/live_qr_display.jsp").forward(req, resp);
    }

    /**
     * The NEW API endpoint that generates a code and returns it as JSON.
     */
    private void handleCodeDataRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));

        // 1. Generate a unique code
        String uniqueCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        // 2. Create session object with the longer expiration time
        Instant now = Instant.now();
        Timestamp createdAt = Timestamp.from(now);
        Timestamp expiresAt = Timestamp.from(now.plus(SESSION_VALIDITY_SECONDS, ChronoUnit.SECONDS));

        AttendanceSession session = new AttendanceSession();
        session.setCourseId(courseId);
        session.setSessionCode(uniqueCode);
        session.setCreatedAt(createdAt);
        session.setExpiresAt(expiresAt);

        // 3. Save the session to the database
        sessionDao.createSession(session);

        // 4. Send the new code back as a JSON response
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Create a simple Map to hold our data
        Map<String, String> responseData = Map.of("code", uniqueCode);

        // Use Gson to convert the Map to a JSON string and write it to the response
        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(responseData));
        out.flush();
    }

    /**
     * Handles the direct request for the QR code image. This logic is unchanged.
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