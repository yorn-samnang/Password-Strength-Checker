package service;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!(){}[]:;<>?,@#$%^&*+=_-~`|./'";
    private static final int DEFAULT_LENGTH = 14;

    private static final SecureRandom random = new SecureRandom();

    public static String generateStrongPassword() {
        StringBuilder sb = new StringBuilder();

        // Ensure ALL requirement types appear at least once
        sb.append(getRandomChar(UPPER));
        sb.append(getRandomChar(LOWER));
        sb.append(getRandomChar(DIGITS));
        sb.append(getRandomChar(SYMBOLS));

        // Mix all categories for the remaining characters
        String all = UPPER + LOWER + DIGITS + SYMBOLS;

        while (sb.length() < DEFAULT_LENGTH) {
            char c = getRandomChar(all);

            // Avoid 3+ repeated characters
            if (sb.length() >= 2 &&
                    sb.charAt(sb.length() - 1) == c &&
                    sb.charAt(sb.length() - 2) == c)
                continue;

            sb.append(c);
        }

        return shuffle(sb.toString());
    }

    private static char getRandomChar(String set) {
        return set.charAt(random.nextInt(set.length()));
    }

    private static String shuffle(String password) {
        char[] chars = password.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
}
