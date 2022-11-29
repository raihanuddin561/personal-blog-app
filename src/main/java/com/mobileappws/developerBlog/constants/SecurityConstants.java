package com.mobileappws.developerBlog.constants;

import com.mobileappws.developerBlog.SpringApplicationContext;
import com.mobileappws.developerBlog.security.AppProperties;

public class SecurityConstants {
    public static final String SIGN_UP_URL = "/users";
    public static final String ERROR = "/error";
    public static final String TEST_URL = "/tests";
    public static final long EXPIRATION_TIME = 864000000;
    public static final long PASSWORD_EXPIRATION_TIME = 3600000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN = "/login";
    public static final String USER_ID = "userId";
    public static final String VERIFY_EMAIL = "/users/email-verification";
    public static final String RESET_PASSWORD = "/users/password-reset-request";

    // public static final String TOKEN_SECRET = "codingmoja";
    public static String getTokenSecret(){
        AppProperties appProperties =(AppProperties) SpringApplicationContext.getBean("appProperties");
        return appProperties.getTokenSecret();
    }
}
