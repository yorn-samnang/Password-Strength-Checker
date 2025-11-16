//package service;
//
//import java.time.Year;
//import java.util.*;
//
//public class PasswordChecker {
//    private static final List<String> commonPasswords = Arrays.asList(
//            "123456", "password", "123456789", "12345678", "12345", "qwerty",
//            "abc123", "password1", "111111", "123123", "letmein", "welcome",
//            "iloveyou", "dragon", "sunshine", "princess", "football", "admin",
//            "login", "passw0rd", "master", "hello", "freedom", "whatever", "trustno1"
//    );
//
//    public boolean isCommonPassword(String pass) {
//        return commonPasswords.contains(pass.toLowerCase());
//    }
//
//    public String checkStrength(String pass, String username) {
//        if (pass == null || pass.isEmpty()) return "Weak";
//        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSymbol = false;
//        String specialChars = "!(){}[]:;<>?,@#$%^&*+=_-~`|./'";
//
//        for (char c : pass.toCharArray()) {
//            if (Character.isUpperCase(c)) hasUpper = true;
//            else if (Character.isLowerCase(c)) hasLower = true;
//            else if (Character.isDigit(c)) hasDigit = true;
//            else if (specialChars.contains(String.valueOf(c))) hasSymbol = true;
//        }
//
//        // Check repeated characters (e.g., "aaaaaa")
//        if (pass.matches("(.)\\1{3,}")) {
//            return "Weak";
//        }
//
//        // Penalize if contains username
//        if (username != null && pass.toLowerCase().contains(username.toLowerCase())) {
//            return "Weak";
//        }
//
//        // Penalize if contains current year or any year pattern
//        int currentYear = Year.now().getValue();
//        if (pass.contains(String.valueOf(currentYear)) || pass.matches(".*(19|20)\\d{2}.*")) {
//            return "Weak";
//        }
//
//        // Determine overall strength
//        if (pass.length() >= 12 && hasUpper && hasLower && hasDigit && hasSymbol)
//            return "Strong";
//        else if (pass.length() >= 8 && ((hasUpper && hasLower) || hasDigit))
//            return "Medium";
//        else
//            return "Weak";
//    }
//}


package service;

import java.time.Year;
import java.util.*;

public class PasswordChecker {

    private static final List<String> commonPasswords = Arrays.asList(
            "123456", "password", "123456789", "12345678", "12345", "qwerty",
            "abc123", "password1", "111111", "123123", "letmein", "welcome",
            "iloveyou", "dragon", "sunshine", "princess", "football", "admin",
            "login", "passw0rd", "master", "hello", "freedom", "whatever", "trustno1"
    );

    private static final String specialChars = "!(){}[]:;<>?,@#$%^&*+=_-~`|./'";

    public boolean isCommonPassword(String pass) {
        return commonPasswords.contains(pass.toLowerCase());
    }

    public String checkStrength(String pass, String username) {

        if (pass == null || pass.isEmpty()) return "Weak";
        if (pass.length() < 6) return "Weak"; // too short to ever be secure

        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSymbol = false;

        for (char c : pass.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (specialChars.indexOf(c) >= 0) hasSymbol = true;
        }

        // ❌ Reject if common password
        if (isCommonPassword(pass)) return "Weak";

        // ❌ Reject repeated same character >= 4 times (aaaa, 1111)
        if (pass.matches(".*(.)\\1{3,}.*")) return "Weak";

        // ❌ Reject if contains username
        if (username != null && !username.isEmpty()) {
            if (pass.toLowerCase().contains(username.toLowerCase())) {
                return "Weak";
            }
        }

        // ❌ Reject year patterns
        int currentYear = Year.now().getValue();
        if (pass.contains(String.valueOf(currentYear)) || pass.matches(".*(19|20)\\d{2}.*")) {
            return "Weak";
        }

        // ❌ Reject simple sequences
        String lower = pass.toLowerCase();
        String[] commonSequences = {
                "1234", "abcd", "qwerty", "password"
        };
        for (String seq : commonSequences) {
            if (lower.contains(seq)) return "Weak";
        }

        // -----------------------------
        // SCORING SYSTEM
        // -----------------------------

        int score = 0;

        if (pass.length() >= 8) score++;
        if (pass.length() >= 12) score++;
        if (hasUpper) score++;
        if (hasLower) score++;
        if (hasDigit) score++;
        if (hasSymbol) score++;

        // Strength evaluation
        if (score <= 2) return "Weak";
        else if (score <= 4) return "Medium";
        else return "Strong";
    }
}
