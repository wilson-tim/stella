package uk.co.firstchoice.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * FileCopy
 * 
 * @author Jyoti Renganathan
 * @version 1.0
 * @return 0 for success 1 for failure
 * @parm inputFileName , outputFileName
 *  
 */
public class FileCopy {

	public static int copy(String inputFileName, String outFileName) {

		int len;
		byte buf[] = new byte[2048];

		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		if (inputFileName == null || outFileName == null) {
			return 1;
		}

		try {
			in = new BufferedInputStream(new FileInputStream(inputFileName)); //Open
																			  // input
																			  // file
			out = new BufferedOutputStream(new FileOutputStream(outFileName)); //#Open
																			   // output
																			   // file

			while ((len = in.read(buf)) >= 0) //Copy the data
			{
				out.write(buf, 0, len);
			}

		} catch (IOException e) {
			System.out.println("ERROR : " + e.getMessage());
			return 1;
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				System.out.println("ERROR : " + e.getMessage());
				return 1;
			}

		} // end of finally
		return 0; // success

	} // end of copy method

} // end of FileCopy class

