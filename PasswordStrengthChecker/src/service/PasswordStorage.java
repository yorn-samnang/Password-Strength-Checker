package service;

import model.Password;
import util.HashUtil;
import java.io.*;
import java.util.*;

public class PasswordStorage {
    private final String fileName;

    public PasswordStorage(String fileName) {
        this.fileName = fileName;
    }

    public List<Password> loadPasswords() {
        List<Password> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("::");
                if (parts.length == 2)
                    list.add(new Password(parts[0], parts[1]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("(No saved passwords yet)");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return list;
    }

    public void savePassword(String passwordValue) {
        String hash = HashUtil.hashPassword(passwordValue);
        List<Password> existing = loadPasswords();

        for (Password p : existing) {
            if (p.getHashedValue().equals(hash)) {
                System.out.println("⚠️ This password already exists.");
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(passwordValue + "::" + hash);
            writer.newLine();
            System.out.println("✅ Password saved successfully (hashed).");
        } catch (IOException e) {
            System.out.println("Error saving password: " + e.getMessage());
        }
    }
}
