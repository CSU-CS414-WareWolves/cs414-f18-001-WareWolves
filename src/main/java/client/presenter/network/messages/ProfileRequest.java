package client.presenter.network.messages;

public class ProfileRequest extends NetworkMessage {

	public final String nickname;
	
	/**
	 * Constructor for presenter
	 * @param nickname the nickname of the profile requesting to see
	 */
	public ProfileRequest(String nickname) {
		super(NET_MESSAGE_TYPE.PROFILE_REQUEST);
		this.nickname = nickname;
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for server
	 * @param data String representation of the message
	 * @param off dummy variable to differentiate constructors
	 */
	public ProfileRequest(String data, int off) {
		super(NET_MESSAGE_TYPE.PROFILE_REQUEST);
		this.nickname = data.split(":")[1];
		length = this.getDataString().getBytes().length;
	}
	
	@Override
	public String getDataString() {
		return type.typeCode+":"+nickname;
	}

}
