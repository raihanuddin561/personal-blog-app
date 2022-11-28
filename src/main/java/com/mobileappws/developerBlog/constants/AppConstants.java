package com.mobileappws.developerBlog.constants;

public class AppConstants {
    public static final String APPLICATION_HAL_JSON = "application/hal+json";
    public static final String HTMLBODY = "<h1>Please verify your email address</h1>"
            +"<p>Thank you for registering with our mobile app. To complete registration process and be able to log in</p>"
            +"click on the following link: "
            +"<a href='http://localhost:8080/verification-service/email-verification.html?token=$tokenValue'>"
            +"Complete your registration"
            +"</a><br/><br/>"
            +"Thank you!";
    public static final String SUBJECT = "One last step to complete your registration with REST app";
}
