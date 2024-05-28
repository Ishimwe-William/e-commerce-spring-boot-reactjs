package com.bunsen.ecommerce.model.dao;

import com.bunsen.ecommerce.model.LocalUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserDao extends ListCrudRepository<LocalUser, Long> {
    Optional<LocalUser> findByUsernameIgnoreCase(String username);
    Optional<LocalUser> findByEmailIgnoreCase(String email);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
