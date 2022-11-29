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
    public static final String PASSWORD_RESET_HTMLBODY = "<h1>A request to reset your passwrod</h1>"
            +"Hi, $firstName!"
            +"<p>Someone has requested to reset your password with our project. If it were not you, please ignore it. Otherwise please click on the link below to set a new password</p>"
            +"click on the following link: "
            +"<a href='http://localhost:8080/verification-service/password-reset.html?token=$tokenValue'>"
            +"LINK"
            +"</a><br/><br/>"
            +"Thank you!";
    public static final String PASSWORD_RESET_SUBJECT = "RESET PASSWORD";
    public static final String SUBJECT = "One last step to complete your registration with REST app";
}
