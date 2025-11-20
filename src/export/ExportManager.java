package export;

import model.Password;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportManager {

    // =============== CSV EXPORT ===============
    public static boolean exportToCSV(List<Password> passwords, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            writer.write("ID,HashedValue,CreatedAt");
            writer.newLine();

            for (Password p : passwords) {
                writer.write(p.getId() + "," +
                        p.getHashedValue() + "," +
                        p.getCreatedAt());
                writer.newLine();
            }

            return true;

        } catch (IOException e) {
            System.out.println("Error exporting CSV: " + e.getMessage());
            return false;
        }
    }

    // =============== JSON EXPORT ===============
    @SuppressWarnings("unchecked")
    public static boolean exportToJSON(List<Password> passwords, String filePath) {
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
            return true;

        } catch (IOException e) {
            System.out.println("Error exporting JSON: " + e.getMessage());
            return false;
        }
    }
}
