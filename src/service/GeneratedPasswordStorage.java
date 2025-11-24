//package service;
//
//import model.Password;
//import util.CryptoUtils;
//
//import javax.crypto.SecretKey;
//import java.io.*;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.Base64;
//
//public class GeneratedPasswordStorage {
//
//    private final String fileName = "src/resources/generated_passwords.txt";
//    private static final char[] MASTER_PASSWORD = "SuperStrongMasterKey!".toCharArray(); // Change this
//
//    public List<Password> loadGeneratedPasswords() {
//        List<Password> list = new ArrayList<>();
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//
//                // Format:
//                // id::encrypted::salt::iv::createdAt
//                String[] parts = line.split("::");
//
//                if (parts.length == 5) {
//                    int id = Integer.parseInt(parts[0]);
//                    String encrypted = parts[1];
//                    byte[] salt = Base64.getDecoder().decode(parts[2]);
//                    byte[] iv = Base64.getDecoder().decode(parts[3]);
//                    String createdAt = parts[4];
//
//                    // Derive key using stored salt
//                    SecretKey key = CryptoUtils.deriveKey(MASTER_PASSWORD, salt);
//
//                    // Decrypt AES-GCM
//                    String decrypted = CryptoUtils.decrypt(encrypted, key, iv);
//
//                    list.add(new Password(id, "Generated", decrypted, encrypted, createdAt));
//                }
//            }
//
//        } catch (FileNotFoundException e) {
//            System.out.println("(No generated passwords yet)");
//        } catch (Exception e) {
//            System.out.println("Error reading generated passwords: " + e.getMessage());
//        }
//
//        return list;
//    }
//
//    public void saveGeneratedPassword(String rawPassword) {
//        List<Password> existing = loadGeneratedPasswords();
//
//        try {
//            // Prepare AES-GCM parameters
//            byte[] salt = CryptoUtils.generateSalt();
//            byte[] iv = CryptoUtils.generateIV();
//            SecretKey key = CryptoUtils.deriveKey(MASTER_PASSWORD, salt);
//
//            // Encrypt the generated password
//            String encrypted = CryptoUtils.encrypt(rawPassword, key, iv);
//
//            // Avoid duplicates (compare decrypted plaintext)
//            for (Password p : existing) {
//                if (p.getDecryptedPassword().equals(rawPassword)) {
//                    System.out.println("⚠️ Generated password already exists.");
//                    return;
//                }
//            }
//
//            int nextId = existing.size() + 1;
//
//            String timestamp = LocalDateTime.now()
//                    .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
//
//                writer.write(nextId + "::"
//                        + encrypted + "::"
//                        + Base64.getEncoder().encodeToString(salt) + "::"
//                        + Base64.getEncoder().encodeToString(iv) + "::"
//                        + timestamp);
//
//                writer.newLine();
//
//                System.out.println("💾 Encrypted password saved to src/resources/generated_passwords.txt");
//
//            }
//
//        } catch (Exception e) {
//            System.out.println("Error saving generated password: " + e.getMessage());
//        }
//    }
//}

package service;

import model.Password;
import util.CryptoUtils;

import javax.crypto.SecretKey;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Base64;

public class GeneratedPasswordStorage {

    private final String fileName = "src/resources/generated_passwords.txt";
    private final String masterPassword;

    public GeneratedPasswordStorage(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public List<Password> loadGeneratedPasswords() {

        List<Password> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("::");
                if (parts.length != 5) continue;

                int id = Integer.parseInt(parts[0]);
                String encrypted = parts[1];
                byte[] salt = Base64.getDecoder().decode(parts[2]);
                byte[] iv = Base64.getDecoder().decode(parts[3]);
                String createdAt = parts[4];

                SecretKey key = CryptoUtils.deriveKey(masterPassword.toCharArray(), salt);
                String decrypted = CryptoUtils.decrypt(encrypted, key, iv);

                list.add(new Password(
                        id,
                        "Generated",
                        decrypted,
                        encrypted,
                        createdAt
                ));
            }

        } catch (FileNotFoundException e) {
            System.out.println("(No generated passwords yet)");
        } catch (Exception e) {
            System.out.println("Error reading generated passwords: " + e.getMessage());
        }

        return list;
    }

    public void saveGeneratedPassword(String rawPassword) {

        List<Password> existing = loadGeneratedPasswords();

        for (Password p : existing) {
            if (p.getDecryptedPassword().equals(rawPassword)) {
                System.out.println("⚠️ Generated password already exists.");
                return;
            }
        }

        int nextId = existing.size() + 1;

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {

            byte[] salt = CryptoUtils.generateSalt();
            byte[] iv = CryptoUtils.generateIV();
            SecretKey key = CryptoUtils.deriveKey(masterPassword.toCharArray(), salt);

            String encrypted = CryptoUtils.encrypt(rawPassword, key, iv);

            writer.write(nextId + "::"
                    + encrypted + "::"
                    + Base64.getEncoder().encodeToString(salt) + "::"
                    + Base64.getEncoder().encodeToString(iv) + "::"
                    + timestamp);

            writer.newLine();

            System.out.println("💾 Generated password saved.");

        } catch (Exception e) {
            System.out.println("Error saving generated password: " + e.getMessage());
        }
    }
}

