package service;

import model.Password;
import util.HashUtil;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GeneratedPasswordStorage {

    private final String fileName = "src/resources/generated_passwords.txt";

    public List<Password> loadGeneratedPasswords() {
        List<Password> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = reader.readLine()) != null) {

                // Format: id::hash::createdAt
                String[] parts = line.split("::");

                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    String hash = parts[1];
                    String createdAt = parts[2];

                    list.add(new Password(id, "N/A", "N/A", hash, createdAt));

                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("(No generated passwords yet)");
        } catch (IOException e) {
            System.out.println("Error reading generated passwords: " + e.getMessage());
        }

        return list;
    }

    public void saveGeneratedPassword(String rawPassword) {
        List<Password> existing = loadGeneratedPasswords();
        String hash = HashUtil.hashPassword(rawPassword);

        // Avoid saving duplicates
        for (Password p : existing) {
            if (p.getHashedValue().equals(hash)) {
                System.out.println("⚠️ Generated password hash already exists.");
                return;
            }
        }

        int nextId = existing.size() + 1;

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {

            writer.write(nextId + "::" + hash + "::" + timestamp);
            writer.newLine();

            System.out.println("💾 Generated password saved to src/resources/generated_passwords.txt");

        } catch (IOException e) {
            System.out.println("Error saving generated password: " + e.getMessage());
        }
    }
}
