package com.fulinlin.model;

/**
 * @program: git-commit-message-helper
 * @author: fulin
 * @create: 2019-12-08 11:36
 **/
public class CommitTemplate {

    private String mType;
    private String mPlatform;
    private String mChangeId;
    private String mBusiness;
    private String mBody;
//    private String changes;
//    private String closes;
//    private String skipCi;

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

    public String getChangeId() {
        return mChangeId;
    }

    public void setChangeId(String mChangeId) {
        this.mChangeId = mChangeId;
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
