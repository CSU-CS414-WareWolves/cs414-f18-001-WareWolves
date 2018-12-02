package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;
import client.presenter.controller.util.HashPasswords;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * Logout message only tells the controllers the view wants to log out
 */
public class Logout extends ViewMessage{

  /**
   *
   */
  public Logout() {
    super(ViewMessageType.LOGOUT);
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof Logout)) {
      return false;
    }
    Logout other = (Logout) o;
    return this.messageType.equals(((Logout) o).messageType);
  }

  @Override
  public int hashCode() {

    return Objects.hash(this.messageType);
  }
}
