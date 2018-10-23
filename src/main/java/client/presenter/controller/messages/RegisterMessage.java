package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;
import client.presenter.controller.util.HashPasswords;
import java.security.NoSuchAlgorithmException;


/**
 * A message from the view with the email, password, and nickname for a register attempt
 */
public class RegisterMessage extends ViewMessage{
  /**
   * The email address the user has chosen
   */
  public final String email;
  /**
   * The password the user has chosen
   */
  public final String password;

  /**
   * The nickname the user has chosen
   */
  public final String nickname;

  /**
   * email, password, and nickname for a registration attempt
   * @param email the email
   * @param password the password
   * @throws NoSuchAlgorithmException could not find the SHA1 hash
   */
  public RegisterMessage(String email, String password, String nickname)
      throws NoSuchAlgorithmException {
    super(ViewMessageType.REGISTER);
    this.email = email;
    this.password = HashPasswords.SHA1FromString(password);
    this.nickname = nickname;

  }


}