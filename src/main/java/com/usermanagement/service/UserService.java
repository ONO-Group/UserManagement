package com.usermanagement.service;

import com.usermanagement.entity.Role;
import com.usermanagement.entity.User;
import com.usermanagement.utils.Util;
import com.usermanagement.exception.GeneralExceptionWithMessage;
import com.usermanagement.repository.RoleRepository;
import com.usermanagement.repository.UserRepository;
import com.usermanagement.response.DefaultResponse;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserService {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    //  post mapping password encryption
    public ResponseEntity<DefaultResponse> create(@RequestBody User user, int roleID, int createdById) throws Exception {

        String response = "";
        Role role = roleRepository.findById(roleID);
        if (role == null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("Role not found", "F01", null), HttpStatus.NOT_ACCEPTABLE);

        User createdBy = userRepository.findById(createdById);
        if (createdBy == null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("Created by user not found", "F01", null), HttpStatus.NOT_ACCEPTABLE);


        if (userRepository.findByEmail(user.getEmail()))
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("Email already exist", "F01", null), HttpStatus.NOT_ACCEPTABLE);


        String base64Password = Util.encryptStringToBase64(user.getPassword());
        user.setPassword(base64Password);
        user.setRole(role);
        user.setCreatedBy(createdBy);
        user.setStatus(1);
        userRepository.save(user);

        return new ResponseEntity<DefaultResponse>(new DefaultResponse("User created successfully", "S01", null), HttpStatus.OK);

    }


    //post mapping for login
    public User Login(User request) throws Exception {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), Util.encryptStringToBase64(request.getPassword()));
        if (user == null)
            throw new GeneralExceptionWithMessage("user is not exist");
        else {

            Timestamp lastLoginTimestamp = user.getLastLogin();
            user.setLastLogin(Timestamp.valueOf(LocalDateTime.now()));
            userRepository.save(user);
            user.setLastLogin(lastLoginTimestamp);
            user.setPassword(Util.decryptStringToBase64(user.getPassword()));
            return user;
        }
    }

    public List<User> getAllUsers(Integer userId) {
        User user = validateUserRole(userId);
        List<User> userList = null;
        if (user.getRole().getId() == 3){
            LOGGER.info("You don't have rights to see all users");
            return userList;
        } else if (user.getRole().getId() == 2) {
            userList = userRepository.findAllByCreatedBy(user);
        }
        else
            userList = userRepository.findAll();
        return userList;

    }

    private User validateUserRole(int userId) {
        User user = userRepository.findById(userId);
        return user;

    }

}



