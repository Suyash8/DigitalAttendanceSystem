package com.college.attendance.util;

import com.college.attendance.dao.DatabaseConnection;
import com.college.attendance.dao.UserDao;
import com.college.attendance.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSeeder {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();

        // 1. Ensure Instructor exists
        User instructor = userDao.getUserByEmail("prof@college.edu");
        if (instructor == null) {
            instructor = new User();
            instructor.setFirstName("John");
            instructor.setLastName("Smith");
            instructor.setEmail("prof@college.edu");
            instructor.setPasswordHash(PasswordUtil.hashPassword("admin123"));
            instructor.setRole("instructor");
            int newId = userDao.createUser(instructor);
            instructor.setUserId(newId);
            System.out.println("Seeded Instructor.");
        }

        // 2. Ensure Student exists
        User student = userDao.getUserByEmail("student@college.edu");
        if (student == null) {
            student = new User();
            student.setFirstName("Jane");
            student.setLastName("Doe");
            student.setEmail("student@college.edu");
            student.setPasswordHash(PasswordUtil.hashPassword("learn123"));
            student.setRole("student");
            int newId = userDao.createUser(student);
            student.setUserId(newId);
            System.out.println("Seeded Student.");
        }

        if (userDao.getUserByEmail("admin@college.edu") == null) {
            User admin = new User();
            admin.setFirstName("App");
            admin.setLastName("Admin");
            admin.setEmail("admin@college.edu");
            admin.setPasswordHash(PasswordUtil.hashPassword("superadmin"));
            admin.setRole("admin");
            int newId = userDao.createUser(admin);
            admin.setUserId(newId);
            System.out.println("Seeded Admin: admin@college.edu / superadmin");
        }

        // 3. Seed a Course and Enrollment (using raw SQL for simplicity here)
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check if course exists
            PreparedStatement checkCourse = conn.prepareStatement("SELECT course_id FROM Courses WHERE course_code = 'CS101'");
            ResultSet rs = checkCourse.executeQuery();

            int courseId;
            if (rs.next()) {
                courseId = rs.getInt("course_id");
            } else {
                // Create Course
                PreparedStatement insertCourse = conn.prepareStatement(
                        "INSERT INTO Courses (course_code, course_name, instructor_id) VALUES (?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                insertCourse.setString(1, "CS101");
                insertCourse.setString(2, "Intro to Java Programming");
                insertCourse.setInt(3, instructor.getUserId());
                insertCourse.executeUpdate();
                ResultSet genKeys = insertCourse.getGeneratedKeys();
                genKeys.next();
                courseId = genKeys.getInt(1);
                System.out.println("Seeded Course: CS101");
            }

            // Check Enrollment
            PreparedStatement checkEnroll = conn.prepareStatement("SELECT * FROM Enrollments WHERE student_id = ? AND course_id = ?");
            checkEnroll.setInt(1, student.getUserId());
            checkEnroll.setInt(2, courseId);
            if (!checkEnroll.executeQuery().next()) {
                // Create Enrollment
                PreparedStatement insertEnroll = conn.prepareStatement(
                        "INSERT INTO Enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, CURRENT_DATE)");
                insertEnroll.setInt(1, student.getUserId());
                insertEnroll.setInt(2, courseId);
                insertEnroll.executeUpdate();
                System.out.println("Seeded Enrollment for Jane Doe in CS101");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}