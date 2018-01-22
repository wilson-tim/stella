package uk.co.firstchoice.common.base.mail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Contains static methods to convert various types of sources into string
 * arrays.
 * <p>
 * This enables different types of source to be returned as correctly formatted
 * parameters for sending e-mails.
 */
public class GetArrays {

    /**
     * Converts each record in a file into an element in the array returned.
     * 
     * @param filename
     *            Full name of file to be converted (must include path).
     * @param debug
     *            <CODE>false</CODE> sets debug off. <CODE>true</CODE> turns
     *            debug on.
     * @return An array where each element contains one record from the file.
     */
    public static String[] convertFileToStringArray(String filename,
            boolean debug)
    /*
     * This method reads records from a file and returns a String array with
     * each element containing a record from the file.
     * 
     * Author: A.James Created: 12/02/03
     */
    {
        Debug db = new Debug(debug);

        int reccnt = 0;
        try {
            FileReader file = new FileReader(filename);
            BufferedReader buff = new BufferedReader(file);
            boolean eof = false;
            while (!eof) {
                String fileRecord = buff.readLine();
                if (fileRecord == null) {
                    eof = true;
                } else {
                    ++reccnt;
                }
            }
            buff.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        db.handleDebugMessage("No. records: " + reccnt);

        String[] returnList;

        if (reccnt > 0) {
            returnList = new String[reccnt];
            reccnt = 0;
            try {
                FileReader file = new FileReader(filename);
                BufferedReader buff = new BufferedReader(file);
                boolean eof = false;
                while (!eof) {
                    String fileRecord = buff.readLine();
                    if (fileRecord == null) {
                        eof = true;
                    } else {
                        returnList[reccnt] = fileRecord;
                        ++reccnt;
                    }
                }
                buff.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            returnList = new String[0];
        }

        return returnList;
    }

    /**
     * Splits the input list into elements in the array returned.
     * <p>
     * The elements in the list will be separated by a specific character or
     * characters.
     * 
     * @param convertString
     *            List to be split into elements.
     * @param searchStr
     *            Character or characters used to separate each element in the
     *            list (most likely to be a comma).
     * @param debug
     *            <CODE>false</CODE> sets debug off. <CODE>true</CODE> turns
     *            debug on.
     * @return An array where each element contains one element from the list.
     */
    public static String[] getStringArray(String convertString,
            String searchStr, boolean debug) {
        /*
         * This method takes an input string and returns it split into a String
         * array.
         * 
         * Author: A.James Created: 12/02/03
         */

        Debug db = new Debug(debug);

        StringTokenizer st = new StringTokenizer(convertString, searchStr);

        int noTokens = st.countTokens();

        db.handleDebugMessage("001- " + "Number of tokens (" + searchStr
                + ") found: " + noTokens);

        // Set up the number of elements in the String array to be returned.

        String[] returnStrArray = new String[noTokens];

        /*
         * Split the input string into segments (determined by occurrence of
         * search string) and move each segment into an element in the array to
         * be returned.
         */

        for (int i = 0; i < noTokens; i++) {
            returnStrArray[i] = st.nextToken();
            db.handleDebugMessage("002- " + "Value for element " + i + ": "
                    + returnStrArray[i]);

        }

        return returnStrArray;

    }

}