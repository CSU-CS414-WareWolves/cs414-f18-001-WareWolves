package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;

import client.presenter.network.messages.*;

public class ChadServer extends Thread{
	/**
	 * Port to listen on, given at construction
	 */
	private final int serverPort;
	/**
	 * Selector for handling keys
	 */
	private Selector select;
	private Query query;
	private HashMap<String, SocketChannel> sessions;
	
	/**
	 * Constructor, takes port number.
	 * @param port Sets server port number
	 */
	public ChadServer(int port) {
		this.serverPort=port;
		query = new Query();
		sessions = new HashMap<String, SocketChannel>();
	}
	
	/**
	 * Setup server, starts socket on port passed at construction.
	 * @throws IOException
	 */
	private void setup() throws IOException {
	    String address = InetAddress.getLocalHost().getHostAddress();
	    select = Selector.open();
	    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
	    serverSocketChannel.configureBlocking(false);
	    serverSocketChannel.socket().bind(new InetSocketAddress(address, serverPort));
	    System.out.println("Starting Server at");
	    System.out.println(address + ":" + serverPort);
	    serverSocketChannel.register(select, SelectionKey.OP_ACCEPT);
	}
	
	
	public void run() {
		try {
			setup();
			while (!Thread.currentThread().isInterrupted()) {
				this.select.select();
				Iterator<SelectionKey> keys = this.select.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey key = (SelectionKey) keys.next();
					keys.remove();
					parseKey(key);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parses SelectionsKeys, whether it is a read or a new accept.
	 * @param key SelectionKey read by run() loop
	 * @throws IOException
	 */
	private void parseKey(SelectionKey key) throws IOException {
		try {
			if (key.isAcceptable())
				this.accept(key);
			if (key.isReadable()) {
				ByteBuffer buff = ByteBuffer.allocate(5000);
				SocketChannel s = (SocketChannel)key.channel();
				int read = 0;
				while(buff.hasRemaining() && read !=-1) {
					read = s.read(buff);
				}
				int len = buff.getInt();
				byte[] msg = new byte[len];
				String message = new String(buff.get(msg).array());
				parseMessage(message, s);
			}
		} catch (CancelledKeyException e) {
			// If key gets canceled it means the client has disconnected
			System.err.println("A client has disconnected");
		}
	}
	
	//TODO: notify of resignation?
	
	private void parseMessage(String msg, SocketChannel sock) {
		NET_MESSAGE_TYPE mt = NET_MESSAGE_TYPE.fromInt(Integer.parseInt(msg.split(":")[0]));
		switch(mt) {
			case LOGIN: try {
				LoginResponse response = query.loginCheck(new Login(msg));
				sock.write(ByteBuffer.allocate(4).putInt(response.length));
				sock.write(ByteBuffer.wrap(response.getDataString().getBytes()));
				if(response.success) {
					sessions.put(response.nickname, sock);
					Players players = query.getPlayers();
					sock.write(ByteBuffer.allocate(4).putInt(players.length));
					sock.write(ByteBuffer.wrap(players.getDataString().getBytes()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			case LOGOUT: sessions.remove(new Logout(msg, 0).nickname);
			case REGISTER: try {
				RegisterResponse response = query.register(new Register(msg));
				sock.write(ByteBuffer.allocate(4).putInt(response.length));
				sock.write(ByteBuffer.wrap(response.getDataString().getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			case UNREGISTER: try {
				UnregisterResponse response = query.unregister(new Unregister(msg));
				sock.write(ByteBuffer.allocate(4).putInt(response.length));
				sock.write(ByteBuffer.wrap(response.getDataString().getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			case GAME_REQUEST: try {
				GameInfo response = query.getGame(new GameRequest(msg).gameID);
				sock.write(ByteBuffer.allocate(4).putInt(response.length));
				sock.write(ByteBuffer.wrap(response.getDataString().getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			case MOVE: {//Upper case is black, lower case is white
				Move move = new Move(msg);
				boolean color = Character.isUpperCase(move.move.charAt(0));
				String opponent = query.move(move, color);
				if(sessions.containsKey(opponent)) {
					try {
						sessions.get(opponent).write(ByteBuffer.allocate(4).putInt(move.length));
						sessions.get(opponent).write(ByteBuffer.wrap(move.getDataString().getBytes()));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			case ACTIVE_GAMES_REQUEST: try {
				ActiveGameResponse response = query.getActiveGames(new ActiveGameRequest(msg).nickname);
				sock.write(ByteBuffer.allocate(4).putInt(response.length));
				sock.write(ByteBuffer.wrap(response.getDataString().getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			case INVITE_REQUEST: query.addInvite(new InviteRequest(msg));
			case INVITE_RESPONSE: query.updateInvite(new InviteResponse(msg));
			case RESIGN: query.resign(new Resign(msg));
			case INBOX_REQUEST: try {
				InboxResponse response = query.getInbox(new InboxRequest(msg, 0).nickname);
				sock.write(ByteBuffer.allocate(4).putInt(response.length));
				sock.write(ByteBuffer.wrap(response.getDataString().getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			case PROFILE_REQUEST: try {
				ProfileResponse response = query.getProfile(new ProfileRequest(msg, 0).nickname);
				sock.write(ByteBuffer.allocate(4).putInt(response.length));
				sock.write(ByteBuffer.wrap(response.getDataString().getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			case SEE_RESULTS: query.setResults(new SeeResults(msg));
			default: System.err.println("Could not parse message type of: "+mt+", with contents: "+msg);break;
		}
	}
	
	
	
	/**
	 * Accept new connections
	 * @param key
	 * @throws IOException
	 */
	private void accept(SelectionKey key) throws IOException {
		// Configure socket
		ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
		SocketChannel channel = serverSocket.accept();
		channel.configureBlocking(false);
		channel.register(select, SelectionKey.OP_READ);
	}
	
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		ChadServer server = new ChadServer(port);
		server.start();
	}

}