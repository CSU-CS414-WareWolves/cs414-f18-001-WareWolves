package edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages;

import java.util.Arrays;

public class Players extends NetworkMessage {
	/**
	 * Array of all the nicknames of players in the database
	 */
	public final String[] players;
	
	/**
	 * Constructor for server
	 * @param players An array of player nicknames in the form of Strings
	 */
	public Players(String[] players) {
		super(NET_MESSAGE_TYPE.PLAYERS);
		this.players = Arrays.copyOf(players, players.length);
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for client
	 * @param data Data string representation of the message
	 */
	public Players(String data) {
		super(NET_MESSAGE_TYPE.PLAYERS);
		String[] splt = data.split(":");
		players = new String[splt.length-1];
		for(int i=1;i<splt.length;i++)
			players[i-1] = splt[i];
		length = this.getDataString().getBytes().length;
	}
	
	
	
	@Override
	public String getDataString() {
		String data = this.type.typeCode+":";
		for(int i=0; i<players.length;i++) {
			if(i==players.length-1)
				data+=players[i];
			else
				data+=players[i]+":";
		}
		return data;
	}

}
