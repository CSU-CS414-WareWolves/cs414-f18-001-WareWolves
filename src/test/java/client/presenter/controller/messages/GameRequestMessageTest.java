package client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameRequestMessageTest {

  private static final int gameID = 1337;
  private static GameRequestMessage testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new GameRequestMessage(gameID);
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.GAME_REQUEST, testMessage.messageType);
  }

  @Test
  public void testGameID() {
    assertEquals(gameID, testMessage.gameID);
  }
}