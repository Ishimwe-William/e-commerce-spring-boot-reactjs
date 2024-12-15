package com.bunsen.ecommerce.model.dao;

import com.bunsen.ecommerce.model.ERole;
import com.bunsen.ecommerce.model.Role;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface RoleDao extends ListCrudRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
