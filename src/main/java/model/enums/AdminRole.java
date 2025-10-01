package model.enums;

public enum AdminRole {
    SUPER_ADMIN("super_admin"),
    MARKETING_MANAGER("marketing_manager"),
    SUPPORT_MANAGER("support_manager"),
    FINANCE_MANAGER("finance_manager"),
    CONTENT_MANAGER("content_manager");

    private final String roleName;

    AdminRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static AdminRole fromRoleName(String roleName) {
        for (AdminRole role : AdminRole.values()) {
            if (role.roleName.equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role name: " + roleName);
    }
}