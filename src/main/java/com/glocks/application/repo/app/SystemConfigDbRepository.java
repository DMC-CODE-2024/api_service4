package com.glocks.application.repo.app;

import com.glocks.application.model.app.SystemConfigurationDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigDbRepository  extends JpaRepository<SystemConfigurationDb, Long> {

	public SystemConfigurationDb findByTag(String tag);
}
