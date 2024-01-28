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
    @GetMapping("test")
    public String welcomePage2(Model model) {
        return "index2";
        // return "pages/common/commingsoon";
    }
}
