package felbenini.camel.notes.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  public UserDetails findByUsername(String username);
  public UserDetails findByEmail(String email);
}
