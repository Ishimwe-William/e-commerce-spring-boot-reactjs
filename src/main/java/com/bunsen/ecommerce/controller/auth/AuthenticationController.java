package com.bunsen.ecommerce.controller.auth;

import com.bunsen.ecommerce.api.model.LoginBody;
import com.bunsen.ecommerce.api.model.RegistrationBody;
import com.bunsen.ecommerce.exception.UserAlreadyExistException;
import com.bunsen.ecommerce.model.AppUser;
import com.bunsen.ecommerce.model.LoginResponse;
import com.bunsen.ecommerce.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        logger.info("Registering user with email: {}", registrationBody.getEmail());
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistException ex) {
            logger.error("User already exists: {}", registrationBody.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) {
        logger.info("Logging in user with username: {}", loginBody.getUsername());
        String jwt = userService.loginUser(loginBody);
        if (jwt == null) {
            logger.error("Login failed for user: {}", loginBody.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            LoginResponse response = new LoginResponse();
            response.setJwt(jwt);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public AppUser getLoggedInUserProfile(@AuthenticationPrincipal AppUser user) {
        return user;
    }
}
