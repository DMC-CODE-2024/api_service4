package com.glocks.application.repo.app;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glocks.application.model.app.CurrentLogin;

@Repository
public interface CurrentLoginRepo extends JpaRepository<CurrentLogin,Long>{
	public CurrentLogin save(CurrentLogin currentLogin);
	public boolean existsByCurrentUserLogin_Id(long id);
	public CurrentLogin findByCurrentUserLogin_Id(long id);
	public void deleteByCurrentUserLogin_Id(long id);
}
