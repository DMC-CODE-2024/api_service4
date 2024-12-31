package com.glocks.application.repoService;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glocks.application.model.app.SystemConfigListDb;
import com.glocks.application.repo.app.SystemConfigDbListRepository;

@Service
public class SystemConfigurationDbRepoService {


	
	@Autowired
	SystemConfigDbListRepository systemConfigRepo;
	
	public ArrayList<SystemConfigListDb> getDataByTag(String tag) {
		
	try {
		ArrayList<SystemConfigListDb> systemConfigurationDb=systemConfigRepo.getByTag(tag);
		return systemConfigurationDb;
	}
		
	catch(Exception e) {
		return new ArrayList<SystemConfigListDb>();
	}
	}
}
