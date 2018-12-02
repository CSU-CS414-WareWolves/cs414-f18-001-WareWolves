package client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InboxMessageTest {

  private static final String nickname = "1337";
  private static InboxMessage testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new InboxMessage(nickname);
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.INBOX, testMessage.messageType);
  }

  @Test
  public void testNickname() {
    assertEquals(nickname, testMessage.nickname);
  }
}