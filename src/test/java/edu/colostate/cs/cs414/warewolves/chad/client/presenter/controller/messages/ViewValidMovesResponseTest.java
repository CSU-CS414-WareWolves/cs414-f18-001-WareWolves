package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViewValidMovesResponseTest {

  private static final String[] locations = {"B4"};
  private static ViewValidMovesResponse testMessage;

  @BeforeEach
  public void setup() {
    testMessage = new ViewValidMovesResponse(locations);
  }

  @Test
  public void testType() {
    assertEquals(ViewMessageType.SHOW_VALID_MOVES_RESPONSE, testMessage.messageType);
  }

  @Test
  public void testLocations() {
    assertEquals(locations, testMessage.locations);
  }
}