package com.bunsen.ecommerce.model.dao;
;

import com.bunsen.ecommerce.model.LocalUser;
import com.bunsen.ecommerce.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationTokenDao extends ListCrudRepository<VerificationToken, Long> {
    Optional<VerificationToken>  findByToken(String token);
    void deleteByUser(LocalUser user);
}
