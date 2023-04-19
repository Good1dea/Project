package com.sydoruk.service;

import com.sydoruk.model.Role;
import com.sydoruk.model.Users;
import com.sydoruk.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isEmailAlreadyInUse(final String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void save(final Users user) {
        if (user.getPassword() != null && user.getEmail() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            log.info("Create new user with email {}", user.getEmail());
        } else {
            throw new IllegalArgumentException("Email or password is incorrect");
        }
    }

    @Override
    public Users loadUserByUsername(final String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
    }

    public String updateUser(final String login, final String email,final String firstName, final String lastName,
                             final String phone, final String password){
        String message = "";
        Users user = loadUserByUsername(login);
        if(email !=null && !email.isEmpty()){
            if(isEmailAlreadyInUse(email)){
                message = message + " User with email " + email + " already exists. Email has not been updated. ";
            } else {
                user.setEmail(email);
                message = message + " Updated email to " + email + ";";
            }
        }
        if(firstName != null && !firstName.isEmpty()){
            user.setFirstName(firstName);
            message = message +" Updated first name to " + firstName + ";";
        }
        if(lastName != null && !lastName.isEmpty()){
            user.setLastName(lastName);
            message = message +" Updated last name to " + lastName + ";";
        }
        if(phone != null && !phone.isEmpty()){
            user.setPhone(phone);
            message = message +" Updated phone to " + phone + ";";
        }
        if(password != null && !password.isEmpty()){
            user.setPassword(passwordEncoder.encode(password));
            message = message +" Updated password";
        }
        userRepository.save(user);
        return message;
    }

    public String deleteByEmail(final String email){
        String message = "";
        if(isEmailAlreadyInUse(email)){
            userRepository.deleteByEmail(email);
            message = "User with email " + email + " deleted";
        } else{
            message ="There is no user with this email " + email;
        }
        return message;
    }

    public String updateUserToAdmin(final String email){
        String message = "";
        if(isEmailAlreadyInUse(email)){
            Users user = loadUserByUsername(email);
            user.setRole(Role.ROLE_ADMIN);
            userRepository.save(user);
            message = "User with email " + email + " upgraded to Admin";
        } else{
            message ="There is no user with this email " + email;
        }
        return message;
    }

}