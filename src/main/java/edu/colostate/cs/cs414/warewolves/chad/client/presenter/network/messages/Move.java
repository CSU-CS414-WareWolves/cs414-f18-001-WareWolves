package edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages;

public class Move extends NetworkMessage {
	/**
	 * ID of the game that the Move belongs too
	 */
	public final int gameID;
	/**
	 * String representation of the move
	 */
	public final String move;
	/**
	 * String representation of the board of the game.
	 */
	public final String board;
	/**
	 * Boolean representing if the move ends the game or not
	 */
	public final boolean ending;
	/**
	 * Boolean representing if the move ends the game in a draw
	 */
	public final boolean draw;
	
	/**
	 * Constructor for mover
	 * @param gameID games ID, from game board/presenter
	 * @param move String representation of the pieces moved
	 * @param ending True if the move being sent causes the end of the game 
	 * @param draw True if the move being sent causes the game to end in a draw
	 */
	public Move(int gameID, String move, String board, boolean ending, boolean draw) {
		super(NET_MESSAGE_TYPE.MOVE);
		this.gameID = gameID;
		this.move = move;
		this.board = board;
		this.ending = ending;
		this.draw = draw;
		length = this.getDataString().getBytes().length;
	}
	
	/**
	 * Constructor for opponent of move
	 * Expected: "8:gameID:move:ending"
	 * @param data String representation of the message
	 */
	public Move(String data) {
		super(NET_MESSAGE_TYPE.MOVE);
		String[] splt = data.split(":");
		this.gameID = Integer.parseInt(splt[1]);
		this.move = splt[2];
		this.board = splt[3];
		this.ending = Boolean.parseBoolean(splt[4]);
		this.draw = Boolean.parseBoolean(splt[5]);
		length = this.getDataString().getBytes().length;
	}

	@Override
	public String getDataString() {
		return type.typeCode+":"+gameID+":"+move+":"+board+":"+ending+":"+draw;
	}

}
