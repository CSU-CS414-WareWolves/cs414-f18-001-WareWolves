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
				//System.out.println("RecieveThread:: Reading Message");
				//System.out.println(sock.getRemoteSocketAddress().toString());

				if(din.available() != 0){
					System.out.println("Data InputStream: " + din.available());
					byte[] bytes = new byte[10000];
					din.read(bytes);
					String msg = new String(bytes).trim();
					System.out.println(msg);
					parseMessage(msg);
				}

				//dataLen = din.read();
				//byte[] bytes = new byte[dataLen];
				//din.readFully(bytes, 0, dataLen);
				//String msg = new String(bytes);
				//System.out.println("RecieveThread:: Got Message");

			} catch (IOException e) {
				System.err.println(e.getMessage());
				Thread.currentThread().interrupt();
				try {
					sock.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
			case LOGIN_RESPONSE:
				message = new LoginResponse(msg);
				break;
			case GAME_INFO:
				message = new GameInfo(msg);
				break;
			case MOVE:
				message = new Move(msg);
				break;
			case ACTIVE_GAMES_RESPONSE:
				message = new ActiveGameResponse(msg);
				break;
			case REGISTER_RESPONSE:
				message = new RegisterResponse(msg);
				break;
			case INBOX_RESPONSE:
				message = new InboxResponse(msg);
				break;
			case PROFILE_RESPONSE:
				message = new ProfileResponse(msg);
				break;
			case PLAYERS:
				message = new Players(msg);
				break;
			case UNREGISTER_RESPONSE:
				new UnregisterResponse(msg);
				break;
			default:
				System.err.println("Could not parse message: "+msg);
				break;
		}
		if(message!=null)
			mgmt.sendToPresenter(message);
	}


}