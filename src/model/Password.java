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

