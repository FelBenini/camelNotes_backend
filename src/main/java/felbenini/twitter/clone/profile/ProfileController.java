package felbenini.twitter.clone.profile;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/profile")
@AllArgsConstructor
@NoArgsConstructor
public class ProfileController {
  @Autowired
  private ProfileRepository profileRepository;

  @GetMapping("/{username}")
  public ResponseEntity getProfile(@PathVariable("username") String username) {
    Profile profile = profileRepository.findByUsername(username);
    if (profile == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    return ResponseEntity.ok(new ProfileResponseDTO(profile.getUsername(),
        profile.getFollowersCount(),
        profile.getFollowingCount(),
        profile.getDisplayName()
    ));
  }
}
