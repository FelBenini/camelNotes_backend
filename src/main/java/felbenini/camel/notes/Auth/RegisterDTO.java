package felbenini.camel.notes.Auth;

import felbenini.camel.notes.user.UserRole;

public record RegisterDTO(String username, String password, UserRole role, String email) {
}
