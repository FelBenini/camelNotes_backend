package felbenini.twitter.clone.Auth;

import felbenini.twitter.clone.user.User;
import felbenini.twitter.clone.user.UserRepository;
import felbenini.twitter.clone.user.UserRole;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
    var auth = this.authenticationManager.authenticate(usernamePassword);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/register")
  public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
    if (this.userRepository.findByUsername(data.username()) != null) return ResponseEntity.badRequest().build();
    String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
    User user = new User(
        data.username(),
        encryptedPassword,
        UserRole.USER
    );
    this.userRepository.save(user);
    return ResponseEntity.ok().build();
  }
}
