package client.presenter.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import client.presenter.network.messages.*;

public class RecieveThread extends Thread{
	/**
	 * A Socket connected to the server
	 */
	private Socket sock;
	/**
	 * DataInputStream from the socket, recieves from the server
	 */
	private DataInputStream din;
	/**
	 * NetworkManager in charge of the RecieveThread, used to pass recieved messages up.
	 */
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
		NetworkMessage message = null;
		switch(mt) {
			case LOGIN_RESPONSE: message = new LoginResponse(msg);
			case GAME_INFO: message = new GameInfo(msg);
			case MOVE: message = new Move(msg);
			case ACTIVE_GAMES_RESPONSE: message = new ActiveGameResponse(msg);
			case REGISTER_RESPONSE: new RegisterResponse(msg);
			case INBOX_RESPONSE: message = new InboxResponse(msg);
			case PROFILE_RESPONSE: message = new ProfileResponse(msg);
			case PLAYERS: message = new Players(msg);
			case UNREGISTER_RESPONSE: new UnregisterResponse(msg);
			default: System.err.println("Could not parse message: "+msg);
		}
		if(message!=null)
			mgmt.sendToPresenter(message);
	}
	
	
}
