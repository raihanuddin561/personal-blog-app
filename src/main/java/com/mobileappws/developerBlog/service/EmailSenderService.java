package com.mobileappws.developerBlog.service;

import com.mobileappws.developerBlog.constants.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailSenderService {
    private String from  = "raihan.sebpo@gmail.com";
    @Autowired
    private JavaMailSender mailSender;
    public void sendSimpleEmail(String toemail,String body,String subject) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,"UTF-8");
        helper.setTo(toemail);
        helper.setFrom(from);
        helper.setText(body,true);
        helper.setSubject(subject);
        mailSender.send(message);
        log.info("Mail sent....");
    }

    public boolean sendPasswordResetRequest(String firstName, String email, String token) {
        String body = AppConstants.PASSWORD_RESET_HTMLBODY.replace("$firstName",firstName)
                .replace("$tokenValue",token);
        boolean result = false;
        try {
            sendSimpleEmail(email,body,AppConstants.PASSWORD_RESET_SUBJECT);
            result = true;
        }catch (Exception e){
            log.error("Mail not sent: "+e.getMessage());
            result = false;
        }
        return result;
    }
}
