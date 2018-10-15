package client.game;

import static org.junit.jupiter.api.Assertions.*;

import client.game.pieces.King;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameBoardTest {

  GameBoard board;

  @BeforeEach
  void setUp() {
    board = new GameBoard("Ri8rd3Kj9ke4");
  }

  @Test
  void testConstructor(){
    GameBoard b = new GameBoard("ra0");
    assertNotEquals(null, b.getPieceAt(0,0),
        "Board should contain a piece at (0,0)");
    assertEquals(null, b.getPieceAt(1,1),
        "Board should not contain a piece at (1,1)");
  }

  @Test
  void testGetPieceAtCoordinates() {
    King expected = new King(new Point("j9"), true);
    assertEquals(expected, board.getPieceAt(9,9),
        "getPieceAt(9,9) should return a Black King");
  }

  @Test
  void testGetPieceAtPoint() {
    King expected = new King(new Point("e4"), false);
    assertEquals(expected, board.getPieceAt(new Point("e4")),
        "getPieceAt(4,4) should return a White King");
  }

  @Test
  void testMovePiece() {
    assertTrue(true);
  }


}