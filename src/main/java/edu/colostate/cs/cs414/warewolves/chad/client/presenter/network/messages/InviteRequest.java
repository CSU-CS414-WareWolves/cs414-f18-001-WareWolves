package edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages;

public class InviteRequest extends NetworkMessage {
	/**
	 * Nickname of the player sending the invite, creator of the invite.
	 */
	public final String sender;
	/**
	 * Nickname of the recipient of the invite.
	 */
	public final String recipient;
	
	/**
	 * Constructor for presenter
	 * @param sender Nickname of sender, the player logged in. From presenter/login view.
	 * @param recipient Nickname of recipient.
	 */
	public InviteRequest(String sender, String recipient) {
		super(NET_MESSAGE_TYPE.INVITE_REQUEST);
		this.sender = sender;
		this.recipient = recipient;
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for server
	 * @param data String representation of the message
	 */
	public InviteRequest(String data) {
		super(NET_MESSAGE_TYPE.INVITE_REQUEST);
		String[] splt = data.split(":");
		this.sender = splt[1];
		this.recipient = splt[2];
		length = this.getDataString().getBytes().length;
	}

	@Override
	public String getDataString() {
		return type.typeCode+":"+sender+":"+recipient;
	}

}
