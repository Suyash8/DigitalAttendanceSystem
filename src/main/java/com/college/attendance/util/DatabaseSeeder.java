package com.college.attendance.util;

import com.college.attendance.dao.UserDao;
import com.college.attendance.model.User;

public class DatabaseSeeder {
    public static void main(String[] args) {
        UserDao userDao = new UserDao();

        // 1. Create an Instructor
        if (userDao.getUserByEmail("prof@college.edu") == null) {
            User instructor = new User();
            instructor.setFirstName("John");
            instructor.setLastName("Smith");
            instructor.setEmail("prof@college.edu");
            // Hash the password "admin123" before saving
            instructor.setPasswordHash(PasswordUtil.hashPassword("admin123"));
            instructor.setRole("instructor");

            userDao.createUser(instructor);
            System.out.println("Seeded Instructor: prof@college.edu / admin123");
        } else {
            System.out.println("Instructor already exists.");
        }

        // 2. Create a Student (for later use)
        if (userDao.getUserByEmail("student@college.edu") == null) {
            User student = new User();
            student.setFirstName("Jane");
            student.setLastName("Doe");
            student.setEmail("student@college.edu");
            student.setPasswordHash(PasswordUtil.hashPassword("learn123"));
            student.setRole("student");

            userDao.createUser(student);
            System.out.println("Seeded Student: student@college.edu / learn123");
        } else {
            System.out.println("Student already exists.");
        }
    }
}