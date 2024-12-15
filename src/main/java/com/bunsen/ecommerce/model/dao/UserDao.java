package com.bunsen.ecommerce.model.dao;

import com.bunsen.ecommerce.model.AppUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends ListCrudRepository<AppUser, Long> {
    Optional<AppUser> findByUsernameIgnoreCase(String username);

    Optional<AppUser> findByEmailIgnoreCase(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    List<AppUser> findAll();
}
