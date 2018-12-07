package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActiveGameMessageTest {

  private static ActiveGameMessage testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new ActiveGameMessage();
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.ACTIVE_GAMES, testMessage.messageType);
  }

}