package com.sydoruk.controllers;

import com.sydoruk.model.Users;
import com.sydoruk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;

    @Autowired
    public HomeController(UserService service) {
        this.userService = service;
    }

    @GetMapping("/home")
    public String getHome(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ANONYMOUS"))){
            return "redirect:login";
        }
        if(auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))){
            return "redirect:admin";
        }

        String login = auth.getName();
        String role = auth.getAuthorities().toString();
        Users user = userService.loadUserByUsername(login);
        String lastName = user.getLastName();
        String firstName = user.getFirstName();
        String phone = user.getPhone();

        model.addAttribute("login", login);
        model.addAttribute("role", role);
        model.addAttribute("lastName",lastName);
        model.addAttribute("firstName",firstName);
        model.addAttribute("phone",phone);

        return "home";
    }

    @GetMapping("")
    public String getRoot(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null && auth instanceof AnonymousAuthenticationToken){
            return "redirect:login";
        }
        return "redirect:home";
    }

}