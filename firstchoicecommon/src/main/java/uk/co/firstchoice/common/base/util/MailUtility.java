package uk.co.firstchoice.common.base.util;


import javax.mail.Session;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import uk.co.firstchoice.common.base.config.properties.SystemConfig;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**
 * Class to make mailing from Java easy.
 */
public class MailUtility {


	
	
    public static void mail(String toAddress, String messageText)
            throws AddressException, MessagingException {
    	
    	Session session = Session.getDefaultInstance(_serverProperties);
        Message message = new MimeMessage(session);
        MimeMultipart messageMimeContent = new MimeMultipart();

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
                toAddress, false));

        MimeBodyPart messagePart = new MimeBodyPart();
        messagePart.setText(messageText);
        messageMimeContent.addBodyPart(messagePart);
        message.setContent(messageMimeContent);

        Transport.send(message);
    }

       
    private static Properties _serverProperties = null;
    static {

    	SystemConfig systemConfig = SystemConfig.getInstance();
    	
        _serverProperties = new Properties();
        _serverProperties.setProperty(SystemConfig.MAIL_PROTOCOL.toString(), systemConfig.getParameter(SystemConfig.MAIL_PROTOCOL));
        _serverProperties.setProperty(SystemConfig.MAIL_HOST.toString(), systemConfig.getParameter(SystemConfig.MAIL_HOST));
        _serverProperties.setProperty(SystemConfig.MAIL_SMTP_HOST.toString(), systemConfig.getParameter(SystemConfig.MAIL_SMTP_HOST));
    }


}