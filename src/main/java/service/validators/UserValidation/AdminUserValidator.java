package service.validators.UserValidation;

import model.Admin;
import model.User;
import model.enums.AdminRole;

public class AdminUserValidator implements UserValidatorStrategy {

    private final StandardUserValidator standardValidator = new StandardUserValidator();
    private static final int MIN_ADMIN_PASSWORD_LENGTH = 8;

    @Override
    public void validate(User user) {
        standardValidator.validate(user);

        if (!(user instanceof Admin)) {
            throw new IllegalArgumentException("AdminUserValidator can only validate Admin users");
        }

        Admin admin = (Admin) user;

        // Stricter password for admins
        String password = user.getPassword();
        if (password != null && password.length() < MIN_ADMIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Admin password must be at least " + MIN_ADMIN_PASSWORD_LENGTH + " characters long");
        }

        if (admin.getRole() != null && !isValidAdminRole(admin.getRole())) {
            throw new IllegalArgumentException("Invalid admin role: " + admin.getRole());
        }
    }

    public void validateRole(AdminRole role) {
        if (role == null) {
            return;
        }
        if (!isValidAdminRole(role)) {
            throw new IllegalArgumentException("Invalid admin role: " + role);
        }
    }

    private boolean isValidAdminRole(AdminRole role) {
        return AdminRole.fromRoleName(role.getRoleName()) != null; // Fixed: Handles null safely if fromRoleName does
    }
}