package com.bunsen.ecommerce.service;

import com.bunsen.ecommerce.api.model.LoginBody;
import com.bunsen.ecommerce.api.model.RegistrationBody;
import com.bunsen.ecommerce.exception.UserAlreadyExistException;
import com.bunsen.ecommerce.model.LocalUser;
import com.bunsen.ecommerce.model.dao.UserDao;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserDao UserDao;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;

    public UserService(UserDao UserDao, EncryptionService encryptionService, JWTService jwtService) {
        this.UserDao = UserDao;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
    }

    public LocalUser registerUser(RegistrationBody registrationBody) throws UserAlreadyExistException {

        if (UserDao.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
                UserDao.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstname(registrationBody.getFirstname());
        user.setLastname(registrationBody.getLastname());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        return UserDao.save(user);
    }

    public String loginUser(LoginBody loginBody) {
        Optional<LocalUser> opUser = UserDao.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())){
                return jwtService.generateJWT(user);
            }
        }
        return null;
    }
}
