package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

public class PasswordChecker {

    private static final Set<String> commonPasswords = new HashSet<>();
    private static final String specialChars = "!(){}[]:;<>?,@#$%^&*+=_-~`|./'";

    static {
        try {
            File file = new File("src/resources/common_passwords.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    commonPasswords.add(line.trim().toLowerCase());
                }
            }
            br.close();

        } catch (IOException e) {
            System.err.println("Failed to read common passwords: " + e.getMessage());
        }
    }

    public boolean isCommonPassword(String pass) {
        return commonPasswords.contains(pass.toLowerCase());
    }

    public String checkStrength(String pass, String username) {
        if (pass == null || pass.isEmpty() || pass.length() < 6) return "Weak";

        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSymbol = false;
        for (char c : pass.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (specialChars.indexOf(c) >= 0) hasSymbol = true;
        }

        if (isCommonPassword(pass)) return "Weak";
        if (pass.matches(".*(.)\\1{3,}.*")) return "Weak";
        if (username != null && !username.isEmpty() && pass.toLowerCase().contains(username.toLowerCase()))
            return "Weak";

        int currentYear = Year.now().getValue();
        if (pass.contains(String.valueOf(currentYear)) || pass.matches(".*(19|20)\\d{2}.*"))
            return "Weak";

        String lower = pass.toLowerCase();
        String[] commonSequences = {"1234", "abcd", "qwerty", "password"};
        for (String seq : commonSequences)
            if (lower.contains(seq)) return "Weak";

        int score = 0;
        if (pass.length() >= 8) score++;
        if (pass.length() >= 12) score++;
        if (hasUpper) score++;
        if (hasLower) score++;
        if (hasDigit) score++;
        if (hasSymbol) score++;

        if (score <= 2) return "Weak";
        else if (score <= 4) return "Medium";
        else return "Strong";
    }
}
