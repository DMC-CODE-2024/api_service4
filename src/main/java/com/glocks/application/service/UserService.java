package com.glocks.application.service;

import com.glocks.application.configuration.PropertiesReaders;
import com.glocks.application.model.app.User;
import com.glocks.application.model.app.UserPasswordHistory;
import com.glocks.application.model.app.UserProfile;
import com.glocks.application.model.audit.AuditDB;
import com.glocks.application.model.audit.AuditTrail;
import com.glocks.application.othermodel.ChangePassword;
import com.glocks.application.repo.app.UserPasswordHistoryRepo;
import com.glocks.application.repo.app.UserRepo;
import com.glocks.application.repo.app.UserSecurityQuestionRepo;
import com.glocks.application.repo.audit.AuditDBRepo;
import com.glocks.application.repoService.AudiTrailRepoService;
import com.glocks.application.repoService.UserPassHistoryRepoService;
import com.glocks.application.response.tags.ProfileTags;
import com.glocks.application.response.tags.RegistrationTags;
import com.glocks.application.util.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    UserSecurityQuestionRepo userSecurityQuestionRepo;
    @Autowired
    UserRepo userRepo;


    @Autowired
    UserPassHistoryRepoService userPassHistoryRepoImpl;

    @Autowired
    UserPasswordHistoryRepo userPasswordHistoryRepo;

    @Autowired
    AudiTrailRepoService audiTrailRepoService;

    @Autowired
    PropertiesReaders propertiesReader;


    @Autowired
    AuditDBRepo auditDb;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ResponseEntity<?> changePassword(ChangePassword password) {
        try {
            log.info("inside change password controller");
            log.info("ChangePassword data from form: " + password);
            User user = userRepo.findById(password.getUserid());
            saveUserTrail(user, "User Management", "Change Password", 41, password.getPublicIp(), password.getBrowser());
            return changePasswordMethod(user, password);
        } catch (Exception e) {
            log.info("exception occur");
            e.printStackTrace();
            HttpResponse response = new HttpResponse(RegistrationTags.COMMAN_FAIL_MSG.getMessage(), 409, RegistrationTags.COMMAN_FAIL_MSG.getTag());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

/*
    public int saveRoles(long usertypes[], long userid) {
        try {
            for (long usertypeId : usertypes) {
                Userrole role = new Userrole();
                User user = new User();
                user.setId(userid);
                role.setUserData(user);

            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
*/


    public long roleCheck(long usertypes[]) {
        try {
            log.info("inside add roles");
            log.info("usertypes= " + Arrays.toString(usertypes));
            log.info("usertypes length is" + usertypes.length);
            if (usertypes.length == 1) {
                long usertypeId = 0;
                for (long id : usertypes) {
                    usertypeId = id;
                }
                return usertypeId;
            } else if (usertypes.length > 1) {
                Arrays.sort(usertypes);
                long usertypeArray1[] = {4, 5, 6};
                long usertypeArray2[] = {4, 5};
                long usertypeArray3[] = {4, 6};
                long usertypeArray4[] = {5, 6};
                Arrays.sort(usertypeArray3);
                log.info("usertype array1= " + Arrays.toString(usertypeArray1));
                log.info("usertype array2= " + Arrays.toString(usertypeArray2));
                log.info("usertype array3= " + Arrays.toString(usertypeArray3));
                log.info("usertype array4= " + Arrays.toString(usertypeArray4));
                boolean b = Arrays.equals(usertypes, usertypeArray4);
                log.info("if roles are DR:  " + b);
                if (Arrays.equals(usertypes, usertypeArray1)) {
                    log.info("if roles are IDR");
                    return 4;
                } else if (Arrays.equals(usertypes, usertypeArray2)) {
                    log.info("if roles are ID");
                    return 4;
                } else if (Arrays.equals(usertypes, usertypeArray3)) {
                    log.info("if roles are IR");
                    return 4;
                } else if (Arrays.equals(usertypes, usertypeArray4)) {
                    log.info("if roles are DR");
                    return 5;
                } else {
                    log.info("if role set not found");
                    return 0;

                }

            }
            return 0;
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }

    public long roleCheck2(long usertypes[], long mainRole) {
        try {
            log.info("inside add roles");
            log.info("usertypes= " + Arrays.toString(usertypes));
            log.info("usertypes length is" + usertypes.length);
            List<Long> roles = new ArrayList<Long>();
            roles.add(4l);
            roles.add(5l);
            roles.add(6l);
            if (roles.contains(mainRole)) {
                if (usertypes.length == 1) {
                    long usertypeId = 0;
                    for (long id : usertypes) {
                        usertypeId = id;
                    }
                    return usertypeId;
                } else if (usertypes.length > 1) {
                    Arrays.sort(usertypes);
                    long usertypeArray1[] = {4, 5, 6};
                    long usertypeArray2[] = {4, 5};
                    long usertypeArray3[] = {4, 6};
                    long usertypeArray4[] = {5, 6};
                    Arrays.sort(usertypeArray3);
                    log.info("usertype array1= " + Arrays.toString(usertypeArray1));
                    log.info("usertype array2= " + Arrays.toString(usertypeArray2));
                    log.info("usertype array3= " + Arrays.toString(usertypeArray3));
                    log.info("usertype array4= " + Arrays.toString(usertypeArray4));
                    boolean b = Arrays.equals(usertypes, usertypeArray4);
                    log.info("if roles are DR:  " + b);
                    if (Arrays.equals(usertypes, usertypeArray1)) {
                        log.info("if roles are IDR");
                        return 4;
                    } else if (Arrays.equals(usertypes, usertypeArray2)) {
                        log.info("if roles are ID");
                        return 4;
                    } else if (Arrays.equals(usertypes, usertypeArray3)) {
                        log.info("if roles are IR");
                        return 4;
                    } else if (Arrays.equals(usertypes, usertypeArray4)) {
                        log.info("if roles are DR");
                        return 5;
                    } else {
                        log.info("if role set not found");
                        return 0;

                    }

                }
                return 0;
            } else {

                return mainRole;
            }
        } catch (Exception e) {

            e.printStackTrace();
            return 0;
        }
    }


/*

    public ResponseEntity<?> updateExpirePassword(ChangePassword password) {
        try {
            log.info("inside change password controller");
            log.info("ChangePassword data from form: " + password);
            User user = userRepo.findById(password.getUserid());
            // user.setPasswordDate(LocalDateTime.now());
            saveUserTrail(user, "User Management", "Reset", 41, password.getPublicIp(), password.getBrowser());
            return changeExpiryPasswordMethod(user, password);
        } catch (Exception e) {
            log.info("exception occur");
            e.printStackTrace();
            HttpResponse response = new HttpResponse();
            response.setStatusCode(409);
            response.setResponse("Oops something wrong happened");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
*/

    public ResponseEntity<?> changePasswordMethod(User user, ChangePassword password) {
        log.info("password details==" + password);

        // if (   password.getOldPassword().equals(user.getPassword())) {
        if (passwordEncoder.matches(password.getOldPassword(), user.getPassword())) {
            log.info("inside password match block");
            boolean passwordExist = userPassHistoryRepoImpl.passwordExist(password.getPassword(), password.getUserid());
            if (passwordExist == true) {
                log.info("if this password exist");
                HttpResponse response = new HttpResponse(ProfileTags.PRO_CPASS_LAST_3PASS_ERROR.getMessage(), 204, ProfileTags.PRO_CPASS_LAST_3PASS_ERROR.getTag());
                log.info("exit from change password");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                log.info("if this password does not exist");
                long count = userPassHistoryRepoImpl.countByUserId(password.getUserid());
                log.info("password count: " + count);
                if (count != 0) {
                    if (count >= 3) {
                        log.info("going to delete password history greater than 3 " + password.getUserid());
                        UserPasswordHistory passHistory = userPassHistoryRepoImpl.getPasswordHistory(password.getUserid());
                        userPasswordHistoryRepo.deleteById(passHistory.getId());
                        // userPasswordHistoryRepo.deleteId(passHistory.getId());
                        user.setPassword(passwordEncoder.encode(password.getPassword()));
                        return setPassword(user);
                    } else {
                        log.info("if password history less than 3");
                        user.setPassword(passwordEncoder.encode(password.getPassword()));
                        return setPassword(user);
                    }
                } else {
                    user.setPassword(passwordEncoder.encode(password.getPassword()));
                    return setPassword(user);
                }

            }
        } else {
            HttpResponse response = new HttpResponse(ProfileTags.PRO_OldPass_Error.getMessage(), 204, ProfileTags.PRO_OldPass_Error.getTag());
            log.info("exit from change password");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

/*    public ResponseEntity<?> changeExpiryPasswordMethod(User user, ChangePassword password) {
        if (password.getOldPassword().equals(user.getPassword())) {
            boolean passwordExist = userPassHistoryRepoImpl.passwordExist(password.getPassword(), password.getUserid());
            if (passwordExist == true) {
                log.info("if this password exist");
                HttpResponse response = new HttpResponse(ProfileTags.PRO_CPASS_LAST_3PASS_ERROR.getMessage(), 204, ProfileTags.PRO_CPASS_LAST_3PASS_ERROR.getTag());
                log.info("exit from change password");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                log.info("if this password does not exist");
                long count = userPassHistoryRepoImpl.countByUserId(password.getUserid());
                log.info("password count: " + count);
                if (count != 0) {
                    if (count >= 3) {
                        log.info("going to delete password history greater than 3");
                        UserPasswordHistory passHistory = userPassHistoryRepoImpl.getPasswordHistory(password.getUserid());
                        userPasswordHistoryRepo.deleteById(passHistory.getId());
                        user.setPassword(password.getPassword());
                        return setPassword2(user);
                    } else {
                        log.info("if password history less than 3");
                        user.setPassword(password.getPassword());
                        return setPassword2(user);
                    }
                } else {
                    user.setPassword(password.getPassword());
                    return setPassword2(user);
                }

            }

        } else {
            HttpResponse response = new HttpResponse(ProfileTags.PRO_OldPass_Error.getMessage(), 204, ProfileTags.PRO_OldPass_Error.getTag());
            log.info("exit from change password");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }*/

    public ResponseEntity<?> setPassword(User user) {
        User output = userRepo.save(user);
        if (output != null) {
            UserPasswordHistory userPassHistory = new UserPasswordHistory();
            userPassHistory.setPassword(user.getPassword());
            userPassHistory.setUserPassword(output);
            userPassHistory.setCreatedOn(LocalDateTime.now());
            userPassHistory.setModifiedOn(LocalDateTime.now());
            log.info("going to save user passowrd in user_password_history table");
            UserPasswordHistory userPasswordOtput = userPassHistoryRepoImpl.saveUserPassword(userPassHistory);
            if (userPasswordOtput != null) {
                log.info("user passowrd sucessfully save");
            }

          /*  boolean notificationStatus = emailUtils.saveNotification("PRO_CHANGE_PASSWORD_BY_USER", output.getUserProfile(), 41, "User Management", "Change Password", output.getUsername(), "", ChannelType.EMAIL, "users", 0);
            log.info("notification save: " + notificationStatus);*/
            HttpResponse response = new HttpResponse(ProfileTags.PRO_CPASS_SUCESS.getMessage(), 200, ProfileTags.PRO_CPASS_SUCESS.getTag());
            log.info("exit from change password");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            HttpResponse response = new HttpResponse(ProfileTags.PRO_CPASS_FAIL.getMessage(), 500, ProfileTags.PRO_CPASS_FAIL.getTag());
            log.info("exit from change password");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
/*
    public ResponseEntity<?> setPassword2(User user) {
        SystemConfigurationDb systemConfiguration = systemConfigurationRepo.getDataByTag("USER_PASS_EXPIRY_DAYS");
        log.info("system config data by tag: USER_PASS_EXPIRY_DAYS" + systemConfiguration.getValue());
        Integer days = 0;
        if (systemConfiguration != null) {
            days = Integer.parseInt(systemConfiguration.getValue());
        }
        log.info("days going to add" + days);
        LocalDateTime date = user.getPasswordDate();
        date = date.plusDays(days);
        log.info("now password expiry date: " + date);
        user.setPasswordDate(date);
        User output = userRepo.save(user);
        if (output != null) {
            UserPasswordHistory userPassHistory = new UserPasswordHistory();
            userPassHistory.setPassword(user.getPassword());
            userPassHistory.setUserPassword(output);
            userPassHistory.setCreatedOn(LocalDateTime.now());
            userPassHistory.setModifiedOn(LocalDateTime.now());

            log.info("going to save user passowrd in user_password_history table");
            UserPasswordHistory userPasswordOtput = userPassHistoryRepoImpl.saveUserPassword(userPassHistory);
            if (userPasswordOtput != null) {
                log.info("user passowrd sucessfully save");
            }
            List<Long> usertypes = new ArrayList<Long>();
            usertypes.add(4l);
            usertypes.add(5l);
            usertypes.add(6l);
            int authorityStatus = 0;

          *//*  boolean notificationStatus = emailUtils.saveNotification("PRO_CHANGE_PASSWORD_BY_USER", output.getUserProfile(), 41, "User Management", "Change Password", output.getUsername(), "", ChannelType.EMAIL, "users", authorityStatus);
            log.info("notification save: " + notificationStatus);*//*
            HttpResponse response = new HttpResponse(ProfileTags.PRO_CPASS_SUCESS.getMessage(), 200, ProfileTags.PRO_CPASS_SUCESS.getTag());
            log.info("exit from change password");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            HttpResponse response = new HttpResponse(ProfileTags.PRO_CPASS_FAIL.getMessage(), 500, ProfileTags.PRO_CPASS_FAIL.getTag());
            log.info("exit from change password");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }*/

    public ResponseEntity<?> setNewPassword(User user) {
        User output = userRepo.save(user);
        if (output != null) {
            UserPasswordHistory userPassHistory = new UserPasswordHistory();
            userPassHistory.setPassword(user.getPassword());
            userPassHistory.setUserPassword(output);
            userPassHistory.setCreatedOn(LocalDateTime.now());
            userPassHistory.setModifiedOn(LocalDateTime.now());
            log.info("going to save user passowrd in user_password_history table");
            UserPasswordHistory userPasswordOtput = userPassHistoryRepoImpl.saveUserPassword(userPassHistory);
            if (userPasswordOtput != null) {
                log.info("user passowrd sucessfully save");
            }
            List<Long> usertypes = new ArrayList<Long>();
            usertypes.add(4l);
            usertypes.add(5l);
            usertypes.add(6l);
            int authorityStatus = 0;
        /*    boolean notificationStatus = emailUtils.saveNotification("FORGOT_PASSWORD_EMAIL", output.getUserProfile(), 41, "User Management", "Forgot Password", output.getUsername(), "", ChannelType.EMAIL, "users", authorityStatus);
            log.info("notification save: " + notificationStatus);*/
            HttpResponse response = new HttpResponse(ProfileTags.NEW_PASS_SUC.getMessage(), 200, ProfileTags.NEW_PASS_SUC.getTag());
            log.info("exit from update new password");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            HttpResponse response = new HttpResponse(ProfileTags.NEW_PASS_FAIL.getMessage(), 500, ProfileTags.NEW_PASS_FAIL.getTag());
            log.info("exit from  update New password");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

/*    public ResponseEntity<?> updateUserStatus(UserStatusRequest userStatus) {
        log.info("inside  update User status  controller");
        log.info(" userStatus data:  " + userStatus);
        log.info("get user  data by userid below");
        User user = userRepo.findById(userStatus.getUserId());
        if (user != null) {
            if (userStatus.getPassword().trim().equals(user.getPassword())) {
                Integer userStatus2 = UserStatus.getUserStatusByDesc(userStatus.getStatus()).getCode();
                user.setPreviousStatus(user.getCurrentStatus());
                user.setCurrentStatus(userStatus2);
                user.setModifiedBy(userStatus.getUsername());
                User output = userRepo.save(user);
                log.info("user data after update the status: " + output);
                if (output != null) {
                    String tag = "";
                    String subFeature = new String();
                    String subject = "";
                    if (output.getCurrentStatus() == UserStatus.APPROVED.getCode()) {
                        tag = "PRO_ENABLE_ACC_BY_USER";
                        log.info("if user status is approved");
                        subFeature = "Enabled";
                        subject = "Account Enabled Notification for " + output.getUsername();

                    } else if (output.getCurrentStatus() == UserStatus.DISABLE.getCode()) {
                        tag = "PRO_DISABLE_ACC_BY_USER";
                        log.info("if user status is disable");
                        subFeature = "Disabled";
                        subject = "Account Disabled Notification for " + output.getUsername();
                    } else if (output.getCurrentStatus() == UserStatus.DEACTIVATE.getCode()) {
                        tag = "PRO_DEACTIVATE_ACC_BY_USER";
                        log.info("if user status is deactivate");
                        subFeature = "Deactivated";
                        subject = "Deactivation Notification for " + output.getUsername();
                    } else {
                        tag = "";
                        log.info("current user status id " + output.getCurrentStatus());
                        subFeature = "";
                    }
                    saveUserTrail(user, "User Management", subFeature, 41, userStatus.getPublicIp(), userStatus.getBrowser());
                    List<Long> usertypes = new ArrayList<Long>();
                    usertypes.add(4l);
                    usertypes.add(5l);
                    usertypes.add(6l);
                    int authorityStatus = 0;

          *//*          boolean notificationStatus = emailUtils.saveNotification(tag, output.getUserProfile(), 41, "User Management", subFeature, output.getUsername(), "", ChannelType.EMAIL, "users", authorityStatus);
                    log.info("notification save: " + notificationStatus);*//*
                    HttpResponse response = new HttpResponse(UpdateUserStatusTags.USER_STATUS_CHANGED.getMessage(), 200, UpdateUserStatusTags.USER_STATUS_CHANGED.getTag());
                    log.info("response send to user:  " + response);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    HttpResponse response = new HttpResponse(UpdateUserStatusTags.USER_STATUS_CHANGE_FAIL.getMessage(), 500, UpdateUserStatusTags.USER_STATUS_CHANGE_FAIL.getTag());
                    log.info("response send to user:  " + response);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            } else {
                log.info("wrong password");
                HttpResponse response = new HttpResponse(ProfileTags.PRO_CORRECT_PASS.getMessage(), 401, ProfileTags.PRO_CORRECT_PASS.getTag());
                log.info("response send to user:  " + response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } else {
            HttpResponse response = new HttpResponse(UpdateUserStatusTags.USER_STATUS_CHANGE_FAIL.getMessage(), 409, UpdateUserStatusTags.USER_STATUS_CHANGE_FAIL.getTag());
            log.info("response send to user:  " + response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }*/

/*
    public ResponseEntity<?> editProfile(long id, UserHeader header) {
        try {
            log.info("inside into edit profile");
            log.info("user id:  " + id);
            log.info("get user  data by userid below");
            UserProfile user = userProfileRepo.findByUser_Id(id);
            if (Objects.nonNull(user)) {
                saveUserTrail(user.getUser(), "User Management", "View Profile", 41, header.getPublicIp(), header.getBrowser());
                log.info("user profile data: " + user);
                List<QuestionPair> questionList = new ArrayList<QuestionPair>();
                log.info("now going to fetch security question data of user");
                for (UserSecurityquestion question : user.getUser().getUserSecurityquestion()) {
                    QuestionPair questionPair = new QuestionPair();
                    questionPair.setId(question.getId());
                    questionPair.setQuestion(question.getSecurityQuestion().getQuestion());
                    questionPair.setQuestionId(question.getSecurityQuestion().getId());
                    questionPair.setAnswer(question.getAnswer());
                    questionList.add(questionPair);
                }
                log.info("questionList:  " + questionList);
                user.setQuestionList(questionList);

                List<Long> rolesId = new ArrayList<Long>();
                log.info("roles Ids :  " + rolesId);
                long[] arr = new long[rolesId.size()];
                arr = Longs.toArray(rolesId);
                user.setRoles(arr);
                List<SystemConfigListDb> asTypeList = systemConfigRepo.getByTag("AS_TYPE");
                for (SystemConfigListDb asType : asTypeList) {
                    Integer value = asType.getValue();
                    if (user.getType() == value) {
                        user.setAsTypeName(asType.getInterp());
                    }
                }
                List<SystemConfigListDb> arrivaPortList = systemConfigRepo.getByTag("CUSTOMS_PORT");
                for (SystemConfigListDb port : arrivaPortList) {
                    Integer value = port.getValue();
                    if (user.getArrivalPort() == value) {
                        user.setArrivalPortName(port.getInterp());
                    }
                }

                List<SystemConfigListDb> nature_Of_Employment = systemConfigRepo.getByTag("Nature_Of_Employment");
                for (SystemConfigListDb emplomentType : nature_Of_Employment) {
                    Integer value = emplomentType.getValue();
                    if (user.getNatureOfEmployment() == value) {
                        user.setNatureOfEmploymentInterp(emplomentType.getInterp());
                    }
                }

        *//*        if (user.getPortAddress() != null) {
                    PortAddress portAddress = portAddressRepoService.getById(user.getPortAddress());
                    user.setPortAddressName(portAddress.getAddress());
                }*//*

                SystemConfigurationDb filePath = systemConfigurationRepo.getDataByTag("upload_file_link");
                if (filePath != null) {
                    if (user.getNidFilename() != null || !"null".equalsIgnoreCase(user.getNidFilename())) {
                        user.setNidFilePath(filePath.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + "/" + user.getUser().getUsername() + "/NID/");
                    }

                    if (user.getPhotoFilename() != null || !"null".equalsIgnoreCase(user.getPhotoFilename())) {
                        user.setPhotoFilePath(filePath.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + "/" + user.getUser().getUsername() + "/photo/");
                    }
                    if (user.getIdCardFilename() != null || !"null".equalsIgnoreCase(user.getIdCardFilename())) {
                        user.setIdCardFilePath(filePath.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + "/" + user.getUser().getUsername() + "/IDCard/");
                    }
                    if (user.getVatFilename() != null || !"null".equalsIgnoreCase(user.getVatFilename())) {
                        user.setVatFilePath(filePath.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + "/" + user.getUser().getUsername() + "/Vat/");
                    }
                }
                log.info("All data now fetched");
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                HttpResponse response = new HttpResponse();
                response.setStatusCode(204);
                response.setResponse("User profile data fail to find by userid");
                log.info("response send to user:  " + response);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.info("exception occur");
            e.printStackTrace();
            HttpResponse response = new HttpResponse();
            response.setStatusCode(409);
            response.setResponse("Oops something wrong happened");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }*/

    /*
     * public void setEmailAndPhoneDetails(UserProfile userProfile, User userData) {
     * String phoneOtp = otpService.phoneOtp(userProfile.getPhoneNo()); String
     * emailOtpData = randomDigits.getNumericString(6);
     * userProfile.setUser(userData); boolean notificationStatus =
     * emailUtils.saveNotification("PRO_VERIFY_OTP_EMAIL_MSG", userProfile, 41,
     * "User Management", "Update Profile Email OTP", userData.getUsername(),
     * emailOtpData, ChannelType.EMAIL, "user_temp", 0);
     * log.info("notification save:  " + notificationStatus); boolean
     * notificationStatusForSms = emailUtils.saveNotification("PRO_VERIFY_OTP__MSG",
     * userProfile, 41, "User Management", "Update Profile SMS OTP",
     * userData.getUsername(), phoneOtp, ChannelType.SMS, "user_temp", 0);
     * log.info("notificationStatusForSms save:  " + notificationStatusForSms);
     * UserTemporarydetails details = new UserTemporarydetails(userData,
     * userProfile.getEmail(), userProfile.getPhoneNo(), emailOtpData, phoneOtp);
     * UserTemporarydetails dataByProfileId =
     * userTemporarydetailsRepo.findByUserDetails_id(userData.getId()); log.
     * info("temporary email and phone no profile details by user profile table id "
     * + userProfile.getId()); if (dataByProfileId != null) {
     * details.setId(dataByProfileId.getId());
     * details.setCreatedOn(dataByProfileId.getCreatedOn());
     * details.setModifiedOn(dataByProfileId.getModifiedOn()); }
     * log.info("going to add data to user profile temporary data table");
     * userTemporarydetailsRepo.save(details);
     * log.info("after adding data to temporaray table");
     * userData.setPreviousStatus(UserStatus.APPROVED.getCode());
     * userData.setCurrentStatus(UserStatus.OTP_VERIFICATION_PENDING.getCode());
     * userRepo.save(userData); log.info("user data changed"); }
     */

    /*
     * public ResponseEntity<?> updateProfile(UserProfile userProfile) { try {
     *
     * log.info("inside into update user profile");
     * log.info("user profile data:  :  " + userProfile); UserProfile
     * userProfileData = userProfileRepo.findById(userProfile.getId());
     * log.info("user  "); if
     * (userProfile.getPassword().equals(userProfileData.getUser().getPassword())) {
     * log.info("user profile data by id:  " + userProfileData);
     *
     * long mainRole=roleCheck(userProfile.getRoles()); if(mainRole>0) {
     *
     * User userData = userRepo.findByUserProfile_Id(userProfile.getId());
     * log.info("user as userData:  " + userData);
     *
     * saveUserTrail(userData, "User Management", "Updated", 41,
     * userProfile.getPublicIp(), userProfile.getBrowser()); boolean emailExist =
     * userProfileRepo.existsByEmailAndUser_CurrentStatusNot(userProfile.getEmail(),
     * 21); boolean phoneExist =
     * userProfileRepo.existsByPhoneNoAndUser_CurrentStatusNot(userProfile.
     * getPhoneNo(), 21); if
     * (!userProfile.getPhoneNo().equals(userProfileData.getPhoneNo()) &&
     * !userProfile.getEmail().equals(userProfileData.getEmail())) {
     *
     * if (emailExist) {
     *
     * HttpResponse response = new
     * HttpResponse(RegistrationTags.Email_Exist.getMessage(), 409,
     * RegistrationTags.Email_Exist.getTag()); return new ResponseEntity<>(response,
     * HttpStatus.OK); }
     *
     * if (phoneExist) {
     *
     * HttpResponse response = new
     * HttpResponse(RegistrationTags.Phone_Exist.getMessage(), 409,
     * RegistrationTags.Phone_Exist.getTag()); return new ResponseEntity<>(response,
     * HttpStatus.OK); } setEmailAndPhoneDetails(userProfile, userData); } else if
     * (!userProfile.getPhoneNo().equals(userProfileData.getPhoneNo()) &&
     * userProfile.getEmail().equals(userProfileData.getEmail())) { if (phoneExist)
     * {
     *
     * HttpResponse response = new
     * HttpResponse(RegistrationTags.Phone_Exist.getMessage(), 409,
     * RegistrationTags.Phone_Exist.getTag()); return new ResponseEntity<>(response,
     * HttpStatus.OK); } setEmailAndPhoneDetails(userProfile, userData);
     *
     * } else if (userProfile.getPhoneNo().equals(userProfileData.getPhoneNo()) &&
     * !userProfile.getEmail().equals(userProfileData.getEmail())) { if (emailExist)
     * {
     *
     * HttpResponse response = new
     * HttpResponse(RegistrationTags.Email_Exist.getMessage(), 409,
     * RegistrationTags.Email_Exist.getTag()); return new ResponseEntity<>(response,
     * HttpStatus.OK); } setEmailAndPhoneDetails(userProfile, userData);
     *
     * }
     *
     * updateUserFields(userProfile, userProfileData);
     * userProfileData.setUser(userData);
     * log.info("userProfile data going to update: " + userProfileData);
     * log.info("now going to save user profile data"); UserProfile output =
     * userProfileRepo.save(userProfileData); log.info("user profile data is save");
     * if (output != null) { // if(userProfile.getRoles()!=null) { //
     * if(userProfile.getRoles().length!=0) { // int
     * rolesOutput=updateRoles(userProfile.getRoles(), output.getUser().getId()); //
     * log.info("user role update output:  "+rolesOutput); // } // }
     *
     * log.info("if user profile table is update ");
     * log.info("going to update user  questions and answer"); int i =
     * updateUserSecurityquestion(userProfileData); log.info("question output:  " +
     * i); String tag = ""; String msg = ""; if (userData.getCurrentStatus() ==
     * UserStatus.APPROVED.getCode()) { tag = ProfileTags.PRO_SUCESS_MSG.getTag();
     * msg = ProfileTags.PRO_SUCESS_MSG.getMessage(); } else { tag =
     * ProfileTags.PRO_SUCESS_OTPMSG.getTag(); msg =
     * ProfileTags.PRO_SUCESS_OTPMSG.getMessage();
     *
     * } UpdateProfileResponse response = new UpdateProfileResponse(msg, 200,
     * UserStatus.getUserStatusByCode(userData.getCurrentStatus()).getDescription(),
     * output.getUser().getId(), tag); log.info("response send to user:  " +
     * response); return new ResponseEntity<>(response, HttpStatus.OK); } else {
     * log.info("user profile to update"); HttpResponse response = new
     * HttpResponse(ProfileTags.PRO_FAIL_MSG.getMessage(), 204,
     * ProfileTags.PRO_FAIL_MSG.getTag()); log.info("response send to user:  " +
     * response); return new ResponseEntity<>(response, HttpStatus.OK); } } else {
     * log.info("user profile to update"); HttpResponse response=new
     * HttpResponse(RegistrationTags.REG_FAIL_ROLES_RESP.getMessage(),204,
     * RegistrationTags.REG_FAIL_ROLES_RESP.getTag());
     * log.info("response send to user:  "+response); return new
     * ResponseEntity<>(response,HttpStatus.OK); } } else {
     * log.info("user profile failed to update"); HttpResponse response = new
     * HttpResponse(ProfileTags.PRO_CORRECT_PASS.getMessage(), 401,
     * ProfileTags.PRO_CORRECT_PASS.getTag()); log.info("response send to user:  " +
     * response); return new ResponseEntity<>(response, HttpStatus.OK); } } catch
     * (Exception e) { log.info("if exception occur"); log.info(e.toString());
     * HttpResponse response = new
     * HttpResponse(RegistrationTags.COMMAN_FAIL_MSG.getMessage(), 409,
     * RegistrationTags.COMMAN_FAIL_MSG.getTag()); return new
     * ResponseEntity<>(response, HttpStatus.OK); }
     *
     * }
     */

    public void updateUserFields(UserProfile userProfile, UserProfile userProfileData) {
        userProfileData.setFirstName(userProfile.getFirstName());
        userProfileData.setMiddleName(userProfile.getMiddleName());
        userProfileData.setLastName(userProfile.getLastName());
        userProfileData.setPropertyLocation(userProfile.getPropertyLocation());
        userProfileData.setStreet(userProfile.getStreet());
        userProfileData.setVillage(userProfile.getVillage());
        userProfileData.setLocality(userProfile.getLocality());
        userProfileData.setDistrict(userProfile.getDistrict());
        userProfileData.setCommune(userProfile.getCommune());
        userProfileData.setPostalCode(userProfile.getPostalCode());
        userProfileData.setCountry(userProfile.getCountry());
        userProfileData.setProvince(userProfile.getProvince());
        userProfileData.setCountry(userProfile.getCountry());
        userProfileData.setQuestionList(userProfile.getQuestionList());
        userProfileData.setAuthorityName(userProfile.getAuthorityName());
        userProfileData.setAuthorityEmail(userProfile.getAuthorityEmail());
        userProfileData.setDesignation(userProfile.getDesignation());
        userProfileData.setAuthorityPhoneNo(userProfile.getAuthorityPhoneNo());
    }

    public HttpResponse updateStatus(User user) {
        HttpResponse response = new HttpResponse();
        try {

            User userDetails = userRepo.findById(user.getId());
            if (userDetails == null) {
                response.setStatusCode(204);
                response.setResponse("User  data not found");
                return response;
            } else {
                user.setCreatedOn(userDetails.getCreatedOn());
                user.setPassword(userDetails.getPassword());
                user.setUsername(userDetails.getUsername());
                userRepo.save(user);
                response.setStatusCode(200);
                response.setResponse("User status is update");
                return response;
            }
        } catch (Exception e) {
            response.setStatusCode(204);
            response.setResponse("User profile data fail to update");
            log.info("response send to user:  " + response);
            return response;
        }
    }

/*    public ResponseEntity<?> profileDataById(long id, long userId, String publicIp, String browser) {
        try {
            log.info("inside into edit profile");
            log.info("user id:  " + id);
            log.info("get user profile data by id below");
            UserProfile user = userProfileRepo.findById(id);
            if (user != null) {
                User userData = userRepo.findById(userId);
                if (userData != null) {
                    saveUserTrail(userData, "Registration Request", "view", 8, publicIp, browser);
                }

                List<Long> rolesId = new ArrayList<Long>();
                List<RolesData> rolesList = new ArrayList<RolesData>();

                log.info("roles Ids :  " + rolesId);
                log.info("user roles :  " + rolesList);

                long[] arr = new long[rolesId.size()];
                arr = Longs.toArray(rolesId);

                user.setRolesList(rolesList);
                user.setRoles(arr);
                List<SystemConfigListDb> asTypeList = systemConfigRepo.getByTag("AS_TYPE");
                for (SystemConfigListDb asType : asTypeList) {
                    Integer value = asType.getValue();
                    if (user.getType() == value) {
                        user.setAsTypeName(asType.getInterp());
                    }
                }
                List<SystemConfigListDb> arrivaPortList = systemConfigRepo.getByTag("CUSTOMS_PORT");
                for (SystemConfigListDb port : arrivaPortList) {
                    Integer value = port.getValue();
                    if (user.getArrivalPort() == value) {
                        user.setArrivalPortName(port.getInterp());
                    }
                }
                List<SystemConfigListDb> nature_Of_Employment = systemConfigRepo.getByTag("Nature_Of_Employment");
                for (SystemConfigListDb emplomentType : nature_Of_Employment) {
                    Integer value = emplomentType.getValue();
                    if (user.getNatureOfEmployment() == value) {
                        user.setNatureOfEmploymentInterp(emplomentType.getInterp());
                    }
                }
       *//*         if (user.getPortAddress() != null) {
                    PortAddress portAddress = portAddressRepoService.getById(user.getPortAddress());
                    user.setPortAddressName(portAddress.getAddress());
                }*//*
                if (Objects.nonNull(user.getUser().getApprovedBy())) {
                    user.setApprovedBy(user.getUser().getApprovedBy());
                }
                SystemConfigurationDb filePath = systemConfigurationRepo.getDataByTag("upload_file_link");
                if (filePath != null) {
                    if (user.getNidFilename() != null || !"null".equalsIgnoreCase(user.getNidFilename())) {
                        user.setNidFilePath(filePath.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + "/" + user.getUser().getUsername() + "/NID/");
                    }
                    if (user.getPhotoFilename() != null || !"null".equalsIgnoreCase(user.getPhotoFilename())) {
                        user.setPhotoFilePath(filePath.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + "/" + user.getUser().getUsername() + "/photo/");
                    }
                    if (user.getIdCardFilename() != null || !"null".equalsIgnoreCase(user.getIdCardFilename())) {
                        user.setIdCardFilePath(filePath.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + "/" + user.getUser().getUsername() + "/IDCard/");
                    }
                    if (user.getVatFilename() != null || !"null".equalsIgnoreCase(user.getVatFilename())) {
                        user.setVatFilePath(filePath.getValue().replace("$LOCAL_IP", propertiesReader.localIp) + "/" + user.getUser().getUsername() + "/Vat/");
                    }
                }
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                HttpResponse response = new HttpResponse();
                response.setStatusCode(204);
                response.setResponse("User profile data not found");
                log.info("response send to user:  " + response);

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.info("exception occur here");
            log.info(e.toString());
            HttpResponse response = new HttpResponse();
            response.setStatusCode(409);
            response.setResponse("Oops something wrong happened");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }*/
/*
    public String emailContent(MessageConfigurationDb msgConfigDb, UserProfile profile, String otp) {
        log.info("tag:  " + msgConfigDb.getTag());
        log.info("value: " + msgConfigDb.getValue());
        log.info("user data:  " + profile.getUser());
        String emailBody = null;

        emailBody = msgConfigDb.getValue();
        HashMap<String, String> map = contentMap(emailBody, profile, otp);
        String[] words = {"<First name>", "<userID>", "<Reason>", "<otp>", "<number>", "<user_type>"};
        for (int i = 0; i < words.length; i++) {
            log.info("tag: " + words[i]);
            if (emailBody.contains(words[i])) {
                log.info("if this tag is present in emailContent");
                emailBody = map.get(words[i]);
            }
        }
        return emailBody;

    }

    public String getsubject(MessageConfigurationDb msgConfigDb, UserProfile profile, String otp) {
        log.info("tag:  " + msgConfigDb.getTag());
        String subject = null;

        subject = msgConfigDb.getSubject();
        HashMap<String, String> map = contentMap(subject, profile, otp);
        String[] words = {"<First name>", "<userID>", "<Reason>", "<otp>", "<number>", "<user_type>"};
        for (int i = 0; i < words.length; i++) {
            log.info("tag: " + words[i]);
            if (subject.contains(words[i])) {
                log.info("if this tag is present in emailContent");
                subject = map.get(words[i]);
            }
        }
        return subject;

    }*/

    public HashMap<String, String> contentMap(String emailBody, UserProfile profile, String otp) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("<First name>", emailBody = emailBody.replaceAll("<First name>", profile.getFirstName()));
        map.put("<userID>", emailBody = emailBody.replaceAll("<userID>", profile.getUser().getUsername()));
        map.put("<Reason>", emailBody = emailBody.replaceAll("<Reason>", profile.getUser().getRemark()));
        map.put("<otp>", emailBody = emailBody.replaceAll("<otp>", otp));
        map.put("<number>", emailBody = emailBody.replaceAll("<number>", otp));
        return map;
    }

    public int saveUserTrail(User user, String feature, String subFeature, long featureId) {
        try {

//			AuditTrail auditTrail=new AuditTrail(user.getId(), user.getUsername(),
//					user.getUsertype().getId(),user.getUsertype().getUsertypeName(), featureId,
//					feature, subFeature,"0","NA",user.getUsertype().getUsertypeName());

            AuditTrail auditTrail = new AuditTrail(0, "Success", "", feature, subFeature, "", propertiesReader.moduleName, propertiesReader.serverName);

            log.info("going to save audit trail=" + auditTrail);
            AuditTrail output = audiTrailRepoService.saveAuditTrail(auditTrail);
            if (output != null) {
                log.info("audit trail sucessfully save");
            } else {
                log.info("user trail fails to save");
            }
            return 1;
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }

   /* public int saveUserTrail(long userId, String username, String userType, long userTypeId, String feature, String subFeature, long featureId, String publicIp, String browser) {
        try {
            AuditTrail auditTrail = new AuditTrail(0, "Success", "", feature, subFeature, "", propertiesReader.moduleName, propertiesReader.serverName);

            AuditDB aauditDB = new AuditDB(userId, username, userTypeId, userType, featureId, Features.Alert_Management, SubFeatures.VIEW_ALL, "", "NA", "", publicIp, browser);

//                    new AuditTrail(userId, username,
//                    userTypeId, userType, featureId,
//                    feature, subFeature, "0", "NA", userType, publicIp, browser);

            auditDb.save(new AuditDB(userId, username, userTypeId, userType, featureId, feature, "Login", "", "NA", "SystemAdmin", publicIp, browser));
            log.info("going to save audit trail with request:::::%%%%");
            AuditTrail output = audiTrailRepoService.saveAuditTrail(auditTrail);
            // AuditDB au=alertDb.save(aauditDB);
            if (output != null) {
                log.info("audit trail sucessfully save");
            } else {
                log.info("user trail fails to save");
            }
            return 1;
        } catch (Exception e) {
            log.info(e.toString());
            return 0;
        }
    }
*/
    public boolean delete(long userId) {
        // TODO Auto-generated method stub
        try {
            userRepo.deleteById(userId);
            return true;
        } catch (Exception e) {
            log.info(e.toString());
            return false;
        }

    }


    public void saveUserTrail(User userData, String registration_Request, String feature, int i, String publicIp, String browser) {
        try {
            // AuditTrail auditTrail = new AuditTrail(0, "Success", "",
            // registration_Request, feature,
            // "",propertiesReader.moduleName,propertiesReader.serverName);

            log.info("going to save in audit tail tables.");

            auditDb.save(new AuditDB(userData.getUserId(), userData.getUsername(), Long.valueOf(userData.getUserTypeId()), "SystemAdmin", Long.valueOf(userData.getFeatureId()), feature, "Login", "", "NA", "SystemAdmin", publicIp, browser));
        } catch (Exception e) {
            log.info(e.toString());
        }
    }

}
