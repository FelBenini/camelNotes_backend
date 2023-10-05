package felbenini.camel.notes.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import felbenini.camel.notes.profile.Profile;
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
  private Long likeCount;
  private PostType type;
  private boolean published = true;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "likedBy")
  @JsonBackReference
  private Set<Profile> likedBy = new HashSet<>();
  private double hotnessScore;

  public Post(String content, Profile profile, PostType type) {
    this.content = content;
    this.profile = profile;
    this.type = type;
    this.likeCount = 0L;
    this.hotnessScore = this.likeCount * 0.6 + ((double) this.postedAt.toEpochMilli() / 3600000000L) * 0.4;
  }

  public void updateHotnessScore() {
    this.hotnessScore = this.likeCount * 0.6 + ((double) this.postedAt.toEpochMilli() / 3600000000L) * 0.4;
  }
}
