package com.ezen.samsamoo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ezen.samsamoo.dto.ResultData;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


@Service
public class MailService {
	
    @Autowired
    private JavaMailSender sender;
    
    // application.yml의 변수를 쓰기위해서 @Value 사용
    // 보내는 사람의 이름과 메일주소 변수를 가지고 온다.
    @Value("${custom.emailFrom}")
    private String emailFrom;
    
    @Value("${custom.emailFromName}")
    private String emailFromName;

    
//----------------------------------------------------------------------------------------------------      
    // 내부 클래스 MailHandler     
    // MailHandler -> 메일보내기 유틸 클래스
    // sender, message, messageHelper 변수 가지고 있음
    private static class MailHandler {
        private JavaMailSender sender;
        private MimeMessage message;
        private MimeMessageHelper messageHelper;

        
        
       // MailHandler 생성자
        public MailHandler(JavaMailSender sender) throws MessagingException {
            this.sender = sender;
            this.message = this.sender.createMimeMessage();
            this.messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        }

        
        // 보내는 사람
        public void setFrom(String mail, String name) throws UnsupportedEncodingException, MessagingException {
            messageHelper.setFrom(mail, name);
        }

        
        // 받는 사람
        public void setTo(String mail) throws MessagingException {
            messageHelper.setTo(mail);
        }

        
        // 메일 제목
        public void setSubject(String subject) throws MessagingException {
            messageHelper.setSubject(subject);
        }

        
        // 메일 내용
        public void setText(String text) throws MessagingException {
            messageHelper.setText(text, true);
        }

        
//----------------------------------------------------------------------------------------------------            
       // 메일 보내기
       // 아래 send 함수를 실행
        public void send() {
            try {
                sender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    
    // 위 함수들에서 모은 데이터를 취합하여 메일 보내기
    public ResultData send(String email, String title, String body) {

        MailHandler mail;
        try {
            mail = new MailHandler(sender);
            mail.setFrom(emailFrom.replaceAll(" ", ""), emailFromName);
            mail.setTo(email);
            mail.setSubject(title);
            mail.setText(body);
            mail.send();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultData("F-1", "메일이 실패하였습니다.");
        }

        return new ResultData("S-1", "메일이 발송되었습니다.");
    }
    
}
