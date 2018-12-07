package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;
import java.util.Objects;

/**
 * LogoutMessage message only tells the controllers the view wants to log out
 */
public class LogoutMessage extends ViewMessage{

  /**
   * Default constructor
   */
  public LogoutMessage() {
    super(ViewMessageType.LOGOUT);
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof LogoutMessage)) {
      return false;
    }
    LogoutMessage other = (LogoutMessage) o;
    return this.messageType.equals(((LogoutMessage) o).messageType);
  }

  @Override
  public int hashCode() {

    return Objects.hash(this.messageType);
  }
}
