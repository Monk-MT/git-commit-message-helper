package com.fulinlin.model;

import b.j.S;

import java.util.List;

/**
 * @program: git-commit-message-helper
 * @author: fulin
 * @create: 2019-12-05 21:22
 **/
public class DataSettings {
    private String template;
    private List<TypeAlias> typeAliases;
    private List<PlatformAlias> platformAliases;
    private List<String> skipCis;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<TypeAlias> getTypeAliases() {
        return typeAliases;
    }

    public void setTypeAliases(List<TypeAlias> typeAliases) {
        this.typeAliases = typeAliases;
    }

    public List<PlatformAlias> getPlatformAliases() {
        return platformAliases;
    }

    public void setPlatformAliases(List<PlatformAlias> platformAliases) {
        this.platformAliases = platformAliases;
    }

    //todo set platformAliases

    public List<String> getSkipCis() {
        return skipCis;
    }

    public void setSkipCis(List<String> skipCis) {
        this.skipCis = skipCis;
    }
}
