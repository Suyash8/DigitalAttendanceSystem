
package com.college.attendance.controller;

import com.college.attendance.dao.UserDao;
import com.college.attendance.model.User;
import com.college.attendance.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDao userDao;

    @Override
    public void init() {
        // Initialize DAO once when the servlet starts
        userDao = new UserDao();
    }

    // GET request just shows the login page
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    // POST request handles the form submission
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = userDao.getUserByEmail(email);

        if (user != null && PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            // LOGIN SUCCESSFUL
            HttpSession session = req.getSession();
            session.setAttribute("user", user);

            // Redirect based on role
            if ("instructor".equals(user.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/instructor_dashboard.jsp");
            } else if ("student".equals(user.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/student_dashboard.jsp");
            } else {
                // Fallback for any other roles (if any)
                req.setAttribute("errorMessage", "Your user role is not recognized.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } else {
            // LOGIN FAILED
            req.setAttribute("errorMessage", "Invalid email or password.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}