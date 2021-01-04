package com.isaes.whocalled.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_call_detail_record")
public class CallDetailRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String callerNo;     // my phone number
    private String dialedNumber; // my friends number
    private Date lastCallTime;
    private int numberOfRings;
    @JsonIgnore
    private Boolean isDialedNumberNotified; // notification tag for my friend  when is online this will turn true we need false
    //Where dialedNumber == myPhoneNumber (Whocalled me) and isDialedNumberNotified == false
    @JsonIgnore
    private Boolean isCallerNotified; // Caller notifications

}
