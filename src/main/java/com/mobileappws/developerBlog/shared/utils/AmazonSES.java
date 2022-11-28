package com.mobileappws.developerBlog.shared.utils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.mobileappws.developerBlog.shared.dto.UserDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AmazonSES {
    final String FROM = "raihan.sebpo@gmail.com";
    final String SUBJECT = "One last step to complete your registration with REST app";
    final String HTMLBODY = "<h1>Please verify your email address</h1>"
            +"<p>Thank you for registering with our mobile app. To complete registration process and be able to log in</p>"
            +"click on the following link: "
            +"<a href='http://localhost:8080/verification-service/email-verification.html?token=$tokenValue'>"
            +"Complete your registration"
            +"</a><br/><br/>"
            +"Thank you!";
    final String TEXTBODY = "Please verify your email address"
            +"Thank you for registering with our mobile app. To complete registration process and be able to log in"
            +"click on the following link: "
            +"href='http://localhost:8080/verification-service/email-verification.html?token=$tokenValue'>"
            +"Complete your registration"
            +"Thank you!";
    public void verifyEmail(UserDto userDto){
        AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_EAST_1)
                .build();
        String htmlBodyWithToken = HTMLBODY.replace("$tokenValue",userDto.getEmailVerificationToken());
        String textBodyWithToken = TEXTBODY.replace("$tokenValue",userDto.getEmailVerificationToken());
        SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(userDto.getEmail()))
                .withMessage(new Message()
                        .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
                                .withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
                        .withSubject(new Content().withCharset("UTF-8").withData(SUBJECT))
                ).withSource(FROM);
        client.sendEmail(request);
        log.info("Email sent to: "+userDto.getEmail());

    }
}
