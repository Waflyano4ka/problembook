package diplom.problembook.controllers.api;

import diplom.problembook.models.*;
import diplom.problembook.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/user")
public class UserRestController {
    private final UserRepository userRepository;

    @Autowired
    public UserRestController(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/mobile")
    public ResponseEntity getMobileToken(@AuthenticationPrincipal User user) {
        user = userRepository.findById(user.getId()).orElseThrow();
        String token;
        if (user.getMobileToken() == null) {
            token = UUID.randomUUID().toString();
            while (userRepository.findByMobileToken(token).size() != 0) {
                token = UUID.randomUUID().toString();
            }
            user.setMobileToken(token);
            userRepository.save(user);
        }
        else {
            token = user.getMobileToken();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(token);
    }
}
