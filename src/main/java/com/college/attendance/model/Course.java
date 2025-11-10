package com.college.attendance.model;

import java.sql.Timestamp;

public class Course {
    private int courseId;
    private String courseCode;
    private String courseName;
    private int instructorId;
    private Timestamp createdAt;

    // No-argument constructor
    public Course() {}

    // Getters and Setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    private double overallAttendance;

    public double getOverallAttendance() {
        return overallAttendance;
    }

    public void setOverallAttendance(double overallAttendance) {
        this.overallAttendance = overallAttendance;
    }
}