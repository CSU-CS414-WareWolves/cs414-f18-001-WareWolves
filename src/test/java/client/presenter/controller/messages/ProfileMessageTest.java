package client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfileMessageTest {

  private static final String nickname = "1337";
  private static ProfileMessage testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new ProfileMessage(nickname);
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.PROFILE, testMessage.messageType);
  }

  @Test
  public void testNickname() {
    assertEquals(nickname, testMessage.nickname);
  }

}