package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InviteMessageTest {
  private static final String sender = "1337";
  private static final String recipient = "6969";
  private static InviteMessage testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new InviteMessage(sender, recipient);
  }

  @Test
  public void testType() {
    Assertions.assertEquals(ViewMessageType.NEW_INVITE, testMessage.messageType);
  }

  @Test
  public void testSender() {
    assertEquals(sender, testMessage.sender);
  }

  @Test
  public void testRecipient() {
    assertEquals(recipient, testMessage.recipient);
  }
}