package com.torneo.projectegestoresportsfinal.controladors;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {

    // Método para encriptar la contraseña
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();  // La contraseña encriptada en formato hexadecimal
    }

    // Método para verificar la contraseña
    public static boolean verifyPassword(String password, String storedHash) throws NoSuchAlgorithmException {
        String hashedPassword = hashPassword(password);
        return hashedPassword.equals(storedHash);
    }
}
