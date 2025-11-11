package com.college.attendance.controller;

import com.college.attendance.dao.ReportDao;
import com.college.attendance.model.Course;
import com.college.attendance.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/instructor-dashboard")
public class InstructorDashboardServlet extends HttpServlet {
    private ReportDao reportDao;

    @Override
    public void init() {
        reportDao = new ReportDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("\n--- DEBUG: InstructorDashboardServlet doGet() ---");
        
        HttpSession session = req.getSession(false);
        if (session == null) {
            System.out.println("DEBUG: Session is null. Redirecting to login.");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            System.out.println("DEBUG: User object in session is null. Redirecting to login.");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // --- Step 1: Check the User Object from the Session ---
        User instructor = (User) userObj;
        System.out.println("DEBUG: User found in session.");
        System.out.println("  - User ID: " + instructor.getUserId());
        System.out.println("  - User Name: " + instructor.getFirstName() + " " + instructor.getLastName());
        System.out.println("  - User Role: " + instructor.getRole());

        if (!"instructor".equals(instructor.getRole())) {
            System.out.println("DEBUG: User is not an instructor. Redirecting.");
            // Handle this case properly, maybe redirect to an error page or their own dashboard
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // --- Step 2: Call the DAO ---
        int instructorId = instructor.getUserId();
        System.out.println("DEBUG: Calling ReportDao.getCourseAttendanceSummaryForInstructor() with ID: " + instructorId);
        
        List<Course> courses = reportDao.getCourseAttendanceSummaryForInstructor(instructorId);

        // --- Step 3: Inspect the Result from the DAO ---
        if (courses == null) {
            System.out.println("DEBUG: DAO returned a null list. This is a problem in the DAO.");
        } else if (courses.isEmpty()) {
            System.out.println("DEBUG: DAO returned an EMPTY list. No courses found for instructor ID " + instructorId);
        } else {
            System.out.println("DEBUG: DAO returned " + courses.size() + " course(s).");
            for (Course course : courses) {
                System.out.println("  - Course: " + course.getCourseName() + " (ID: " + course.getCourseId() + ")");
                System.out.println("    - Attendance %: " + course.getOverallAttendance());
            }
        }
        
        // --- Step 4: Set Attribute and Forward ---
        System.out.println("DEBUG: Setting 'courses' attribute and forwarding to instructor_dashboard.jsp");
        req.setAttribute("courses", courses);
        req.getRequestDispatcher("/instructor_dashboard.jsp").forward(req, resp);
        System.out.println("--- DEBUG: Forward complete ---");
    }
}