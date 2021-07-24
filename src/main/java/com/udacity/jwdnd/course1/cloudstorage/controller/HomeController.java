package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getHomePage(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = this.userService.getUser(username);
        String fullName = user.getFirstName() + " " + user.getLastName();

        model.addAttribute("userFullName", fullName);
        model.addAttribute("username", username);

        return "home";
    }
}
