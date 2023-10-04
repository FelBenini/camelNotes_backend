package felbenini.camel.notes.profile;

public class ProfileResponseDTO {
  public String username;
  public Long followers;
  public Long following;
  public String displayName;
  public String description;

  public ProfileResponseDTO(Profile profile) {
    this.username = profile.getUsername();
    this.followers = profile.getFollowersCount();
    this.following = profile.getFollowingCount();
    this.displayName = profile.getDisplayName();
    this.description = profile.getDescription();
  }
}
