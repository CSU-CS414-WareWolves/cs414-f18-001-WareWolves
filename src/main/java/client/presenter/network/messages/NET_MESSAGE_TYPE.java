package client.presenter.network.messages;

public enum NET_MESSAGE_TYPE {
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
	INVITE_RESPONSE(12),
	RESIGN(13);
	
	//Int representation of types
	protected final int typeCode;
	
	/**
	 * 
	 * @param type type code for NET_MESSAGE_TYPE
	 */
	NET_MESSAGE_TYPE(int type) {
		this.typeCode = type;
	}
	
	/**
	 * 
	 * @param i integer to convert to NET_MESSAGE_TYPE
	 * @return NET_MESSAGE_TYPE of the integer i
	 */
	public static NET_MESSAGE_TYPE fromInt(int i) {
		switch(i) {
			case 1: return NET_MESSAGE_TYPE.LOGIN;
			case 2: return NET_MESSAGE_TYPE.LOGIN_RESPONSE;
			case 3: return NET_MESSAGE_TYPE.LOGOUT;
			case 4: return NET_MESSAGE_TYPE.REGISTER;
			case 5: return NET_MESSAGE_TYPE.UNREGISTER;
			case 6: return NET_MESSAGE_TYPE.GAME_REQUEST;
			case 7: return NET_MESSAGE_TYPE.GAME_INFO;
			case 8: return NET_MESSAGE_TYPE.MOVE;
			case 9: return NET_MESSAGE_TYPE.ACTIVE_GAMES_REQUEST;
			case 10: return NET_MESSAGE_TYPE.ACTIVE_GAMES_RESPONSE;
			case 11: return NET_MESSAGE_TYPE.INVITE_REQUEST;
			case 12: return NET_MESSAGE_TYPE.INVITE_RESPONSE;
			case 13: return NET_MESSAGE_TYPE.RESIGN;
		}
		return null;
	}
	
	/**
	 * 
	 * @return int version of typeCode of Message type 
	 */
	public int code() {
		return this.typeCode;
	}
	
}
