package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;

public class InviteMessageResponse extends ViewMessage {

  /**
   * Sender of the invite
   */
  public final int inviteID;
  /**
   * Recipient of the invite
   */
  public final boolean response;

  /**
   * Sends a message from the view with an invite request from a sender to a recipient
   * @param inviteID nickname of the sender
   * @param response nickname fo the recipient
   */
  public InviteMessageResponse(int inviteID, boolean response) {
    super(ViewMessageType.INVITE_RESPONSE);
    this.inviteID = inviteID;
    this.response = response;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof InviteMessageResponse)) {
      return false;
    }
    InviteMessageResponse other = (InviteMessageResponse) o;
    return inviteID == other.inviteID && response == other.response;
  }
}
