package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.info;

public class ActiveGameInfo {

  /**
   * GameIDs of the game
   */
  private final int gameID;
  /**
   * String representation of the game board for the games.
   */
  private final String gameBoard;
  /**
   * Nickname of the opponent for the game.
   */
  private final String opponent;
  /**
   * Start date of the active game.
   */
  private final String startDate;
  /**
   * The current turn of the active game.
   */
  private final boolean turn;
  /**
   * Color of the requesting player for the active game.
   */
  private final boolean color;
  /**
   * Whether or not the active game has been ended. If it is, the requesting player has not seen the results yet.
   */
  private final boolean ended;

  /**
   * Sets all the information about a game
   * @param gameID the gameId
   * @param gameBoard the current board
   * @param opponent the nickname of the person being played against
   * @param startDate the start date of the game
   * @param turn which colors turn it is
   * @param color the color of the player
   * @param ended if the game is over
   */
  public ActiveGameInfo(int gameID, String gameBoard, String opponent,
      String startDate, boolean turn, boolean color, boolean ended) {

    // Check for valid data
    if(gameBoard == null || (gameBoard.length()% 3) != 0)
    {
      throw new IllegalArgumentException("ActiveGameInfo:: The given game board is not valid " + gameBoard);
    }
    if(opponent == null || opponent.length() == 0)
    {
      throw new IllegalArgumentException("ActiveGameInfo:: The given opponent is not valid " + opponent);
    }
    // Set values
    this.gameID = gameID;
    this.gameBoard = gameBoard;
    this.opponent = opponent;
    this.startDate = startDate;
    this.turn = turn;
    this.color = color;
    this.ended = ended;
  }

  /**
   * Sets all the information about a game from an array of strings in this order
   * gameID, gameBoard, opponent, startDate, turn, color, ended
   *
   * @param gameInfo: the game info
   */
  public ActiveGameInfo(String[] gameInfo){
    this(Integer.parseInt(gameInfo[0]), gameInfo[1], gameInfo[2], gameInfo[3],
        Boolean.parseBoolean(gameInfo[4]), Boolean.parseBoolean(gameInfo[5]),
        Boolean.parseBoolean(gameInfo[6]));
  }

  /**
   * Gets the gameID
   * @return the gameId
   */
  public int getGameID() {
    return gameID;
  }

  /**
   * Gets the current board
   * @return the current board
   */
  public String getGameBoard() {
    return gameBoard;
  }

  /**
   * Gets the opponent nickname
   * @return the opponent nickname
   */
  public String getOpponent() {
    return opponent;
  }

  /**
   * Gets the start date of the game
   * @return the start date of the game
   */
  public String getStartDate() {
    return startDate;
  }

  /**
   * Gets the current players turn
   * @return the current players turn
   */
  public boolean getTurn() {
    return turn;
  }

  /**
   * Gets the color of the player for this game
   * @return the color of the player for this game
   */
  public boolean getColor() {
    return color;
  }

  /**
   * Gets if the game is over
   * @return the game is over
   */
  public boolean getEnded() {
    return ended;
  }

  /**
   * Gets all the information about a game in a String[] in the same order as the constructor.
   * @return the information about the game
   */
  public String[] getInfoArray(){
    return new String[] {String.valueOf(getGameID()), getGameBoard(),
        getOpponent(), getStartDate(), String.valueOf(getTurn()),
        String.valueOf(getColor()), String.valueOf(getEnded()) };
  }
}
