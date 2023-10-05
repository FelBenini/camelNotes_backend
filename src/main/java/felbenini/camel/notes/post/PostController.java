package felbenini.camel.notes.post;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
  @Autowired
  private PostService postService;

  @PostMapping
  public ResponseEntity createPost(@RequestBody @Valid PostRequestDTO data, @RequestHeader(value = "Authorization") String authHeader) {
    return postService.createPost(data, authHeader);
  }
  @GetMapping("/{username}")
  public ResponseEntity getPostsByUser(@PathVariable("username") String username, @RequestParam(value = "page", required = false) Integer page) {
    return this.postService.getPostsFromUser(username, page);
  }
  @GetMapping("/feed")
  public ResponseEntity getFollowingPosts(@RequestHeader(value = "Authorization") String token, @RequestParam(value = "page", required = false) Integer page) {
    if (page == null) page = 1;
    return this.postService.getPostsFromFollowing(token, page);
  }
  @GetMapping("/hot-posts")
  public ResponseEntity getHotPosts(@RequestParam(value = "page", required = false) Integer page) {
    if (page == null) page = 1;
    return this.postService.getHotPosts(page);
  }
  @PostMapping("/like/{id}")
  public ResponseEntity likeAPost(@PathVariable("id") String id, @RequestHeader(value = "Authorization") String token) {
    return this.postService.likeAPost(id, token);
  }

  @PostMapping("/reply/{id}")
  public ResponseEntity reply(@PathVariable("id") String id, @RequestHeader(value = "Authorization") String token, @RequestBody @Valid PostRequestDTO data) {
    return this.postService.replyToAPost(id, token, data);
  }

  @GetMapping("/replies/{id}")
  public ResponseEntity getReplies(@PathVariable("id") String id, @RequestParam(value = "page", required = false) Integer page) {
    if (page == null) page = 1;
    return this.postService.getReplies(id, page);
  }
}
