package com.college.attendance.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Get the current session, but don't create a new one if it doesn't exist
        HttpSession session = req.getSession(false);

        if (session != null) {
            // Invalidate the session, removing all attributes (like the "user" object)
            session.invalidate();
        }

        // Redirect the user back to the login page
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }
}