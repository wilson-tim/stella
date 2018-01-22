package uk.co.firstchoice.util.mail;

/**
 * Java application for sending e-mails using control files.
 * <p>
 * This enables e-mails to be sent where the values for some of the e-mail
 * parameters are held in control files.
 * 
 * @author A.James
 * @since 1.2
 */
public class SendMailUsingControlFiles {

	/**
	 * Sends e-mails using control files.
	 * 
	 * <p>
	 * </p>
	 * <b>Input Parameters </b>
	 * <ol>
	 * <li>Mail host to be used for distributing e-mails.</li>
	 * <li>Name of file (full path) containing one record for each mail
	 * recipient.</li>
	 * <li>Sender of e-mail. <br>
	 * N.B. Can be only one sender.</br></li>
	 * <li>Subject of e-mail.</li>
	 * <li>Name of file (full path) containing message text.</li>
	 * <li>Name of file (full path) containing one record for each file to be
	 * attached. Can be an empty file.</li>
	 * <li>Debug mode. A value of <CODE>false</CODE> sets debug off and so no
	 * debug messages will be produced. A value of <CODE>true</CODE> turns
	 * debug on and will produce debug messages.</li>
	 * </ol>
	 * 
	 * @param args
	 *            Input Parameters.
	 */
	public static void main(String[] args) {

		for (int i = 0; i < args.length; i++) {
			System.out.println("Value for argument " + i + ": " + args[i]);
		}

		if (args.length != 7) {
			System.out
					.println("usage: java sendMail <mailhost> <to list(filename)> <from> <subject> <mail message(filename)> <attach list(filename separated)> true|false");
			System.exit(1);
		}

		String host = args[0];

		boolean debug = Boolean.valueOf(args[6]).booleanValue();

		String[] toList = GetArrays.convertFileToStringArray(args[1], debug);

		String from = args[2];
		String subject = args[3];
		String mailmsgFilename = args[4];

		String[] fileList = GetArrays.convertFileToStringArray(args[5], debug);

		SMTPExtensions sendmail = new SMTPExtensions();
		try {
			sendmail.sendMail(host, toList, from, subject, mailmsgFilename,
					fileList, debug);
		} catch (NoMailRecipientException nmre) {
			System.out.println("No recipient for mail message: "
					+ mailmsgFilename);
			nmre.printStackTrace();
		}

	}

}