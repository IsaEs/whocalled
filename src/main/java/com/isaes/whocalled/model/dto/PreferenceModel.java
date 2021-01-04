package com.isaes.whocalled.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class PreferenceModel implements Serializable {
    private Boolean notification;
    private String language;
}
