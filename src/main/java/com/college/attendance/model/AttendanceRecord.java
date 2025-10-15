package com.college.attendance.model;

import java.sql.Date;
import java.sql.Timestamp;

public class AttendanceRecord {
    private int recordId;
    private int studentId;
    private int courseId;
    private Date lectureDate;
    private String status; // "Present", "Absent", "Late"
    private Timestamp recordedAt;

    // No-argument constructor
    public AttendanceRecord() {}

    // Getters and Setters
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getLectureDate() {
        return lectureDate;
    }

    public void setLectureDate(Date lectureDate) {
        this.lectureDate = lectureDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Timestamp recordedAt) {
        this.recordedAt = recordedAt;
    }
}