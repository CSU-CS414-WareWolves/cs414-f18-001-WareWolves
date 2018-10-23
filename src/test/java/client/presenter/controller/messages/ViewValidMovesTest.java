package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewValidMovesTest {

  private final int rowTest = 5;
  private final int colTest = 7;

  private ViewValidMoves testValidMove = new ViewValidMoves(colTest, rowTest );

  @Test
  public void testCol(){
    assertEquals(colTest, testValidMove.location.getArrayCol());
  }

  @Test
  public void testRow(){
    assertEquals(rowTest, testValidMove.location.getArrayRow());
  }

  @Test
  public void testType() {assertEquals(ViewMessageType.SHOW_VALID_MOVES, testValidMove.messageType);}

}