package edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages;

public class InboxRequest extends NetworkMessage {
	/**
	 * Nickname of the player who's inbox is being requested
	 */
	public final String nickname;
	
	/**
	 * Constructor for presenter
	 * @param nickname The logged in user's ID
	 */
	public InboxRequest(String nickname) {
		super(NET_MESSAGE_TYPE.INBOX_REQUEST);
		this.nickname = nickname;
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for server
	 * @param data String representation of the message
	 * @param off dummy variable to differentiate constructors
	 */
	public InboxRequest(String data, int off) {
		super(NET_MESSAGE_TYPE.INBOX_REQUEST);
		this.nickname = data.split(":")[1];
		length = this.getDataString().getBytes().length;
	}
	
	@Override
	public String getDataString() {
		return type.typeCode+":"+nickname;
	}

}
