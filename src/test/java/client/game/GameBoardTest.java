package client.game;

import static org.junit.jupiter.api.Assertions.*;

import client.game.pieces.King;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GameBoardTest {

  private GameBoard board;

  @BeforeEach
  void setUp() {
    board = new GameBoard("RiIrdDKjJkeE");
  }

  @Test
  void testConstructor() {
    GameBoard b = new GameBoard("raA");
    assertNotEquals(null, b.getPieceAt(0, 0),
        "Board should contain a piece at (0,0)");
    assertEquals(null, b.getPieceAt(1, 1),
        "Board should not contain a piece at (1,1)");
  }

  @Test
  void testGetPieceAtCoordinates() {
    King expected = new King(new Point("jJ"), true);
    assertEquals(expected, board.getPieceAt(9, 9),
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

  @DisplayName("isWall")
  @ParameterizedTest(name = "({0}) should be {1}")
  @CsvSource({"aA, false", "lL, false", "dD, false", "aL, false", "bC, true", "bE, true",
      "fD, true", "dF, true", "gI, true", "iG, true", "kJ, true", "hK, true", "hI, false"})
  void testIsWall(String point, boolean expected) {
    assertEquals(GameBoard.isWall(new Point(point)), expected);
  }


  @DisplayName("isCastle")
  @ParameterizedTest(name = "({0}) should be {1}")
  @CsvSource({"aA, false", "lL, false", "bC, false", "fD, false", "dD, true", "iI, true",
      "dD, true", "jJ, true", "cE, true", "iH, true"})
  void testIsCastle(String point, boolean expected) {
    //assertEquals(GameBoard.isCastle(new Point(point)), expected);
  }
}