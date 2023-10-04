package felbenini.camel.notes.auth;

import felbenini.camel.notes.user.User;
import felbenini.camel.notes.user.UserRepository;
import felbenini.camel.notes.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private TokenService tokenService;
  @Autowired
  private UserRepository userRepository;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
    if (this.userRepository.findByUsername(data.username()) != null || this.userRepository.findByEmail(data.email()) != null) throw new ResponseStatusException(HttpStatus.CONFLICT);
    String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
    User user = new User(
        data.username(),
        encryptedPassword,
        data.email()
    );
    this.userRepository.save(user);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity login(@RequestBody @Valid LoginDTO data) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
    var auth = this.authenticationManager.authenticate(usernamePassword);
    var token = tokenService.generateToken((User) auth.getPrincipal());

    return ResponseEntity.ok(new LoginResponseDTO(token));
  }

  @GetMapping("/session")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity session(@RequestHeader(value = "Authorization") String authHeader) {
    var session = tokenService.extractAllTheClaims(authHeader.replace("Bearer ", ""));
    return ResponseEntity.ok(session);
  }
}
