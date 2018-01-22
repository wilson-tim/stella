package uk.co.firstchoice.util.mail;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * This class implements a simple mail object for sending e-mails.
 * <p>
 * The mail can be sent to multiple recipients and can include file attachments
 * if required.
 * </p>
 * <p>
 * It uses the JavaMail classes.
 * 
 * @author A.James
 * @since 1.2
 * @see "JavaMail API on Sun Java website."
 */
public class SMTPExtensions {

	/** Constructor. */
	public void SMTPExtensions() {
	}

	/**
	 * Sends the e-mail.
	 * 
	 * @param host
	 *            Mail server for distributing e-mails.
	 * @param toList
	 *            List of recipients.
	 * @param from
	 *            Sender of e-mail.
	 * @param subject
	 *            Subject of e-mail
	 * @param mailmsgFilename
	 *            Name of file (full path) containing text of mail message. If
	 *            no file exists default message <I>No mail message text
	 *            available... </I> is used.
	 * @param attachFileList
	 *            List of files to be attached to the e-mail. Can be a list with
	 *            no elements.
	 * @param debug
	 *            <CODE>true</CODE> sets debug on and generates debug
	 *            messages. <CODE>false</CODE> sets debug off.
	 * @throws NoMailRecipientException
	 *             If recipient list is empty.
	 */
	public void sendMail(String host, String[] toList, String from,
			String subject, String mailmsgFilename, String[] attachFileList,
			boolean debug) throws NoMailRecipientException {

		/**
		 * sendMail will create a multipart message with the second block of the
		 * message being the given files.
		 * <p>
		 * 
		 * usage: <code>java sendMail <i>to from smtp file true|false</i></code>
		 * where <i>to </i> and <i>from </i> are the destination and origin
		 * email addresses, respectively, and <i>smtp </i> is the hostname of
		 * the machine that has smtp server running. <i>file </i> is the file to
		 * send. The next parameter either turns on or turns off debugging
		 * during sending.
		 * 
		 * @author A.James
		 */

		// create some properties and get the default Session
		Properties props = System.getProperties();
		props.put("mail.smtp.host", host);

		Debug db = new Debug(debug);
		db.handleDebugMessage("Subject: " + subject);
		db.handleDebugMessage("Mail message filename: " + mailmsgFilename);

		for (int i = 0; i < toList.length; i++) {
			db.handleDebugMessage("To recipient " + (i + 1) + ": " + toList[i]);
		}

		for (int i = 0; i < attachFileList.length; i++) {
			db.handleDebugMessage("Attach file " + (i + 1) + ": "
					+ attachFileList[i]);
		}

		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(debug);

		try {
			// create a message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));

			db.handleDebugMessage("Starting recipients");
			db.handleDebugMessage("There are " + toList.length
					+ " recipients to be converted to Internet addresses.");

			if (toList.length > 0) {
				db
						.handleDebugMessage("Recipients are being converted to Internet addresses.");

				// Set up the list of Internet addresses passed in the toList.
				InternetAddress[] address = new InternetAddress[toList.length];

				for (int i = 0; i < toList.length; i++) {
					address[i] = new InternetAddress(toList[i]);
				}

				db.handleDebugMessage("There are " + address.length
						+ " recipients to be added.");
				msg.setRecipients(Message.RecipientType.TO, address);
			} else {
				throw new NoMailRecipientException();
			}

			db.handleDebugMessage("Finished recipients");
			msg.setSubject(subject);

			// create the Multipart.
			Multipart mp = new MimeMultipart();

			// create and fill the first message part and add it to the
			// Multipart.
			try {
				FileInputStream fis = new FileInputStream(mailmsgFilename);
				MimeBodyPart mbp1 = new MimeBodyPart(fis);
				fis.close();
				mp.addBodyPart(mbp1);
				msg.setContent(mp);
			} catch (IOException mex) {
				MimeBodyPart errmbp1 = new MimeBodyPart();
				errmbp1.setText("No mail message text available...\n.");
				mp.addBodyPart(errmbp1);
				msg.setContent(mp);
				System.out.println("File I/O error (file " + mailmsgFilename
						+ ")...");
			}

			// attach the files to the Multipart.
			if (attachFileList.length > 0) {
				for (int i = 0; i < attachFileList.length; i++) {
					db.handleDebugMessage("Attaching file: "
							+ attachFileList[i]);

					// create the second message part
					MimeBodyPart mbp2 = new MimeBodyPart();

					FileDataSource fds = new FileDataSource(attachFileList[i]);
					mbp2.setDataHandler(new DataHandler(fds));
					mbp2.setFileName(fds.getName());
					mp.addBodyPart(mbp2);

					// add the Multipart to the message
					msg.setContent(mp);
				}
			}

			// set the Date: header
			msg.setSentDate(new java.util.Date());

			// send the message
			Transport.send(msg);

		} catch (MessagingException mex) {
			mex.printStackTrace();
			Exception ex = null;
			if ((ex = mex.getNextException()) != null) {
				ex.printStackTrace();
			}
		}
	}
}