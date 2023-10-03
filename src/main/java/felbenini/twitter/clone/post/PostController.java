package felbenini.twitter.clone.post;

import felbenini.twitter.clone.infra.security.TokenService;
import felbenini.twitter.clone.profile.Profile;
import felbenini.twitter.clone.profile.ProfileRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private ProfileRepository profileRepository;
  @Autowired
  private TokenService tokenService;

  @PostMapping
  public ResponseEntity createPost(@RequestBody @Valid PostRequestDTO data, @RequestHeader(value = "Authorization") String authHeader) {
    String username = tokenService.extractUsername(authHeader.replace("Bearer ", ""));
    Profile profile = profileRepository.findByUsername(username);
    if (profile == null) return ResponseEntity.notFound().build();
    Post post = new Post(data.getContent(), profile, PostType.POST);
    postRepository.save(post);
    return ResponseEntity.ok(post);
  }

  @GetMapping("/{username}")
  public ResponseEntity getPostsByUser(@PathVariable("username") String username, @RequestParam(value = "page", required = false) Integer page) {
    if (page == null) page = 1;
    Profile profile = profileRepository.findByUsername(username);
    if (profile == null) return ResponseEntity.notFound().build();
    Page<Post> posts = postRepository.findByProfile_Username(username, PageRequest.of(page - 1, 15));
    return ResponseEntity.ok(posts);
  }
}
