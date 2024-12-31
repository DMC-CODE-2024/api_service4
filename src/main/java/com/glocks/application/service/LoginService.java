package com.glocks.application.service;


import com.glocks.application.model.app.*;
import com.glocks.application.model.constants.UserStatus;
import com.glocks.application.othermodel.LoginResponse;
import com.glocks.application.othermodel.NewPassword;
import com.glocks.application.othermodel.UserLogin;
import com.glocks.application.repo.app.*;
import com.glocks.application.repoService.SystemConfigDbRepoService;
import com.glocks.application.repoService.UserPassHistoryRepoService;
import com.glocks.application.response.tags.ProfileTags;
import com.glocks.application.response.tags.RegistrationTags;
import com.glocks.application.util.HttpResponse;
import com.glocks.application.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Service
public class LoginService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    UserRepo userRepo;
    @Autowired
    LoginTrackingRepo loginTrackingRepo;
    @Autowired
    UserSecurityQuestionRepo userSecurityQuestionRepo;
    @Autowired
    SystemConfigDbRepoService systemConfigurationRepo;
    @Autowired
    Utility utility;

    @Autowired
    UserService userService;

    @Autowired
    UserPassHistoryRepoService userPassHistoryRepoImpl;

    @Autowired
    UserPasswordHistoryRepo userPasswordHistoryRepo;

/*    @Autowired
    OtpService otpService;

    @Autowired
    GenerateRandomDigits randomDigits;*/

    @Autowired
    CurrentLoginRepo currentLoginRepo;


//	@Autowired
//	JwtServiceImpl jwtService;

//	@Autowired
//	AuthenticationManager authenticationManager;


    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<?> userLogin(UserLogin userLogin) {
        try {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("payload " + userLogin);
                User user = new User();
                user.setUsername(userLogin.getUsername());
                user.setPassword(userLogin.getPassword());
                user.setCurrentStatus(UserStatus.APPROVED.getCode());
                Integer status2 = UserStatus.DISABLE.getCode();
                Integer status_delete = UserStatus.DELETE.getCode();
                User UserData = new User();
                UserData = userRepo.findByUsername(user.getUsername());
                log.info("user table response : " + UserData);
                if (UserData != null) {

                    if (authentication.isAuthenticated()) {
                        if (UserData.getCurrentStatus() == 5) {

                            HttpResponse response = new HttpResponse(RegistrationTags.LOGIN_UNAUTHORIZED.getMessage(), 204, RegistrationTags.LOGIN_UNAUTHORIZED.getTag());
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        } else if (UserData.getCurrentStatus() == user.getCurrentStatus() || UserData.getCurrentStatus() == status2) {
                            SystemConfigurationDb systemConfiguration = systemConfigurationRepo.getDataByTag("USER_PASS_EXPIRY_DAYS");
                            log.info("USER_PASS_EXPIRY_DAYS tag value : " + systemConfiguration.getValue());
                            Integer days = 0;
                            if (systemConfiguration != null) {
                                days = Integer.parseInt(systemConfiguration.getValue());
                            }
                            log.info("days going to add" + days);
                            String dateToString = utility.convertlocalTimeToString(UserData.getPasswordDate());
                            Date userPasswordDate = utility.stringToDate(dateToString);
                            Date currentDate = utility.currentOnlyDate();
                            Date passwordExpiryDate = utility.addDaysInDate(days, userPasswordDate);
                            log.info("current date {}  userPasswordDate {}  expiry date {}", currentDate, userPasswordDate, passwordExpiryDate);

                            if (currentDate.after(passwordExpiryDate)) {
                                LoginResponse response = new LoginResponse("Password is expire now", 401, UserData.getId());
                                return new ResponseEntity<>(response, HttpStatus.OK);
                            } else {
                                CurrentLogin currentLoginOutput = currentLoginRepo.findByCurrentUserLogin_Id(UserData.getId());
                                if (Objects.nonNull(currentLoginOutput)) {
                                    currentLoginOutput.setCreatedOn(LocalDateTime.now());
                                    currentLoginOutput.setModifiedOn(LocalDateTime.now());
                                    currentLoginRepo.save(currentLoginOutput);
                                } else {
                                    CurrentLogin currentLogin = new CurrentLogin(1, UserData);
                                    log.info("going to save record in current_login table");
                                    currentLoginRepo.save(currentLogin);
                                }


                             /*   LoginTracking loginTrack = new LoginTracking(1, UserData);
                                loginTrackingRepo.save(loginTrack);
                                CurrentLogin currentLogin = new CurrentLogin(1, UserData);
                                List<CurrentLogin> currentLoginOutput = currentLoginRepo.findByCurrentUserLogin_Id(UserData.getId());
                                //  log.info("currentLoginOutput response : " + currentLoginOutput);
                                if (currentLoginOutput == null || currentLoginOutput.isEmpty()) {
                                    log.info("going to save record in current_login table");
                                    currentLoginRepo.save(currentLogin);
                                } else {
                                    log.info(" updating record in current_login table");
                                    for (CurrentLogin loginUser : currentLoginOutput) {
                                        loginUser.setCreatedOn(LocalDateTime.now());
                                        loginUser.setModifiedOn(LocalDateTime.now());
                                    }
                                    currentLoginRepo.saveAll(currentLoginOutput);
                                }*/

                           /*     SystemConfigurationDb systemConfigData = systemConfigurationDbRepoImpl.getDataByTag("GRACE_PERIOD_END_DATE");
                                String periodInterp = new String();

                                StatesInterpretationDb stateInterup = stateInterupRepoService.getByFeatureIdAndState(8, UserData.getCurrentStatus());
                                String status = new String();
                                if (stateInterup.getInterp() != null) {
                                    status = stateInterup.getInterp();
                                }*/

                                LoginResponse response = new LoginResponse("user credentials are correct", 200
                                        , UserData.getUsername(), UserData.getId(), UserData.getUserProfile().getFirstName(), "default", 0L,
                                        null, UserData.getUserProfile().getOperatorTypeName(),
                                        UserData.getUserProfile().getOperatorTypeId(), UserData.getUserLanguage(),
                                        null, UserData.getCurrentStatus(), 0,
                                        "");
                        /*    String token = getUserAuthentication();
                            if (token == null) {
                                token = "NA";
                            }
                            response.setToken(token);*/
                                log.info("login response:  " + response);
                                return new ResponseEntity<>(response, HttpStatus.OK);
                            }
                        } else if (UserData.getCurrentStatus() == status_delete) {
                            HttpResponse response = new HttpResponse(RegistrationTags.SOFT_DELETE_MESSAGE.getMessage(), 422, RegistrationTags.SOFT_DELETE_MESSAGE.getTag());
                            log.info("login response:  " + response);
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        } else {
                            HttpResponse response = new HttpResponse(RegistrationTags.LOGIN_UNAUTHORIZED.getMessage(), 204, RegistrationTags.LOGIN_UNAUTHORIZED.getTag());
                            log.info("login response:  " + response);
                            return new ResponseEntity<>(response, HttpStatus.OK);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                HttpResponse response = new HttpResponse(RegistrationTags.LOGIN_WRONG_DETAILS.getMessage(), 403, RegistrationTags.LOGIN_WRONG_DETAILS.getTag());
                log.info("login response:  " + response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpResponse response = new HttpResponse(RegistrationTags.COMMAN_FAIL_MSG.getMessage(), 409, RegistrationTags.COMMAN_FAIL_MSG.getTag());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    public ResponseEntity<?> sessionTracking(LoginTracking loginTracking, String publicIP, String browser) {
        try {
            userService.saveUserTrail(loginTracking.getUserTrack(), "User Management", "Logout", 41, publicIP, browser);

            //   LoginTracking loginTrackingOutput = loginTrackingRepo.save(loginTracking);

            long userId = loginTracking.getUserTrack().getId();
            log.info("now going to check data in current_login table by user id: " + userId);
            CurrentLogin loginExist = currentLoginRepo.findByCurrentUserLogin_Id(userId);
            if (Objects.nonNull(loginExist)) {
                log.info("login exist so going to delete data by id : " + loginExist.getId());
                currentLoginRepo.deleteById(loginExist.getId());
                HttpResponse response = new HttpResponse("user session sucessfully added", 200);
                log.info("exist from sessionTracking");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                log.info("login not exitexist  by user id : " + userId);
                HttpResponse response = new HttpResponse("user session failed to add", 204);
                log.info("exist from sessionTracking");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            HttpResponse response = new HttpResponse("Oops something wrong happened", 409);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

/*    public ResponseEntity<?> changeLanguage(ChangeLanguage languageData, String publicIP, String browser) {
        try {
            log.info("lanugage data:  " + languageData);
            User user = new User();
            userService.saveUserTrail(languageData.getUserId(), languageData.getUsername(),
                    languageData.getUserType(), languageData.getUserTypeId(), "User Management", SubFeatures.Change_Language, 41, publicIP, browser);
            user = userRepo.findById(languageData.getUserId());
            if (user != null) {
                user.setUserLanguage(languageData.getLanguage());
                User output = userRepo.save(user);
                if (output != null) {
                    HttpResponse response = new HttpResponse("user language sucessfully update", 200);
                    log.info("response send: " + response);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    HttpResponse response = new HttpResponse("user language fails to update", 204);
                    log.info("response send: " + response);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            } else {
                HttpResponse response = new HttpResponse("user id is incorrect", 204);
                log.info("response send: " + response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpResponse response = new HttpResponse("Oops something wrong happened", 409);
            log.info("response send: " + response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }*/
/*

    public ResponseEntity<?> forgotPassword(ForgotPassword forgotPassword, String publicIp, String browser) {

        log.info("payload {}", forgotPassword);
        User userData = userRepo.findByUsername(forgotPassword.getUsername());
        log.info("userdata id by username= " + userData);
        if (userData != null) {
            log.info("now match user question and answer on UserSecurityquestion");
            userService.saveUserTrail(userData, "User Management", "Reset", 41, publicIp, browser);
            Securityquestion securityData = new Securityquestion();
            securityData.setId(forgotPassword.getQuestionId());
            UserSecurityquestion questionDetails = userSecurityQuestionRepo.findByUser_IdAndSecurityQuestion_IdAndAnswer(userData.getId(), securityData.getId(), forgotPassword.getAnswer());
            log.info("user question mapping data: " + questionDetails);
            if (questionDetails != null) {
                String phoneOtp = otpService.phoneOtp(userData.getUserProfile().getPhoneNo());
                String emailOtpData = randomDigits.getNumericString(6);
                userData.setPreviousStatus(UserStatus.APPROVED.getCode());
                userData.setCurrentStatus(UserStatus.OTP_VERIFICATION_PENDING.getCode());
                UserProfile profile = userData.getUserProfile();
                profile.setUser(userData);
                profile.setEmailOtp(emailOtpData);
                profile.setPhoneOtp(phoneOtp);
                userData.setUserProfile(profile);
                userRepo.save(userData);
                log.info("user data changed");
                UpdateProfileResponse response = new UpdateProfileResponse(ProfileTags.PRO_SUCESS_OTPMSG.getMessage(), 200, UserStatus.getUserStatusByCode(userData.getCurrentStatus()).getDescription(),
                        userData.getId(), ProfileTags.PRO_SUCESS_OTPMSG.getTag());
                log.info("response send to user:  " + response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                HttpResponse response = new HttpResponse(ProfileTags.SECU_QUEST_NOT_MATCH.getMessage(), 409, ProfileTags.SECU_QUEST_NOT_MATCH.getTag());
                log.info("response send to user:  " + response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else {
            HttpResponse response = new HttpResponse(ProfileTags.WRONG_USERID.getMessage(), 401, ProfileTags.WRONG_USERID.getTag());
            log.info("response send to user:  " + response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

*/
    public ResponseEntity<?> updateNewUserPassword(NewPassword password, String publicIp, String browser) {
        log.info("inside set update new password controller publicIp=" + publicIp + " browser==" + browser);
        log.info(" password data:  " + password);
        log.info("get user  data by username below");
        User user = userRepo.findByUsername(password.getUsername());
        if (user != null) {
            userService.saveUserTrail(user, "Forgot Password", "Reset", 0, publicIp, browser);
            boolean passwordExist = userPassHistoryRepoImpl.passwordExist(password.getPassword(), user.getId());
            if (passwordExist == true) {
                log.info("if this password exist");
                HttpResponse response = new HttpResponse(ProfileTags.PRO_CPASS_LAST_3PASS_ERROR.getMessage(), 204,
                        ProfileTags.PRO_CPASS_LAST_3PASS_ERROR.getTag());
                log.info("exit from change password");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                log.info("if this password does not exist");
                long count = userPassHistoryRepoImpl.countByUserId(user.getId());
                log.info("password count: " + count);
                if (count != 0) {
                    if (count >= 3) {
                        log.info("going to delete password history greater than 3");
                        UserPasswordHistory passHistory = userPassHistoryRepoImpl.getPasswordHistory(user.getId());
                        userPasswordHistoryRepo.deleteById(passHistory.getId());
                        user.setPassword(password.getPassword());
                        return userService.setNewPassword(user);
                    } else {
                        log.info("if password history less than 3");
                        user.setPassword(password.getPassword());
                        return userService.setNewPassword(user);
                    }
                } else {
                    user.setPassword(password.getPassword());
                    return userService.setNewPassword(user);
                }

            }
        } else {
            HttpResponse response = new HttpResponse(ProfileTags.NEW_PASS_FAIL.getMessage(), 409, ProfileTags.NEW_PASS_FAIL.getTag());
            log.info("response send to user:  " + response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    /*public String getUserAuthentication() {
        String token = "";
        SystemConfigurationDb systemConfigData = systemConfigurationDbRepoImpl.getDataByTag("EIRS-SUPPORT-AUTH-SIGNIN");
        if (systemConfigData != null) {
            String date[] = systemConfigData.getValue().split("#");

            //log.info("getUserAuthentication() API Result: URL : " + date[0]+ "User Name ["+date[1]+"] and Pass ["+date[2]+"]");
            String url = date[0];
            String jsonInputString = "{\"userName\":\"" + date[1] + "\", \"password\":\"" + date[2] + "\"}";
//    			String url = "https://lab1.goldilocks-tech.com/eirsbackend/api/auth/signin";
//    			String jsonInputString = "{\"userName\":\"dina.yoeurn\", \"password\":\"Admin@1234\"}";

            try {
                // Create URL object
                URL obj = new URL(url);
                // Open connection
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                // Set request method
                con.setRequestMethod("POST");
                // Set request headers
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                // Send request
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                // Get the response code
                int responseCode = con.getResponseCode();
                log.info("Response Code: " + responseCode);

                // Read the response
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                }

                // Print the response
                log.info("Response Body: " + response.toString());

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                String apiResult = jsonResponse.getString("apiResult");
                String message = jsonResponse.getString("message");
                token = jsonResponse.getString("token");
                String type = jsonResponse.getString("type");
                int id = jsonResponse.getInt("id");
                String userName = jsonResponse.getString("userName");
                String email = jsonResponse.getString("email");

                // Output the extracted values
                log.info("API Result: " + apiResult);
                log.info("Message: " + message);
                log.info("Token: " + token);
                log.info("Type: " + type);
                log.info("ID: " + id);
                log.info("User Name: " + userName);
                log.info("Email: " + email);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return token;
        }
        return token;
    }*/
}
