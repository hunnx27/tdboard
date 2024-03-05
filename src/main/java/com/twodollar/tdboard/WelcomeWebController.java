package com.twodollar.tdboard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/")
public class WelcomeWebController {
    @GetMapping("")
    public String welcomePage(Model model, HttpServletRequest request) {
        System.out.println(request.getSession(true).getCreationTime());
        System.out.println(request.getSession(true).getMaxInactiveInterval());
        return "index";
        // return "pages/common/commingsoon";
    }

    @GetMapping("/session-info")
    @ResponseBody
    public Map<String,Object> sessionInfo(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        Map<String,Object> r = new HashMap<>();
        if (session == null) {
            return r;
        }

        log.info("sessionId = {}", session.getId());
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessTime={}", new Date(session.getLastAccessedTime()));

        String profile = System.getProperty("spring.profiles.active");
        r.put("sessionId", session.getId());
        r.put("getMaxInactiveInterval", session.getMaxInactiveInterval());
        r.put("creationTime", new Date(session.getCreationTime()));
        r.put("lastAccessTime", new Date(session.getLastAccessedTime()));
        r.put("profile", profile);
        return r;
    }
}
