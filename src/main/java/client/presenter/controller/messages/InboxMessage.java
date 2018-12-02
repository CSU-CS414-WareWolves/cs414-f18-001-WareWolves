package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;

/**
 * A message from the view with the nickname for the requested user's inbox
 */
public class InboxMessage extends ViewMessage {

  /**
   * The nickname the user wants to see the inbox of
   */
  public final String nickname;

  /**
   * Nickname for the requested inbox is sent
   * @param nickname nickname for the requested inbox
   */
  public InboxMessage(String nickname) {
    super(ViewMessageType.INBOX);
    this.nickname = nickname;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof InboxMessage)) {
      return false;
    }
    InboxMessage other = (InboxMessage) o;
    return nickname.equals(other.nickname);
  }
}
