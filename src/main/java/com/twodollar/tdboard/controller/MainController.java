package com.twodollar.tdboard.controller;

import com.twodollar.tdboard.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping("")
    public String welcomePage(Model model) {
        List<User> userList = new ArrayList<>();
        for(int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId((long) i);
            userList.add(user);
        }

        model.addAttribute(userList);
        return "index";
    }
}
