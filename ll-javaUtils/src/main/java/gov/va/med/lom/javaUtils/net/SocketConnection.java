/*
 * Created on Sep 5, 2004
 */
package gov.va.med.lom.javaUtils.net;

import java.net.*;
import java.io.*;

/**
 * @author Robert M. Durkin
 */
public class SocketConnection implements Cloneable {

  private InetAddress addr = null;
  private String host = null;
  private int port = 0;
  private Socket clientSocket = null;
  private PrintWriter out = null;
  private BufferedReader in = null;
  private IOException lastException;
  private ISocketEventsCallback callback;

  public SocketConnection() {
   // 
  }
  
  public SocketConnection(String host, int port) {
    this.host = host;
    this.port = port;
  }
  
  public SocketConnection(Socket socket) throws IOException {
  	this.clientSocket = socket;
  	out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);      
  	in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
  }
  
  public boolean connect() throws IOException {
    try {
  	  // Get the host address
  	  addr = InetAddress.getByName(host);
  	  // Create a socket and writer
  	  clientSocket = new Socket(addr, port);
  	  clientSocket.setKeepAlive(true);
  	  out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())), true);      
  	  in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      if (callback != null)
        callback.onSocketConnect();
      return isConnected();
    } catch(IOException ioe) {
      lastException = ioe;
      if (callback != null)
        callback.onSocketException(ioe);
      throw ioe;
    }
  }
  
  public void setSocketEventsCallback(ISocketEventsCallback callback) {
    this.callback = callback;
  }
  
  public Socket getSocket() {
  	return clientSocket;
  }
  
  public String getLocalHostAddress() {
  	return clientSocket.getLocalAddress().getHostAddress();
  }  
  
  public String getLocalHostName() {
  	return clientSocket.getLocalAddress().getCanonicalHostName();
  }
  
  public int getLocalPort() {
  	return clientSocket.getLocalPort();
  }

  public String getRemoteHostAddress() {
  	return clientSocket.getInetAddress().getHostAddress();
  }  
  
  public String getRemoteHostName() {
  	return clientSocket.getInetAddress().getCanonicalHostName();
  }
  
  public int getRemotePort() {
  	return clientSocket.getPort();
  }
  
  public void setSocketOptionLinger(boolean enabled, int linger) {
    try {
      clientSocket.setSoLinger(enabled, linger);
    } catch(SocketException so) {}
  }
  
  public void print(boolean b) throws IOException {
    out.print(b);
    checkWriterError();
  }

  public void print(char c) throws IOException {
    out.print(c);
    checkWriterError();
  }

  public void print(char[] s) throws IOException {
    out.print(s);
    checkWriterError();
  }

  public void print(double d) throws IOException {
    out.print(d);
    checkWriterError();
  }

  public void print(float f) throws IOException {
    out.print(f);
    checkWriterError();
  }

  public void print(int i) throws IOException {
    out.print(i);
    checkWriterError();
  }

  public void print(long l) throws IOException {
    out.print(l);
    checkWriterError();
  }

  public void print(Object o) throws IOException {
    out.print(o);
    checkWriterError();
  }

  public void print(String s) throws IOException {
    out.print(s);
    checkWriterError();
  }

  public void println() throws IOException {
    out.println();
    checkWriterError();
  }

  public void println(boolean x) throws IOException {
    out.println(x);
    checkWriterError();
  }

  public void println(char x) throws IOException {
    out.println(x);
    checkWriterError();
  }

  public void println(char[] x) throws IOException {
    out.println(x);
    checkWriterError();
  }

  public void println(double x) throws IOException {
    out.println(x);
    checkWriterError();
  }

  public void println(float x) throws IOException {
    out.println(x);
    checkWriterError();
  }

  public void println(int x) throws IOException {
    out.println(x);
    checkWriterError();
  }

  public void println(long x) throws IOException {
    out.println(x);
    checkWriterError();
  }

  public void println(Object x) throws IOException {
    out.println(x);
    checkWriterError();
  }

  public void println(String x) throws IOException {
    out.println(x);
    checkWriterError();
  }

  public void write(char[] buf) throws IOException {
    out.write(buf);
    checkWriterError();
  }

  public void write(char[] buf, int off, int len) throws IOException {
    out.write(buf, off, len);
    checkWriterError();
  }

  public void write(int c) throws IOException {
    out.write(c);
    checkWriterError();
  }

  public void write(String s) throws IOException {
    out.write(s);
    checkWriterError();
  }  

  public void write(String s, int off, int len) throws IOException {
    out.write(s, off, len);
    checkWriterError();
  }  

  public int read() throws IOException {
    try {
      return in.read();
    } catch(IOException ioe) {
      lastException = ioe;
      throw ioe;
    }
  } 

  public int read(char[] cbuf, int off, int len) throws IOException {
    try {
      return in.read(cbuf, off, len);
    } catch(IOException ioe) {
      lastException = ioe;
      throw ioe;
    }      
  } 

  public String readLine() throws IOException {
    try {
      return in.readLine();
    } catch(IOException ioe) {
      lastException = ioe;
      throw ioe;
    }    
  } 

  public boolean isConnected() {
    return clientSocket.isConnected();
  }

  public boolean isClosed() {
    return clientSocket.isClosed();
  }

  public boolean close() throws IOException {
    try {
      out.close();
      checkWriterError();
      in.close();
      if (callback != null) {
        if (isClosed())
          callback.onSocketDisconnect();
      }
      return (isClosed());
    } catch(IOException ioe) {
      lastException = ioe;
      if (callback != null)
        callback.onSocketException(ioe);
      throw ioe;
    }      
  }
  
  public InetAddress getAddr() {
    return addr;
  }

  public String getHost() {
    return host;
  }
  
  public IOException getLastException() {
    return lastException;
  }

  public boolean outputError() {
    return out.checkError();
  }
  
  public int getPort() {
    return port;
  }
  
  // Allows this object to be cloned (shallow)
  public Object clone(){
    Object clone = null;
    try{
      clone = super.clone();
    } catch(CloneNotSupportedException e){
      // should never happen
    }
    ((SocketConnection)clone).host = new String(host);
    return clone;
  }
  
  // Private Methods
  private void checkWriterError() throws IOException {
    if (out.checkError()) {
      lastException = new IOException("Writer Error.");
      throw lastException;
    }  
  } 
}
