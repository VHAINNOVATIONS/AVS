/*
 * TestPatientPhotoRpc.java
 * 
 * Author: Rob Durkin (rob.durkin@va.gov)
 * Version 1.0 (10/25/2010)
 *  
 * Tests Patient Photo RPCs
 *
 * Usage: java TestPatientPhotoRpc AUTH_PROPS NETWORK_PROPS
 * where AUTH_PROPS is the name of a properties file containing VistA sign-on info.
 * 
 * Required Libraries:
 *   javaBroker.jar 
 *   
 */

import java.io.*;
import java.util.ResourceBundle;
import java.util.ArrayList;

import gov.va.med.lom.javaBroker.rpc.*;
import gov.va.med.lom.javaBroker.rpc.patient.*;
import gov.va.med.lom.javaBroker.rpc.patient.models.*;
import gov.va.med.lom.javaBroker.rpc.user.*;
import gov.va.med.lom.javaBroker.rpc.user.models.*;
import gov.va.med.lom.javaBroker.rpc.BrokerException;
import gov.va.med.lom.javaBroker.rpc.RpcBroker;

import gov.va.med.lom.javaBroker.util.PassphraseCrypto;
import gov.va.med.lom.javaBroker.util.Console;
import gov.va.med.lom.javaBroker.util.DateUtils;
import gov.va.med.lom.javaBroker.util.StringUtils;

public class TestPatientPhotoRpc {
  
  static final String KEY = "snTLze>u\\Nd4W^_|{_N,P\\j-)K!<9oFKErc9kZ?U\"@*PT\".!+h}5t]ICbOK^&Oh";
  
  public static void main(String[] args) throws Exception {
    // If user didn't pass the auth properties file, then print usage and exit
    String server = null;
    int port = 0;
    String access = null;
    String verify = null;
    if (args.length != 2) {
      System.out.println("Usage: java TestVistaImagingRpcs AUTH_PROPS");
      System.out.println("where AUTH_PROPS is the name of a properties file containing VistA sign-on info.");
      System.exit(1);
    } else {
      ResourceBundle res = ResourceBundle.getBundle(args[0]);
      server = res.getString("server");
      port = Integer.valueOf(res.getString("port")).intValue();
      access = res.getString("accessCode");
      verify = res.getString("verifyCode");
    }  
    
    try {
      // Call the static signon method and get an instance of the vista signon rpc
      VistaSignonRpc vistaSignonRpc = ExampleVistaSignonRpc.doVistaSignon(server, port, access, verify);
      // Get the vista signon result and check if signon was successful
      VistaSignonResult vistaSignonResult = vistaSignonRpc.getVistaSignonResult(); 
      if (vistaSignonResult.getSignonSucceeded()) {
        
        RpcBroker rpcBroker = vistaSignonRpc.getRpcBroker();
        
        String patientDfn = Console.readLine("Enter patient DFN: ");
        
        // Get network params from resource bundle
        ResourceBundle networkBundle = ResourceBundle.getBundle(args[1]);
        String localPath = networkBundle.getString("network." + rpcBroker.getStationNo() + ".localpath");
        String smbUsername = networkBundle.getString("network." + rpcBroker.getStationNo() + ".username");
        String smbPassword = networkBundle.getString("network." + rpcBroker.getStationNo() + ".password");
        smbUsername = PassphraseCrypto.decrypt(smbUsername, KEY);
        smbPassword = PassphraseCrypto.decrypt(smbPassword, KEY);
        
        // Call "MAGG PAT PHOTOS" to retrieve a list of all patient photos on file in the Image File. 
        rpcBroker.createContext("MAG WINDOWS");
        String x = rpcBroker.call("MAGG PAT PHOTOS", patientDfn);
        /* Returns 1^count of photos returned OR 0^error message
           If there are photos then each entry in list is 
            $P(1^2^3)     IEN^Image FullPath and name^Abstract FullPath and Name
            $P(4)           SHORT DESCRIPTION field and desc of offline JukeBox
            $P(5)           PROCEDURE/ EXAM DATE/TIME field
            $P(6)           OBJECT TYPE
            $P(7)           PROCEDURE field
            $P(8)          display date
            $P(9)          to return the PARENT DATA FILE image pointer
            $p(10)        return the ABSTYPE  'M' magnetic 'W' worm  'O' offline
            $p(11)        is  'A' accessable   'O' offline
            $p(12^13)  Dicom Series Number  $p(12) and   Image Number  $p(13)
            $p(14        is count of images in group, 1 if single image.
        */
        ArrayList<String> list = StringUtils.getArrayList(x);
        int index = 0;
        for (String s : list) {
          if (index == 0) {
            int numPhotos = 0;
            String result = StringUtils.piece(s, 1);
            System.out.println("Success? " + StringUtils.strToBool(result, "1"));
            // Check for error
            if (result.equals("0")) {
              System.out.println("Error: " + StringUtils.piece(s, 2));
              break;
            } else {
              numPhotos = Integer.valueOf(StringUtils.piece(s, 2));
              System.out.println("# Photos = " + numPhotos);
            }
          // Print parsed results
          } else {
            System.out.println("\nImage #" + index
                );
            System.out.println("IEN = " + StringUtils.piece(s, 2));
            String remotePath = StringUtils.piece(s, 3);
            System.out.println("Image Full Path and Name = " + remotePath);
            System.out.println("Abstract Full Path and Name  = " + StringUtils.piece(s, 4));
            System.out.println("Short Description = " + StringUtils.piece(s, 5));
            System.out.println("Procedure/Exam Date/Time = " + DateUtils.fmDateTimeToAnsiDateTime(Double.valueOf(StringUtils.piece(s, 6))));
            System.out.println("Object Type = " + StringUtils.piece(s, 7));
            System.out.println("Procedure = " + StringUtils.piece(s, 8));
            System.out.println("Display Date = " + StringUtils.piece(s, 9));
            System.out.println("Parent Date File pointer = " + StringUtils.piece(s, 10));
            System.out.println("ABSTYPE = " + StringUtils.piece(s, 11));
            System.out.println("Accessability = " + StringUtils.piece(s, 12));
            System.out.println("DICOM Series Number = " + StringUtils.piece(s, 13));
            System.out.println("Image Number = " + StringUtils.piece(s, 14));
            System.out.println("# Images in Group = " + StringUtils.piece(s, 15));
            
            // Copy file from network folder to local folder
            copyNetworkFile(remotePath, localPath, smbUsername, smbPassword);
          }
          index++;
        }
        
        /*
           4. Save the image to the file path and name given by the server
         */
        
        
      } else {
        System.out.println(vistaSignonResult.getMessage());
      }    
      // Close the connection to the broker
      vistaSignonRpc.disconnect();
    } catch(BrokerException be) {
      System.err.println(be.getMessage());
      System.err.println("Action: " + be.getAction());
      System.err.println("Code: " + be.getCode());
      System.err.println("Mnemonic: " + be.getMnemonic());
    }      
  }
  
  public static String copyNetworkFile(String remotePath, String localPath, 
                                       String smbUsername, String smbPassword) throws Exception {
    String localFilename = null;
    if (!remotePath.startsWith("\\\\")) {
      // Local path
      InputStream in = null;
      OutputStream out = null;
      try {
        File remoteFile = new File(remotePath);
        File localFile = new File(localPath + File.separatorChar + remoteFile.getName());
        localFilename = localFile.getCanonicalPath();
        in = new FileInputStream(remoteFile);
        out = new FileOutputStream(localFile);
        byte[] buf = new byte[1024];
        int len, totBytes = 0;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
          totBytes += len;
        }
        System.out.println("Copied " + totBytes + " bytes.");
      } finally {
        try {
          in.close();
          out.close();
        } catch(IOException ioe2) {}
      }
    } else {
   /*
      // SMB path        
      jcifs.smb.SmbFile smbFile = null;
      jcifs.smb.SmbFileInputStream in = null;
      java.io.FileOutputStream out = null;
      try {
        System.setProperty("jcifs.smb.client.username", smbUsername);
        System.setProperty("jcifs.smb.client.password", smbPassword);
        // Convert path to a SMB path
        String smbPath = "smb:" + StringUtils.replaceChar(remotePath, '\\', '/');
        // Copy file from remote SMB server to local file system
        smbFile = new jcifs.smb.SmbFile(smbPath);
        localFilename = localPath + java.io.File.separatorChar + smbFile.getName();
        in = new jcifs.smb.SmbFileInputStream(smbFile);
        out = new java.io.FileOutputStream(localFilename);
        byte[] b = new byte[8192];
        int n, totBytes = 0;
        while ((n = in.read(b)) > 0) {
          out.write(b, 0, n);
          totBytes += n;
        }
        System.out.println("Copied " + totBytes + " bytes.");
      } finally {
        try {
          in.close();
          out.close();
        } catch(IOException ioe) {}
      }
      */
    }
    return localFilename;
  }
  
  
  
}