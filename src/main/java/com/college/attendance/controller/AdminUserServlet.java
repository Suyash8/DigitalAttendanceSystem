package com.college.attendance.controller;

import com.college.attendance.dao.UserDao;
import com.college.attendance.model.User;
import com.college.attendance.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// Note the URL pattern starts with /admin/
@WebServlet("/admin/manage-users")
public class AdminUserServlet extends HttpServlet {
    private UserDao userDao;

    @Override
    public void init() {
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "edit":
                showEditForm(req, resp);
                break;
            default:
                listUsers(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action) {
            case "create":
                createUser(req, resp);
                break;
            case "update":
                updateUser(req, resp);
                break;
            default:
                listUsers(req, resp);
                break;
        }
    }

    private void listUsers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> userList = userDao.getAllUsers();
        req.setAttribute("userList", userList);
        req.getRequestDispatcher("/manage_users.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = Integer.parseInt(req.getParameter("userId"));
        User user = userDao.getUserById(userId);
        req.setAttribute("userToEdit", user);
        req.getRequestDispatcher("/user_form.jsp").forward(req, resp);
    }

    private void createUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User();
        user.setFirstName(req.getParameter("firstName"));
        user.setLastName(req.getParameter("lastName"));
        user.setEmail(req.getParameter("email"));
        user.setRole(req.getParameter("role"));
        // Hash the password before saving
        String plainPassword = req.getParameter("password");
        user.setPasswordHash(PasswordUtil.hashPassword(plainPassword));

        userDao.createUser(user);
        resp.sendRedirect(req.getContextPath() + "/admin/manage-users");
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = new User();
        user.setUserId(Integer.parseInt(req.getParameter("userId")));
        user.setFirstName(req.getParameter("firstName"));
        user.setLastName(req.getParameter("lastName"));
        user.setEmail(req.getParameter("email"));
        user.setRole(req.getParameter("role"));

        userDao.updateUser(user);
        resp.sendRedirect(req.getContextPath() + "/admin/manage-users");
    }
}