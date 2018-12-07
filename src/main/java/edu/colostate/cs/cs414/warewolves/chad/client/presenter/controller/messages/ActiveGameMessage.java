package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;

/**
 * A message from the view with the nickname for the requested user's active games
 */
public class ActiveGameMessage extends ViewMessage {


  /**
   * Nickname for the requested games is sent
   */
  public ActiveGameMessage() {
    super(ViewMessageType.ACTIVE_GAMES);
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof ActiveGameMessage)) {
      return false;
    }
    ActiveGameMessage other = (ActiveGameMessage) o;
    return this.messageType.equals(other.messageType);
  }
}
