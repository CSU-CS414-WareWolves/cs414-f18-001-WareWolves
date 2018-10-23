package client.presenter.controller.util;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.SharedTestAttributes;
import client.presenter.controller.ViewMessageType;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.UnregisterMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;

class ViewMessageFactoryTest implements SharedTestAttributes{

  private final ViewMessageFactory factory = ViewMessageFactory.getInstance();

  @Test
  void createViewMessageRegister() throws NoSuchAlgorithmException {

    RegisterMessage expected =
        new RegisterMessage(TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD, TEST_NICKNAME);

    String[] info = {TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD, TEST_NICKNAME};
    testMessageEquals(expected, info, ViewMessageType.REGISTER);

  }

  @Test
  void createViewMessageUnregister() throws NoSuchAlgorithmException {

    UnregisterMessage expected =
        new UnregisterMessage(TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD, TEST_NICKNAME);

    String[] info = {TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD, TEST_NICKNAME};
    testMessageEquals(expected, info, ViewMessageType.UNREGISTER);
  }

  @Test
  void createViewMessageLogin() throws NoSuchAlgorithmException {

    LoginMessage expected =
        new LoginMessage(TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD);

    String[] info = {TEST_LOGIN_EMAIL, TEST_LOGIN_PASSWORD};
    testMessageEquals(expected, info, ViewMessageType.LOGIN);
  }

  @Test
  void createViewMessageViewMoves() throws NoSuchAlgorithmException {

    ViewValidMoves expected = new ViewValidMoves(FROM_COL, FROM_ROW);

    String[] info = {Integer.toString(FROM_COL), Integer.toString(FROM_ROW)};
    testMessageEquals(expected, info, ViewMessageType.SHOW_VALID_MOVES);
  }

  @Test
  void createViewMessageMovePiece() throws NoSuchAlgorithmException {

    MovePieceMessage expected = new MovePieceMessage(FROM_COL, FROM_ROW, TO_COL, TO_COL);

    String[] info = {Integer.toString(FROM_COL), Integer.toString(FROM_ROW),
        Integer.toString(TO_COL), Integer.toString(TO_COL)};
    testMessageEquals(expected, info, ViewMessageType.MOVE_PIECE);
  }

  /**
   * Tests different types of messages to see if they are created correctly
   * @param expected the expected message
   * @param info the info to build the message
   * @param type the type of message
   * @throws NoSuchAlgorithmException the SHA1 hash could not be found
   */
  private void testMessageEquals(ViewMessage expected, String[] info, ViewMessageType type)
      throws NoSuchAlgorithmException {
    ViewMessage result = factory.createViewMessage(type, info);
    assertEquals(expected, result);
  }

}