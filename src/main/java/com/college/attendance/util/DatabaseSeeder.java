package com.college.attendance.util;

import com.college.attendance.dao.CourseDao;
import com.college.attendance.dao.UserDao;
import com.college.attendance.model.Course;
import com.college.attendance.model.User;

public class DatabaseSeeder {

    public static void main(String[] args) {
        System.out.println("Starting robust database seeding...");

        UserDao userDao = new UserDao();
        CourseDao courseDao = new CourseDao();

        // --- 1. Find or Create Users (Now returns full User objects) ---
        User admin = findOrCreateUser(userDao, "App", "Admin", "admin@college.edu", "superadmin", "admin");
        User instructor = findOrCreateUser(userDao, "John", "Smith", "prof@college.edu", "admin123", "instructor");
        User student1 = findOrCreateUser(userDao, "Jane", "Doe", "student@college.edu", "learn123", "student");
        User student2 = findOrCreateUser(userDao, "Peter", "Jones", "peter@college.edu", "learn123", "student");

        // --- 2. Find or Create Courses (Now returns full Course objects) ---
        Course javaCourse = findOrCreateCourse(courseDao, "CS101", "Intro to Java Programming", instructor.getUserId());
        Course physCourse = findOrCreateCourse(courseDao, "PHYS201", "Modern Physics", instructor.getUserId());

        // --- 3. Manage Enrollments (Now uses correct, non-zero IDs) ---
        enrollStudent(courseDao, student1.getUserId(), javaCourse.getCourseId());
        enrollStudent(courseDao, student2.getUserId(), javaCourse.getCourseId());
        enrollStudent(courseDao, student1.getUserId(), physCourse.getCourseId());
        
        System.out.println("Database seeding complete.");
    }

    private static User findOrCreateUser(UserDao dao, String firstName, String lastName, String email, String password, String role) {
        User user = dao.getUserByEmail(email);
        if (user == null) {
            User newUser = new User();
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setEmail(email);
            newUser.setPasswordHash(PasswordUtil.hashPassword(password));
            newUser.setRole(role);
            user = dao.createUser(newUser); // This now returns the full user object with its ID
            System.out.println("CREATED User: " + email + " with ID: " + user.getUserId());
        }
        return user;
    }

    private static Course findOrCreateCourse(CourseDao dao, String code, String name, int instructorId) {
        Course course = dao.getCourseByCode(code);
        if (course == null) {
            Course newCourse = new Course();
            newCourse.setCourseCode(code);
            newCourse.setCourseName(name);
            newCourse.setInstructorId(instructorId);
            course = dao.createCourse(newCourse); // This now returns the full course object with its ID
            System.out.println("CREATED Course: " + code + " with ID: " + course.getCourseId());
        }
        return course;
    }
    
    private static void enrollStudent(CourseDao dao, int studentId, int courseId) {
        if (!dao.isStudentEnrolled(studentId, courseId)) {
            dao.addEnrollment(studentId, courseId);
            System.out.println("ENROLLED Student ID " + studentId + " in Course ID " + courseId);
        }
    }
}