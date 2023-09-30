package felbenini.twitter.clone.Auth;

import felbenini.twitter.clone.user.UserRole;

public record RegisterDTO(String username, String password, UserRole role, String email) {
}
