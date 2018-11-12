package client.presenter.network.messages;

public class UnregisterResponse extends NetworkMessage {
	/**
	 * Success status of paired Unregister request
	 */
	public final boolean success;
	
	/**
	 * Constructor for server
	 * @param success True if player was unregistered successfully. False if not.
	 */
	public UnregisterResponse(boolean success) {
		super(NET_MESSAGE_TYPE.UNREGISTER_RESPONSE);
		this.success = success;
		length = this.getDataString().getBytes().length;
	}

	/**
	 * Constructor for NetworkManager
	 * @param data String representation of the message
	 */
	public UnregisterResponse(String data) {
		super(NET_MESSAGE_TYPE.UNREGISTER_RESPONSE);
		String[] splt = data.split(":");
		success = Boolean.parseBoolean(splt[1]);
		length = this.getDataString().getBytes().length;
	}
	
	@Override
	public String getDataString() {
		return type.typeCode+":"+success;
	}
}
