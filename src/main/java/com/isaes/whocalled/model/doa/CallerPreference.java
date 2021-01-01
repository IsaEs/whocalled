package com.isaes.whocalled.model.doa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Getter
@Setter
//@Entity
//@Table(name = "tbl_caller_preference")
public class CallerPreference implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String callerNo;

    private Boolean notification;

    private String language;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", referencedColumnName = "id", unique=true)
//    private User user;

//    @OneToMany
//    @JoinColumn(name = "callerNo", referencedColumnName = "callerNo")
//    private List<CallDetailRecord> dialedNumbers;

//    @OneToMany
//    @JoinColumn(name = "dialedNumber", referencedColumnName = "callerNo")
//    private List<CallDetailRecord> missedCalls;

    @Override
    public String toString() {
        return "CallerPreference{" +
                "callerNo='" + callerNo + '\'' +
                ", notification=" + notification +
                ", language='" + language + '\'' +
                '}';
    }
}
