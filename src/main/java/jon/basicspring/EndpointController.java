package jon.basicspring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointController {

    @PostMapping("/home")
    public String home(){
        return "you are home";
    }

}
