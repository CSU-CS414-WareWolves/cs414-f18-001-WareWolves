package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;

public class InviteMessage extends ViewMessage {

  /**
   * Sender of the invite
   */
  public final String sender;
  /**
   * Recipient of the invite
   */
  public final String recipient;

  /**
   * Sends a message from the view with an invite request from a sender to a recipient
   * @param sender nickname of the sender
   * @param recipient nickname fo the recipient
   */
  public InviteMessage(String sender, String recipient) {
    super(ViewMessageType.INVITE);
    this.sender = sender;
    this.recipient = recipient;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof InviteMessage)) {
      return false;
    }
    InviteMessage other = (InviteMessage) o;
    return sender.equals(other.sender) && recipient.equals(other.recipient);
  }
}
