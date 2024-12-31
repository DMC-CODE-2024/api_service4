package com.glocks.application.repo.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.glocks.application.model.audit.AuditDB;
public interface AuditDBRepo extends JpaRepository<AuditDB,Long>{

}
