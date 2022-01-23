package pl.polsl.skarbonka.error.exception;

import pl.polsl.skarbonka.model.User;

public class PermissionDeniedException extends RuntimeException {
    private final User.Role role;

    public PermissionDeniedException(User.Role role) {
        this.role = role;
    }

    public User.Role getRole() {
        return role;
    }
}
