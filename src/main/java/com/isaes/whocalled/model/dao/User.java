package com.isaes.whocalled.model.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "tbl_user")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private String phoneNo;

	@Column
	@JsonIgnore
	private String password;

	private Boolean notification;

	private String language;

//	//MappedBy indicate field name in other side
//	@OneToOne(cascade = CascadeType.ALL, mappedBy="user")
//	@JsonIgnore
//	private CallerPreference calllerPreference;

//	@OneToMany
//	@JsonIgnore
//    @JoinColumn(name = "dialedNumber", referencedColumnName = "phoneNo")
//    private List<CallDetailRecord> missedCalls;

}