package gov.va.med.lom.javaUtils.misc;

import java.io.*;
import java.net.*;

/*
 * Provides an interface to LPD print servers running on network hosts
 * and network connected printers.  Conforms to RFC 1179.
 */

public class Printer {
  
  private boolean printRaw = true;
  private boolean useOutOfBoundsPorts = false;
  private static int jobNumber = 0;
  
  /*
   * By default prints all files as raw binary data.
   * Set this value to false to use the text formatting of the spooler on the host.
   */
  public void setPrintRaw(boolean printRawData) {
    printRaw = printRawData;
  }
  
  public boolean getPrintRaw() {
    return(printRaw);
  }
  
  /*
   * The RFC for lpr specifies the use of local ports numbered 721 - 731, however
   * TCP/IP also requires that any port that is used will not be released for 3 minutes
   * which means that it get stuck on the 12th job if prints are sent quickly.
   *
   * To resolve this issue you can use out of bounds ports which most print servers
   * will support
   *
   * The default for this is off
   */
  public void setUseOutOfBoundPorts(boolean OutOfBoundsPorts) {
    useOutOfBoundsPorts = OutOfBoundsPorts;
  }
  
  public boolean getUseOutOfBoundPorts() {
    return(useOutOfBoundsPorts);
  }
  
  private Socket getSocket(String hostName) 
        throws IOException, InterruptedException {
    if (useOutOfBoundsPorts) {
      return(new Socket(hostName, 515));
    }
    else {
      Socket tmpSocket = null;
      for(int j = 0; (j < 30) && (tmpSocket == null); j++) {
        for (int i = 721; (i <= 731) && (tmpSocket == null); i++) {
          try {
            tmpSocket = new Socket(hostName, 515, InetAddress.getLocalHost(), i);
          }
          catch (BindException be) {
          }
        }
        if (tmpSocket == null) {
          Thread.sleep(10000);
        }

      }
      if (tmpSocket == null) {
        throw new BindException("Can't bind to local port/address");
      }
      return(tmpSocket);
    }
  }
  
  /*
   * Print a file to a network host or printer
   */
  
  public void printFile(String fileName, String hostName, String printerName)  throws IOException, InterruptedException {
    printFile(fileName, hostName, printerName, fileName);
  }
  
  /*
   * Print a file to a network host or printer
   * fileName The path to the file to be printed
   * hostName The host name or IP address of the print server
   * printerName The name of the remote queue or the port on the print server
   * documentName The name of the document as displayed in the spooler of the host
   */
  public void printStream(ByteArrayOutputStream stream, String hostName, String printerName, String documentName)
      throws IOException, InterruptedException {  
    String controlFile = "";
    byte buffer[] = new byte[1000];
    String s;
    String strJobNumber;
    
    //Job number cycles from 001 to 999
    if (++jobNumber >= 1000) {
      jobNumber = 1;
    }
    strJobNumber = "" + jobNumber;
    while (strJobNumber.length() < 3) {
      strJobNumber = "0" + strJobNumber;
    }
    
    String userName = System.getProperty("user.name");
    if (userName == null) {
      userName = "Unknown";
    }
    
    Socket socketLpr = getSocket(hostName);
    socketLpr.setSoTimeout(30000);
    OutputStream sOut = socketLpr.getOutputStream();
    InputStream sIn = socketLpr.getInputStream();
 
    //Open printer
    s = "\002" + printerName + "\n";
    sOut.write(s.getBytes());
    sOut.flush();
    acknowledge(sIn, "Failed to open printer");

    //Send control file   
    controlFile += "H" + hostName + "\n";
    controlFile += "P" + userName + "\n";
    controlFile += ((printRaw) ? "o":"p") +"dfA" + strJobNumber + hostName + "\n";
    controlFile += "UdfA" + strJobNumber + hostName + "\n";
    controlFile += "N" + documentName + "\n";
    
    s = "\002" + (controlFile.length()) + " cfA" + strJobNumber + hostName + "\n";
    sOut.write(s.getBytes());
    
    acknowledge(sIn, "Failed to send control header");
    
    buffer = controlFile.getBytes();
    sOut.write(buffer);
    buffer[0] = 0;
    sOut.write(buffer, 0, 1);
    sOut.flush();
    
    acknowledge(sIn, "Failed to send control file");
    
    s = "\003" + (stream.size()) + " dfA" + strJobNumber + hostName + "\n";
    sOut.write(s.getBytes());
    sOut.flush();
    acknowledge(sIn, "Failed to send print file command");
    
    stream.writeTo(sOut);
    sOut.flush();
    
    buffer[0] = 0;
    sOut.write(buffer,0,1);
    sOut.flush();
    acknowledge(sIn, "Failed to send print file");

    socketLpr.close();    
  }
  
  /*
   * Print a file to a network host or printer
   * fileName The path to the file to be printed
   * hostName The host name or IP address of the print server
   * printerName The name of the remote queue or the port on the print server
   * documentName The name of the document as displayed in the spooler of the host
   */
  public void printFile(String fileName, String hostName, String printerName, String documentName)
      throws IOException, InterruptedException {
    File f = null;
    byte buffer[] = new byte[1000];
    
    //Send print file   
    f = new File(fileName);
    if (!(f.exists() && f.isFile() && f.canRead())) {
      throw new IOException("Error opening print file");
    }
    
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    FileInputStream fs = new FileInputStream(f);
    int readCounter;
    do {
      readCounter = fs.read(buffer);
      if (readCounter > 0)
        output.write(buffer, 0, readCounter);
    } while (readCounter > 0);

    printStream(output, hostName, printerName, documentName);
  }
  
  private void acknowledge(InputStream in, String alert) throws IOException {
    if (in.read() != 0) {
      throw new IOException(alert);
    }
  }
  
  public static void main(String args[]) {
    try {
      if (args.length != 3) {
        System.out.println("Useage: Printer HostName PrinterName PrintFile");
        return;
      }
      Printer myLpr = new Printer();
      myLpr.setPrintRaw(true);
      myLpr.setUseOutOfBoundPorts(true);
      myLpr.printFile(args[2], args[0], args[1]);
      System.out.println("Printed");
    }
    catch (Exception e) {
      System.out.println(e);
    }
  }
  
}