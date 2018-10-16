package client.game;

import static org.junit.jupiter.api.Assertions.*;

import client.game.pieces.King;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameBoardTest {

  GameBoard board;

  @BeforeEach
  void setUp() {
    board = new GameBoard("RiIrdDKjJkeE");
  }

  @Test
  void testConstructor(){
    GameBoard b = new GameBoard("raA");
    assertNotEquals(null, b.getPieceAt(0,0),
        "Board should contain a piece at (0,0)");
    assertEquals(null, b.getPieceAt(1,1),
        "Board should not contain a piece at (1,1)");
  }

  @Test
  void testGetPieceAtCoordinates() {
    King expected = new King(new Point("jJ"), true);
    assertEquals(expected, board.getPieceAt(9,9),
        "getPieceAt(9,9) should return a Black King");
  }

  @Test
  void testGetPieceAtPoint() {
    King expected = new King(new Point("eE"), false);
    assertEquals(expected, board.getPieceAt(new Point("eE")),
        "getPieceAt(4,4) should return a White King");
  }

  @Test
  void testMovePiece() {
    assertTrue(true);
  }


}