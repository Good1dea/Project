package com.sydoruk.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Entity
@Getter
@Setter
public class Invoice {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users user;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "place_id")
    private ParkingPlace reservedPlace;
    @NotNull
    private String carNumber;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    private Double totalPrice;

    public void setStartDate(String strDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        startDate = LocalDate.parse(strDate, formatter);
    }

    public void setEndDate(String strDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        endDate = LocalDate.parse(strDate, formatter);
    }

    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = LocalDate.now();;
        }
        if (startDate != null && endDate != null && reservedPlace != null) {
            long reservedDays = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
            totalPrice = reservedDays * reservedPlace.getPricePerDay();
        }
    }
}