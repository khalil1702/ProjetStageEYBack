package tn.projetStage.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAcceptedEmail(String toEmail, String userName, String cin, String password, String role)

    throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // Encodage UTF-8

        helper.setTo(toEmail);
        helper.setSubject("EY Medical - Approbation de votre compte");

        String htmlContent = String.format(
                "<html>" +
                        "<head>" +
                        "    <style>" +
                        "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                        "        .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "        .header { text-align: center; padding: 20px 0; }" +
                        "        .header h1 { color: #2a5c8d; font-size: 28px; margin-bottom: 10px; }" +
                        "        .logo { max-width: 150px; margin-bottom: 20px; }" +
                        "        .content { padding: 20px; }" +
                        "        .content h2 { color: #2a5c8d; font-size: 24px; margin-bottom: 20px; }" +
                        "        .content p { color: #555555; font-size: 16px; line-height: 1.6; }" +
                        "        .credentials { background-color: #f0f7ff; padding: 15px; border-radius: 5px; margin: 20px 0; border-left: 4px solid #2a5c8d; }" +
                        "        .credentials p { margin: 5px 0; }" +
                        "        .footer { text-align: center; padding: 20px 0; color: #888888; font-size: 14px; border-top: 1px solid #eeeeee; margin-top: 20px; }" +
                        "        .footer a { color: #2a5c8d; text-decoration: none; }" +
                        "        .button { background-color: #2a5c8d; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; display: inline-block; margin: 15px 0; }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='container'>" +
                        "        <div class='header'>" +
                        "            <h1>EY Medical</h1>" +
                        "            <p style='color: #2a5c8d; font-weight: bold;'>Centre Médical Spécialisé</p>" +
                        "        </div>" +
                        "        <div class='content'>" +
                        "            <p>Cher(e) <strong>%s</strong>,</p>" +
                        "            <p>Nous avons le plaisir de vous informer que votre demande de création de compte a été approuvée par notre équipe administrative.</p>" +
                        "            <p>Votre rôle au sein de notre plateforme est: <strong>%s</strong>. Ce rôle vous donne accès à des fonctionnalités spécifiques de notre système.</p>" +
                        "            <div class='credentials'>" +
                        "                <p><strong>Informations d'accès:</strong></p>" +
                        "                <p><strong>CIN:</strong> %s</p>" +
                        "                <p><strong>Mot de passe temporaire:</strong> %s</p>" +
                        "            </div>" +
                        "            <p>Pour des raisons de sécurité, nous vous recommandons de modifier votre mot de passe après votre première connexion.</p>" +
                        "            <p>Vous pouvez maintenant accéder à notre plateforme en cliquant sur le bouton ci-dessous:</p>" +
                        "            <p><a href='[URL_DE_CONNEXION]' class='button'>Accéder à la plateforme</a></p>" +
                        "            <p>Si vous rencontrez des difficultés pour vous connecter ou si vous avez des questions concernant votre compte, n'hésitez pas à contacter notre service informatique à <a href='mailto:support@eymedical.com'>support@eymedical.com</a>.</p>" +
                        "            <p>Nous vous remercions pour votre confiance et vous souhaitons la bienvenue au sein d'EY Medical.</p>" +
                        "        </div>" +
                        "        <div class='footer'>" +
                        "            <p>Cordialement,</p>" +
                        "            <p><strong>L'équipe EY Medical</strong></p>" +
                        "            <p>Tél: +27 123 456  | Email: contact@eymedical.com</p>" +
                        "            <p>© 2025 EY Medical - Tous droits réservés</p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>", userName, role, cin, password);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendRejectionEmail(String toEmail, String userName, String rejectionReason) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject("EY Medical - Statut de votre demande de compte");

        String htmlContent = String.format(
                "<html>" +
                        "<head>" +
                        "    <style>" +
                        "        body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                        "        .container { max-width: 600px; margin: 20px auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "        .header { text-align: center; padding: 20px 0; }" +
                        "        .header h1 { color: #2a5c8d; font-size: 28px; margin-bottom: 10px; }" +
                        "        .content { padding: 20px; }" +
                        "        .content h2 { color: #2a5c8d; font-size: 24px; margin-bottom: 20px; }" +
                        "        .content p { color: #555555; font-size: 16px; line-height: 1.6; }" +
                        "        .footer { text-align: center; padding: 20px 0; color: #888888; font-size: 14px; border-top: 1px solid #eeeeee; margin-top: 20px; }" +
                        "        .rejection-reason { background-color: #fff0f0; padding: 15px; border-radius: 5px; margin: 20px 0; border-left: 4px solid #d9534f; }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='container'>" +
                        "        <div class='header'>" +
                        "            <h1>EY Medical</h1>" +
                        "            <p style='color: #2a5c8d; font-weight: bold;'>Centre Médical Spécialisé</p>" +
                        "        </div>" +
                        "        <div class='content'>" +
                        "            <p>Cher(e) <strong>%s</strong>,</p>" +
                        "            <p>Nous avons examiné votre demande de création de compte pour accéder à notre plateforme.</p>" +
                        "            <div class='rejection-reason'>" +
                        "                <p><strong>Malheureusement, votre demande n'a pas pu être approuvée pour la raison suivante:</strong></p>" +
                        "                <p>%s</p>" +
                        "            </div>" +
                        "            <p>Si vous pensez qu'il s'agit d'une erreur ou si vous souhaitez plus d'informations, vous pouvez contacter notre service administratif à <a href='mailto:admin@eymedical.com'>admin@eymedical.com</a>.</p>" +
                        "            <p>Nous vous remercions pour l'intérêt que vous portez à EY Medical et nous sommes désolés de ne pas pouvoir donner suite favorable à votre demande pour le moment.</p>" +
                        "        </div>" +
                        "        <div class='footer'>" +
                        "            <p>Cordialement,</p>" +
                        "            <p><strong>L'équipe EY Medical</strong></p>" +
                        "            <p>© 2023 EY Medical - Tous droits réservés</p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>", userName, rejectionReason);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}