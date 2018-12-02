package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;

/**
 * A message from the view with the nickname for the requested user's active games
 */
public class ActiveGameMessage extends ViewMessage {

  /**
   * The nickname the user wants to see the active games of
   */
  public final String nickname;

  /**
   * Nickname for the requested games is sent
   * @param nickname nickname for the requested games
   */
  public ActiveGameMessage(String nickname) {
    super(ViewMessageType.ACTIVE_GAMES);
    this.nickname = nickname;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof ActiveGameMessage)) {
      return false;
    }
    ActiveGameMessage other = (ActiveGameMessage) o;
    return nickname.equals(other.nickname);
  }
}
