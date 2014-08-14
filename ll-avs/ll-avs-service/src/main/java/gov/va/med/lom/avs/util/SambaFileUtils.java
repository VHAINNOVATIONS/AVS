package gov.va.med.lom.avs.util;

import gov.va.med.lom.javaUtils.misc.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SambaFileUtils {

	private static final Log log = LogFactory.getLog(SambaFileUtils.class);
	
	public static String copyNetworkFile(String localPath, String remotePath,  
			                                 String domain, String smbUsername, String smbPassword) {
	  
		String remoteFilename = null;
		File localFile = new File(localPath);
		if (!remotePath.startsWith("\\\\")) {
			// Local path
			InputStream in = null;
			OutputStream out = null;
			try {
				File remoteFile = new File(localPath + File.separatorChar + localFile.getName());
				remoteFilename = remoteFile.getCanonicalPath();
				in = new FileInputStream(localFile);
				out = new FileOutputStream(remoteFile);
				byte[] buf = new byte[1024];
				int len, totBytes = 0;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
					totBytes += len;
				}
				log.info("Copied " + totBytes + " bytes.");
			} catch (Exception e){
				e.printStackTrace();
			} finally {
				try {
					in.close();
					out.close();
				} catch(IOException ioe2) {}
			}
		} else {
			// SMB path        
			jcifs.smb.SmbFile smbFile = null;
			jcifs.smb.SmbFileOutputStream out = null;
			java.io.FileInputStream in = null;
			try {
			  jcifs.smb.NtlmPasswordAuthentication auth = 
	          new jcifs.smb.NtlmPasswordAuthentication(domain, smbUsername, smbPassword);
			  if (!remotePath.endsWith("\\")) {
			    remotePath = remotePath + "\\";
			  }
				// Convert path to a SMB path
				String smbPath = "smb:" + StringUtils.replaceChar(remotePath, '\\', '/') + localFile.getName();
				// Copy file from local file system to SMB server
				smbFile = new jcifs.smb.SmbFile(smbPath, auth);
				remoteFilename = remotePath + smbFile.getName();
				in = new java.io.FileInputStream(localPath);
				out = new jcifs.smb.SmbFileOutputStream(smbFile);
				
				byte[] b = new byte[8192];
				int n, totBytes = 0;
				while ((n = in.read(b)) > 0) {
					out.write(b, 0, n);
					totBytes += n;
				}
				log.info("Copied " + totBytes + " bytes.");
			} catch (Exception e){
				e.printStackTrace();
			} finally {
				try {
					in.close();
					out.close();
				} catch(IOException ioe) {}
			}
		}
		return remoteFilename;
	}
	
}
