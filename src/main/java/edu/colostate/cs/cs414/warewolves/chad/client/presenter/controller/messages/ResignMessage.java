package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;

/**
 * View sends message to presenter to resign game
 */
public class ResignMessage extends ViewMessage {

  /**
   * Game ID for the requested resigned game
   */
  public final int gameID;

  /**
   * Message with a game id for a requested resign game
   * @param gameID game id for the requested resign game
   */
  public ResignMessage(int gameID) {
    super(ViewMessageType.RESIGN);
    this.gameID = gameID;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof ResignMessage)) {
      return false;
    }
    ResignMessage other = (ResignMessage) o;
    return this.gameID == other.gameID;
  }
}
