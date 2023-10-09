package felbenini.camel.notes.profile;

public class ProfileResponseDTO {
  public String username;
  public Long followers;
  public Long following;
  public String displayName;
  public String description;
  public boolean isFollowing;
  public boolean isFollowed;

  public ProfileResponseDTO(Profile profile) {
    this.username = profile.getUsername();
    this.followers = profile.getFollowersCount();
    this.following = profile.getFollowingCount();
    this.displayName = profile.getDisplayName();
    this.description = profile.getDescription();
    this.isFollowed = false;
    this.isFollowing = false;
  }

  public ProfileResponseDTO(Profile profile, Profile profileReq) {
    this.username = profile.getUsername();
    this.followers = profile.getFollowersCount();
    this.following = profile.getFollowingCount();
    this.displayName = profile.getDisplayName();
    this.description = profile.getDescription();
    if (profileReq == null) {
      this.isFollowing = false;
      this.isFollowed = false;
    } else {
      this.isFollowed = profile.getFollowers().contains(profileReq);
      this.isFollowing = profileReq.getFollowers().contains(profile);
    }

  }
}
