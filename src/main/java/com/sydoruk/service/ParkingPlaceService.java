package com.sydoruk.service;

import com.sydoruk.model.ParkingPlace;
import com.sydoruk.repository.ParkingPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingPlaceService {

    private final ParkingPlaceRepository repository;

    @Autowired
    public ParkingPlaceService(ParkingPlaceRepository repository) {
        this.repository = repository;
    }

    public List<ParkingPlace> getAllParkingPlace() {
        return repository.findAllByOrderByIdAsc();
    }

    public void updateParkingPlace(final int id, final boolean reserved) {
        ParkingPlace place = getById(id);
        place.setReserved(reserved);
        repository.save(place);
    }

    public ParkingPlace getById(final int id) {
        return repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Parking place number " + id + "not found"));
    }
}