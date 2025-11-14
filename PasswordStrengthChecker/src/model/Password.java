package model;

public class Password {
    private String value;
    private String hashedValue;

    public Password(String value, String hashedValue) {
        this.value = value;
        this.hashedValue = hashedValue;
    }

    public String getValue() {
        return value;
    }

    public String getHashedValue() {
        return hashedValue;
    }

    @Override
    public String toString() {
        return value;
    }
}
