package model;

import model.enums.UserType;

public class StandardUser extends User {
    public StandardUser(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    public StandardUser(int userId, String firstName, String lastName, String email, String password) {
        super(userId, firstName, lastName, email, password);
    }

    public StandardUser() {
    }

    @Override
    public UserType getUserType() {
        return UserType.USER;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }
}