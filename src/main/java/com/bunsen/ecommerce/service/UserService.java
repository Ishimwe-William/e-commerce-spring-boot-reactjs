package com.bunsen.ecommerce.service;

import com.bunsen.ecommerce.api.model.LoginBody;
import com.bunsen.ecommerce.api.model.RegistrationBody;
import com.bunsen.ecommerce.exception.UserAlreadyExistException;
import com.bunsen.ecommerce.model.AppUser;
import com.bunsen.ecommerce.model.ERole;
import com.bunsen.ecommerce.model.Role;
import com.bunsen.ecommerce.model.dao.UserDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserDao userDao;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;

    public UserService(UserDao UserDao, EncryptionService encryptionService, JWTService jwtService) {
        this.userDao = UserDao;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public AppUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistException {
        if (userDao.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
                userDao.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        AppUser user = new AppUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstname(registrationBody.getFirstname());
        user.setLastname(registrationBody.getLastname());
        user.setUsername(registrationBody.getUsername());
        user.setEnabled(true);
        user.setTokenExpired(true);
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        Role userRole = new Role();
        userRole.setName(ERole.ROLE_CUSTOMER);
        user.getRoles().add(userRole);

        return userDao.save(user);
    }

    public String loginUser(LoginBody loginBody) {
        Optional<AppUser> opUser = userDao.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()) {
            AppUser user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                user.setTokenExpired(false);
                userDao.save(user);
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }

    public List<AppUser> getAllUsers(){
        return userDao.findAll();
    }
}
