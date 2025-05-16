package com.fulinlin.model;

import com.fulinlin.model.enums.PlatformDisplayStyleEnum;
import com.fulinlin.model.enums.TypeDisplayStyleEnum;
import org.apache.batik.util.Platform;

public class CentralSettings {

    private TypeDisplayStyleEnum typeDisplayStyle;

    private PlatformDisplayStyleEnum platformDisplayStyle;

    private Integer typeDisplayNumber;

    private Integer platformDisplayNumber;

    private String skipCiDefaultValue;

    private Boolean skipCiDefaultApprove;

    private Boolean skipCiComboboxEnable;

    private Hidden hidden;


    public TypeDisplayStyleEnum getTypeDisplayStyle() {
        return typeDisplayStyle;
    }

    public void setTypeDisplayStyle(TypeDisplayStyleEnum typeDisplayStyle) {
        this.typeDisplayStyle = typeDisplayStyle;
    }

    public PlatformDisplayStyleEnum getPlatformDisplayStyle() {
        return platformDisplayStyle;
    }

    public void setPlatformDisplayStyle(PlatformDisplayStyleEnum platformDisplayStyle) {
        this.platformDisplayStyle = platformDisplayStyle;
    }

    public Integer getTypeDisplayNumber() {
        return typeDisplayNumber;
    }

    public void setTypeDisplayNumber(Integer typeDisplayNumber) {
        this.typeDisplayNumber = typeDisplayNumber;
    }

    public Integer getPlatformDisplayNumber() {
        return platformDisplayNumber;
    }

    public void setPlatformDisplayNumber(Integer platformDisplayNumber) {
        this.platformDisplayNumber = platformDisplayNumber;
    }

    public String getSkipCiDefaultValue() {
        return skipCiDefaultValue;
    }

    public void setSkipCiDefaultValue(String skipCiDefaultValue) {
        this.skipCiDefaultValue = skipCiDefaultValue;
    }

    public Boolean getSkipCiDefaultApprove() {
        return skipCiDefaultApprove;
    }

    public void setSkipCiDefaultApprove(Boolean skipCiDefaultApprove) {
        this.skipCiDefaultApprove = skipCiDefaultApprove;
    }

    public Boolean getSkipCiComboboxEnable() {
        return skipCiComboboxEnable;
    }

    public void setSkipCiComboboxEnable(Boolean skipCiComboboxEnable) {
        this.skipCiComboboxEnable = skipCiComboboxEnable;
    }

    public Hidden getHidden() {
        return hidden;
    }

    public void setHidden(Hidden hidden) {
        this.hidden = hidden;
    }

    public static class Hidden {
        private Boolean type;
        private Boolean platform;
        private Boolean changeId;
        private Boolean business;
        private Boolean body;
//        private Boolean changes;
//        private Boolean closed;
//        private Boolean skipCi;

        public Boolean getType() {
            return type;
        }

        public void setType(Boolean type) {
            this.type = type;
        }

        public Boolean getPlatform() {
            return platform;
        }

        public void setPlatform(Boolean platform) {
            this.platform = platform;
        }

        public Boolean getChangeId() {
            return changeId;
        }

        public void setChangeId(Boolean changeId) {
            this.changeId = changeId;
        }

        public Boolean getBusiness() {
            return business;
        }

        public void setBusiness(Boolean business) {
            this.business = business;
        }

        public Boolean getBody() {
            return body;
        }

        public void setBody(Boolean body) {
            this.body = body;
        }

//        public Boolean getChanges() {
//            return changes;
//        }
//
//        public void setChanges(Boolean changes) {
//            this.changes = changes;
//        }
//
//        public Boolean getClosed() {
//            return closed;
//        }
//
//        public void setClosed(Boolean closed) {
//            this.closed = closed;
//        }
//
//        public Boolean getSkipCi() {
//            return skipCi;
//        }
//
//        public void setSkipCi(Boolean skipCi) {
//            this.skipCi = skipCi;
//        }
    }

}
