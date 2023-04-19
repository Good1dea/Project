package com.sydoruk.repository;

import com.sydoruk.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, String> {

    Page<Invoice> findAllByOrderByDateDesc(Pageable pageable);

    List<Invoice> findByEndDate(final LocalDate date);

    List<Invoice> findAllByDateOrderByDateDesc(final LocalDate date);
    @Query("SELECT i FROM Invoice i " +
            "WHERE i.reservedPlace.id = :id " +
            "ORDER BY i.date DESC")
    List<Invoice> findByReservedPlaceId(@Param("id") int id);

    @Query("SELECT i FROM Invoice i " +
            "WHERE LOWER(i.user.email) = LOWER(:email) " +
            "ORDER BY i.date DESC")
    List<Invoice> findByUserEmail(@Param("email") String email);

    @Query("SELECT i FROM Invoice i " +
            "WHERE UPPER(i.carNumber) " +
            "LIKE CONCAT('%', UPPER(:carNumber), '%')")
    List<Invoice> findByCarNumber(@Param("carNumber") String carNumber);
}
