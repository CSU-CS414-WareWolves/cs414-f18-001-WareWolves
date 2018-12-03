package client.presenter.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import client.presenter.network.messages.*;

public class Sender {
	/**
	 * Socket that is connected to server
	 */
	private Socket sock;
	/**
	 * DataOutputStream from socket
	 */
	private DataOutputStream outToServer;
	
	/**
	 * Sender handles all sending of messages to the server.
	 * @param Sock Socket connected to server from NetworkManager
	 * @throws IOException
	 */
	public Sender(Socket Sock) throws IOException {
		sock = Sock;
		outToServer = new DataOutputStream(sock.getOutputStream());
	}
	
	/**
	 * Primary method of this class, sends messages to server
	 * @param msg A NetworkMessage to send to the server.
	 * @return
	 */
	public boolean sendToServer(NetworkMessage msg) {
		try {
			//outToServer.writeInt(msg.length);
			outToServer.write(msg.getDataString().getBytes());
			outToServer.flush();
			return true;
		} catch (IOException e) {
			System.err.println("Failed to send message: "+msg.getDataString());
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	
	
}
