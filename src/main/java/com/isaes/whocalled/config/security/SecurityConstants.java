package com.isaes.whocalled.config.security;

public class SecurityConstants {
    public static final String SECRET = "VERYVERYSECRET";
    public static final long EXPIRATION_TIME = 400_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String REGISTER_URL = "/api/signin";
    public static final String LOG_IN_URL = "/api/signup";
}
