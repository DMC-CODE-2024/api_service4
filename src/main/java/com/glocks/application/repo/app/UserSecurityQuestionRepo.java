package com.glocks.application.repo.app;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glocks.application.model.app.UserSecurityquestion;

public interface UserSecurityQuestionRepo extends JpaRepository<UserSecurityquestion, Integer>{

	public UserSecurityquestion save(UserSecurityquestion userSecurityquestion); 
	public UserSecurityquestion findByUser_IdAndSecurityQuestion_IdAndAnswer(long userId,long questionId,String answer);
	    
}
