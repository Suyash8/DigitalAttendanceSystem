CREATE TABLE Users (
    user_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- Never store plain text passwords!
    role ENUM('student', 'instructor') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Courses (
    course_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL,
    course_name VARCHAR(255) NOT NULL,
    instructor_id INT UNSIGNED NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_instructor
        FOREIGN KEY (instructor_id) REFERENCES Users(user_id)
        ON DELETE RESTRICT -- Prevent deleting an instructor who has courses
        ON UPDATE CASCADE
);

CREATE TABLE Enrollments (
    enrollment_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    student_id INT UNSIGNED NOT NULL,
    course_id INT UNSIGNED NOT NULL,
    enrollment_date DATE NOT NULL,
    CONSTRAINT fk_student_enrollment
        FOREIGN KEY (student_id) REFERENCES Users(user_id)
        ON DELETE CASCADE, -- If a student is deleted, their enrollments are removed
    CONSTRAINT fk_course_enrollment
        FOREIGN KEY (course_id) REFERENCES Courses(course_id)
        ON DELETE CASCADE, -- If a course is deleted, its enrollments are removed
    UNIQUE KEY unique_enrollment (student_id, course_id) -- Prevents a student from enrolling in the same course twice
);

CREATE TABLE AttendanceRecords (
    record_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    student_id INT UNSIGNED NOT NULL,
    course_id INT UNSIGNED NOT NULL,
    lecture_date DATE NOT NULL,
    status ENUM('Present', 'Absent', 'Late') NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_student_attendance
        FOREIGN KEY (student_id) REFERENCES Users(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_course_attendance
        FOREIGN KEY (course_id) REFERENCES Courses(course_id)
        ON DELETE CASCADE,
    UNIQUE KEY unique_attendance_record (student_id, course_id, lecture_date) -- Prevents duplicate attendance entries
);

CREATE TABLE AttendanceSessions (
    session_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    course_id INT UNSIGNED NOT NULL,
    session_code VARCHAR(10) NOT NULL, -- The short, random code
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_course_session
        FOREIGN KEY (course_id) REFERENCES Courses(course_id)
        ON DELETE CASCADE
);

