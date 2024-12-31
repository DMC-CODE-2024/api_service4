package com.glocks.application.response;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;

import java.util.List;
import java.util.Map;

public class FeatureListModel {
    private long id;
    private String name;
    private String link;
    private String logo;
    private String iframeURL;
    private Map<String, String> map;

    private List<String> iconState;



    public FeatureListModel(long id, String name, String link, String iframeURL, String logo) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.iframeURL = iframeURL;
        this.logo = logo;
    }

    public List<String> getIconState() {
        return iconState;
    }

    public void setIconState(List<String> iconState) {
        this.iconState = iconState;
    }

    public String getIframeURL() {
        return iframeURL;
    }

    public void setIframeURL(String iframeURL) {
        this.iframeURL = iframeURL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String featureName) {
        this.name = featureName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FeatureListModel{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", link='").append(link).append('\'');
        sb.append(", logo='").append(logo).append('\'');
        sb.append(", iframeURL='").append(iframeURL).append('\'');
        sb.append(", map=").append(map);
        sb.append(", iconState=").append(iconState);
        sb.append('}');
        return sb.toString();
    }
}
