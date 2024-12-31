package com.glocks.application.controller;

import com.glocks.application.model.app.LoginTracking;
import com.glocks.application.model.app.User;
import com.glocks.application.othermodel.NewPassword;
import com.glocks.application.othermodel.UserLogin;
import com.glocks.application.repoService.UserRepoService;
import com.glocks.application.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/Login")
@CrossOrigin
public class LoginController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    LoginService loginService;

    @Autowired
    UserRepoService userRepoService;

    @PostMapping("/checkUser")
    public ResponseEntity<?> userLogin(@RequestBody UserLogin user) {
        return loginService.userLogin(user);
    }

    @PostMapping("/sessionTracking/{userid}")
    public ResponseEntity<?> sessionTracking(@PathVariable("userid") long userid, @RequestParam(name = "publicIP") String publicIP, @RequestParam(name = "browser") String browser) {
        log.info("inside sessionTracking controller and userId is: " + userid);
        User output = userRepoService.findByUSerId(userid);
        // 0 -for logout 1-for login
        log.info("publicIP is: " + publicIP + "   browser      ====" + browser);
        LoginTracking loginTracking = new LoginTracking(0, output);
        return loginService.sessionTracking(loginTracking, publicIP, browser);
    }

 /*   @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPassword forgotPassword, @RequestParam(name = "publicIP") String publicIP, @RequestParam(name = "browser") String browser) {
        return loginService.forgotPassword(forgotPassword, publicIP, browser);
    }*/

    @PostMapping("/updateNewPassword")
    public ResponseEntity<?> updateNewPassword(@RequestBody NewPassword password, @RequestParam(name = "publicIP") String publicIP, @RequestParam(name = "browser") String browser) {
        return loginService.updateNewUserPassword(password, publicIP, browser);
    }
}
