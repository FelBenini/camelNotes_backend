package felbenini.camel.notes.post;

import felbenini.camel.notes.profile.Profile;
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
  private PostResponseDTO mainPost;
  private Long replyCount;
  private boolean liked;

  public PostResponseDTO(
      Post post
  ) {
    this.id = post.getId();
    this.content = post.getContent();
    this.postedAt = post.getPostedAt();
    this.likeCount = post.getLikeCount();
    this.type = post.getType();
    this.profile = new ProfileResponseDTO(post.getProfile());
    if (post.getMainPost() == null) {
      this.mainPost = null;
    } else {
      this.mainPost = new PostResponseDTO(post.getMainPost());
    }
    this.replyCount = post.getReplyCount();
    this.liked = false;
  }

  public PostResponseDTO(Post post, Profile profileReq) {
    this.id = post.getId();
    this.content = post.getContent();
    this.postedAt = post.getPostedAt();
    this.likeCount = post.getLikeCount();
    this.type = post.getType();
    this.profile = new ProfileResponseDTO(post.getProfile(), profileReq);
    if (post.getMainPost() == null) {
      this.mainPost = null;
    } else {
      this.mainPost = new PostResponseDTO(post.getMainPost(), profileReq);
    }
    this.replyCount = post.getReplyCount();
    this.liked = this.isLiked(post, profileReq);
  }

  private boolean isLiked(Post post, Profile profile) {
    return post.getLikedBy().contains(profile);
  }
}
