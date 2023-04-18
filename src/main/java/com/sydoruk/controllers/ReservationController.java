package com.sydoruk.controllers;

import com.sydoruk.model.Invoice;
import com.sydoruk.service.InvoiceService;
import com.sydoruk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;

@Slf4j
@Controller
public class ReservationController {

    private final InvoiceService invoiceService;
    private final UserService userService;


    @Autowired
    public ReservationController(InvoiceService service, UserService userService) {
        this.invoiceService = service;
        this.userService = userService;
    }

    @GetMapping("/reservation")
    public String goToInvoice(@ModelAttribute("invoice")Invoice invoice, Model model) {
        if(invoice.getReservedPlace() == null) return "redirect:map";
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("login", login);
        model.addAttribute("invoice", invoice);
        model.addAttribute("place_id", invoice.getReservedPlace().getId());
        return  "reservation";
    }

    @PostMapping("/reservation/invoice")
    public ModelAndView saveInvoice(@ModelAttribute Invoice invoice, ModelAndView modelAndView) throws MessagingException {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        invoice.setUser(userService.loadUserByUsername(login));
        String message = "This parking place is reserved already, please choose another";
        if(invoiceService.save(invoice)) {
            message = invoiceService.sendInvoiceToMail(invoice);
            log.info("Create new invoice {} and sent to email {}", invoice.getId(), login);
            modelAndView.addObject("invoice", invoice);
            modelAndView.setViewName("invoice");
        } else {
            modelAndView.setViewName("reservation");
        }
        modelAndView.addObject("message", message);
        return modelAndView;
    }
}