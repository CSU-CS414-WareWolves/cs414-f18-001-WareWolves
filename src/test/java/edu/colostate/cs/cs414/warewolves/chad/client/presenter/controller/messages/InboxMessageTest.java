package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InboxMessageTest {

  private static final String nickname = "1337";
  private static InboxMessage testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new InboxMessage();
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.INBOX, testMessage.messageType);
  }

}