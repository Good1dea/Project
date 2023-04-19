package com.sydoruk.repository;

import com.sydoruk.model.ParkingPlace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingPlaceRepository extends CrudRepository<ParkingPlace, Integer> {

    List<ParkingPlace> findAllByOrderByIdAsc();

}

