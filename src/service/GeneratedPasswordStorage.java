package service;

import model.Password;
import util.CryptoUtils;

import javax.crypto.SecretKey;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Handles storage and retrieval of generated passwords.
 * Encrypts passwords with a master password using CryptoUtils.
 */
public class GeneratedPasswordStorage {

    private final String fileName = "src/resources/passwords.txt";
    private final String masterPassword; // simulate device KEK / user master key

    public GeneratedPasswordStorage(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    /**
     * Load all passwords and decrypt them using masterPassword.
     * Skips entries that cannot be decrypted.
     *
     * @return list of successfully decrypted Password objects
     */
    public List<Password> loadGeneratedPasswords() {
        List<Password> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("::");
                if (parts.length != 5) continue; // skip malformed lines

                try {
                    int id = Integer.parseInt(parts[0]);
                    byte[] salt = Base64.getDecoder().decode(parts[1]);
                    byte[] iv = Base64.getDecoder().decode(parts[2]);
                    String encrypted = parts[3];
                    String createdAt = parts[4];

                    SecretKey key = CryptoUtils.deriveKey(masterPassword.toCharArray(), salt);
                    String decrypted = CryptoUtils.decrypt(encrypted, key, iv);

                    list.add(new Password(id, decrypted, createdAt));
                } catch (Exception ignored) {
                    // silently skip failed decryptions
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("(No generated passwords yet)");
        } catch (IOException e) {
            System.out.println("Error reading generated passwords: " + e.getMessage());
        }

        return list;
    }

    /**
     * Save a new password securely with encryption, preventing duplicates.
     *
     * @param rawPassword The plaintext password to save
     */
    public void saveGeneratedPassword(String rawPassword) {
        List<Password> existing = loadGeneratedPasswords();

        // Prevent duplicates
        for (Password p : existing) {
            if (p.getValue().equals(rawPassword)) {
                System.out.println("⚠️ Generated password already exists.");
                return;
            }
        }

        // Use max ID + 1 for nextId
        int nextId = existing.stream().mapToInt(Password::getId).max().orElse(0) + 1;
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8, true))) {
            byte[] salt = CryptoUtils.generateSalt();
            byte[] iv = CryptoUtils.generateIV();
            SecretKey key = CryptoUtils.deriveKey(masterPassword.toCharArray(), salt);
            String encrypted = CryptoUtils.encrypt(rawPassword, key, iv);

            String line = nextId + "::" +
                    Base64.getEncoder().encodeToString(salt) + "::" +
                    Base64.getEncoder().encodeToString(iv) + "::" +
                    encrypted + "::" +
                    timestamp;

            writer.write(line);
            writer.newLine();

            System.out.println("💾 Password saved securely.");
        } catch (Exception e) {
            System.out.println("Error saving password: " + e.getMessage());
        }
    }

    /**
     * Optional: Validate master password before loading.
     * Returns true if at least one password can be decrypted successfully.
     */
    public boolean validateMasterPassword() {
        List<Password> list = loadGeneratedPasswords();
        return !list.isEmpty();
    }
}
