package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;

/**
 * A message from the view with the game id for the requested game
 */
public class GameRequestMessage extends ViewMessage{

  /**
   * The game id for the requested game
   */
  public final int gameID;

  /**
   * Game id for the requested game is sent
   * @param gameID game id of the requested game
   */
  public GameRequestMessage(int gameID) {
    super(ViewMessageType.GAME_REQUEST);
    this.gameID = gameID;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof GameRequestMessage)) {
      return false;
    }
    GameRequestMessage other = (GameRequestMessage) o;
    return gameID == other.gameID;
  }
}
