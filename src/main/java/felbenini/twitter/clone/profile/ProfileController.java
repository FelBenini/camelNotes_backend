package felbenini.twitter.clone.profile;

import felbenini.twitter.clone.infra.security.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@AllArgsConstructor
@NoArgsConstructor
public class ProfileController {
  @Autowired
  private ProfileRepository profileRepository;
  @Autowired
  private ProfileService profileService;
  @Autowired
  private TokenService tokenService;

  @GetMapping("/{username}")
  public ResponseEntity getProfile(@PathVariable("username") String username) {
    Profile profile = profileRepository.findByUsername(username);
    if (profile == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return ResponseEntity.ok(new ProfileResponseDTO(profile));
  }

  @PostMapping("/follow/{username}")
  public ResponseEntity followProfile(@PathVariable("username") String username, @RequestHeader(value = "Authorization") String authHeader) {
    String followingUsername = tokenService.extractUsername(authHeader.replace("Bearer ", ""));
    boolean isFollowing = profileService.toggleFollow(username, followingUsername);
    if (isFollowing) {
      return ResponseEntity.ok("Followed successfully");
    } else {
      return ResponseEntity.ok("Unfollowed successfully");
    }
  }

  @GetMapping("/{username}/followers")
  public ResponseEntity getFollowers(@PathVariable("username") String username, @RequestParam(value = "page", required = false) Integer page) {
    if (page == null) page = 1;
    return profileService.findFollowers(username, page);
  }

  @PutMapping("/edit")
  public ResponseEntity editProfile(@RequestHeader(value = "Authorization") String authToken, @RequestBody @Valid ProfileEditDTO data) {
    String username = tokenService.extractUsername(authToken.replace("Bearer ", ""));
    Profile userProfile = profileRepository.findByUsername(username);
    if (userProfile == null) return ResponseEntity.notFound().build();
    userProfile.setDisplayName(data.displayName());
    userProfile.setDescription(data.description());
    profileRepository.save(userProfile);
    return ResponseEntity.ok(new ProfileResponseDTO(userProfile));
  }
}
