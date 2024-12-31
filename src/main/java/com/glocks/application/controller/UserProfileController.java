package com.glocks.application.controller;

import com.glocks.application.othermodel.ChangePassword;
import com.glocks.application.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userProfile")
@CrossOrigin
public class UserProfileController {

    @Autowired
    UserService userService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword password) {
        return userService.changePassword(password);
    }
}
