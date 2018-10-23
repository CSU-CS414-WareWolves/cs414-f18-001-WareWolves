package client.presenter.controller.messages;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.ViewMessageType;
import org.junit.jupiter.api.Test;

class MovePieceMessageTest {

  private final int fromRow = 5;
  private final int fromCol = 7;
  private final int toRow = 8;
  private final int toCol = 10;


  private MovePieceMessage testMove = new MovePieceMessage(fromCol, fromRow, toCol, toRow );

  @Test
  public void testFromCol(){
    assertEquals(fromCol, testMove.fromLocation.getArrayCol());
  }

  @Test
  public void testFromRow(){
    assertEquals(fromRow, testMove.fromLocation.getArrayRow());
  }

  @Test
  public void testToCol(){
    assertEquals(toCol, testMove.toLocation.getArrayCol());
  }

  @Test
  public void testToRow(){
    assertEquals(toRow, testMove.toLocation.getArrayRow());
  }

  @Test
  public void testType() {assertEquals(ViewMessageType.MOVE_PIECE, testMove.messageType);}

}