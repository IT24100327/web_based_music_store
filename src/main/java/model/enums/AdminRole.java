package model.enums;

public enum AdminRole {
    SUPER_ADMIN("super_admin"),
    MARKETING_MANAGER("marketing_manager"),
    SUPPORT_MANAGER("support_manager"),
    FINANCE_MANAGER("finance_manager"),
    CONTENT_MANAGER("content_manager");

    private final String dbValue;

    AdminRole(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getRoleName() {
        return dbValue;
    }

    public static AdminRole fromRoleName(String dbValue) {
        for (AdminRole role : AdminRole.values()) {
            if (role.dbValue.equalsIgnoreCase(dbValue)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role name: " + dbValue);
    }
}