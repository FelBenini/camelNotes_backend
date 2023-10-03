package felbenini.twitter.clone.profile;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import felbenini.twitter.clone.post.Post;
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
  private String description;
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "follows"
  )
  @JsonBackReference
  private Set<Profile> followers = new HashSet<>();

  @ManyToMany(mappedBy = "followers")
  @JsonManagedReference
  private Set<Profile> following = new HashSet<>();
  @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
  private Set<Post> posts;

  public Profile(String username, String displayName) {
    this.username = username;
    this.displayName = displayName;
    this.followersCount = 0L;
    this.followingCount = 0L;
  }
}
