package searchengine.controllers;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
public class DefaultController {

    // Request mapping for the default route ("/")
    @RequestMapping("/")
    public String index() {
        return "index"; // Return the name of the view ("index")
    }
}
