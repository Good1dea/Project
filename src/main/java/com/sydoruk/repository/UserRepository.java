package com.sydoruk.repository;

import com.sydoruk.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Users, String> {

    Optional<Users> findByEmail(final String email);

    default void deleteByEmail(final String email) {
        findByEmail(email).ifPresent(this::delete);
    }
}