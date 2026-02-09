package org.example.hash;

import java.security.MessageDigest;

/**
 * Clasa pentru hash-uirea parolelor
 */
public class PasswordHasher {
    /**
     * Metoda pentru hash-uirea parolei folosind SHA-256
     * @param password String, parola de hashuire
     * @return String, parola hashuita
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metoda pentru verificarea parolei
     * @param plainPassword String, parola nehashuita
     * @param hashedPassword String, parola hashuita
     * @return boolean, true daca parolele corespund, false altfel
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return hashPassword(plainPassword).equals(hashedPassword);
    }
}
