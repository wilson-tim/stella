/**
 * This class will create a zip of any file The pkunzip on DOS winzip on WINDOWS
 * unzip on UNIX can be used to extract the file
 * 
 * Note : for dos rename the zip file to 8 digit filename and the try to extract
 * otherwise CAN'T FIND FILENAME will be displayed by pkunzip Creation date:
 * (01/07/02 11:05:07 AM) Contact Information: FirstChoice
 * 
 * @version 1.0
 * @author: Dyanesh Badve
 */

package uk.co.firstchoice.common.base.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {

    static final int BUFFER = 2048;

    /**
     * This method set the copys reads and writes with a specific buffer size
     * 
     * @param InputStream
     * @param OutputStream
     * @throws IOException
     */
    /*
     * public static final void copyInputStream(InputStream in, OutputStream
     * out) throws IOException { byte[] buffer = new byte[1024]; int len;
     * 
     * while((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
     * 
     * in.close(); out.close(); }
     */
    public Zip(String filename, String zipfilename) throws Exception {
        String[] filenamelist = new String[0];
        filenamelist[0] = filename;

        try {
            zipfile(filenamelist, zipfilename);
        } catch (Exception e) {
            throw new Exception("" + e);
        }
    }

    /**
     * constructor method for zip
     * 
     * @param filename
     *            java.lang.String
     */

    public Zip(String[] filenamelist, String zipfilename) throws Exception {
        try {
            zipfile(filenamelist, zipfilename);
        } catch (Exception e) {
            throw new Exception("" + e);
        }
    }

    public void zipfile(String[] filenamelist, String zipfilename)
            throws Exception {
        try {
            BufferedInputStream origin = null;

            String fn = "";

            FileOutputStream dest = new FileOutputStream(zipfilename);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));

            byte data[] = new byte[BUFFER];

            for (int i = 0; i < filenamelist.length; i++) {

                FileInputStream fi = new FileInputStream(filenamelist[i]);

                origin = new BufferedInputStream(fi, BUFFER);

                //ZipEntry entry = new ZipEntry(filenamelist[i]);
                ZipEntry entry = new ZipEntry(getFileName(filenamelist[i]));

                out.putNextEntry(entry);

                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();

        } catch (Exception e) {

            e.printStackTrace();
            throw new Exception("" + e);

        }

    }

    public static String removeExtension(String fn) {
        int dot = fn.indexOf('.');
        if (dot > 0) {
            fn = fn.substring(0, dot);
        }
        return fn;
    }

    // get the filename which includes path
    public static String getFileName(String fullfilename) {
        File file = new File(fullfilename);
        return file.getName();

    }

    /**
     * This main method which could have 2 argument
     * 
     * @return none
     * @param argv
     *            java.lang.String[]
     */
    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Usage: filename");
            return;
        }
        try {
            Zip z = new Zip(argv[0], argv[1]);
        } catch (Exception e) {
            System.out.println("Exception occured while compressing the file"
                    + e);
        }
    }

}

/**
 * ****************************************** END
 * ***************************************
 */