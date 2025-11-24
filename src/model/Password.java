package model;

public class Password {

    private int id;
    private String username;          // NEW
    private String decryptedPassword; // NEW
    private String encryptedValue;    // NEW (AES-GCM ciphertext)
    private String createdAt;

    /**
     * New full constructor (used by PasswordStorage and GeneratedPasswordStorage)
     */
    public Password(int id, String username, String decryptedPassword,
                    String encryptedValue, String createdAt) {
        this.id = id;
        this.username = username;
        this.decryptedPassword = decryptedPassword;
        this.encryptedValue = encryptedValue;
        this.createdAt = createdAt;
    }

    /**
     * OLD constructor (for backward compatibility)
     * - username becomes "N/A"
     * - decryptedPassword = value
     * - encryptedValue = value
     */
    public Password(int id, String value, String createdAt) {
        this.id = id;
        this.username = "N/A";
        this.decryptedPassword = value;
        this.encryptedValue = value;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public String getEncryptedValue() {
        return encryptedValue;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
