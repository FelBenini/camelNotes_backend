package felbenini.twitter.clone.profile;

import felbenini.twitter.clone.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String profileId;
  @Column(unique = true)
  private String username;
  private String displayName;
  private Long followersCount;
  private Long followingCount;
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "userId")
  private User user;
  @ManyToMany
  @JoinColumn(
      name = "follows"
  )
  private Set<Profile> followers = new HashSet<>();

  public Profile(String username, String displayName, Long followers, Long following) {
    this.username = username;
    this.displayName = displayName;
    this.followersCount = Long.valueOf(0);
    this.followingCount = Long.valueOf(0);
  }

  public String getProfileId() {
    return profileId;
  }

  public void setProfileId(String profileId) {
    this.profileId = profileId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public Long getFollowersCount() {
    return followersCount;
  }

  public void setFollowersCount(Long followers) {
    this.followersCount = followers;
  }

  public Long getFollowingCount() {
    return followingCount;
  }

  public void setFollowing(Long following) {
    this.followingCount = following;
  }
}
