package com.changgou.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by fyf on 2019/9/3
 */
@Controller
@RequestMapping("/oauth")
public class LoginRedirectController {
    @RequestMapping("/login")
    public String login(String From, Model model){
        model.addAttribute("From",From);
        return "login";
    }
}
