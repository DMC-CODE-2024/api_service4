package com.glocks.application.repo.app;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glocks.application.model.app.Securityquestion;
public interface SecurityQuestionRepo extends JpaRepository<Securityquestion, Long>{
  public List<Securityquestion> findAll();
                                      
}
