package felbenini.twitter.clone.post;

import felbenini.twitter.clone.profile.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

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
  private Instant postedAt;
  private String content;
  @ManyToOne
  @JoinColumn(name = "profileId", nullable = false)
  private Profile profile;
  private Long likeCount;
}
