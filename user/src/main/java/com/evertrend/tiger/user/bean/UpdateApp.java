package com.evertrend.tiger.user.bean;

import java.io.Serializable;

public class UpdateApp implements Serializable {
    private int isUpdate;
    private String newVersion;
    private String apkFileUrl;
    private String updateLog;
    private String targetSize;
    private int isConstraint;//是否强制更新

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getApkFileUrl() {
        return apkFileUrl;
    }

    public void setApkFileUrl(String apkFileUrl) {
        this.apkFileUrl = apkFileUrl;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public String getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(String targetSize) {
        this.targetSize = targetSize;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public int getIsConstraint() {
        return isConstraint;
    }

    public void setIsConstraint(int isConstraint) {
        this.isConstraint = isConstraint;
    }
}
