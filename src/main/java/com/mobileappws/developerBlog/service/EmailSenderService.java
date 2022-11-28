package com.mobileappws.developerBlog.service;

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
    @Autowired
    private JavaMailSender mailSender;
    public void sendSimpleEmail(String toemail,String body,String subject) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,"UTF-8");
        helper.setTo(toemail);
        helper.setFrom("raihan.sebpo@gmail.com");
        helper.setText(body,true);
        helper.setSubject(subject);
        mailSender.send(message);
        log.info("Mail sent....");
    }
}
