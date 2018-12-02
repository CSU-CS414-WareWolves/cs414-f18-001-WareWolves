package client.gui.swing.info;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActiveGameInfoTest {

  private static final int gameIDs = 1234;
  private static final String gameBoard = "QbbqccRdd";
  private static final String opponents = "testUser2";
  private static final String dates = "02-14-18";
  private static final boolean turns = false;
  private static final boolean colors = false;
  private static final boolean ended = false;

  private static ActiveGameInfo testGameInfo;

  @BeforeEach
  void setUp() {
    testGameInfo = new ActiveGameInfo(gameIDs, gameBoard, opponents, dates, turns, colors, ended);
  }

  @Test
  void getGameID() {
    assertEquals(gameIDs, testGameInfo.getGameID());
  }

  @Test
  void getGameBoard() {
    assertEquals(gameBoard, testGameInfo.getGameBoard());
  }

  @Test
  void getOpponent() {
    assertEquals(opponents, testGameInfo.getOpponent());
  }

  @Test
  void getStartDate() {
    assertEquals(dates, testGameInfo.getStartDate());
  }

  @Test
  void getTurn() {
    assertEquals(turns, testGameInfo.getTurn());
  }

  @Test
  void getColor() {
    assertEquals(colors, testGameInfo.getColor());
  }

  @Test
  void getEnded() {
    assertEquals(gameIDs, testGameInfo.getGameID());
  }

  @Test
  void getInfoArray() {
    String[] expectedInfo = {String.valueOf(gameIDs), gameBoard, opponents, dates,
        String.valueOf(turns), String.valueOf(colors), String.valueOf(ended)};
    String[] resultInfo = testGameInfo.getInfoArray();
    assertArrayEquals(expectedInfo, resultInfo);

  }

  @Test
  void gameBoardNull() {
    assertThrows(IllegalArgumentException.class, () -> new ActiveGameInfo(gameIDs, null,
        opponents, dates, turns, colors, ended));
  }

  @Test
  void gameBoardInvalid() {
    assertThrows(IllegalArgumentException.class, () -> new ActiveGameInfo(gameIDs, "asca",
        opponents, dates, turns, colors, ended));
  }

  @Test
  void opponentNull() {
    assertThrows(IllegalArgumentException.class, () -> new ActiveGameInfo(gameIDs, gameBoard,
        null, dates, turns, colors, ended));
  }

  @Test
  void opponentInvalid() {
    assertThrows(IllegalArgumentException.class, () -> new ActiveGameInfo(gameIDs, gameBoard,
        "", dates, turns, colors, ended));
  }

}