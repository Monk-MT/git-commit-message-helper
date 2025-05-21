package com.dcar.model.enums;

public enum PlatformDisplayStyleEnum {
    CHECKBOX("Checkbox"),
    RADIO("Radio"),
    MIXING("Mixing");

    private final String name;


    PlatformDisplayStyleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
