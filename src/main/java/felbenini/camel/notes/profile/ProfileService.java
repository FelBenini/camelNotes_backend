package felbenini.camel.notes.profile;

import felbenini.camel.notes.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class ProfileService {
  @Autowired
  private ProfileRepository profileRepository;
  @Autowired
  private TokenService tokenService;

  public boolean toggleFollow(String followedUsername, String followerUsername) {
    Profile follower = profileRepository.findByUsername(followerUsername);
    Profile followed = profileRepository.findByUsername(followedUsername);
    if (follower != null && followed != null) {
      if (followed.getFollowers().contains(follower)) {
        follower.getFollowing().remove(followed);
        followed.getFollowers().remove(follower);

        follower.setFollowingCount(follower.getFollowingCount() - 1);
        followed.setFollowersCount(followed.getFollowersCount() - 1);
      } else {
        follower.getFollowing().add(followed);
        followed.getFollowers().add(follower);

        follower.setFollowingCount(Long.sum(follower.getFollowingCount(), 1L));
        followed.setFollowersCount(Long.sum(follower.getFollowersCount(), 1L));
      }
      profileRepository.save(follower);
      profileRepository.save(followed);
      return follower.getFollowing().contains(followed);
    }
    return false;
  }

  public ResponseEntity findFollowers(String username, Integer pageNumber) {
    int page = pageNumber - 1;
    Profile profile = profileRepository.findByUsername(username);
    if (profile == null) return ResponseEntity.notFound().build();
    Pageable paginated = PageRequest.of(page, 15);
    Page<Profile> followers = profileRepository.findByFollowing_Username(username, paginated);
    Page<ProfileResponseDTO> followersDTO = followers.map(ProfileResponseDTO::new);
    return ResponseEntity.ok(followersDTO);
  }

  public Profile extractProfileFromToken(String token) {
    token = token.replace("Bearer ", "");
    String username = this.tokenService.extractUsername(token);
    return profileRepository.findByUsername(username);
  }
}
