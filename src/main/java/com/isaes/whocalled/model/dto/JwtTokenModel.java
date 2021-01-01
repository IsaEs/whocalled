package com.isaes.whocalled.model.dto;

import java.io.Serializable;

public class JwtTokenModel implements Serializable {
	private final String token;
	public JwtTokenModel(String token) {
		this.token = token;
	}
	public String getToken() { return this.token; }
}