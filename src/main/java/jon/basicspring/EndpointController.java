package jon.basicspring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointController {

    @PostMapping("/home")
    public ResponseEntity<String> home()
    {
        return ResponseEntity.ok("you are home");

    }

}
