package felbenini.camel.notes.post;

import felbenini.camel.notes.profile.ProfileRepository;
import felbenini.camel.notes.infra.security.TokenService;
import felbenini.camel.notes.profile.Profile;
import felbenini.camel.notes.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
  @Autowired
  private ProfileRepository profileRepository;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private TokenService tokenService;
  @Autowired
  ProfileService profileService;

  public ResponseEntity getPostsFromUser(String username, Integer page) {
    if (page == null) page = 1;
    Profile profile = profileRepository.findByUsername(username);
    if (profile == null) return ResponseEntity.notFound().build();
    Page<Post> posts = postRepository.findByProfile_Username(username, PageRequest.of(page - 1, 15));
    Page<PostResponseDTO> postsResponse = posts.map(PostResponseDTO::new);
    return ResponseEntity.ok(postsResponse);
  }

  public ResponseEntity createPost(PostRequestDTO data, String token) {
    Profile profile = profileService.extractProfileFromToken(token);
    if (profile == null) return ResponseEntity.notFound().build();
    Post post = new Post(data.getContent(), profile, PostType.POST);
    postRepository.save(post);
    return ResponseEntity.ok(new PostResponseDTO(post));
  }

  public ResponseEntity getPostsFromFollowing(String token) {
    Profile profile = profileService.extractProfileFromToken(token);
    Page<Post> posts = postRepository.findByProfileIn(new ArrayList<>(profile.getFollowing()), PageRequest.of(0, 15, Sort.by(Sort.Direction.DESC, "postedAt")));
    return ResponseEntity.ok(posts);
  }
}
