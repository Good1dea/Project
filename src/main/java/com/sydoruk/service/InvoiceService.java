package com.sydoruk.service;

import com.sydoruk.model.Invoice;
import com.sydoruk.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository repository;
    private final ParkingPlaceService service;
    private final EmailService emailService;

    @Autowired
    public InvoiceService(InvoiceRepository repository, ParkingPlaceService service, EmailService emailService){
        this.repository = repository;
        this.service = service;
        this.emailService = emailService;
    }

    public boolean save(final Invoice invoice) {
        final int placeId = invoice.getReservedPlace().getId();
        boolean isReserved = false;
        if(!(service.getById(placeId).getReserved())){
            service.updateParkingPlace(placeId, true);
            repository.save(invoice);
            isReserved = true;
        }
        return isReserved;
    }

    public Page<Invoice> getPaginatedInvoices(PageRequest pageable) {
        return repository.findAllByOrderByDateDesc(pageable);
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public String checkInvoices() {
        String message = "There are no invoices due today";
        final LocalDate today = LocalDate.now();
        final List<Invoice> invoices = repository.findByEndDate(today);
        if(invoices != null){
            message = "The invoice check is successful, the parking places reservation status has been changed ";
            for (Invoice invoice : invoices) {
                if(invoice.getReservedPlace().getReserved()) {
                    int placeId = invoice.getReservedPlace().getId();
                    service.updateParkingPlace(placeId, false);
                    emailService.sendEmail(invoice.getUser().getEmail(), "Reservation has expired",
                            " Your parking space number " + placeId + " reservation has expired. Visit the site to order a new reservation.");
                    message = message + " " + placeId;
                }
            }
        }
        return message;
    }

    public List<Invoice> findByPlaceId(final int id){
        return repository.findByReservedPlaceId(id);
    }

    public List <Invoice> findByUserEmail(final String email){
        return repository.findByUserEmail(email);
    }

    public Invoice findById(final String id){
        return repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Invoice with id " + id + "not found"));
    }

    public String sendInvoiceToMail (final Invoice invoice) throws MessagingException {
        emailService.sendInvoiceEmail(invoice);
        return "Invoice " + invoice.getId() + " was sent to email " + invoice.getUser().getEmail();
    }

    public List<Invoice> findInvoicesToday(){
        return repository.findAllByDateOrderByDateDesc(LocalDate.now());
    }

    public List<Invoice> findInvoiceByCarNumber(final String carNumber) {
        return repository.findByCarNumber(carNumber);
    }

}