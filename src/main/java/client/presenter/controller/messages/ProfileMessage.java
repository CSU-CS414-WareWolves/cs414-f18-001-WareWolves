package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;
import java.util.Objects;

/**
 * A message from the view with the nickname for the requested user
 */
public class ProfileMessage extends ViewMessage{

  /**
   * The nickname the user wants to see the profile of
   */
  public final String nickname;

  /**
   * Nickname for the requested profile is sent
   * @param nickname nickname for the requested profile
   */
  public ProfileMessage(String nickname) {
    super(ViewMessageType.PROFILE);
    this.nickname = nickname;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof ProfileMessage)) {
      return false;
    }
    ProfileMessage other = (ProfileMessage) o;
    return nickname.equals(other.nickname);
  }
}
