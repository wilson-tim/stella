package uk.co.firstchoice.common.base.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Collection of File-related utilities.
 * 
 * <p>.
 */
public class FileUtility {

	protected FileUtility() {
	}

	/**
	 * Convenience one-liner to save data to a specific file on Disk. Will abend
	 * if file already exists.
	 * 
	 * @param file
	 *            name
	 * @param contents
	 */
	public static void saveFileToDisk(String fileName, String contents)
			throws IOException {
		FileUtility.saveFileToDisk(new File(fileName), contents);
	}

	/**
	 * Convenience one-liner to save data to a specific file on Disk. Will abend
	 * if file already exists.
	 * 
	 * @param file
	 * @param contents
	 */
	public static void saveFileToDisk(File file, String contents)
			throws IOException {
		if (file.exists())
			throw new IOException("File already exists.");
		if (!file.createNewFile())
			throw new IOException("File Can't be created.");

		FileWriter writer = new FileWriter(file);
		writer.write(contents);

		writer.close();
	}

	/**
	 * Convenience one-liner to save data to a specific file on Disk. Will abend
	 * if file already exists.
	 * 
	 * @param file
	 *            name
	 * @param contents
	 */
	public static void saveFileToDisk(String fileName, byte[] contents)
			throws IOException {
		FileUtility.saveFileToDisk(new File(fileName), contents);
	}

	/**
	 * Convenience one-liner to save data to a specific file on Disk. Will abend
	 * if file already exists.
	 * 
	 * @param file
	 * @param contents
	 */
	public static void saveFileToDisk(File file, byte[] contents)
			throws IOException {
		if (file.exists())
			throw new IOException("File already exists.");
		if (!file.createNewFile())
			throw new IOException("File Can't be created.");

		FileOutputStream stream = new FileOutputStream(file);
		stream.write(contents);

		stream.close();
	}

	/**
	 * Convenience one-liner to create a directory if the file doesn't exist.
	 * Will validate that the file is a directory if it does exist. Will throw
	 * an exception of the file exists and s not a directory.
	 * 
	 * @param Directory
	 *            Name
	 */
	public static void createDirectory(String dirName) throws IOException {
		File dir = null;

		dir = new File(dirName);
		if (dir.exists()) {
			if (!dir.isDirectory())
				throw new IOException(
						"File already exists and isn't a directory.");
		} else
			dir.mkdirs();
	}

	/**
	 * Convenience one-liner to purge a directory including all files and
	 * subdirectories -- Use With Extreme Caution!
	 */
	public static void purgeDirectory(File dir) {
		File[] fileSystemEntry = dir.listFiles();
		for (int i = 0; fileSystemEntry != null && i < fileSystemEntry.length; i++) {
			if (fileSystemEntry[i].isDirectory())
				FileUtility.purgeDirectory(fileSystemEntry[i]);
			fileSystemEntry[i].delete();
		}
	}

	/**
	 * Convenience one-liner to purge a directory including all files and
	 * subdirectories -- Use With Extreme Caution!
	 * 
	 * @param Directory
	 *            Name
	 */
	public static void purgeDirectory(String dirName) {
		File dir = new File(dirName);
		FileUtility.purgeDirectory(dir);
	}

	/**
	 * Convenience one-liner to purge underlying files older than a specified
	 * age -- Use With Extreme Caution!
	 * 
	 * @param Directory
	 *            Name
	 * @param Earliest
	 *            Date Permitted
	 */
	public static void purgeUnderlyingFiles(String dirName,
			GregorianCalendar updatedBeforeDate) {
		FileUtility.purgeUnderlyingFiles(dirName, updatedBeforeDate.getTime()
				.getTime());
	}

	/**
	 * Convenience one-liner to purge underlying files older than a specified
	 * age -- Use With Extreme Caution!
	 * 
	 * @param Directory
	 *            Name
	 * @param Earliest
	 *            Date Permitted
	 */
	public static void purgeUnderlyingFiles(String dirName,
			Date updatedBeforeDate) {
		FileUtility.purgeUnderlyingFiles(dirName, updatedBeforeDate.getTime());
	}

	/**
	 * Convenience one-liner to purge underlying files older than a specified
	 * age -- Use With Extreme Caution!
	 * 
	 * @param Directory
	 *            Name
	 * @param Earliest
	 *            Date Permitted
	 */
	public static void purgeUnderlyingFiles(String dirName,
			long updatedBeforeTime) {
		File dir = new File(dirName);
		FileUtility.purgeUnderlyingFiles(dir, updatedBeforeTime);
	}

	/**
	 * Convenience one-liner to purge underlying files older than a specified
	 * age -- Use With Extreme Caution!
	 * 
	 * @param Directory
	 * @param Earliest
	 *            Date Permitted in millis
	 */
	public static void purgeUnderlyingFiles(File dir, long updatedBeforeTime) {
		if (dir.isDirectory()) {
			File[] fileSystemEntry = dir.listFiles();
			for (int i = 0; fileSystemEntry != null
					&& i < fileSystemEntry.length; i++) {
				if (fileSystemEntry[i].isDirectory())
					FileUtility.purgeUnderlyingFiles(fileSystemEntry[i],
							updatedBeforeTime);
				else if (fileSystemEntry[i].lastModified() < updatedBeforeTime)
					fileSystemEntry[i].delete();
			}
		} else if (dir.lastModified() < updatedBeforeTime)
			dir.delete();
	}

	/**
	 * Convenience reference for the "file.separator" property from
	 * System.getProperties().
	 */
	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");
}