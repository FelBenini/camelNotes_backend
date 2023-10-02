package felbenini.twitter.clone.profile;

import felbenini.twitter.clone.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
  private Long followers;
  private Long following;
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "userId")
  private User user;

  public Profile(String username, String displayName, Long followers, Long following) {
    this.username = username;
    this.displayName = displayName;
    this.followers = followers;
    this.following = following;
  }
}
