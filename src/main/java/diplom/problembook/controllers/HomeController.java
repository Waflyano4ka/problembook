package diplom.problembook.controllers;

import diplom.problembook.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String home(Model model, @AuthenticationPrincipal User user){
        HashMap<Object, Object> data = new HashMap<>();

        if (user != null) {
            data.put("profile", user);
        }

        model.addAttribute("frontendData", data);
        return "index";
    }
}
