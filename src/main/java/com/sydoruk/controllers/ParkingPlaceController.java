package com.sydoruk.controllers;

import com.sydoruk.model.Invoice;
import com.sydoruk.model.ParkingPlace;
import com.sydoruk.service.ParkingPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ParkingPlaceController {
    private final ParkingPlaceService service;

    @Autowired
    public ParkingPlaceController(ParkingPlaceService service){
        this.service = service;
    }

    @GetMapping("/map")
    public String getMap (Model model){
        List<ParkingPlace> places = service.getAllParkingPlace();
        model.addAttribute("places", places);
        return "map";
    }

    @PostMapping("/map")
    public String postMap(@ModelAttribute @RequestBody ParkingPlace place, final RedirectAttributes redirectAttributes){
        Invoice invoice = new Invoice();
        invoice.setReservedPlace(place);
        redirectAttributes.addFlashAttribute("invoice", invoice);
        return "redirect:reservation";
    }

}