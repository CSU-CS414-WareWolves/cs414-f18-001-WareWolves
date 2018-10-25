package client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovePieceResponseTest {

  private static final boolean success = true;
  private static MovePieceResponse testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new MovePieceResponse(success);
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.MOVE_PIECE_RESPONSE, testMessage.messageType);
  }

  @Test
  public void testSuccess() {
    assertEquals(success, testMessage.success);
  }

}