/*
*  Generated by AN Unibo
*/
package it.unibo.contact.platformuv;
import java.net.ServerSocket;
import java.net.Socket;
import it.unibo.is.interfaces.protocols.*;
import it.unibo.is.interfaces.IOutputView;
import it.unibo.platform.tcp.SocketTcpConnSupport;
import it.unibo.platform.tcp.SocketTcpSupport;

import it.unibo.is.interfaces.IMessage;


public class TcpIn extends ConnProtIn {
protected ITcpConnection supportTcp;
protected boolean goon = true;
protected ServerSocket serverSocket;

	public TcpIn(
		String receiver, String msgId, int portNum, boolean withAnswer, IOutputView view){
		super("TcpIn",receiver, msgId, portNum, withAnswer, view);
	}
	
	@Override
	protected void waitForRequest() throws Exception{	
	  supportTcp = new SocketTcpSupport("TcpIn"+receiver, view);
	  serverSocket = supportTcp.connectAsReceiver(portNum);
	   while (goon) {
			ITcpInteraction kernelTcp = acceptAConnection(serverSocket);
			curConns.add(kernelTcp);
			String receivedMsg = kernelTcp.receiveALine( );
			IMessage mm = new Message(receivedMsg);
			receivedMsgIds.add( mm.msgId() );
			println("accepted " + mm + " " + curConns.size() + " " + receivedMsgIds.size() );
			RunTimeKb.addSubjectInConnSupport( receiver, msgId, kernelTcp );
			new ConnReceiver(receiver, mm, kernelTcp, withAnswer, view).start();
	   }
	}

	protected ITcpInteraction acceptAConnection(ServerSocket serverSocket) throws Exception{
 		println( "waits for a connection "  );	
		Socket socket 	= supportTcp.acceptAConnection(serverSocket);
		return new SocketTcpConnSupport(receiver,socket,view);
	}
	
	@Override
	protected void  handleException(Exception ee){
	
		try {
			String mm = receiver + "_" + msgId + "(tcpIn," + msgId+ ",exception('"+ee.getMessage()+"'),0)";
			LindaLike.getSpace().out(mm);
			terminate();
		} catch (Exception e) {
			doprintln("failure" + e.getMessage() );
		}	
	
	}
	
	@Override
	public void terminate() {
		super.terminate();
		if( serverSocket != null ){
		goon = false;
		//doprintln( " on " + portNum + " CLOSING ");
		try {
			serverSocket.close();
			serverSocket = null;
		} catch (Exception e) {
			doprintln("failure" + e.getMessage() );
		}
		}	
	}	
}
