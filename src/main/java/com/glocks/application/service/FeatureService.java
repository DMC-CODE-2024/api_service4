package com.glocks.application.service;

import com.glocks.application.configuration.PropertiesReaders;
import com.glocks.application.repo.app.FeatureRepo;
import com.glocks.application.response.FeatureListModel;
import com.glocks.application.util.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FeatureService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    PropertiesReaders propertiesReader;

    @Autowired
    FeatureRepo featureRepo;


    public ResponseEntity<?> featureData(Integer userId) {
        try {
            log.info("user id is: " + userId);
            List<FeatureListModel> featureList = new ArrayList<FeatureListModel>();
            List<FeatureListModel> data = featureRepo.getFeature(Long.valueOf(userId));
            log.info("data {}", data);
            if (data.size() > 0) {
                for (FeatureListModel featureListModel : data) {
                    List<String> featureIconList = featureRepo.getFeatureIcon(userId, featureListModel.getId());
                    if (Objects.isNull(featureListModel.getIframeURL())) {
                        featureListModel.setLink(featureListModel.getLink());
                    } else {
                        featureListModel.setLink(propertiesReader.basePath + "/" + featureListModel.getIframeURL());
                    }
                    featureListModel.setIconState(featureIconList);
                    featureList.add(featureListModel);
                }
            }
            if (!featureList.isEmpty()) {
                log.info("featureList {}", featureList);
                return new ResponseEntity<>(featureList, HttpStatus.OK);
            } else {
                HttpResponse response = new HttpResponse();
                response.setStatusCode(204);
                response.setResponse("Feature Data not Found");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("exception found");
            log.info(e.toString());
            HttpResponse response = new HttpResponse();
            response.setStatusCode(409);
            response.setResponse("Oops something wrong happened");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}