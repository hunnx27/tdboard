package com.twodollar.tdboard;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WelcomeWebController {
    @GetMapping("")
    public String welcomePage(Model model) {
        return "index";
        // return "pages/common/commingsoon";
    }
}
