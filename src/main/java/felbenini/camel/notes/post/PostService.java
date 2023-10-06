package felbenini.camel.notes.post;

import felbenini.camel.notes.profile.ProfileRepository;
import felbenini.camel.notes.profile.Profile;
import felbenini.camel.notes.profile.ProfileResponseDTO;
import felbenini.camel.notes.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PostService {
  @Autowired
  private ProfileRepository profileRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  ProfileService profileService;

  public ResponseEntity getPostsFromUser(String username, Integer page) {
    if (page == null) page = 1;
    Profile profile = this.profileRepository.findByUsername(username);
    if (profile == null) return ResponseEntity.notFound().build();
    Page<Post> posts = this.postRepository.findByProfile_Username(username, PageRequest.of(page - 1, 15));
    Page<PostResponseDTO> postsResponse = posts.map(PostResponseDTO::new);
    return ResponseEntity.ok(postsResponse);
  }

  public ResponseEntity createPost(PostRequestDTO data, String token) {
    Profile profile = this.profileService.extractProfileFromToken(token);
    if (profile == null) return ResponseEntity.notFound().build();
    Post post = new Post(data.getContent(), profile, PostType.POST);
    postRepository.save(post);
    return ResponseEntity.ok(new PostResponseDTO(post));
  }

  public ResponseEntity getPostsFromFollowing(String token, Integer pageNumber) {
    Profile profile = this.profileService.extractProfileFromToken(token);
    Integer page = pageNumber - 1;
    Page<Post> posts = this.postRepository.findByProfileIn(new ArrayList<>(profile.getFollowing()), PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "postedAt")));
    Page<PostResponseDTO> postsResponse = posts.map(PostResponseDTO::new);
    return ResponseEntity.ok(postsResponse);
  }

  public ResponseEntity getHotPosts(Integer page) {
    Page<Post> posts = this.postRepository.findAll(PageRequest.of(page - 1, 15, Sort.by(Sort.Direction.DESC, "hotnessScore")));
    Page<PostResponseDTO> postsResponse = posts.map(PostResponseDTO::new);
    return ResponseEntity.ok(postsResponse);
  }

  public ResponseEntity likeAPost(String id, String token) {
    Profile profile = profileService.extractProfileFromToken(token);
    Optional<Post> post = this.postRepository.findById(id);
    if (post.isEmpty()) return ResponseEntity.notFound().build();
    Post postValue = post.get();
    if (postValue.getLikedBy().contains(profile)) {
      postValue.setLikeCount(postValue.getLikeCount() - 1);
      postValue.getLikedBy().remove(profile);
      postValue.updateHotnessScore();
      postRepository.save(postValue);
      return ResponseEntity.ok("Removed like from Post");
    }
    postValue.setLikeCount(postValue.getLikeCount() + 1);
    postValue.getLikedBy().add(profile);
    postValue.updateHotnessScore();
    postRepository.save(postValue);
    return ResponseEntity.ok("Added the like to the post");
  }

  public ResponseEntity replyToAPost(String postId, String token, PostRequestDTO data) {
    Profile profile = profileService.extractProfileFromToken(token);
    Optional<Post> mainPost = postRepository.findById(postId);
    if (mainPost.isEmpty()) return ResponseEntity.notFound().build();
    Post postValue = mainPost.get();
    Post reply = new Post(data.getContent(), profile, postValue);
    postValue.getReplies().add(reply);
    reply.setMainPost(postValue);
    postValue.setReplyCount(postValue.getReplyCount() + 1);
    postRepository.save(postValue);
    postRepository.save(reply);
    return ResponseEntity.ok(new PostResponseDTO(reply));
  }

  public ResponseEntity getReplies(String postId, Integer page) {
    Optional<Post> post = postRepository.findById(postId);
    if (post.isEmpty()) return ResponseEntity.notFound().build();
    Page<Post> replies = postRepository.findByMainPost_Id(postId, PageRequest.of(page - 1, 15));
    Page<PostResponseDTO> repliesDTO = replies.map(PostResponseDTO::new);
    return ResponseEntity.ok(repliesDTO);
  }

  public ResponseEntity getLikes(String id, Integer page) {
    Optional<Post> post = postRepository.findById(id);
    if (post.isEmpty()) return ResponseEntity.notFound().build();
    Page<Profile> usersThatLiked = profileRepository.findByLikedPosts_Id(post.get().getId(), PageRequest.of(page - 1, 15));
    Page<ProfileResponseDTO> usersThatLikedResponse = usersThatLiked.map(ProfileResponseDTO::new);
    return ResponseEntity.ok(usersThatLikedResponse);
  }
}
