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

        //Task 2 Email must be unique
        if (userRepository.findByEmail(user.getEmail())!=null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("Email already exist", "F01", null), HttpStatus.NOT_ACCEPTABLE);

        User createdBy = userRepository.findById(createdById);
        if (createdBy == null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("Created by user not found", "F01", null), HttpStatus.NOT_ACCEPTABLE);

        if(!validateUserRole(createdBy))
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("You don't have rights to create a new user", "F01", null), HttpStatus.NOT_ACCEPTABLE);


        Role role = roleRepository.findById(roleID);
        if (role == null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("Role not found", "F01", null), HttpStatus.NOT_ACCEPTABLE);

        //Task 3 Encrypt the password
        String base64Password = Util.encryptStringToBase64(user.getPassword());
        user.setPassword(base64Password);

        //task 4 set roles and created by user
        user.setRole(role);
        user.setCreatedBy(createdBy);
        user.setStatus(1);
        userRepository.save(user);

        return new ResponseEntity<DefaultResponse>(new DefaultResponse("User created successfully", "S01", null), HttpStatus.OK);

    }


    //post mapping for login
    public User Login(User request) throws Exception {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), Util.encryptStringToBase64(request.getPassword()));

        //validate user
        if (user == null)
            throw new GeneralExceptionWithMessage("user is not exist");
        else {
            //Task 6 save and get their last login detail
            Timestamp lastLoginTimestamp = user.getLastLogin();
            user.setLastLogin(Timestamp.valueOf(LocalDateTime.now()));
            userRepository.save(user);
            user.setLastLogin(lastLoginTimestamp);
            user.setPassword(Util.decryptStringToBase64(user.getPassword()));
            return user;
        }
    }

    public List<User> getAllUsers(int userId) {

        List<User> userList = null;
        User user = userRepository.findById(userId);

        //Task 9
        //validate user has admin rights or moderator
        if(validateUserRole(user)){
            if (user.getRole().getId() == 2) {
                userList = userRepository.findAllByCreatedBy(user);
            }
            else
                userList = userRepository.findAll();
        }else {
            LOGGER.info("You don't have rights to see all users");

        }
        return userList;
    }

    public ResponseEntity<DefaultResponse> updateSingleUser(User request) throws Exception {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), Util.encryptStringToBase64(request.getPassword()));

        //validate user
        if (user == null)
            return new ResponseEntity<DefaultResponse>(new DefaultResponse("User not found", "F01", null), HttpStatus.NOT_ACCEPTABLE);
        else {
         userRepository.save(request);

            return new ResponseEntity<DefaultResponse>(new DefaultResponse("User updated successfully", "S01", null), HttpStatus.OK);
        }
    }


    public ResponseEntity<DefaultResponse> updateMultipleUser(List<User> userList) {
        for (User user:userList) {
            User userEntity = userRepository.findById(user.getId());
            if (userEntity == null)
                LOGGER.info("user "+ user.getId()+" not found");
            else
                userRepository.save(user);

        }

        return new ResponseEntity<DefaultResponse>(new DefaultResponse("Users updated successfully", "S01", null), HttpStatus.OK);
    }

    private boolean validateUserRole(User user) {
        if (user.getRole().getId() != 3) //simple user
            return true;
        else
            return false;

    }
}



