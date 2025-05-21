package com.fulinlin.model;

import java.util.List;

/**
 * @program: git-commit-message-helper
 * @author: fulin
 * @create: 2019-12-05 21:22
 **/
public class DataSettings {
    private String template;
    private List<Alias> typeAliases;
    private List<Alias> platformAliases;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<Alias> getTypeAliases() {
        return typeAliases;
    }

    public void setTypeAliases(List<Alias> aliases) {
        this.typeAliases = aliases;
    }

    public List<Alias> getPlatformAliases() {
        return platformAliases;
    }

    public void setPlatformAliases(List<Alias> aliases) {
        this.platformAliases = aliases;
    }

}
