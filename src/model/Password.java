package model;

public class Password {

    private int id;
    private String value; // decrypted password
    private String createdAt;

    public Password(int id, String value, String createdAt) {
        this.id = id;
        this.value = value;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
