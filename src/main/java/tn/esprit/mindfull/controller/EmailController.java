package tn.esprit.mindfull.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
public class EmailController {

    private final JavaMailSender mailSender;

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RequestMapping("/send_email")
    public String sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("aymenftita1@gmail.com");
        message.setTo("aymenftita1@gmail.com");
        message.setSubject("test email");
        message.setText("<img src='https://th.bing.com/th/id/OIP.dp3EI63Sypg0Vo_7MLcnXAHaHp?rs=1&pid=ImgDetMain'/>");

        mailSender.send(message);

        return "Email sent!!";
    }

    @RequestMapping("/send-html-email")
    public String sendHtmlEmail() {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("aymenftita1@gmail.com");
            helper.setTo("aymenftita1@gmail.com");
            helper.setSubject("Java email with attachment | From GC");

            try (var inputStream = Objects.requireNonNull(EmailController.class.getResourceAsStream("/template.html"))) {
                helper.setText(
                        new String(inputStream.readAllBytes(), StandardCharsets.UTF_8),
                        true
                );
            }

            mailSender.send(message);
            return "success!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @RequestMapping("/send_email_html")
    public String sendEmailHtml() throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message
        helper.setFrom("aymenftita1@gmail.com");
        helper.setTo("aymenftita1@gmail.com");
        helper.setSubject("test email");

        // Set the HTML content
        String htmlContent = "<html><body><h1>This is HTML email</h1>" +
                "<img src='https://th.bing.com/th/id/OIP.dp3EI63Sypg0Vo_7MLcnXAHaHp?rs=1&pid=ImgDetMain'/>" +
                "</body></html>";
        helper.setText(htmlContent, true); // true indicates HTML

        mailSender.send(message);

        return "Email sent!!";
    }
}
