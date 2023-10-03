package felbenini.twitter.clone.post;

import felbenini.twitter.clone.profile.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
  public Page<Post> findByProfile_Username(String username, Pageable pageable);
}
