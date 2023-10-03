package felbenini.twitter.clone.profile;

public class ProfileResponseDTO {
  public String username;
  public Long followers;
  public Long following;
  public String displayName;

  public ProfileResponseDTO(Profile profile) {
    this.username = profile.getUsername();
    this.followers = profile.getFollowersCount();
    this.following = profile.getFollowingCount();
    this.displayName = profile.getDisplayName();
  }
}
