package felbenini.twitter.clone;

import felbenini.twitter.clone.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sample")
public class SampleController {

  @Autowired
  private TokenService tokenService;
  @GetMapping
  public String sample(@RequestHeader(value = "Authorization") String headerToken) {
    String token = headerToken.replace("Bearer", "");
    var username = tokenService.extractUsername(token);
    return username;
  }

  @PostMapping
  public String postSample() {
    return "I should be authenticated to see this";
  }
}
