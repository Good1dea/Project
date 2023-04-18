package com.sydoruk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class ParkingPlace {

    @Id
    private int id;
    private boolean reserved;
    private double pricePerDay;

    public boolean getReserved() {
        return reserved;
    }

}