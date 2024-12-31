package com.glocks.application.repoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glocks.application.model.app.User;
import com.glocks.application.repo.app.UserRepo;

@Service
public class UserRepoService {
    public UserRepoService() {

    }

    public UserRepoService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    UserRepo userRepo;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public User findByUSerId(long userId) {

        try {
            return userRepo.findById(userId);
        } catch (Exception e) {
            log.info("user data failed to find bu this id: " + userId);
            return null;
        }
    }

}
