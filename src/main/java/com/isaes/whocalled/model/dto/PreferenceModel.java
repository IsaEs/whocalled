package com.isaes.whocalled.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PreferenceModel implements Serializable {
    private Boolean notification;
    private String language;

    @Override
    public String toString() {
        return "PreferenceModel{" +
                ", notification=" + notification +
                ", language='" + language + '\'' +
                '}';
    }
}
