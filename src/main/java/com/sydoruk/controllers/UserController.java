package com.sydoruk.controllers;

import com.sydoruk.model.Role;
import com.sydoruk.model.Users;
import com.sydoruk.service.InvoiceService;
import com.sydoruk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class UserController {
    private final UserService userService;
    private final InvoiceService invoiceService;

    @Autowired
    public UserController(UserService service, InvoiceService invoiceService) {
        this.userService = service;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        return "login";
    }

    @GetMapping("/logout")
    public String getLogout(Model model) {
        return "redirect:";
    }

    @GetMapping("/registration")
    public ModelAndView goToRegistration(ModelAndView modelAndView) {
        modelAndView.addObject("user", new Users());
        modelAndView.setViewName("registration");
        return modelAndView;
    }
    @PostMapping("/registration")
    public ModelAndView registration(@ModelAttribute Users user, ModelAndView modelAndView) {
        String email = user.getEmail();
        if (userService.isEmailAlreadyInUse(email)) {
            modelAndView.addObject("error", "User with this email already exists. Please use a different email.");
            modelAndView.setViewName("registration");
        } else {
            user.setRole(Role.ROLE_USER);
            userService.save(user);
            log.info("Create new user with email{}", email);
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }
    @GetMapping("/user")
    public String goToUser(@ModelAttribute("message")String message,Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String login = auth.getName();
        Users user = userService.loadUserByUsername(login);
        model.addAttribute("login", login);
        model.addAttribute("lastName",user.getLastName());
        model.addAttribute("firstName",user.getFirstName());
        model.addAttribute("phone",user.getPhone());
        model.addAttribute("message", message);
        return "user_info";
    }
    @GetMapping("/user/update")
    public String getUpdate(Model model) {
        return "redirect:/user";
    }

    @PostMapping("/user/update")
    public ModelAndView goToUserCabinet(@RequestParam("email") String email, @RequestParam("firstName") String firstName,
                                        @RequestParam("lastName") String lastName, @RequestParam("phone") String phone,
                                        @RequestParam("password") String password,
                                        ModelAndView modelAndView, final RedirectAttributes redirectAttributes) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        String message = userService.updateUser(login, email, firstName, lastName, phone, password);
        log.info("Updated user {} info", login);
        redirectAttributes.addFlashAttribute("message", message);
        modelAndView.setViewName("redirect:/user");
        return modelAndView;
    }
    @GetMapping("/user/history")
    public ModelAndView goToUserHistory(ModelAndView modelAndView) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        modelAndView.addObject("invoices", invoiceService.findByUserEmail(login));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))){
            modelAndView.setViewName("redirect:/invoice-list-paging/1");
        }
        else {
            modelAndView.setViewName("history");
        }
        return modelAndView;
    }


}