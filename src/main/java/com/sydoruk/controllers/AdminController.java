package com.sydoruk.controllers;

import com.sydoruk.model.Invoice;
import com.sydoruk.service.InvoiceService;
import com.sydoruk.service.ParkingPlaceService;
import com.sydoruk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
public class AdminController {

    private final UserService userService;
    private final InvoiceService invoiceService;
    private final ParkingPlaceService parkingPlaceService;
    @Value("${invoice.perpage}")
    private int invoicePerPage;

    @Autowired
    public AdminController(UserService service, InvoiceService invoiceService, ParkingPlaceService parkingPlaceService) {
        this.userService = service;
        this.invoiceService = invoiceService;
        this.parkingPlaceService = parkingPlaceService;
    }

    @GetMapping("/admin")
    public ModelAndView goToAdmin(ModelAndView modelAndView) {
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @GetMapping("/admin/invoices")
    public ModelAndView findInvoicesByPlace(@RequestParam("placeId") String placeId, ModelAndView modelAndView) {
        modelAndView.addObject("invoices", invoiceService.findByPlaceId(Integer.parseInt(placeId)));
        String message = "All invoices by parking place " + placeId;
        modelAndView.addObject("message", message);
        modelAndView.setViewName("history");
        return modelAndView;
    }

    @RequestMapping(value = "/invoice-list-paging/{page}")
    public ModelAndView listInvoicePageByPage(@PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("invoice-list-paging");
        PageRequest pageable = PageRequest.of(page-1, invoicePerPage);
        Page<Invoice> invoicePage = invoiceService.getPaginatedInvoices(pageable);
        int totalPages = invoicePage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
        modelAndView.addObject("activeInvoiceList", true);
        modelAndView.addObject("invoiceList", invoicePage.getContent());
        return modelAndView;
    }

    @GetMapping("/admin/sendmail")
    public ModelAndView sendInvoiceToEmail(@RequestParam("invoiceId") String id, ModelAndView modelAndView) throws MessagingException {
        invoiceService.sendInvoiceToMail(invoiceService.findById(id));
        modelAndView.addObject("messageSend", invoiceService.sendInvoiceToMail(invoiceService.findById(id)));
        log.info("Admin sent invoice {} to user email", id);
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @GetMapping("/admin/user_invoices")
    public ModelAndView findInvoicesByUserEmail(@RequestParam("email") String email, ModelAndView modelAndView) {
        modelAndView.addObject("invoices", invoiceService.findByUserEmail(email));
        String message = "All invoices by user email " + email;
        modelAndView.addObject("message", message);
        modelAndView.setViewName("history");
        return modelAndView;
    }

    @PostMapping("/admin/map_refresh")
    public ModelAndView refreshMap(ModelAndView modelAndView) {
        modelAndView.addObject("message", invoiceService.checkInvoices());
        log.info("Admin refresh parking map");
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @PostMapping("/admin/delete_user")
    public ModelAndView deleteUserByEmail(@RequestParam("email") String email,ModelAndView modelAndView) {
        String message = userService.deleteByEmail(email);
        modelAndView.addObject("message", message);
        log.info("Admin deleted user with email {}", email);
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @PostMapping("/admin/block_place")
    public ModelAndView blockParkingPlaceByNumber(@RequestParam("id") String id,ModelAndView modelAndView) {
        parkingPlaceService.updateParkingPlace(Integer.parseInt(id), true);
        modelAndView.addObject("message", "Parking place number " + id + " is blocked");
        log.info("Admin blocked parking place number {}", id);
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @PostMapping("/admin/unlock_place")
    public ModelAndView unlockedParkingPlaceByNumber(@RequestParam("place_id") String place_id,ModelAndView modelAndView) {
        parkingPlaceService.updateParkingPlace(Integer.parseInt(place_id), false);
        modelAndView.addObject("message", "Parking place number " + place_id + " is unlocked");
        log.info("Admin unlocked parking place number {}", place_id);
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @PostMapping("/admin/create_admin")
    public ModelAndView updateToAdmin(@RequestParam("emailAdmin") String email,ModelAndView modelAndView) {
        String message = userService.updateUserToAdmin(email);
        modelAndView.addObject("message", message);
        log.info("Admin upgraded user {} to ADMIN", email);
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @GetMapping("/admin/invoices_today")
    public ModelAndView invoicesByDay(ModelAndView modelAndView) {
        modelAndView.addObject("invoices", invoiceService.findInvoicesToday());
        String message = "All new invoices today";
        modelAndView.addObject("message", message);
        modelAndView.setViewName("history");
        return modelAndView;
    }

    @GetMapping("/admin/invoices_car")
    public ModelAndView findAllInvoices(@RequestParam("carNumber") String carNumber,ModelAndView modelAndView) {
        modelAndView.addObject("invoices", invoiceService.findInvoiceByCarNumber(carNumber));
        modelAndView.addObject("message", "Invoices by car number "+ carNumber);
        modelAndView.setViewName("history");
        return modelAndView;
    }
}