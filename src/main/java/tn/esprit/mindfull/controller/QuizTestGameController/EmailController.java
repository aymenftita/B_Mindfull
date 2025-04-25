package tn.esprit.mindfull.controller.QuizTestGameController;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.HTMLDocument;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @SneakyThrows
    @RequestMapping("/send_email_game")
    public String sendEmailGame() throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message
        helper.setFrom("aymenftita1@gmail.com");
        helper.setTo("aymenftita1@gmail.com");
        helper.setSubject("Mindfull");


       /* ClassPathResource resource = new ClassPathResource("src/main/java/tn/esprit/mindfull/controller/QuizTestGameController/template.html");
        String htmlContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);*/

        String htmlContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /><title>Congratulations from Mindfull!</title><style type=\"text/css\">body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }img { -ms-interpolation-mode: bicubic; border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; }body { margin: 0 !important; padding: 0 !important; width: 100% !important; }a[x-apple-data-detectors] {color: inherit !important;text-decoration: none !important;font-size: inherit !important;font-family: inherit !important;font-weight: inherit !important;line-height: inherit !important;}body {font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;color: #333333;background-color: #f7f7f7;}.container {max-width: 600px;margin: 0 auto;padding: 20px;}.header {text-align: center;padding: 20px 0;}.logo {max-width: 180px;}.content {background-color: #ffffff;border-radius: 12px;padding: 30px;box-shadow: 0 4px 12px rgba(0,0,0,0.05);}h1 {color: #4a6cf7;font-size: 24px;font-weight: 600;margin-top: 0;margin-bottom: 20px;text-align: center;}p {font-size: 16px;line-height: 1.6;margin-bottom: 20px;}.highlight-box {background-color: #f8f9ff;border-left: 4px solid #4a6cf7;padding: 15px;margin: 20px 0;border-radius: 0 8px 8px 0;}.button {display: inline-block;background-color: #4a6cf7;color: #ffffff !important;text-decoration: none;padding: 12px 25px;border-radius: 30px;font-weight: 600;margin: 20px 0;text-align: center;}.footer {text-align: center;padding: 20px 0;font-size: 14px;color: #999999;}.social-icons {margin: 20px 0;}.social-icons a {display: inline-block;margin: 0 10px;}.game-image {max-width: 100%;border-radius: 8px;margin: 20px 0;}</style></head><body><div class=\"container\"><div class=\"header\"><img src=\"https://www.mkoester.com/wp-content/uploads/mindfull/mindfull_header_s.jpg\" alt=\"Mindfull\" class=\"logo\" /></div><div class=\"content\"><h1>🎉 Congratulations on Completing the Game!</h1><p>Dear [User's Name],</p><p>We're thrilled to let you know that you've successfully completed today's Mindfull game! Your dedication to mindfulness and personal growth is truly inspiring.</p><img src=\"https://img.freepik.com/premium-vector/brain-game-logo-vector-illustration-mind-game-logo-design-icon_617472-5858.jpg\" alt=\"Game completed\" class=\"game-image\" /><div class=\"highlight-box\"><p><strong>Your achievement:</strong> You've maintained a [X]-day streak and earned [Y] mindfulness points this week. Keep up the amazing work!</p></div><p>Remember, every moment of mindfulness brings you closer to a more balanced and peaceful life. We can't wait to see what you'll achieve tomorrow!</p><p style=\"text-align: center;\"><a href=\"http://localhost:4200\" class=\"button\">Play Your Next Game</a></p><p>With gratitude,<br>The Mindfull Team</p></div><div class=\"footer\"><div class=\"social-icons\"><a href=\"https://twitter.com/mindfullapp\"><img src=\"https://i.pinimg.com/originals/99/65/5e/99655e9fe24eb0a7ea38de683cedb735.jpg\" alt=\"Twitter\" width=\"24\" /></a><a href=\"https://facebook.com/mindfullapp\"><img src=\"https://th.bing.com/th/id/R.63815dbaaf16875b86ea585567c791e8?rik=kJFqJpRt8QiuCA&pid=ImgRaw&r=0\" alt=\"Facebook\" width=\"24\" /></a><a href=\"https://instagram.com/mindfullapp\"><img src=\"https://th.bing.com/th/id/R.9c299beed82dde897920210755c4db13?rik=SRBTu73K%2f58fPQ&pid=ImgRaw&r=0\" alt=\"Instagram\" width=\"24\" /></a></div><p>© 2023 Mindfull. All rights reserved.</p><p>123 Mindfulness Lane, San Francisco, CA 94107</p><p><a href=\"https://mindfullapp.com/privacy\">Privacy Policy</a> |<a href=\"https://mindfullapp.com/terms\">Terms of Service</a></p><p><a href=\"[UNSUBSCRIBE_LINK]\">Unsubscribe</a> |<a href=\"[MANAGE_PREFERENCES_LINK]\">Manage Preferences</a></p></div></div></body></html>";


        htmlContent = htmlContent.replace("[User's Name]", "aymen");
        htmlContent = htmlContent.replace("[X]", "7");
        htmlContent = htmlContent.replace("[Y]", "150");
        htmlContent = htmlContent.replace("[UNSUBSCRIBE_LINK]", "https://mindfullapp.com/unsubscribe");
        htmlContent = htmlContent.replace("[MANAGE_PREFERENCES_LINK]", "https://mindfullapp.com/preferences");

        // Replace image URLs
        htmlContent = htmlContent.replace("https://example.com/mindfull-logo.png", "https://yourdomain.com/logo.png");
        htmlContent = htmlContent.replace("https://example.com/game-completion-image.jpg", "https://yourdomain.com/game-image.jpg");
        helper.setText(htmlContent, true); // true indicates HTML

        mailSender.send(message);

        return "Email sent!!";
    }

    @RequestMapping("/send_email_Quiz")
    public String sendEmailQuiz() throws MessagingException {
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
