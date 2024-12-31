package com.glocks.application.repo.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glocks.application.model.audit.AuditTrail;

public interface AuditTrailRepo extends JpaRepository<AuditTrail,Long>{

	
}
