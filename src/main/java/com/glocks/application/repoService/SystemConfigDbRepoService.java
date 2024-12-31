package com.glocks.application.repoService;

import com.glocks.application.model.app.SystemConfigurationDb;
import com.glocks.application.repo.app.SystemConfigDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class SystemConfigDbRepoService {

//	@Autowired
//	SystemConfigDbListRepository systemConfigRepo;
//	
//	public ArrayList<SystemConfigListDb> getDataByTag(String tag) {
//		
//	try {
//		ArrayList<SystemConfigListDb> systemConfigurationDb=systemConfigRepo.getByTag(tag);
//		return systemConfigurationDb;
//	}
//		
//	catch(Exception e) {
//		return new ArrayList<SystemConfigListDb>();
//	}
//	}
	
	@Autowired
	SystemConfigDbRepository systemConfigRepo;
	
	public SystemConfigurationDb getDataByTag(String tag) {
		
	try {
		SystemConfigurationDb systemConfigurationDb=systemConfigRepo.findByTag(tag);
		return systemConfigurationDb;
	}
		
	catch(Exception e) {
		return null;
	}
	}
}
