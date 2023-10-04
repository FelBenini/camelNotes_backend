package felbenini.camel.notes.profile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
  public Profile findByUsername(String username);
  public Page<Profile> findByFollowers_Username(String username, Pageable pageable);
  public Page<Profile> findByFollowing_Username(String username, Pageable pageable);
}
