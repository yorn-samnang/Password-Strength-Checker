//package export;
//
//import model.Password;
//import java.io.*;
//import java.util.*;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//
//public class ExportManager {
//
//    public static void exportToCSV(List<Password> passwords, String filePath) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            writer.write("Password,HashedValue");
//            writer.newLine();
//            for (Password p : passwords) {
//                writer.write(p.getValue() + "," + p.getHashedValue());
//                writer.newLine();
//            }
//            System.out.println("✅ Exported successfully to " + filePath);
//        } catch (IOException e) {
//            System.out.println("Error exporting to CSV: " + e.getMessage());
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    public static void exportToJSON(List<Password> passwords, String filePath) {
//        JSONArray array = new JSONArray();
//        for (Password p : passwords) {
//            JSONObject obj = new JSONObject();
//            obj.put("password", p.getValue());
//            obj.put("hash", p.getHashedValue());
//            array.add(obj);
//        }
//
//        try (FileWriter file = new FileWriter(filePath)) {
//            file.write(array.toJSONString());
//            System.out.println("✅ Exported successfully to " + filePath);
//        } catch (IOException e) {
//            System.out.println("Error exporting to JSON: " + e.getMessage());
//        }
//    }
//}


package export;

import model.Password;
import java.io.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ExportManager {

    // ---------------- CSV EXPORT ----------------
    public static void exportToCSV(List<Password> passwords, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            // CSV headers (NO plaintext passwords)
            writer.write("ID,HashedValue,CreatedAt");
            writer.newLine();

            for (Password p : passwords) {
                writer.write(p.getId() + "," + p.getHashedValue() + "," + p.getCreatedAt());
                writer.newLine();
            }

            System.out.println("✅ Exported CSV successfully to " + filePath);

        } catch (IOException e) {
            System.out.println("Error exporting to CSV: " + e.getMessage());
        }
    }

    // ---------------- JSON EXPORT ----------------
    @SuppressWarnings("unchecked")
    public static void exportToJSON(List<Password> passwords, String filePath) {
        JSONArray array = new JSONArray();

        for (Password p : passwords) {
            JSONObject obj = new JSONObject();
            obj.put("id", p.getId());
            obj.put("hash", p.getHashedValue());
            obj.put("createdAt", p.getCreatedAt().toString());
            array.add(obj);
        }

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(array.toJSONString());
            System.out.println("✅ Exported JSON successfully to " + filePath);

        } catch (IOException e) {
            System.out.println("Error exporting to JSON: " + e.getMessage());
        }
    }
}
