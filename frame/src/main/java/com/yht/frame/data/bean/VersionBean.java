package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * 版本model
 *
 * @author dundun
 */
public class VersionBean implements Serializable {
    private static final long serialVersionUID = -8438526479985188981L;
    private String minVersion;
    private String version;
    private String downloadUrl;
    private String notes;
    private String publishAt;
    private String title;

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(String publishAt) {
        this.publishAt = publishAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
