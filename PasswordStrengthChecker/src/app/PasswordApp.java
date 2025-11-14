package app;

import model.Password;
import service.*;
import export.ExportManager;
import java.util.*;

public class PasswordApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PasswordChecker checker = new PasswordChecker();
        PasswordStorage storage = new PasswordStorage("passwords.txt");

        System.out.println("==============================");
        System.out.println(" 🔐 Password Strength Checker ");
        System.out.println("==============================");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Check Password Strength");
            System.out.println("2. View Saved Passwords");
            System.out.println("3. Export Passwords");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = getInt(sc);

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter username (for validation): ");
                    String username = sc.nextLine();
                    System.out.print("Enter password: ");
                    String passwordInput = sc.nextLine();

                    if (checker.isCommonPassword(passwordInput)) {
                        System.out.println("❌ Too common password!");
                        continue;
                    }

                    String strength = checker.checkStrength(passwordInput, username);
                    System.out.println("Password Strength: " + strength);

                    if (strength.equals("Strong")) {
                        storage.savePassword(passwordInput);
                    } else {
                        System.out.println("⚠️ Try a stronger password.");
                    }
                }
                case 2 -> {
                    List<Password> list = storage.loadPasswords();
                    if (list.isEmpty()) System.out.println("No passwords saved.");
                    else list.forEach(p -> System.out.println("- " + p.getValue()));
                }
                case 3 -> {
                    List<Password> passwords = storage.loadPasswords();
                    System.out.print("Export as (1) CSV or (2) JSON: ");
                    int opt = getInt(sc);
                    if (opt == 1) ExportManager.exportToCSV(passwords, "passwords.csv");
                    else ExportManager.exportToJSON(passwords, "passwords.json");
                }
                case 4 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static int getInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Invalid number, try again: ");
            sc.next();
        }
        int n = sc.nextInt();
        sc.nextLine();
        return n;
    }
}
