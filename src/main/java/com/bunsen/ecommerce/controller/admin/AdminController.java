package com.bunsen.ecommerce.controller.admin;

import com.bunsen.ecommerce.model.AppUser;
import com.bunsen.ecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
//    @PreAuthorize("hasRole('ADMIN')")
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }
}