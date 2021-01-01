package com.isaes.whocalled.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class RingModel implements Serializable {
    private String dialedNumber;
    private Date lastCallTime;
    private int numberOfRings;

    @Override
    public String toString() {
        return "RingModel{" +
                "dialedNumber='" + dialedNumber + '\'' +
                ", lastCallTime=" + lastCallTime +
                ", numberOfRings=" + numberOfRings +
                '}';
    }
}
