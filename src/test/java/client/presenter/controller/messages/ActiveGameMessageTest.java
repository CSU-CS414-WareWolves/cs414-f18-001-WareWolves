package client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActiveGameMessageTest {

  private static final String nickname = "1337";
  private static ActiveGameMessage testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new ActiveGameMessage(nickname);
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.ACTIVE_GAMES, testMessage.messageType);
  }

  @Test
  public void testNickname() {
    assertEquals(nickname, testMessage.nickname);
  }
}