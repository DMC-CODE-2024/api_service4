package com.glocks.application.model.app;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity 
//@Audited
@Table(name = "user_login_status")

public class LoginTracking {
	private static long serialVersionUID = 1L;
	@Id       
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable =false)
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;
	private Integer loginStatus;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="user_id",nullable = false)
	private User userTrack;        



	
	public long getId() { 
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	public Integer getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(Integer loginStatus) {
		this.loginStatus = loginStatus;
	}
	public User getUserTrack() {
		return userTrack;
	}
	public void setUserTrack(User userTrack) {
		this.userTrack = userTrack;
	}
	
	public LoginTracking() {
		super();
	}
	public LoginTracking(Integer loginStatus, User userTrack) {
		super();
		this.loginStatus = loginStatus;
		this.userTrack = userTrack;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	public LoginTracking(Integer loginStatus, User userTrack,LocalDateTime createdOn) {
		super();
		this.loginStatus = loginStatus;
		this.userTrack = userTrack;
		this.createdOn = createdOn;
	}
	
	
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginTracking [id=");
		builder.append(id);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", loginStatus=");
		builder.append(loginStatus);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
