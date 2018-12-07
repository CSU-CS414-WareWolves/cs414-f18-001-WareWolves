package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginResponseMessageTest {

  private static final boolean success = true;
  private static final String nickname = "Frodo";
  private static LoginResponseMessage testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new LoginResponseMessage(success, nickname);
  }

  @Test
  public void testType() {
    Assertions.assertEquals(ViewMessageType.LOGIN_RESPONSE, testMessage.messageType);
  }

  @Test
  public void testSuccess() {
    assertEquals(success, testMessage.success);
  }

  @Test
  public void testNickname() {
    assertEquals(nickname, testMessage.nickname);
  }
}