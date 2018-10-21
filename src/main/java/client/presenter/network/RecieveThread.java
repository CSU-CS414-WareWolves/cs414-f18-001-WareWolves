package client.presenter.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import client.presenter.network.messages.NET_MESSAGE_TYPE;

public class RecieveThread extends Thread{

	private Socket sock;
	private DataInputStream din;
	private NetworkManager mgmt;
	
	
	/**
	 * Constructor, instantiates DataInputStream of socket
	 * @param Sock socket connected to server from NetworkManager
	 * @param net NetworkManager, to send message up the chain
	 * @throws IOException thrown if getting DataInputStream fails
	 */
	public RecieveThread(Socket Sock, NetworkManager net) throws IOException {
		sock = Sock;
		din = new DataInputStream(sock.getInputStream());
		mgmt = net;
	}
	
	
	/**
	 * Run loop, waits to be active to read in data
	 */
	@Override
	public void run() {
		int dataLen;
		while(!Thread.currentThread().isInterrupted()) {
			try {
				dataLen = din.readInt();
				byte[] bytes = new byte[dataLen];
				din.readFully(bytes, 0, dataLen);
				String msg = new String(bytes);
				parseMessage(msg);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Reads message type code, sends to appropriate NetworkManager method to handle message type
	 * @param msg String of message received 
	 */
	protected void parseMessage(String msg) {
		NET_MESSAGE_TYPE mt = NET_MESSAGE_TYPE.fromInt(Integer.parseInt(msg.split(":")[0]));
		switch(mt) {
			case LOGIN: 
			case LOGIN_RESPONSE:
			case LOGOUT:
			case REGISTER:
			case UNREGISTER:
			case GAME_REQUEST:
			case GAME_INFO:
			case MOVE:
			case ACTIVE_GAMES_REQUEST:
			case ACTIVE_GAMES_RESPONSE:
			case INVITE_REQUEST:
			case INVITE_RESPONSE:
			case RESIGN:
		}
	}
	
	
}
