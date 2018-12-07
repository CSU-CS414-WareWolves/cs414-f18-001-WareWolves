package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;
import java.util.Arrays;

/**
 * A message from the view with the game id for the requested game
 */
public class GameRequestMessage extends ViewMessage{

  /**
   * The game id for the requested game
   */
  public final String[] gameInfo;


  public GameRequestMessage(String[] gameInfo) {
    super(ViewMessageType.GAME_REQUEST);
    this.gameInfo = gameInfo;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof GameRequestMessage)) {
      return false;
    }
    GameRequestMessage other = (GameRequestMessage) o;
    return Arrays.equals(this.gameInfo, other.gameInfo);
  }
}
