package felbenini.twitter.clone.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import felbenini.twitter.clone.profile.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "postId")
  private String id;
  private Instant postedAt = Instant.now();
  private String content;

  @ManyToOne
  @JoinColumn(name = "profileId", nullable = false)
  @JsonManagedReference
  private Profile profile;
  private Long likeCount = 0L;
  private PostType type;
  private boolean published = true;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "likedBy")
  @JsonBackReference
  private Set<Profile> likedBy = new HashSet<>();

  public Post(String content, Profile profile, PostType type) {
    this.content = content;
    this.profile = profile;
    this.type = type;
  }
}
