package com.college.attendance.filter;

import com.college.attendance.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// This filter protects all URLs under the "/admin/" path
@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        boolean isAdmin = false;
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if ("admin".equals(user.getRole())) {
                isAdmin = true;
            }
        }

        if (isAdmin) {
            // User is an admin, allow the request to proceed to the servlet
            chain.doFilter(request, response);
        } else {
            // User is not an admin, redirect to login page with an error
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?error=access_denied");
        }
    }
    // init() and destroy() methods can be left empty
    @Override public void init(FilterConfig filterConfig) throws ServletException {}
    @Override public void destroy() {}
}