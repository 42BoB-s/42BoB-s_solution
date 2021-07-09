package bobs.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/errors")
public class CustomErrorController implements ErrorController {

    @GetMapping
    public String error400Page() {
        return "/errors/error";
    }
    @Override
    public String getErrorPath() {
        return null;
    }
}
