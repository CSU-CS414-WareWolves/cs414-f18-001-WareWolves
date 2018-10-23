package client.presenter.network.messages;

public class Unregister extends NetworkMessage {
	
	private String email;
	private String nickname;
	private String password;
	
	/**
	 * Constructor for presenter
	 * A user must know all of the information about an account to unregister
	 * @param Email User email keyed in
	 * @param Nickname User email keyed in
	 * @param Password Hash of password keyed in
	 */
	public Unregister(String Email, String Nickname, String Password) {
		super(NET_MESSAGE_TYPE.UNREGISTER);
		email = Email;
		nickname = Nickname;
		password = Password;
	}

	/**
	 * Constructor for server
	 * @param data string read in from RecieveThread
	 */
	public Unregister(String data) {
		super(NET_MESSAGE_TYPE.UNREGISTER);
		String[] splt = data.split(":");
		email = splt[1];
		nickname = splt[2];
		password = splt[3];
	}
	
	
	@Override
	public String getDataString() {
		return type.typeCode+":"+email+":"+nickname+":"+password;
	}

}
