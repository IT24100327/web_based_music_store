package model.enums;

public enum UserType {
    USER("userUser"),
    ADMIN("adminUser"),
    ARTIST("artistUser");

    private final String dbValue;

    UserType(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static UserType fromDbValue(String dbValue) {
        for (UserType type : values()) {
            if (type.dbValue.equals(dbValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown user type: " + dbValue);
    }
}