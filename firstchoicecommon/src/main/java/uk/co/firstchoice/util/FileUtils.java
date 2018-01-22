package uk.co.firstchoice.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.Comparator;

public class FileUtils {

	public static String fileGetTimeStamp() {
		GregorianCalendar cal = new GregorianCalendar();
		int day = cal.get(GregorianCalendar.DAY_OF_MONTH);
		int month = cal.get(GregorianCalendar.MONTH) + 1;
		int year = cal.get(GregorianCalendar.YEAR) - 2000;
		int hour = cal.get(GregorianCalendar.HOUR_OF_DAY);
		int minute = cal.get(GregorianCalendar.MINUTE);
		String dateStr = (day > 9 ? "" : "0") + day + (month > 9 ? "" : "0")
				+ month + (year > 9 ? "" : "0") + year + "_"
				+ (hour > 9 ? "" : "0") + hour + (minute > 9 ? "" : "0")
				+ minute;
		return dateStr;
	}

	/*
	 * Added by JR on 18/06/03 @author : Jyoti Renganathan
	 * 
	 * @version 1.0 @return date in the form of DDMMYY
	 */

	public static String fileGetDateStamp() {
		GregorianCalendar cal = new GregorianCalendar();
		int day = cal.get(GregorianCalendar.DAY_OF_MONTH);
		int month = cal.get(GregorianCalendar.MONTH) + 1;
		int year = cal.get(GregorianCalendar.YEAR) - 2000;
		String dateStr = (day > 9 ? "" : "0") + day + (month > 9 ? "" : "0")
				+ month + (year > 9 ? "" : "0") + year;
		return dateStr;
	}

	/**
	 * createZipFile creates zip file for the files whih are passed in the
	 * argument inputFiles and creates zip file with the name passed in the
	 * second argument both the arguments should have fully quilified path
	 * otherwise it will fail to find location for the input files
	 * 
	 * @author Jyoti Renganathan
	 * @version 1.0
	 * @return return code 1 for failure, 0 if success
	 * @param 1)
	 *            inPath - full Path for input files 2) inputFiles - list of all
	 *            files(excluding path) 2) zipFileName - name of the zip file
	 *            (include path if needs in particular dir )
	 */

	public static int createZipFile(String inPath, String[] inputFiles,
			String zipFileName) {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipFileName));

			// Compress the files
			for (int i = 0; i < inputFiles.length; i++) {

				FileInputStream in = new FileInputStream(new File(inPath,
						inputFiles[i]));

				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(inputFiles[i]));

				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				// Complete the entry
				out.closeEntry();
				in.close();
			}

			// Complete the ZIP file
			out.close();
			return 0;

		} catch (IOException e) {
			return 1; // error
		}

	} // end of createZipFiles

	/**
	 * unZipFile Unzips a compressed file passed in as an argument. There are
	 * three overloaded classes, the most basic just requires the path of the
	 * zip file and the zipfile name. The middle class requires a directory name
	 * to unzip the files to and the top level class requires a file name to
	 * extract from the zip file. If no unzip directory is passed the files are
	 * unzipped into the same directory as the zip file. If no file name (to
	 * decompress) is passed then all files are extracted
	 * 
	 * @author John Durnford
	 * @version 1.0
	 * @return return code 1 for failure, 0 if success
	 * @param 1)
	 *            inPath - full Path for input files 2) inputFiles - list of all
	 *            files(excluding path) 2) zipFileName - name of the zip file
	 *            (include path if needs in particular dir )
	 */
	public static int unZipFile(String inZipPath, String inZipName,
			String outZipPath, String outZipName) {
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(
					inZipPath + inZipName));
			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry e;
			byte[] b = new byte[512];
			int len = 0;
			boolean unZipFile = false;

			try {
				while ((e = zin.getNextEntry()) != null) {
					unZipFile = ((outZipName.equals("ALL")) || (outZipName
							.equalsIgnoreCase(e.getName())));
					if (unZipFile) {
						FileOutputStream out = new FileOutputStream(outZipPath
								+ e.getName());
						while ((len = zin.read(b)) != -1) {
							out.write(b, 0, len);
						}
						out.close();
					}
				}
				zin.close();
				return 0;
			} catch (IOException ioEx) {
				return 1;
			}
		} catch (FileNotFoundException fnfEx) {
			return 1;
		}
	}

	public static int unZipFile(String inZipPath, String inZipName,
			String outZipPath) {
		int unZipRet = unZipFile(inZipPath, inZipName, outZipPath, "ALL");
		return unZipRet;
	}

	public static int unZipFile(String inZipPath, String inZipName) {
		int unZipRet = unZipFile(inZipPath, inZipName, inZipPath);
		return unZipRet;
	}


  public static String[] dirListByAscendingDate(File folder) {
    if (!folder.isDirectory()) {
      return null;
    }
   // File files[] = folder.listFiles();
     String files[] = folder.list();
    Arrays.sort( files, new Comparator()
    {
      public int compare(final Object o1, final Object o2) {
        return new Long(((File)o1).lastModified()).compareTo
             (new Long(((File) o2).lastModified()));
      }
    });
    return files;
  }


  public static File[] dirListByDescendingDate(File folder) {
    if (!folder.isDirectory()) {
      return null;
    }
    String fileLists[] ;
    File files[] = folder.listFiles();
    Arrays.sort( files, new Comparator()
    {
      public int compare(final Object o1, final Object o2) {
        return new Long(((File)o2).lastModified()).compareTo
             (new Long(((File) o1).lastModified()));
      }
    });

   
    return files;
  }

}