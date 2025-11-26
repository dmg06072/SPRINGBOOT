package com.example.demo.Restful.C05Google;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/google/mail")
public class GoogleMailAPIController {
    @Autowired
    JavaMailSender javaMailSender;

    @GetMapping("/req")
    @ResponseBody
    public void req(
            @RequestParam("email") String recvedEmail,
            @RequestParam("text") String text
    )
    {
        log.info("GET /google/mail/req...{},{}",recvedEmail,text);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recvedEmail);
        message.setSubject("[Web발신] 메일 테스트입니다");
        message.setText(text);

        javaMailSender.send(message);

    }
}
