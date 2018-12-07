package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;

/**
 * A message from the view with the nickname for the requested user's inbox
 */
public class InboxMessage extends ViewMessage {


  /**
   * Nickname for the requested inbox is sent
   */
  public InboxMessage() {
    super(ViewMessageType.INBOX);
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof InboxMessage)) {
      return false;
    }
    InboxMessage other = (InboxMessage) o;
    return this.messageType.equals(other.messageType);
  }
}
