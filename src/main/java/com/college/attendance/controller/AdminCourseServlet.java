package com.college.attendance.controller;

import com.college.attendance.dao.CourseDao;
import com.college.attendance.dao.UserDao;
import com.college.attendance.model.Course;
import com.college.attendance.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/manage-courses")
public class AdminCourseServlet extends HttpServlet {
    private CourseDao courseDao;
    private UserDao userDao;

    @Override
    public void init() {
        courseDao = new CourseDao();
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "edit":
            case "new":
                showCourseForm(req, resp);
                break;
            case "enroll":
                showEnrollmentForm(req, resp);
                break;
            default:
                listCourses(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "create":
                createOrUpdateCourse(req, resp);
                break;
            case "updateEnrollment":
                updateEnrollment(req, resp);
                break;
        }
    }

    private void listCourses(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("courseList", courseDao.getAllCourses());
        req.getRequestDispatcher("/manage_courses.jsp").forward(req, resp);
    }

    private void showCourseForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String courseIdParam = req.getParameter("courseId");
        if (courseIdParam != null) {
            req.setAttribute("courseToEdit", courseDao.getCourseById(Integer.parseInt(courseIdParam)));
        }
        req.setAttribute("instructors", userDao.getUsersByRole("instructor"));
        req.getRequestDispatcher("/course_form.jsp").forward(req, resp);
    }

    private void showEnrollmentForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        req.setAttribute("course", courseDao.getCourseById(courseId));
        req.setAttribute("enrolledStudents", courseDao.getStudentsByCourseId(courseId));
        req.setAttribute("allStudents", userDao.getUsersByRole("student"));
        req.getRequestDispatcher("/manage_enrollments.jsp").forward(req, resp);
    }

    private void createOrUpdateCourse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Course course = new Course();
        course.setCourseCode(req.getParameter("courseCode"));
        course.setCourseName(req.getParameter("courseName"));
        course.setInstructorId(Integer.parseInt(req.getParameter("instructorId")));
        
        String courseIdParam = req.getParameter("courseId");
        if (courseIdParam == null || courseIdParam.isEmpty()) {
            courseDao.createCourse(course);
        } else {
            course.setCourseId(Integer.parseInt(courseIdParam));
            courseDao.updateCourse(course);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/manage-courses");
    }

    private void updateEnrollment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int courseId = Integer.parseInt(req.getParameter("courseId"));
        // First, remove all existing enrollments for this course
        List<User> currentlyEnrolled = courseDao.getStudentsByCourseId(courseId);
        for (User student : currentlyEnrolled) {
            courseDao.removeEnrollment(student.getUserId(), courseId);
        }
        // Then, add the newly selected students
        String[] selectedStudentIds = req.getParameterValues("studentIds");
        if (selectedStudentIds != null) {
            for (String studentIdStr : selectedStudentIds) {
                courseDao.addEnrollment(Integer.parseInt(studentIdStr), courseId);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/manage-courses");
    }
}