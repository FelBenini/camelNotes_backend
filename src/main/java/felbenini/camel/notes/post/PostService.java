package felbenini.camel.notes.post;

import felbenini.camel.notes.profile.ProfileRepository;
import felbenini.camel.notes.profile.Profile;
import felbenini.camel.notes.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
}
