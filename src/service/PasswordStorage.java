//package service;
//
//import model.Password;
//import util.HashUtil;
//
//import java.io.*;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//public class PasswordStorage {
//
//    private final String fileName;
//
//    public PasswordStorage(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public List<Password> loadPasswords() {
//        List<Password> list = new ArrayList<>();
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Format stored as: id::hash::createdAt
//                String[] parts = line.split("::");
//
//                if (parts.length == 3) {
//                    int id = Integer.parseInt(parts[0]);
//                    String hash = parts[1];
//                    String createdAt = parts[2];
//
//                    // Match new constructor (int, String, String)
//                    list.add(new Password(id, hash, createdAt));
//                }
//            }
//
//        } catch (FileNotFoundException e) {
//            System.out.println("(No saved passwords yet)");
//        } catch (IOException e) {
//            System.out.println("Error reading file: " + e.getMessage());
//        }
//
//        return list;
//    }
//
//    // Save password with NEW MODEL
//    public void savePassword(String rawPassword) {
//        List<Password> existing = loadPasswords();
//        String hash = HashUtil.hashPassword(rawPassword);
//
//        // Prevent duplicate hashes
//        for (Password p : existing) {
//            if (p.getHashedValue().equals(hash)) {
//                System.out.println("⚠️ Password hash already saved.");
//                return;
//            }
//        }
//
//        int nextId = existing.size() + 1;
//
//        // Timestamp stored as String (ISO)
//        String timestamp = LocalDateTime.now()
//                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
//            writer.write(nextId + "::" + hash + "::" + timestamp);
//            writer.newLine();
//
//            System.out.println("✅ Password saved securely (hashed only).");
//
//        } catch (IOException e) {
//            System.out.println("Error writing file: " + e.getMessage());
//        }
//    }
//}


package service;

import model.Password;
import util.CryptoUtil;
import util.HashUtil;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PasswordStorage {

    private final String fileName;

    public PasswordStorage(String fileName) {
        this.fileName = fileName;
    }

    // ================= LOAD PASSWORDS ==================
    public List<Password> loadPasswords() {
        List<Password> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = reader.readLine()) != null) {

                // NEW FORMAT:
                // id::username::encryptedPassword::hashedPassword::createdAt
                String[] parts = line.split("::");

                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String username = parts[1];
                    String encrypted = parts[2];
                    String hash = parts[3];
                    String createdAt = parts[4];

                    list.add(new Password(id, username, encrypted, hash, createdAt));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("(No saved passwords yet)");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return list;
    }

    // ================= SAVE PASSWORD ===================
    public void savePassword(String username, String rawPassword) {

        List<Password> existing = loadPasswords();

        // Encrypt + hash password
        String encrypted = CryptoUtil.encrypt(rawPassword);
        String hash = HashUtil.hashPassword(rawPassword);

        // Prevent duplicate hashed passwords
        for (Password p : existing) {
            if (p.getHashedValue().equals(hash)) {
                System.out.println("⚠️ Password already exists (same hash).");
                return;
            }
        }

        int nextId = existing.size() + 1;

        // Timestamp (ISO8601)
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {

            // NEW FORMAT:
            // id::username::encryptedPassword::hashedPassword::createdAt
            writer.write(nextId + "::" + username + "::" + encrypted + "::" + hash + "::" + timestamp);
            writer.newLine();

            System.out.println("✅ Password saved securely!");

        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
