package client.presenter.network.messages;

public class Logout extends NetworkMessage {

	private String nickname;
	
	/**
	 * Constructor for presenter
	 * @param Nickname the nickname of the currently logged in user
	 */
	public Logout(String Nickname) {
		super(NET_MESSAGE_TYPE.LOGOUT);
		nickname = Nickname;
		length = getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for server
	 * @param data data string read from RecieveThread 
	 * @param off dummy variable to differentiate constructors
	 */
	public Logout(String data, int off) {
		super(NET_MESSAGE_TYPE.LOGOUT);
		nickname = data.split(":")[1];
		length = getDataString().getBytes().length;
	}
	
	
	@Override
	public String getDataString() {
		return type.typeCode+":"+nickname;
	}

}
