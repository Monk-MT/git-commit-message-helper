package com.dcar.model;

import java.util.List;

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
