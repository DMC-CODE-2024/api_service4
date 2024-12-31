package com.glocks.application.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glocks.application.service.FeatureService;


@RestController
@CrossOrigin
public class FeatureController{
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	FeatureService featureService;

	//@ApiOperation(value = "user feature list", response = HttpResponse.class)
	/*
	 * @PostMapping("/featureList") public ResponseEntity<?>
	 * featureList(@RequestParam Integer userid,UserLogin userLogin){ return
	 * featureService.featureData(userid,userLogin) ; }
	 */
	@PostMapping("/featureList/{userid}")     
	public ResponseEntity<?> featureList(@PathVariable Integer userid){
		return featureService.featureData(userid) ;
	}


	//@ApiOperation(value = "all features", response = StakeholderFeature.class)
/*	@CrossOrigin
	@PostMapping("/getAllFeatures") 
	public ResponseEntity<?> featureData(){
		return featureService.featureData();
	}
	*/
	//@ApiOperation(value = "feature name by Id", response = GenricResponse.class)
/*
	@CrossOrigin
	@PostMapping("/nameById/{id}") 
	public GenricResponse featureNameById(@PathVariable("id")long id){
		return featureService.featureNameById(id);
	}
	

*/


}