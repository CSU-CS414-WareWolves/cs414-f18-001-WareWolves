package client.presenter.network.messages;

public class SeeResults extends NetworkMessage{
	public final int gameID;
	public final boolean color;//T=black, F=white
	
	/**
	 * Constructor for presenter
	 * @param gameID the gameID of the game that results have been seen.
	 * @param color the color of the player in the game.
	 */
	public SeeResults(int gameID, boolean color) {
		super(NET_MESSAGE_TYPE.SEE_RESULTS);
		this.gameID = gameID;
		this.color = color;
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for server.
	 * @param data 
	 */
	public SeeResults(String data) {
		super(NET_MESSAGE_TYPE.SEE_RESULTS);
		String[] splt = data.split(":");
		this.gameID = Integer.parseInt(splt[1]);
		this.color = Boolean.parseBoolean(splt[2]);
		length = this.getDataString().getBytes().length;
	}
	
	
	@Override
	public String getDataString() {
		return new String(type.typeCode+":"+gameID+":"+color);
	}
}
