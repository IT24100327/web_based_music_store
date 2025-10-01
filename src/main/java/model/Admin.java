package model;

import model.enums.AdminRole;

public class Admin extends User {
    private AdminRole role;

    public Admin(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        this.role = null;
    }

    public Admin(int userId, String firstName, String lastName, String email, String password) {
        super(userId, firstName, lastName, email, password);
        this.role = null;
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public String getRoleDescription() {
        return "Administrator";
    }

    public AdminRole getRole() {
        return role;
    }

    public void setRole(AdminRole role) {
        this.role = role;
    }
}