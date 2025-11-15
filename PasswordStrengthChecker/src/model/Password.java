//package model;
//
//public class Password {
//    private String value;
//    private String hashedValue;
//    private int id;
//    private String createdAt;
//
//    public Password(String value, String hashedValue) {
//        this.value = value;
//        this.hashedValue = hashedValue;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public String getHashedValue() {
//        return hashedValue;
//    }
//
//    @Override
//    public String toString() {
//        return value;
//    }
//
//    public int getId() {
//        return id;
//    }
//    public String getCreatedAt() {
//        return createdAt;
//    }
//}

package model;

public class Password {

    private int id;
    private String hashedValue;
    private String createdAt; // stored as ISO string (e.g., 2025-11-14T22:00)

    // Constructor used when loading from file
    public Password(int id, String hashedValue, String createdAt) {
        this.id = id;
        this.hashedValue = hashedValue;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getHashedValue() {
        return hashedValue;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

