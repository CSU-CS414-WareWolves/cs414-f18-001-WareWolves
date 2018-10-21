package client.presenter.network.messages;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Login extends NetworkMessage{
	//Clas vars
	private String username;
	private String passwordAttempt;
	private InetAddress userIP;
	private int userPort;
	public static final NET_MESSAGE_TYPE type = NET_MESSAGE_TYPE.LOGIN;
	
	
	public Login(String user, String password, InetAddress ip, int port) {
		username = user;
		passwordAttempt = password;
		userIP = ip;
		userPort = port;
		length = getDataString().getBytes().length;
	}
	
	//Expected: username:password:IP:port
	public Login(String data) throws UnknownHostException {
		String[] spilt = data.split(":");
		username = spilt[0];
		passwordAttempt = spilt[1];
		userIP = InetAddress.getByName(spilt[2]);
		userPort = Integer.parseInt(spilt[3]);
		length = getDataString().getBytes().length;
	}
	
	//Writes out as username:password:IP:port
	public String getDataString() {
		return new String(type.typeCode+":"+username+":"+passwordAttempt+":"+userIP.toString()+":"+userPort);
	}
	
}
