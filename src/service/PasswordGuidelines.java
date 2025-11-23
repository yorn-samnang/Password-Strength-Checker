package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PasswordGuidelines {

    private static final String FILE_PATH = "src/resources/password_guidelines.txt";

    public static String loadPasswordGuidelines() {
        StringBuilder sb = new StringBuilder();

        try {
            File file = new File(FILE_PATH);
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            br.close();
            return sb.toString();

        } catch (IOException e) {
            System.err.println("Failed to read password guidelines: " + e.getMessage());
        }

        return "Password guidelines could not be loaded.";
    }
}
