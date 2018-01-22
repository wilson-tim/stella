package uk.co.firstchoice.common.base.mail;

/**
 * Java application for sending e-mails using lists.
 * 
 * @author A.James
 * @since 1.2
 */
public class SendMailUsingLists {

    /**
     * Sends e-mails using lists.
     * 
     * <p>
     * </p>
     * <b>Input Parameters </b>
     * <ol>
     * <li>Mail host to be used for distributing e-mails.</li>
     * <li>Character or characters used to separate elements in the lists.
     * </li>
     * <li>List of mail recipients.</li>
     * <li>Sender of e-mail. <br>
     * N.B. Can be only one sender.</li>
     * <li>Subject of e-mail.</li>
     * <li>Name of file (full path) containing message text.</li>
     * <li>List of files to be attached to the e-mail. Can be an empty list.
     * </li>
     * <li>Debug mode. A value of <CODE>false</CODE> sets debug off and so no
     * debug messages will be produced. A value of <CODE>true</CODE> turns
     * debug on and will produce debug messages.</li>
     * </ol>
     * 
     * @param args
     *            Parameters passed.
     */
    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            System.out.println("Value for argument " + i + ": " + args[i]);
        }

        if (args.length != 8) {
            System.out
                    .println("usage: java sendMail <mailhost> <list separator> <to list> <from> <subject> <mail message(filename)> <attached files list> true|false");
            System.exit(1);
        }

        String host = args[0];
        String listSeparator = args[1];

        boolean debug = Boolean.valueOf(args[7]).booleanValue();

        String[] toList = GetArrays.getStringArray(args[2], listSeparator,
                debug);

        String from = args[3];
        String subject = args[4];
        String mailmsgFilename = args[5];

        String[] fileList = GetArrays.getStringArray(args[6], listSeparator,
                debug);

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