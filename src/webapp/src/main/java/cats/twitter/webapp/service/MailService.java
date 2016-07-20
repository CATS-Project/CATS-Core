package cats.twitter.webapp.service;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 * Created by Anthony on 18/07/2016.
 */
public class MailService {

    private final String MAIL = "cats.activation@gmail.com";
    private final String MDP = "FabienRico.";

    private Properties props;
    public MailService(){
        props = System.getProperties();
        props.put("mail.smtp.user", MAIL);
        props.put("mail.smtp.password", MDP);
        props.put("mail.smtp.starttls.enable", "true"); // added this line
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

    }


    public void SendMailActivation(String user){
        Session session = Session.getInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        try {
            InternetAddress from = new InternetAddress(MAIL);
            message.setSubject("CATS: activation pending");
            message.setFrom(from);
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(user));
            Multipart multipart = new MimeMultipart("alternative");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String htmlMessage = "<h1>Hi {$login},</h1>" +
                    "<p>" +
                    "You recently registered on CATS." +
                    "</p>" +
                    "<p>The administrators have been notified and will review your registration. You will receive an e-mail once your account has been activated.</p>" +
                    "<p>Thank you for your interest in CATS!</p>" +
                    "<p>The CATS team</p>" +
                    "<p><a href='http://mediamining.univ-lyon2.fr/cats'>http://mediamining.univ-lyon2.fr/cats</a>";
            messageBodyPart.setContent(htmlMessage, "text/html");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", MAIL, MDP);
            System.out.println("Transport: " + transport.toString());
            transport.sendMessage(message, message.getAllRecipients());


        } catch (AddressException e) {
            e.printStackTrace();
            System.out.println(e);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public void SendMailActivated(String user){
        Session session = Session.getInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        try {
            InternetAddress from = new InternetAddress(MAIL);
            message.setSubject("Activated mail");
            message.setFrom(from);
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(user));
            Multipart multipart = new MimeMultipart("alternative");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("some text to send");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String htmlMessage = "Our html text";
            messageBodyPart.setContent(htmlMessage, "text/html");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", MAIL, MDP);
            System.out.println("Transport: " + transport.toString());
            transport.sendMessage(message, message.getAllRecipients());


        } catch (AddressException e) {
            e.printStackTrace();
            System.out.println(e);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
