package gov.va.med.lom.javaBroker.rpc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

import gov.va.med.lom.javaBroker.net.ISocketEventsCallback;
import gov.va.med.lom.javaBroker.net.SocketConnection;

public class RpcProtocolV1 extends AbstractRpcProtocol implements ISocketEventsCallback {
  
    // NAMED CONSTANTS
    public static final String XWB_PREFIX = "{XWB}";
  
    // CONSTRUCTOR
    public RpcProtocolV1(int protocolVersion) {
        super(protocolVersion);
    }
    
    // PUBLIC METHODS
    public boolean connect(String server, int port) throws BrokerException {
        boolean connected = false;
        errorMessage = "";
        ServerSocket serverSocket = null;
        try {
            // Create a server socket that will wait for a connection from the VistA server
            serverSocket = new ServerSocket(0);
            // If it doesn't already exist, create a socket connection to the VistA server
            if (socket == null) {
                socket = new SocketConnection(server, port);
                socket.setSocketEventsCallback(this);
            }
            if (socket.connect()) {
                // Turn off socket option to linger
                socket.setSocketOptionLinger(false, 0);
                // Send a message to VistA containing callback info (host address and port)
                StringBuffer sb = new StringBuffer();
                sb.append("TCPconnect^");
                sb.append(socket.getLocalHostAddress() + "^");
                sb.append(String.valueOf(serverSocket.getLocalPort()) + "^");
                sb.append(socket.getLocalHostName() + "^");
                String rpcVer = varPack(getRpcVersion());
                String x = "00000" + String.valueOf(sb.length() + rpcVer.length() + 5);
                String out = new String(x.substring(x.length() - 5, x.length()) +
                                        rpcVer + strPack(sb.toString(), 5));
                StringBuffer result = new StringBuffer();
                String message = XWB_PREFIX + out;
                socket.print(message);
                char[] cbuf = new char[1024];
                int numBytes = 0;
                while (true) {
                    numBytes = socket.read(cbuf, 0, 1024);
                    if(numBytes == -1){
                      createBrokerException(socket.getLastException(), "SocketError", XWB_M_REJECT);
                    }
                    result.append(new String(cbuf, 0, numBytes));
                    if (cbuf[numBytes - 1] == 4)
                        break;
                }
                if (result.indexOf("accept") >= 0) {
                    socket.close();
                    // Wait for VistA to connect to our server socket
                    Socket sock = serverSocket.accept();
                    socket = new SocketConnection(sock);
                    socket.setSocketEventsCallback(this);
                    connected = true;
                } else {
                    brokerException = createBrokerException(socket.getLastException(), "connect", XWB_CONNECT_REFUSED);
                    if (eventHandler != null)
                        eventHandler.onRpcBrokerError(brokerException);
                    throw brokerException;
                }
            }
        } catch(IOException ioe) {
            brokerException = createBrokerException(socket.getLastException(), "connect", 0);
            if (eventHandler != null)
                eventHandler.onRpcBrokerError(brokerException);
            throw brokerException;
        }
        return connected;      
    }
    
    public String call(String remoteProcedure, Params params) throws BrokerException {
        super.call(remoteProcedure, params);
        String message = XWB_PREFIX + getRpcMessage(params, remoteProcedure, getRpcVersion());
        currentSentData = message;
        if (eventHandler != null)
            eventHandler.onRpcBrokerSend(message.toCharArray());
        String result = null;
        if (!isConnected()) {
          errorMessage = "Broker Connection Error";
          Exception e = new Exception(errorMessage);
          throw createBrokerException(e, "call", XWB_NO_CONNECTION_TO_SERVER);
        }
        StringBuffer data = new StringBuffer();
        try {
            socket.print(message);
            result = doReceiveData(data);
            // Check if vista returned #BYE#, indicating that it disconnected for some reason. 
            if ((result.length() > 0) &&
                (result.charAt(0) == '#') && 
                result.equals("#BYE#")) {
                throw createBrokerException(new Exception(), "receive", XWB_DISCONNECTED_WITH_BYE);
            }
        } catch(Exception e) {
            if (e instanceof BrokerException) 
                throw (BrokerException)e;
            else {
                e.printStackTrace();
                throw createBrokerException(e, "call", 0);
            }
        }
        currentReceivedData = data.toString();
        if (eventHandler != null) {
            eventHandler.onRpcBrokerReceive(data.toString().toCharArray());
        }
        return result;
    }   
    
    public boolean disconnect() throws BrokerException {
        if( (socket == null) || !socket.isConnected() )
            return true;
        String tmp = strPack("#BYE#",5);
        String bye = strPack(tmp,5);
        try{
            socket.print(bye);
            doReceiveData(new StringBuffer());
        }catch (Exception e){
            /* no op */
        }
        return super.disconnect();
    }    
    
    // PRIVATE METHODS
    private String doReceiveData(StringBuffer data) throws Exception {
        StringBuffer result = new StringBuffer();
        String str = null;
        // 1. Get security segment 
        str = getServerPacket();
        if (str != null)
            data.append(str);
        securitySegment = str;
        // 2. Get application segment
        str = getServerPacket();
        if (str != null)
            data.append(str);
        applicationSegment = str;
        // 3. Get data segment
        char[] cbuf = new char[1024];
        int numBytes = 0;
        while (true) {
            numBytes = socket.read(cbuf, 0, 1024);
            if (numBytes <= 0)
              break;
            data.append(new String(cbuf, 0, numBytes));
            result.append(new String(cbuf, 0, numBytes));
            if (cbuf[numBytes - 1] == 4)
                break;
        }
        if (result.length() == 0)
          throw createBrokerException(socket.getLastException(), "No data received", XWB_NO_DATA_RECEIVED);
        if (result.charAt(0) == 24) {
            String msg = result.substring(1);
            brokerException = createBrokerException(socket.getLastException(), msg, XWB_M_REJECT);
            if (eventHandler != null)
                eventHandler.onRpcBrokerError(brokerException);
            throw brokerException;
        } 
        if (securitySegment != null) {
            brokerException = createBrokerException(socket.getLastException(), securitySegment, XWB_RPC_NOT_REG);
            throw brokerException;
        } else if (applicationSegment != null) {
            brokerException = createBrokerException(socket.getLastException(), applicationSegment, XWB_RPC_NOT_REG);
            throw brokerException;
        }
        // Remove end-of-line ASCII 4 character(s)
        int i = result.length()-1;
        while ( (i >= 0) && (result.charAt(i) == 4))
            result.deleteCharAt(i--);
        return result.toString();
    }
    
    private static String getRpcMessage(Params params, String api, String version) {
        // Build parameter list
        ArrayList sin = new ArrayList();
        String x = null;
        String subscript = null;
        int arr = 0;
        StringBuffer param = new StringBuffer();
        int paramCount = params != null ? params.getCount() : 0;
        for (int i=0; i < paramCount; i++) {
            int pType = params.getParameter(i).getType();
            StringBuffer value = new StringBuffer();
            switch (pType) {
                case IParamType.LITERAL   : value.append(params.getParameter(i).getValue());
                                            param.append(strPack("0" + value.toString(), 3)); break;
                                            
                case IParamType.REFERENCE : value.append(params.getParameter(i).getValue());
                                            param.append(strPack("1" + value.toString(), 3)); break;
                
                case IParamType.LIST      :
                case IParamType.MULT      : value.append(".x");
                                            param.append(strPack("2" + value.toString(), 3));
                                            if (value.indexOf(".") >= 0)
                                                x = value.substring(1, value.length());
                                            Mult mult = null;
                                            if (pType == IParamType.MULT)
                                                mult = params.getParameter(i).getMult();
                                            else {
                                                List list = params.getParameter(i).getList();
                                                mult = new Mult();
                                                for(int j = 0; j < list.size(); j++) {
                                                    mult.setMultiple(String.valueOf(j+1), (String)list.get(j));
                                                }
                                                params.getParameter(i).setMult(mult);
                                          }

                                            String[] nameValue = mult.getFirst();
                                            subscript = nameValue[0];
                                            while (subscript.length() > 0) {
                                                if (mult.getValue(subscript).equals("")) {
                                                    char[] c = {1};
                                                    mult.setValue(subscript, new String(c));
                                                }
                                                sin.add(strPack(subscript, 3) + strPack(mult.getValue(subscript), 3));
                                                String[] multiple = mult.order(subscript, 1);
                                                subscript = multiple[0];
                                            }
                                            sin.add("000");
                                            arr = 1;
                                            break;
            }
        }
        int tSize = 0;
        // Build header
        String hdr = strPack("XWB;;;;", 3);
        // Build API
        StringBuffer strOut = new StringBuffer();
        api = strPack(String.valueOf(arr) + api + '^' + strPack(param.toString(), 5), 5);
        strOut.append(strPack(hdr + api, 5));
        String rpcVer = varPack(version);
        int num = 0;
        if (sin.size()-1 > 0)
            num = sin.size() - 1;
        if (num > 0) {
            for (int i = 0; i <= num; i++)
                tSize += ((String)sin.get(i)).length();
            x = "00000" + String.valueOf(tSize + strOut.length() + rpcVer.length());
        } else
            x = "00000" + String.valueOf(strOut.length() + rpcVer.length());
        StringBuffer tResult = new StringBuffer(x.substring(x.length() - 6, x.length() - 1) +
                rpcVer + strOut.toString());
        if (num > 0) {
            for (int i = 0; i <= num; i++)
                tResult.append((String)sin.get(i));
        }
        return tResult.toString();
    }    
    
    private static String strPack(String n, int p) {
        char[] zero = new char[p];
        for (int i=0; i < zero.length; i++)
            zero[i] = '0';
        StringBuffer t = new StringBuffer(new String(zero));
        String x = String.valueOf(n.length());
        t.append(x);
        int l = x.length();
        int r = l + p;
        return t.substring(l, r) + n;
    }
  
    private static String varPack(String n) {
        if (n.length() == 0)
            n = "0";
        return "|" + (char)n.length() + n;
    }       
    
}
