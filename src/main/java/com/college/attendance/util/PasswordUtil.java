package com.college.attendance.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    /**
     * Hashes a plain-text password using BCrypt.
     *
     * @param plainPassword The password to hash.
     * @return A salted and hashed password string.
     */
    public static String hashPassword(String plainPassword) {
        // The gensalt() method automatically handles random salting
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Checks if a plain-text password matches a hashed password.
     *
     * @param plainPassword  The password from user input.
     * @param hashedPassword The hashed password from the database.
     * @return true if the passwords match, false otherwise.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}