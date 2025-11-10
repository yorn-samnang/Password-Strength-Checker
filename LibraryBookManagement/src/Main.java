import java.io.*;
import java.util.*;

public class Main {
    private static final List<String> commonPasswords = Arrays.asList(
            "123456", "password", "123456789", "12345678", "12345", "qwerty",
            "abc123", "password1", "111111", "123123", "letmein", "welcome",
            "iloveyou", "dragon", "sunshine", "princess", "football", "admin",
            "login", "passw0rd", "master", "hello", "freedom", "whatever", "trustno1"
    );

    private static final String FILE_NAME = "passwords.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("===========================");
        System.out.println(" Password Strength Checker ");
        System.out.println("===========================");
        loadSavedPasswords();

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Check Password Strength");
            System.out.println("2. View Saved Passwords");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (1-3).");
                continue;
            }

            switch (choice) {
                case 1:
                    checkPassword(sc);
                    break;
                case 2:
                    displaySavedPasswords();
                    break;
                case 3:
                    System.out.println("Exiting... Goodbye!");
                    sc.close();
                    return; // ✅ Exit program cleanly
                default:
                    System.out.println("Invalid choice. Try again!");
            }
        }
    }

    // === Load saved passwords from file ===
    private static void loadSavedPasswords() {
        System.out.println("\nPreviously Saved Passwords:");
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean empty = true;
            while ((line = reader.readLine()) != null) {
                System.out.println("- " + line);
                empty = false;
            }
            if (empty) {
                System.out.println("(No saved passwords yet)");
            }
        } catch (FileNotFoundException e) {
            System.out.println("(No saved passwords yet)");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // === Save strong password to file, avoid duplicates ===
    private static void savePassword(String password) {
        try {
            // Check if password already exists
            List<String> existing = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    existing.add(line.trim());
                }
            } catch (FileNotFoundException ignored) {}

            if (existing.contains(password)) {
                System.out.println("⚠️ This password is already saved.");
                return;
            }

            // Append to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                writer.write(password);
                writer.newLine();
            }
            System.out.println("✅ Password saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving password: " + e.getMessage());
        }
    }

    // === Display saved passwords ===
    private static void displaySavedPasswords() {
        System.out.println("\n=== Saved Passwords ===");
        loadSavedPasswords();
        System.out.println("========================");
    }

    // === Main password check logic ===
    private static void checkPassword(Scanner sc) {
        while (true) {
            System.out.print("\nEnter the password: ");
            String password = sc.nextLine();

            try {
                if (password.isEmpty()) {
                    throw new IllegalArgumentException("Password cannot be empty.");
                }

                if (password.length() < 8) {
                    System.out.println("❌ Too short! Must be at least 8 characters.");
                } else if (isCommonPassword(password)) {
                    System.out.println("❌ This password is too common. Try again.");
                } else {
                    String strength = passwordStrength(password);
                    System.out.println("Password Strength: " + strength);

                    if (strength.equals("Strong")) {
                        System.out.println("✅ Password accepted and saved!");
                        savePassword(password);
                        return; // ✅ Go back to main menu
                    } else {
                        System.out.println("⚠️ Try to make it stronger (use upper, lower, digits, and symbols).");
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }
    }

    // === Check if password is in common list ===
    public static boolean isCommonPassword(String pass) {
        return commonPasswords.contains(pass.toLowerCase());
    }

    // === Determine password strength ===
    public static String passwordStrength(String pass) {
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;
        String specialChars = "!(){}[]:;<>?,@#$%^&*+=_-~`|./'";

        for (char c : pass.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (specialChars.contains(String.valueOf(c))) hasSymbol = true;
        }

        boolean longEnough = pass.length() >= 12;
        if (longEnough && hasUpper && hasLower && hasDigit && hasSymbol) {
            return "Strong";
        } else if (pass.length() < 8 || (!hasUpper && !hasLower && !hasDigit && !hasSymbol)) {
            return "Weak";
        } else {
            return "Medium";
        }
    }
}
