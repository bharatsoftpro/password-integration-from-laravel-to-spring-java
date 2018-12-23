package com.***.***.utility; //TODO: Change your package

import org.mindrot.jbcrypt.BCrypt; // TODO: you need to add the dependency in your pom.xml like below
//        <!-- https://mvnrepository.com/artifact/org.mindrot/jbcrypt -->
//        <dependency>
//            <groupId>org.mindrot</groupId>
//            <artifactId>jbcrypt</artifactId>
//            <version>0.3m</version>
//        </dependency>

import org.springframework.stereotype.Component;

@Component
/**
 * This utility can be use for entire application
 */
public class Hash {

    // Define the BCrypt workload to use when generating password hashes. 10-31 is a valid value.
    private static int workload = 10;

    /**
     * This method can be used to generate a string representing an account password
     * suitable for storing in a database. It will be an OpenBSD-style crypt(3) formatted
     * hash string of length=60
     * The bcrypt workload is specified in the above static variable, a value from 10 to 31.
     * A workload of 12 is a very reasonable safe default as of 2013.
     * This automatically handles secure 128-bit salt generation and storage within the hash.
     *
     * @param passwordPlaintext The account's plaintext password as provided during account creation,
     *                           or when changing an account's password.
     * @return String - a string of length 60 that is the bcrypt hashed password in crypt(3) format.
     */
    public static String make(String passwordPlaintext) {
        String salt = BCrypt.gensalt(workload);
        String hashedPassword = BCrypt.hashpw(passwordPlaintext, salt);

        return (hashedPassword);
    }

    /**
     * This method can be used to verify a computed hash from a plaintext (e.g. during a login
     * request) with that of a stored hash from a database. The password hash from the database
     * must be passed as the second variable.
     *
     * @param passwordPlaintext The account's plaintext password, as provided during a login request
     * @param storedHash        The account's stored password hash, retrieved from the authorization database
     * @return boolean - true if the password matches the password of the stored hash, false otherwise
     */
    public static boolean check(String passwordPlaintext, String storedHash) {
        boolean passwordVerified = false;

        if (null == storedHash || !storedHash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        passwordVerified = BCrypt.checkpw(passwordPlaintext, storedHash);

        return (passwordVerified);
    }
}

/**
 * POC fo testing
 */
class TestHash {

    public static void main(String[] args) {
        //Note: if your password start with prefix '$2y$' in laravel then you need to change '$2a$' before check during migration
        if (Hash.check("Test12345", "$2a$10$hrzKVFeo8zLvcyXFPm4iaeLo02asx.6LdLvkev2/Ub0TmVFbi3fV.") == false) {
            throw new Exception("PASSWORD_MISMATCH");
        }

        //request plain input mathch with db's stored hash
//        User user = userFromDB.findOneById(1)
//        if (Hash.check(input.getPassword(), user.getPassword()) == false) {
//            throw new Exception("PASSWORD_MISMATCH");
//        }

        //Make hasht to store into DB for new user like Laravel
        String newPasswordHash = Hash.make(input.getPassword());
        System.out.println(newPasswordHash);

    }
}