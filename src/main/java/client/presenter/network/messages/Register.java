package client.presenter.network.messages;

public class Register extends NetworkMessage{
	private String email;
	private String nickname;
	private String password;
	
	/**
	 * Constructor for the presenter
	 * @param Email email keyed in by user
	 * @param Nickname chosen nickname
	 * @param HashedPass Hashed password entered in by user
	 */
	public Register(String Email, String Nickname, String HashedPass) {
		super(NET_MESSAGE_TYPE.REGISTER);
		email = Email;
		nickname = Nickname;
		password = HashedPass;
		length = getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for server
	 * @param data String representation of message from RecieveThread
	 */
	public Register(String data) {
		super(NET_MESSAGE_TYPE.REGISTER);
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
