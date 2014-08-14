package gov.va.med.lom.javaBroker.rpc;

import java.io.IOException;
import java.util.List;

import gov.va.med.lom.javaBroker.net.ISocketEventsCallback;
import gov.va.med.lom.javaBroker.net.SocketConnection;

public class RpcProtocolV2 extends AbstractRpcProtocol implements
        ISocketEventsCallback {

    // NAMED CONSTANTS
    protected final String XWB_PREFIX = "[XWB]";

    public RpcProtocolV2(int protocolVersion) {
        super(protocolVersion);
    }

    /**
     * prepend the length of the string in p characters to the value of n
     */
    private String lpack(String n, int p) {

        StringBuffer t = new StringBuffer();
        char[] zero = new char[p];
        for (int i = 0; i < zero.length; i++)
            zero[i] = '0';
        t.append(zero);
        t.append(String.valueOf(n.length()));

        String s = t.substring(t.length() - p) + n;
        return s;

    }

    /**
     * prepend the length of the string in one byte to the the value of s; the
     * length of str must be <= 255
     */
    private String spack(String s) {
        StringBuffer t = new StringBuffer();
        t.append((char) s.length());
        t.append(s);
        return t.toString();
    }
    
    
    /* 
     * builds a StringBuffer from 
     * a MULT/GLOBAL parameter type
     */  
    private StringBuffer buildMultParam(Mult mult){
        
        StringBuffer payload = new StringBuffer();
        String[] vals;
        String subscript;
        String val;
        
        // if there are no parameters in the mult
        if(mult.getCount() == 0){
        	payload.append(lpack("",3));
        	payload.append("f");
        	return payload;
        }
        
        for (int x = 0; x < mult.getCount(); x++) {

            if (x > 0)
                payload.append("t");

            vals = mult.getMultiple(x);
            subscript = vals[0];
            val = vals[1];

            if (subscript.equals("")) {
                StringBuffer a = new StringBuffer();
                a.append((char)1);
                payload.append(lpack(a.toString(), 3));
            } else {
                payload.append(lpack(subscript, 3));
            }

            payload.append(lpack(val, 3));
        }
        payload.append("f");
        return payload;
    }
    

    private StringBuffer buildpar(String rpc, Params params) {

        StringBuffer payload = new StringBuffer();

        payload.append("5");
        int paramCount = params != null ? params.getCount() : 0;
        for (int i = 0; i < paramCount; i++) {
            int pType = params.getParameter(i).getType();
            StringBuffer value = new StringBuffer();
            switch (pType) {

            case IParamType.LITERAL:
                value.append(params.getParameter(i).getValue());
                payload.append("0" + lpack(value.toString(), 3) + "f");
                break;
            case IParamType.REFERENCE:
                value.append(params.getParameter(i).getValue());
                payload.append("1" + lpack(value.toString(), 3) + "f");
                break;
            case IParamType.LIST:
                payload.append("2");
                // create a mult with incrementing keys and
                // then create a MULT param string
                Mult mult = new Mult();
                List list = params.getParameter(i).getList();
                for(int j = 0; j < list.size(); j++) {
                    mult.setMultiple(String.valueOf(j+1), (String)list.get(j));
                }
                payload.append(buildMultParam(mult));
                break;
            case IParamType.MULT:
                payload.append("2");
                payload.append(buildMultParam(params.getParameter(i).getMult()));
                break;
            case IParamType.GLOBAL:
                payload.append("3");
                payload.append(buildMultParam(params.getParameter(i).getMult()));
                break;
            case IParamType.EMPTY:
                payload.append("4f");
                break;
            case IParamType.STREAM:
            	value.append(params.getParameter(i).getValue());
                payload.append("5" + lpack(value.toString(), 3) + "f");
            }
        }

        if (payload.equals("5")) { // no parameters
            payload.append("4f");
        }

        StringBuffer t = new StringBuffer();
        t.append(XWB_PREFIX + "11302" + spack(this.rpcVersion) + spack(rpc)
                + payload + (char) 4);

        return t;
    }

    private String networkCall(StringBuffer payload) throws BrokerException {

        StringBuffer result = new StringBuffer();
        BrokerException brokerException;
        if (eventHandler != null)
            eventHandler.onRpcBrokerSend(payload.toString().toCharArray());

        try {
            // send the payload
            socket.print(payload);

            // 1. Get security segment
            securitySegment = getServerPacket();

            // 2. Get application segment
            applicationSegment = getServerPacket();

            // 3. Get data segment
            char[] cbuf = new char[1024];
            while (true) {
                int numBytes = socket.read(cbuf, 0, 1024);
                if(numBytes <= 0){
                	break;
                }
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
                brokerException = createBrokerException(socket
                        .getLastException(), securitySegment, XWB_RPC_NOT_REG);
                throw brokerException;
            } else if (applicationSegment != null) {
                brokerException = createBrokerException(socket
                        .getLastException(), applicationSegment,
                        XWB_RPC_NOT_REG);
                throw brokerException;
            }

            if (eventHandler != null) {
                eventHandler
                        .onRpcBrokerReceive(result.toString().toCharArray());
            }

            if (result.charAt(result.length() - 1) == 4) {
                result.deleteCharAt(result.length() - 1);
            }

            return result.toString();

        } catch (BrokerException e) {
            throw e;
        } catch (IOException ioe) {
            brokerException = createBrokerException(socket.getLastException(),
                    "call", 0);
            if (eventHandler != null)
                eventHandler.onRpcBrokerError(brokerException);
            throw brokerException;
        }

    }

    /* if return = false check _Error for errors */
    public boolean connect(String server, int port) throws BrokerException {
        errorMessage = "";
        String result;

        if (socket != null && socket.isConnected()) {
            return true;
        }

        try {

            // create a socket connection to the VistA server
            socket = new SocketConnection(server, port);
            socket.setSocketEventsCallback(this);

            if (!socket.connect()) {
                brokerException = createBrokerException(socket
                        .getLastException(), "connect", XWB_CONNECT_REFUSED);
                if (eventHandler != null)
                    eventHandler.onRpcBrokerError(brokerException);
                throw brokerException;
            }

            // Turn off socket option to linger
            // socket.setSocketOptionLinger(false, 0);

            // Send a connect message to VistA
            StringBuffer sb = new StringBuffer();
            sb.append(XWB_PREFIX 
                    + "10304" 
                    + (char)10 
                    + "TCPConnect50"
                    + lpack(socket.getLocalHostAddress(), 3) + "f0"
                    + lpack("0", 3) + "f0"
                    + lpack(socket.getLocalHostName(), 3) + "f" 
                    + (char) 4);

            result = networkCall(sb);

            if (result.equals("(connection lost)")) {
                socket.close();
                errorMessage = result;
                return false;
            }

        } catch (IOException ioe) {
            BrokerException brokerException = createBrokerException(socket
                    .getLastException(), "connect", 0);
            if (eventHandler != null)
                eventHandler.onRpcBrokerError(brokerException);
            throw brokerException;
        }

        return true;

    }

    public String call(String remoteProcedure, Params params)
            throws BrokerException {
      
        super.call(remoteProcedure, params);
        StringBuffer param_str;
        String result;
        
        if (!isConnected()) {
            errorMessage = "Broker Connection Error";
            Exception e = new Exception(errorMessage);
            throw createBrokerException(e, "call", XWB_NO_CONNECTION_TO_SERVER);
        }

        param_str = buildpar(remoteProcedure, params);
        currentSentData = param_str.toString();
        
        result = networkCall(param_str);
        
        // Check if vista returned #BYE#, indicating that it
        // disconnected for some reason.
        if ((result.length() > 0) &&
            (result.charAt(0) == '#') && 
            (result.equals("#BYE#"))) {
            throw createBrokerException(new Exception(), "receive",
                    XWB_DISCONNECTED_WITH_BYE);
        }
        
        return result;
        
    }

    public boolean disconnect() throws BrokerException {

        if (socket == null || !socket.isConnected()) {
            return true;
        }

  
        StringBuffer payload = new StringBuffer();
        payload.append(XWB_PREFIX
                       + "10304" 
                       + (char)5 
                       + "#BYE#"
                       + (char)4);
        
        networkCall(payload);
        
        return super.disconnect();
                    
    }
}
