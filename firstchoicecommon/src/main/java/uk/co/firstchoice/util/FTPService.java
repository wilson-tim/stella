/*
 * FTPManager.java
 *
 * Created on 12 September 2003, 15:05
 */

package uk.co.firstchoice.util;

/**
 * This class facilitates the file transfers and replaces the old FTPService.
 * The implementation is now changed where as the client contract lies the same.
 * Note: There are no changes on to how to use this service. Anyone who's using
 * this service previously should be able to use as it is. But you may have to
 * deploy ftp.jar in your classpath (or app server path).
 * 
 * @author mkonda
 * @created 05 Sept 03
 * @see
 */
import java.io.IOException;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;

public class FTPService {

	// global variable of the FTPClient
	private FTPClient _client = null;

	/**
	 * This constructor needs remote servername, username and passwd. When
	 * initiated it will connect to the server with the credentials supplied.
	 * 
	 * @param remoteServer
	 *            Name of the remote machine where you are FTPing
	 * @param userName
	 *            Username to logon to the remote machine
	 * @param passWord
	 *            Password for the usernam
	 */
	public FTPService(String remoteServer, String userName, String passWord)
			throws IOException, FTPException {
		_client = new FTPClient();
		_client.setRemoteHost(remoteServer);
		_client.connect();
		_client.login(userName, passWord);
	}

	/**
	 * The following two methods download or get the files from the remote
	 * directory. It prints out the results in a String object
	 * 
	 * @param remoteDir
	 *            remote directory Remote directory to which client wishes to
	 *            login
	 * @param remoteFile
	 *            remote file name Remote filename to do a get
	 * @param asc
	 *            transfer type Ascii or Binary type. If true, the transfer type
	 *            is set to Ascii. (Default ascii)
	 */
	public String download(String remoteDir, String remoteFile)
			throws FTPException, IOException {
		return download(remoteDir, remoteFile, true);
	}

	/**
	 * This method downloads or gets the files from the remote directory. It
	 * prints out the results in a String object
	 * 
	 * @param remoteDir
	 *            remote directory Remote directory to which client wishes to
	 *            login
	 * @param remoteFile
	 *            remote file name Remote filename to do a get
	 * @param asc
	 *            transfer type Ascii or Binary type. If true, the transfer type
	 *            is set to Ascii. (Default ascii)
	 * @return String return file
	 */
	public String download(String remoteDir, String remoteFile, boolean asc)
			throws FTPException, IOException {

		byte[] returnFileContents = null;
		if (!remoteDir.equals(""))
			_client.chdir(remoteDir);
		setTransferType(asc);
		returnFileContents = _client.get(remoteFile);
		_client.quit();

		return new String(returnFileContents);
	}

	/**
	 * This method uploads or puts the files into the remote directory. It
	 * prints out the results in a String object
	 * 
	 * @param remoteDir
	 *            remote directory Remote directory to which client wishes to
	 *            login
	 * @param localFile
	 *            local file name Remote filename to do a get
	 * @param what
	 *            file file
	 *  
	 */
	public void upload(String remoteDir, String localFile, String what)
			throws FTPException, IOException {
		upload(remoteDir, localFile, what, true);
	}

	/**
	 * This method uploads or puts the files into the remote directory. It
	 * prints out the results in a String object
	 * 
	 * @param remoteDir
	 *            remote directory Remote directory to which client wishes to
	 *            login
	 * @param localFile
	 *            local file name Remote filename to do a get
	 * @param what
	 *            file file
	 *  
	 */
	public void upload(String remoteDir, String localFile, String what,
			boolean asc) throws FTPException, IOException {

		if (!remoteDir.equals(""))
			_client.dir(remoteDir);

		setTransferType(asc);
		_client.put(remoteDir, localFile);
		_client.quit();
	}

	/**
	 * The following two methods uploads the file to the remote server. Invoke
	 * this method as shown: 1. If the transfer is BINARY:
	 * _service.uploadFile("","data.properties",
	 * "V:\\proj\\java\\classes\\data.properties",false); The last parameter
	 * dictates the transfer mode, in this case if false, transfer is BINARY. 2.
	 * If the transfer is ASCII _service.uploadFile("","data.properties",
	 * "V:\\proj\\java\\classes\\data.properties",true); The only difference is
	 * that of the boolean value.
	 * 
	 * If user wises not to provide Transfer Type, then the transfer type is set
	 * to ASCII. In this case user can omitt the last argument as shown below:
	 * _service.uploadFile("","data.properties",
	 * "V:\\proj\\java\\classes\\data.properties");
	 * 
	 * @param targetPath
	 *            target path
	 * @param fileName
	 *            filename
	 * @param filePath
	 *            filepath
	 * @return void
	 * @exception IOException,FTPException
	 */

	public void uploadFile(String targetPath, String fileName, String filePath)
			throws IOException, FTPException {
		uploadFile(targetPath, fileName, filePath, true);
	}

	public void uploadFile(String targetPath, String fileName, String filePath,
			boolean asc) throws FTPException, IOException {

		// filePath is local path
		// filenam is remote file name

		if (!targetPath.equals(""))
			_client.chdir(targetPath);
		setTransferType(asc);

		_client.put(filePath, fileName);
	}

	/**
	 * This method is a utility method to check the size of the file transfered.
	 */

	public byte[] checkSize(String fileName) throws IOException, FTPException {
		return _client.get(fileName);
		
	}

	/**
	 * This method is a utility method to check the size of the file transfered. returns size
	 */

	public long getFileSize(String fileName) throws IOException, FTPException {
		return _client.size(fileName);
	}
	
	
	
	// private methods

	private void setTransferType(boolean transferType) throws FTPException,
			IOException {
		if (transferType)
			_client.setType(FTPTransferType.ASCII);
		else
			_client.setType(FTPTransferType.BINARY);
	}
}