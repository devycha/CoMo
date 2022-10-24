package com.dongjji.como.common.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@RequiredArgsConstructor
@Component
public class GoogleMailSender {

    private final JavaMailSender javaMailSender;

    public void sendEmailAuthMail(String email, String uuid) {
        String to = email;
        String subject = "CoMo 이메일 인증 메일";
        String text = "아래 링크를 클릭하여 이메일 인증을 완료하세요.</p>"
                + "<div><a target='_blank' href='http://localhost:8080/user/email-auth?auth-key=" + uuid + "'>이메일 인증 하기</a></div>";
        
        sendMail(to, subject, text);
    }

    public boolean sendMail(String to, String subject, String text) {

        boolean result = false;

        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(to);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(text, true);
            }
        };

        try {
            javaMailSender.send(msg);
            result = true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }
}

