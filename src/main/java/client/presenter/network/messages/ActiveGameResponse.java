package client.presenter.network.messages;

import java.util.Arrays;

public class ActiveGameResponse extends NetworkMessage {
	/**
	 * GameIDs of active games for the user with the paired ActiveGameRequest.
	 */
	public final int[] gameIDs;
	/**
	 * String representation of game boards for the active games.
	 */
	public final String[] gameBoards;
	/**
	 * Nickname of the opponents for the active games.
	 */
	public final String[] opponents;
	/**
	 * Start dates of the active games.
	 */
	public final String[] startDates;
	/**
	 * The current turn of the active games.
	 */
	public final boolean[] turns;
	/**
	 * Color of the requesting player for the active games.
	 */
	public final boolean[] color;//F= white, T=black
	/**
	 * Whether or not the active game has been ended. If it is, the requesting player has not seen the results yet.
	 */
	public final boolean[] ended;
	
	
	/**
	 * Constructor for the server.
	 * @param gameIDs Arrays of game IDs.
	 * @param gameBoards Array of game boards in string form.
	 * @param opponents Array of opponent nicknames.
	 * @param startDates Array of start dates of the games.
	 * @param turns Array representing the turn of the games.
	 * @param color Array of the requesting players color for the games. 
	 * @param ended Array of where or not the active games have been ended or not.
	 */
	public ActiveGameResponse(int[] gameIDs, String[] gameBoards, String[] opponents, String[] startDates, boolean[] turns, boolean[] color, boolean[] ended) {
		super(NET_MESSAGE_TYPE.ACTIVE_GAMES_RESPONSE);
		this.gameIDs = Arrays.copyOf(gameIDs, gameIDs.length);
		this.gameBoards = Arrays.copyOf(gameBoards, gameBoards.length);
		this.opponents = Arrays.copyOf(opponents, opponents.length);
		this.startDates = Arrays.copyOf(startDates, startDates.length);
		this.turns = Arrays.copyOf(turns, turns.length);
		this.color = Arrays.copyOf(color, color.length);
		this.ended = Arrays.copyOf(ended, ended.length);
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for RecieveThread.
	 * @param data String representation of the message.
	 */
	public ActiveGameResponse(String data) {
		super(NET_MESSAGE_TYPE.ACTIVE_GAMES_RESPONSE);
		String[] recs = data.split("#");
		this.gameIDs = new int[recs.length];
		this.gameBoards = new String[recs.length];
		this.opponents = new String[recs.length];
		this.startDates = new String[recs.length];
		this.turns = new boolean[recs.length];
		this.color = new boolean[recs.length];
		this.ended = new boolean[recs.length];
		for(int i=0;i<recs.length;i++) {
			String[] splt = recs[i].split(":");
			if(i==0) {
				gameIDs[i]=Integer.parseInt(splt[1]);
				gameBoards[i]=splt[2];
				opponents[i]=splt[3];
				startDates[i]=splt[4];
				turns[i] = Boolean.parseBoolean(splt[5]);
				color[i] = Boolean.parseBoolean(splt[6]);
				ended[i] = Boolean.parseBoolean(splt[7]);
			}
			else {
				gameIDs[i]=Integer.parseInt(splt[0]);
				gameBoards[i]=splt[1];
				opponents[i]=splt[2];
				startDates[i]=splt[3];
				turns[i] = Boolean.parseBoolean(splt[4]);
				color[i] = Boolean.parseBoolean(splt[5]);
				ended[i] = Boolean.parseBoolean(splt[6]);
			}
		}
		length = this.getDataString().getBytes().length;
	}

	@Override
	public String getDataString() {
		String data = type.typeCode+":";
		for(int i=0;i<gameIDs.length;i++) {
			if(i == gameIDs.length-1)
				data+=gameIDs[i]+":"+gameBoards[i]+":"+opponents[i]+":"+startDates[i]+":"+turns[i]+":"+color[i]+":"+ended[i];
			else
				data+=gameIDs[i]+":"+gameBoards[i]+":"+opponents[i]+":"+startDates[i]+":"+turns[i]+":"+color[i]+":"+ended[i]+"#";
		}
		return data;
	}

}
