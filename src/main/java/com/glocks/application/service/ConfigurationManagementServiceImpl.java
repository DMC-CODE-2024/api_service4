package com.glocks.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.glocks.application.exceptions.ResourceServicesException;
import com.glocks.application.model.app.Notification;
import com.glocks.application.repo.app.NotificationRepository;
import com.glocks.application.response.GenricResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConfigurationManagementServiceImpl {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	NotificationRepository notificationRepository;

	public GenricResponse saveNotification(Notification notification) {
		try {

			notificationRepository.save(notification);
			return new GenricResponse(0, "Notification have been saved Sucessfully", "");

		} catch (Exception e) {
			logger.info("Exception found="+e.getMessage());
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

//	public GenricResponse saveNotification(String channelType, String message, User userId, Long featureId, String featureName, String subFeature, String featureTxnId,String subject) {
//		try {
//
//			notificationRepository.save(new Notification(channelType, message, userId, featureId, featureName, subFeature, featureTxnId,subject));
//
//			return new GenricResponse(0, "Notification have been saved Sucessfully", "");
//		} catch (Exception e) {
//			logger.info("Exception found="+e.getMessage());
//			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
//		}
//	}

	public GenricResponse saveNotification(String channelType, String message, Long userId, Long featureId, String featureName,
			String subFeature, String featureTxnId, String subject, int retryCount, String referTable, String roleType, String receiverUserType) {
			try {

			Notification notification = notificationRepository.save(new Notification(channelType, message, userId, featureId, featureName,
			subFeature, featureTxnId, subject, retryCount, referTable, roleType, receiverUserType));

			return new GenricResponse(0, "Notification have been saved Sucessfully", Long.toString(notification.getId()));
			} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
			}
			}
}
