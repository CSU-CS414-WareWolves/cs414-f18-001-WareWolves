package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;

/**
 * A message with the email and password for a login attempt
 */
public class LoginMessage extends ViewMessage{

  /**
   * The email address
   */
  public final String email;
  /**
   * The password
   */
  public final String password;

  /**
   * Sets the email and password information for the message
   * @param email the email
   * @param password the password
   */
  public LoginMessage(String email, String password) {
    super(ViewMessageType.LOGIN);

    this.email = email;
    this.password = password;
  }
}
