package felbenini.twitter.clone.user;

import felbenini.twitter.clone.profile.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "userId")
  private String id;
  @Column(unique = true)
  private String username;
  private String password;
  @Enumerated(EnumType.STRING)
  private UserRole role;
  @OneToOne(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
  private Profile profile;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_MODERATOR"), new SimpleGrantedAuthority("ROLE_USER"));
    if (this.role == UserRole.MODERATOR) return List.of(new SimpleGrantedAuthority("ROLE_MODERATOR"), new SimpleGrantedAuthority("ROLE_USER"));
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.role = UserRole.USER;
    this.profile = new Profile(username, username, Long.valueOf(0), Long.valueOf(0));
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
