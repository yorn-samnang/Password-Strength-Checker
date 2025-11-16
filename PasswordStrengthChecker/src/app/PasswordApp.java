package app;

import model.Password;
import service.GeneratedPasswordStorage;
import service.PasswordChecker;
import service.PasswordStorage;
import service.PasswordGenerator;
import service.PasswordGuidelines;
import export.ExportManager;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class PasswordApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PasswordChecker checker = new PasswordChecker();
        PasswordStorage storage = new PasswordStorage("passwords.txt");
        PasswordGuidelines guidelines = new PasswordGuidelines();

        System.out.println("==============================");
        System.out.println(" 🔐 Password Strength Checker ");
        System.out.println("==============================");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Check Password Strength");
            System.out.println("2. View Saved Passwords");
            System.out.println("3. Export Passwords");
            System.out.println("4. Generate Strong Password");
            System.out.println("5. View Password Guidelines");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = getInt(sc);

            switch (choice) {
                case 1 -> handlePasswordCheck(sc, checker, storage);
                case 2 -> handlePasswordView(storage);
                case 3 -> handlePasswordExport(sc, storage);
                case 4 -> handlePasswordGeneration(sc, storage);
                case 5 -> handlePasswordGuidelines(guidelines);
                case 6 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ================ 1. CHECK PASSWORD ==================
    private static void handlePasswordCheck(Scanner sc, PasswordChecker checker, PasswordStorage storage) {
        System.out.print("Enter username (for validation): ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String passwordInput = sc.nextLine();

        if (checker.isCommonPassword(passwordInput)) {
            System.out.println("❌ That password is too common!");
            return;
        }

        String strength = checker.checkStrength(passwordInput, username);
        System.out.println("Password Strength: " + strength);

        if (strength.equalsIgnoreCase("Strong")) {
            storage.savePassword(passwordInput);
        } else {
            System.out.println("⚠️ Try to create a stronger password.");
        }
    }

    // ================ 2. VIEW SAVED PASSWORDS ==================
    private static void handlePasswordView(PasswordStorage storage) {
        List<Password> list = storage.loadPasswords();

        if (list.isEmpty()) {
            System.out.println("No passwords saved.");
            return;
        }

        System.out.println("\n=== Saved Passwords (Secure View) ===");
        for (Password p : list) {
            System.out.println("----------------------------------------");
            System.out.println("ID        : " + p.getId());
            System.out.println("Hash      : " + p.getHashedValue());
            System.out.println("Created At: " + p.getCreatedAt());
        }
        System.out.println("----------------------------------------");
    }

    // ================ 3. EXPORT PASSWORDS ==================
    private static void handlePasswordExport(Scanner sc, PasswordStorage storage) {
        List<Password> passwords = storage.loadPasswords();

        if (passwords.isEmpty()) {
            System.out.println("No passwords to export.");
            return;
        }

        System.out.print("Export as (1) CSV or (2) JSON: ");
        int opt = getInt(sc);

        // Create Saved folder
        File savedFolder = new File("Saved");
        if (!savedFolder.exists() && savedFolder.mkdir()) {
            System.out.println("📁 'Saved' folder created.");
        }

        String filePath = (opt == 1) ? "Saved/passwords.csv" : "Saved/passwords.json";

        boolean success = (opt == 1)
                ? ExportManager.exportToCSV(passwords, filePath)
                : ExportManager.exportToJSON(passwords, filePath);

        if (success) {
            System.out.println("✅ Exported successfully to: " + filePath);
        } else {
            System.out.println("❌ Failed to export file.");
        }
    }

    // ================ 4. GENERATE STRONG PASSWORD ==================
    private static void handlePasswordGeneration(Scanner sc, PasswordStorage storage) {
        System.out.println("Generating a strong password...");
        String generated = PasswordGenerator.generateStrongPassword();

        System.out.println("\n🔐 Your generated strong password:");
        System.out.println("👉 " + generated);

        System.out.print("\nWould you like to save this password? (y/n): ");
        String save = sc.nextLine().trim();

        if (save.equalsIgnoreCase("y")) {
            GeneratedPasswordStorage genStorage = new GeneratedPasswordStorage();
            genStorage.saveGeneratedPassword(generated);

        } else {
            System.out.println("Password not saved.");
        }
    }

    // ================ 5. VIEW PASSWORD GUIDELINES ==================
    private static void handlePasswordGuidelines(PasswordGuidelines guidelines) {
        String description = guidelines.loadPasswordGuidelines();
        System.out.println(description);
    }

    // ================ HELPER ==================
    private static int getInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Invalid number, try again: ");
            sc.next();
        }
        int n = sc.nextInt();
        sc.nextLine(); // consume newline
        return n;
    }
}

