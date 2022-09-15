package com.usermanagement.controller;

import com.usermanagement.entity.User;
import com.usermanagement.response.DefaultResponse;
import com.usermanagement.service.UserService;
import com.usermanagement.utils.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);
    @Autowired
    private UserService userService;


    //post mapping for password encryption
    @PostMapping({"createUser", "signUp"})
    public ResponseEntity create(@RequestBody User user, @RequestParam Integer roleId, @RequestParam Integer createdBy) throws Exception {
        LOGGER.info("Received crate user request");

        ResponseEntity<DefaultResponse> response;
        if (Util.ValidateSigUpRequest(user, roleId, createdBy))
            response = userService.create(user, roleId, createdBy);
        else
            response = new ResponseEntity<DefaultResponse>(new DefaultResponse("Your request data isn't correct","F02",user),HttpStatus.NOT_ACCEPTABLE);
        return response;
    }


    //login form through params
    @PostMapping("/login")
    public ResponseEntity Login(@RequestBody User request) throws Exception {
        LOGGER.info("Received login request");

        User user = userService.Login(request);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List> Login(@RequestParam Integer userId) throws Exception {
        LOGGER.info("Received getAllUsers request");

        List<User> list = userService.getAllUsers(userId);
        return new ResponseEntity<List>(list, HttpStatus.OK);
    }

}
