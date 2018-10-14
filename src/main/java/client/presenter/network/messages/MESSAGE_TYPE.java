package client.presenter.network.messages;

public enum MESSAGE_TYPE {
	LOGIN(1),
	LOGIN_RESPONSE(2),
	LOGOUT(3),
	REGISTER(4),
	UNREGISTER(5),
	GAME_REQUEST(6),
	GAME_INFO(7),
	MOVE(8),
	ACTIVE_GAMES_REQUEST(9),
	ACTIVE_GAMES_RESPONSE(10),
	INVITE_REQUEST(11),
	INVITE_RESPONSE(12);	
	
	//Int representation of types
	private final int typeCode;
	
	//Set Message type code
	MESSAGE_TYPE(int type) {
		this.typeCode = type;
	}
	
	//Get Message type code
	public int code() {
		return this.typeCode;
	}
	
}
