package model;

import util.CryptoUtil; // your encryption/decryption utility

public class Password {

    private int id;
    private String username;
    private String encryptedValue;   // 🔐 stored encrypted
    private String hashedValue;      // optional, still stored but no longer displayed
    private String createdAt;

    public Password(int id, String username, String encryptedValue, String hashedValue, String createdAt) {
        this.id = id;
        this.username = username;
        this.encryptedValue = encryptedValue;
        this.hashedValue = hashedValue;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEncryptedValue() {
        return encryptedValue;
    }

    public String getHashedValue() {
        return hashedValue;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    // 🔓 return decrypted password for display
    public String getDecryptedPassword() {
        return CryptoUtil.decrypt(encryptedValue);
    }
}
