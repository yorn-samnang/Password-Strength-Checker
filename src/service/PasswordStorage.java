//package service;
//
//import model.Password;
//import util.CryptoUtils;
//
//import javax.crypto.SecretKey;
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.Base64;
//
///**
// * Handles secure storage of passwords using AES-GCM + a master password.
// */
//public class PasswordStorage {
//
//    private final String fileName;
//    private final String masterPassword;
//
//    /**
//     * Main constructor – you can inject a custom master password.
//     */
//    public PasswordStorage(String fileName, String masterPassword) {
//        this.fileName = fileName;
//        this.masterPassword = masterPassword;
//    }
//
//    /**
//     * Convenience constructor – uses a default master password.
//     * This lets PasswordApp keep using:
//     *   new PasswordStorage("src/resources/passwords.txt");
//     */
////    public PasswordStorage(String fileName) {
////        this(fileName, "SuperStrongMasterKey!"); // TODO: change to a better secret
////    }
//
//    /**
//     * Load all passwords and decrypt them using masterPassword.
//     * Supports both:
//     *   old format: id::salt::iv::encrypted::createdAt
//     *   new format: id::username::salt::iv::encrypted::createdAt
//     */
//    public List<Password> loadPasswords() {
//        List<Password> list = new ArrayList<>();
//
//        try (BufferedReader reader =
//                     new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split("::");
//
//                // old: 5 parts, new: 6 parts
//                if (parts.length != 5 && parts.length != 6) {
//                    continue; // skip malformed
//                }
//
//                try {
//                    int id = Integer.parseInt(parts[0]);
//
//                    String username;
//                    int saltIdx;
//                    int ivIdx;
//                    int encIdx;
//                    int tsIdx;
//
//                    if (parts.length == 6) {
//                        // id::username::salt::iv::encrypted::createdAt
//                        username = parts[1];
//                        saltIdx = 2;
//                        ivIdx = 3;
//                        encIdx = 4;
//                        tsIdx = 5;
//                    } else {
//                        // id::salt::iv::encrypted::createdAt   (no username stored)
//                        username = "N/A";
//                        saltIdx = 1;
//                        ivIdx = 2;
//                        encIdx = 3;
//                        tsIdx = 4;
//                    }
//
//                    byte[] salt = Base64.getDecoder().decode(parts[saltIdx]);
//                    byte[] iv = Base64.getDecoder().decode(parts[ivIdx]);
//                    String encrypted = parts[encIdx];
//                    String createdAt = parts[tsIdx];
//
//                    SecretKey key = CryptoUtils.deriveKey(masterPassword.toCharArray(), salt);
//                    String decrypted = CryptoUtils.decrypt(encrypted, key, iv);
//
//                    // Assuming Password(int id, String username, String decrypted, String storedValue, String createdAt)
//                    list.add(new Password(id, username, decrypted, encrypted, createdAt));
//
//                } catch (Exception ignored) {
//                    // skip corrupted/undecryptable line
//                }
//            }
//
//        } catch (FileNotFoundException e) {
//            System.out.println("(No saved passwords yet)");
//        } catch (IOException e) {
//            System.out.println("Error reading passwords: " + e.getMessage());
//        }
//
//        return list;
//    }
//
//    /**
//     * Save a new password with username, encrypted using AES-GCM.
//     * Prevents duplicates (same username + same password).
//     */
//    public void savePassword(String username, String rawPassword) {
//        List<Password> existing = loadPasswords();
//
//        // Prevent duplicates by username + plaintext combo
//        for (Password p : existing) {
//            if (p.getUsername().equals(username)
//                    && p.getDecryptedPassword().equals(rawPassword)) {
//                System.out.println("⚠️ Password for this user already exists.");
//                return;
//            }
//        }
//
//        int nextId = existing.stream().mapToInt(Password::getId).max().orElse(0) + 1;
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//
//        try (BufferedWriter writer =
//                     new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8, true))) {
//
//            byte[] salt = CryptoUtils.generateSalt();
//            byte[] iv = CryptoUtils.generateIV();
//            SecretKey key = CryptoUtils.deriveKey(masterPassword.toCharArray(), salt);
//            String encrypted = CryptoUtils.encrypt(rawPassword, key, iv);
//
//            // New format: id::username::salt::iv::encrypted::createdAt
//            String line = nextId + "::"
//                    + username + "::"
//                    + Base64.getEncoder().encodeToString(salt) + "::"
//                    + Base64.getEncoder().encodeToString(iv) + "::"
//                    + encrypted + "::"
//                    + timestamp;
//
//            writer.write(line);
//            writer.newLine();
//
//            System.out.println("✅ Password saved securely.");
//
//        } catch (Exception e) {
//            System.out.println("Error saving password: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Very simple validation: returns true if at least one password
//     * can be decrypted successfully with the current master password.
//     */
//    public boolean validateMasterPassword() {
//        List<Password> list = loadPasswords();
//        return !list.isEmpty();
//    }
//}

package service;

import model.Password;
import util.CryptoUtils;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Base64;

public class PasswordStorage {

    private final String fileName;
    private final String masterPassword;

    public PasswordStorage(String fileName, String masterPassword) {
        this.fileName = fileName;
        this.masterPassword = masterPassword;
    }

    public List<Password> loadPasswords() {

        List<Password> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {

                // Supports old + new format
                String[] parts = line.split("::");
                if (parts.length != 6 && parts.length != 5) continue;

                try {
                    int id = Integer.parseInt(parts[0]);

                    String username;
                    int saltIdx, ivIdx, encIdx, tsIdx;

                    if (parts.length == 6) {
                        username = parts[1];
                        saltIdx = 2;
                        ivIdx = 3;
                        encIdx = 4;
                        tsIdx = 5;
                    } else {
                        username = "N/A";
                        saltIdx = 1;
                        ivIdx = 2;
                        encIdx = 3;
                        tsIdx = 4;
                    }

                    byte[] salt = Base64.getDecoder().decode(parts[saltIdx]);
                    byte[] iv = Base64.getDecoder().decode(parts[ivIdx]);
                    String encrypted = parts[encIdx];
                    String createdAt = parts[tsIdx];

                    SecretKey key = CryptoUtils.deriveKey(masterPassword.toCharArray(), salt);
                    String decrypted = CryptoUtils.decrypt(encrypted, key, iv);

                    list.add(new Password(id, username, decrypted, encrypted, createdAt));

                } catch (Exception ignored) {}
            }

        } catch (IOException e) {
            System.out.println("Error reading stored passwords: " + e.getMessage());
        }

        return list;
    }

    public void savePassword(String username, String rawPassword) {

        List<Password> existing = loadPasswords();

        for (Password p : existing) {
            if (p.getUsername().equals(username)
                    && p.getDecryptedPassword().equals(rawPassword)) {

                System.out.println("⚠️ Password for this username already exists.");
                return;
            }
        }

        int nextId = existing.stream()
                .mapToInt(Password::getId)
                .max()
                .orElse(0) + 1;

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(fileName, StandardCharsets.UTF_8, true))) {

            byte[] salt = CryptoUtils.generateSalt();
            byte[] iv = CryptoUtils.generateIV();
            SecretKey key = CryptoUtils.deriveKey(masterPassword.toCharArray(), salt);

            String encrypted = CryptoUtils.encrypt(rawPassword, key, iv);

            writer.write(nextId + "::"
                    + username + "::"
                    + Base64.getEncoder().encodeToString(salt) + "::"
                    + Base64.getEncoder().encodeToString(iv) + "::"
                    + encrypted + "::"
                    + timestamp);

            writer.newLine();

            System.out.println("✅ Password saved securely.");

        } catch (Exception e) {
            System.out.println("Error saving password: " + e.getMessage());
        }
    }

    // correct password if at least 1 decrypt works
    public boolean validateMasterPassword() {
        return !loadPasswords().isEmpty();
    }
}

