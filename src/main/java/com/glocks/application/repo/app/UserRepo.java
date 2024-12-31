package com.glocks.application.repo.app;

import com.glocks.application.model.app.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepo extends JpaRepository<User, Long> ,JpaSpecificationExecutor<User>{
    
	@Transactional
	public User save(User u); 
	public User findById(long id);
	public User findByUsername(String user);
	public User deleteById(long id);

}
