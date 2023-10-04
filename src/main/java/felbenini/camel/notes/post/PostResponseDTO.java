package felbenini.camel.notes.post;

import felbenini.camel.notes.profile.ProfileResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PostResponseDTO {
  private String id;
  private String content;
  private Instant postedAt;
  private Long likeCount;
  private PostType type;
  private ProfileResponseDTO profile;

  public PostResponseDTO(
      Post post
  ) {
    this.id = post.getId();
    this.content = post.getContent();
    this.postedAt = post.getPostedAt();
    this.likeCount = post.getLikeCount();
    this.type = post.getType();
    this.profile = new ProfileResponseDTO(post.getProfile());
  }
}
