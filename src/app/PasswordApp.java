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
        PasswordGuidelines guidelines = new PasswordGuidelines();

        System.out.println("==============================");
        System.out.println(" 🔐 Password Strength Manager ");
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
                case 1 -> handlePasswordCheck(sc, checker);
                case 2 -> handlePasswordView(sc);
                case 3 -> handlePasswordExport(sc);
                case 4 -> handlePasswordGeneration(sc, checker);
                case 5 -> handlePasswordGuidelines(guidelines);
                case 6 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // ==================== 1. CHECK PASSWORD ====================
    private static void handlePasswordCheck(Scanner sc, PasswordChecker checker) {
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        System.out.print("Enter password: ");
        String passwordInput = sc.nextLine();

        if (checker.isCommonPassword(passwordInput)) {
            System.out.println("❌ That password is too common!");
            return;
        }

        String strength = checker.checkStrength(passwordInput, username);
        System.out.println("Password Strength: " + strength);

        if (!strength.equalsIgnoreCase("Strong")) {
            System.out.println("⚠️ Password is not strong enough.");
            return;
        }

        System.out.print("Save this strong password? (y/n): ");
        String save = sc.nextLine().trim();
        if (!save.equalsIgnoreCase("y")) {
            System.out.println("Password not saved.");
            return;
        }

        // Prompt master password only when saving
        System.out.print("Enter your master password to save: ");
        String masterPassword = sc.nextLine().trim();
        PasswordStorage storage = new PasswordStorage("src/resources/passwords.txt", masterPassword);
        storage.savePassword(passwordInput);
    }

    // ==================== 2. VIEW SAVED PASSWORDS ====================
    private static void handlePasswordView(Scanner sc) {
        System.out.print("Enter your master password to view saved passwords: ");
        String masterPassword = sc.nextLine().trim();
        PasswordStorage storage = new PasswordStorage("src/resources/passwords.txt", masterPassword);

        List<Password> list = storage.loadPasswords();
        if (list.isEmpty()) {
            System.out.println("No passwords saved or master password incorrect.");
            return;
        }

        System.out.println("\n=== Saved Passwords ===");
        for (Password p : list) {
            System.out.println("-----------------------------");
            System.out.println("ID       : " + p.getId());
            System.out.println("Password : " + p.getValue());
            System.out.println("Created  : " + p.getCreatedAt());
        }
    }

    // ==================== 3. EXPORT PASSWORDS ====================
    private static void handlePasswordExport(Scanner sc) {
        System.out.print("Enter your master password to export passwords: ");
        String masterPassword = sc.nextLine().trim();
        PasswordStorage storage = new PasswordStorage("src/resources/passwords.txt", masterPassword);

        List<Password> list = storage.loadPasswords();
        if (list.isEmpty()) {
            System.out.println("No passwords to export or master password incorrect.");
            return;
        }

        System.out.print("Export as (1) CSV or (2) JSON: ");
        int opt = getInt(sc);

        File savedFolder = new File("Saved");
        if (!savedFolder.exists()) savedFolder.mkdir();

        String filePath = (opt == 1) ? "Saved/passwords.csv" : "Saved/passwords.json";
        boolean success = (opt == 1)
                ? ExportManager.exportToCSV(list, filePath)
                : ExportManager.exportToJSON(list, filePath);

        if (success) System.out.println("✅ Exported to " + filePath);
        else System.out.println("❌ Export failed.");
    }

    // ==================== 4. GENERATE STRONG PASSWORD ====================
    private static void handlePasswordGeneration(Scanner sc, PasswordChecker checker) {
        String generated = PasswordGenerator.generateStrongPassword();
        System.out.println("\nGenerated Password: " + generated);

        String strength = checker.checkStrength(generated, "");
        System.out.println("Password Strength: " + strength);

        if (!strength.equalsIgnoreCase("Strong")) {
            System.out.println("⚠️ Generated password is weak, not saving.");
            return;
        }

        System.out.print("Save generated password? (y/n): ");
        String save = sc.nextLine().trim();
        if (!save.equalsIgnoreCase("y")) {
            System.out.println("Password not saved.");
            return;
        }

        System.out.print("Enter your master password to save: ");
        String masterPassword = sc.nextLine().trim();
        GeneratedPasswordStorage genStorage = new GeneratedPasswordStorage(masterPassword);
        genStorage.saveGeneratedPassword(generated);
    }

    // ==================== 5. VIEW PASSWORD GUIDELINES ====================
    private static void handlePasswordGuidelines(PasswordGuidelines guidelines) {
        System.out.print(guidelines.loadPasswordGuidelines());
    }

    // ==================== HELPER ====================
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
