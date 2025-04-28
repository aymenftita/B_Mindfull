package tn.esprit.mindfull.Controller;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final JavaMailSender mailSender;

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @RequestMapping("/send_email")
    public String sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ouazamira804@gmail.com");
        message.setTo("amira.ouaz@esprit.tn");
        message.setSubject("Nouveau contenu ajouté");
        message.setText("Nous avons le plaisir de vous informer qu’un nouveau contenu a été ajouté à votre programme." +"/n"+
                " Veuillez vous connecter pour découvrir les détails et en profiter pleinement."+"/n"+
                "N’hésitez pas à nous contacter si vous avez des questions ou besoin d’assistance." +"/n"+
                "L’équipe Coaching Mindfull" );

       // String subject = "Nouveau contenu ajouté";
      //  String content = "Bonjour, un nouveau contenu a été ajouté à votre programme. Veuillez vérifier les détails.";
        mailSender.send(message);

        return "Email sent!!";
    }

}