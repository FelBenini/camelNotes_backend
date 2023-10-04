package felbenini.camel.notes.post;

import felbenini.camel.notes.profile.ProfileRepository;
import felbenini.camel.notes.infra.security.TokenService;
import felbenini.camel.notes.profile.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PostService {
  @Autowired
  private ProfileRepository profileRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private TokenService tokenService;

  public ResponseEntity getPostsFromUser(String username, Integer page) {
    if (page == null) page = 1;
    Profile profile = profileRepository.findByUsername(username);
    if (profile == null) return ResponseEntity.notFound().build();
    Page<Post> posts = postRepository.findByProfile_Username(username, PageRequest.of(page - 1, 15));
    Page<PostResponseDTO> postsResponse = posts.map(PostResponseDTO::new);
    return ResponseEntity.ok(postsResponse);
  }

  public ResponseEntity createPost(PostRequestDTO data, String token) {
    String username = tokenService.extractUsername(token.replace("Bearer ", ""));
    Profile profile = profileRepository.findByUsername(username);
    if (profile == null) return ResponseEntity.notFound().build();
    Post post = new Post(data.getContent(), profile, PostType.POST);
    postRepository.save(post);
    return ResponseEntity.ok(new PostResponseDTO(post));
  }
}
