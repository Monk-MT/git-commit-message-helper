package com.dcar.model;

public class CommitTemplate {

    private String mType;
    private String mPlatform;
    private String mTaskId;
    private String mBusiness;
    private String mBody;

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getPlatform() {
        return mPlatform;
    }

    public void setPlatform(String mPlatform) {
        this.mPlatform = mPlatform;
    }

    public String getTaskId() {
        return mTaskId;
    }

    public void setTaskId(String mTaskId) {
        this.mTaskId = mTaskId;
    }

    public String getBusiness() {
        return mBusiness;
    }

    public void setBusiness(String mBusiness) {
        this.mBusiness = mBusiness;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String mBody) {
        this.mBody = mBody;
    }
}
