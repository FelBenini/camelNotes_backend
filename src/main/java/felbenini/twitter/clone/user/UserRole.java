package felbenini.twitter.clone.user;

public enum UserRole {
  ADMIN("admin"),
  USER("user"),
  MODERATOR("moderator");

  private String role;

  UserRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return this.role;
  }
}
