package com.glocks.application.repo.app;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glocks.application.model.app.LoginTracking;

public interface LoginTrackingRepo extends JpaRepository<LoginTracking, Integer>{

	public LoginTracking save(LoginTracking loginTracking);
}
