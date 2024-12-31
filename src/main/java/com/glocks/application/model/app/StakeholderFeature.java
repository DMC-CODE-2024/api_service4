/*
package com.ceir.CeirCode.model.app;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.NotAudited;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
  @Table(name="feature_list")

public class StakeholderFeature {
	
	private static long serialVersionUID = 1L;
	@Id       
	private long id;
	
	@Column(nullable =false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@CreationTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdOn;
	
	@Column(nullable =false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@UpdateTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime modifiedOn;
    private String category;
    private String name;
    private String logo;
    private String link;
    
    @NotAudited
    @JsonIgnore
    @OneToMany(mappedBy ="stakeholderFeature",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    private List<UserToStakehoderfeatureMapping> UserTofeatureMapping;

	@Transient
	private List<String> iconState;


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static void setSerialVersionUID(long serialVersionUID) {
		StakeholderFeature.serialVersionUID = serialVersionUID;
	}

	public List<String> getIconState() {
		return iconState;
	}

	public void setIconState(List<String> iconState) {
		this.iconState = iconState;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@JsonIgnore
	public List<UserToStakehoderfeatureMapping> getUserTofeatureMapping() {
		return UserTofeatureMapping;
	}
	public void setUserTofeatureMapping(List<UserToStakehoderfeatureMapping> userTofeatureMapping) {
		UserTofeatureMapping = userTofeatureMapping;
	}
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("StakeholderFeature{");
		sb.append("id=").append(id);
		sb.append(", createdOn=").append(createdOn);
		sb.append(", modifiedOn=").append(modifiedOn);
		sb.append(", category='").append(category).append('\'');
		sb.append(", name='").append(name).append('\'');
		sb.append(", logo='").append(logo).append('\'');
		sb.append(", link='").append(link).append('\'');
		sb.append(", UserTofeatureMapping=").append(UserTofeatureMapping);
		sb.append(", iconState=").append(iconState);
		sb.append('}');
		return sb.toString();
	}
}

*/
