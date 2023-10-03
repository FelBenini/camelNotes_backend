package felbenini.twitter.clone.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
  @Autowired
  private ProfileRepository profileRepository;

  public boolean toggleFollow(String followedUsername, String followerUsername) {
    Profile follower = profileRepository.findByUsername(followerUsername);
    Profile followed = profileRepository.findByUsername(followedUsername);
    if (follower != null && followed != null) {
      if (followed.getFollowers().contains(follower)) {
        follower.getFollowing().remove(followed);
        followed.getFollowers().remove(follower);
      } else {
        follower.getFollowing().add(followed);
        followed.getFollowers().add(follower);
      }
      profileRepository.save(follower);
      profileRepository.save(followed);
      return follower.getFollowing().contains(followed);
    }
    return false;
  }
}
