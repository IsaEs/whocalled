package com.isaes.whocalled.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserModel implements Serializable {
	private String phoneNo;
	private String password;
	private Boolean notification;
	private String language;
}