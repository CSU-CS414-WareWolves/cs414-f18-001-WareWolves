package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;
import client.presenter.controller.util.HashPasswords;
import java.security.NoSuchAlgorithmException;

/**
 * A message from the view with the email and password for a login attempt
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
   * @throws NoSuchAlgorithmException could not find the SHA1 hash
   */
  public LoginMessage(String email, String password) throws NoSuchAlgorithmException {
    super(ViewMessageType.LOGIN);

    this.email = email;
    this.password = HashPasswords.SHA1FromString(password);
  }
}
