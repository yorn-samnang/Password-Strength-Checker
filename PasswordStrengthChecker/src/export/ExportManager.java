package export;

import model.Password;
import java.io.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ExportManager {

    public static void exportToCSV(List<Password> passwords, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Password,HashedValue");
            writer.newLine();
            for (Password p : passwords) {
                writer.write(p.getValue() + "," + p.getHashedValue());
                writer.newLine();
            }
            System.out.println("✅ Exported successfully to " + filePath);
        } catch (IOException e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void exportToJSON(List<Password> passwords, String filePath) {
        JSONArray array = new JSONArray();
        for (Password p : passwords) {
            JSONObject obj = new JSONObject();
            obj.put("password", p.getValue());
            obj.put("hash", p.getHashedValue());
            array.add(obj);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(array.toJSONString());
            System.out.println("✅ Exported successfully to " + filePath);
        } catch (IOException e) {
            System.out.println("Error exporting to JSON: " + e.getMessage());
        }
    }
}
