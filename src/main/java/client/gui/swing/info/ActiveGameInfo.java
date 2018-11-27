package client.gui.swing.info;

public class ActiveGameInfo {


  private final int gameIDs;
  private final String gameBoards;
  private final String opponents;
  private final String startDates;
  private final boolean turns;
  private final boolean color;
  private final boolean ended;

  public ActiveGameInfo(int gameIDs, String gameBoards, String opponents,
      String startDates, boolean turns, boolean color, boolean ended) {

    if(gameBoards == null || (gameBoards.length()% 3) != 0)
    {
      throw new RuntimeException("ActiveGameInfo:: The given game board is not valid " + gameBoards);
    }
    if(opponents == null || opponents.length() == 0)
    {
      throw new RuntimeException("ActiveGameInfo:: The given opponents is not valid " + opponents);
    }
    this.gameIDs = gameIDs;
    this.gameBoards = gameBoards;
    this.opponents = opponents;
    this.startDates = startDates;
    this.turns = turns;
    this.color = color;
    this.ended = ended;
  }

  public int getGameIDs() {
    return gameIDs;
  }

  public String getGameBoards() {
    return gameBoards;
  }

  public String getOpponents() {
    return opponents;
  }

  public String getStartDates() {
    return startDates;
  }

  public boolean getTurns() {
    return turns;
  }

  public boolean getColor() {
    return color;
  }

  public boolean getEnded() {
    return ended;
  }
}
