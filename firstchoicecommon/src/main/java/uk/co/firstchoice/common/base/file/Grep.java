/* Simple utility to find the string in a file */

package uk.co.firstchoice.common.base.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Grep {

    private String filename = "";

    private String search = "";

    public static void main(String args[]) {

        Grep gp = new Grep();

        if (args.length == 2) {
            gp.setSearch(args[0]);
            gp.setFileName(args[1]);
            System.out.println("Found " + gp.readFileAndSearch());
        } else {
            System.out.println(" Usage java grep <search string> <filename>");
        }
    }

    public void setFileName(String filename) {
        this.filename = filename;
    }

    public String getFileName() {
        return this.filename;
    }

    public void setSearch(String search) {
        this.search = search.toLowerCase();
    }

    public String getSearch() {
        return this.search;
    }

    /*
     * @author Jyoti Renganathan
     * 
     * @version 1.0 @returns true - if finds the string , false - can not find
     *          the specified string in a file
     *  
     */
    public boolean readFileAndSearch() {
        try {

            FileReader fr = new FileReader(filename);
            BufferedReader inFile = new BufferedReader(fr);

            String str;
            while ((str = inFile.readLine()) != null) {
                //System.out.println(str);
                if ((str.toLowerCase()).indexOf(search) != -1) {
                    return true;
                }
            } // end of while

        } catch (Exception ex) {
            System.out.println("Error!" + ex);
        }

        return false;
    }

    /*
     * @author Jyoti Renganathan
     * 
     * @version 1.0 @parm file - full file name including path, Search string
     *          @returns String of all Lines where it finds the passed string
     *          else blank string
     *  
     */

    public static String searchFile(File file, String search) {
        String[] s = new String[0];
        s[0] = search;
        return searchFile(file, s);
    }

    public static String searchFile(File file, String[] search) {

        String errStr = "";
        BufferedReader inFile = null;
        String str;

        try {

            FileReader fr = new FileReader(file);
            inFile = new BufferedReader(fr);

            while ((str = inFile.readLine()) != null) {
                for (int j = 0; j < search.length; j++) {
                    if ((str.toLowerCase()).indexOf(search[j].toLowerCase()) != -1) {
                        errStr += str + "\n";
                        break;
                    }
                } // end of for loop
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error!" + ex);
        }

        return errStr;

    }

} // end of grep
