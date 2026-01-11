package jutjubic.isa.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationEmail(String to, String activationLink) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("slike.rimini2024@gmail.com");
        msg.setTo(to);
        msg.setSubject("Aktivacija naloga");
        msg.setText("Klikni na link za aktivaciju:\n" + activationLink);

        mailSender.send(msg);
    }
}
