package com.isaes.whocalled.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class RingModel implements Serializable {
    private String dialedNumber;
    private Date lastCallTime;
    private int numberOfRings;
}
