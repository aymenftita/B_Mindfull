package tn.esprit.mindfull.Service.UserServices;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public void sendPasswordResetEmail(String to, String resetToken) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();
        context.setVariable("resetLink", "http://localhost:4200/reset-password?token=" + resetToken);
        String htmlContent = templateEngine.process("password-reset-email", context);

        helper.setTo(to);
        helper.setSubject("Password Reset Request");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}