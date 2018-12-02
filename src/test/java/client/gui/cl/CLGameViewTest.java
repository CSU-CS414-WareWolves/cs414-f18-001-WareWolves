package client.gui.cl;

import static org.junit.jupiter.api.Assertions.*;

import client.game.Game;
import client.game.pieces.King;
import client.game.pieces.Piece;
import client.game.pieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CLGameViewTest {

  private CLGameView game;
  private String default_gb = "rdCreDRiHRjIrcCkdDreERhHKiIRjJrcDrdERhIRiJrcERhJreCRjH";
  // Piece representations
  private static final String WKING = "\u2654";
  private static final String BKING = "\u265A";

  @BeforeEach
  void setUp() {
    game = new CLGameView();
  }

  @Test
  void showCurrentGames() {
    int[] ids = {23,12};
    String[] nicks = {"theGameMaster", "AI"};
    try {
      game.showCurrentGames(ids, nicks);
    } catch (Exception e) {
      fail("");
    }
  }

  @Test
  void showGameBoard() {
    try {
      game.showGameBoard(default_gb);
    } catch (Exception e) {
      fail("");
    }
  }

  @ParameterizedTest
  @CsvSource({"1,2", "1,3", "1,4", "5,2", "5,3", "5,4", "2,1", "3,1", "4,1", "2,5", "3,5", "4,5",
      "6,7", "6,8", "6,9", "10,7", "10,8", "10,9", "7,6", "8,6", "9,6", "7,10", "8,10", "9,10"})
  void checkWallTrue(int row, int col) {
    assertTrue(game.checkWall(row,col));
  }

  @ParameterizedTest
  @CsvSource({"1,7", "1,8", "1,9", "6,2", "6,3", "6,4", "1,1", "2,2", "3,3", "5,5", "6,6", "7,7",
      "8,8", "9,9", "10,10", "11,11", "12,12"})
  void checkWallFalse(int row, int col) {
    assertFalse(game.checkWall(row,col));
  }

  @ParameterizedTest
  @CsvSource({"kdD","KdD"})
  void pieceToCharacter(String s) {
    Piece p = new King(s);
    String check = game.pieceToCharacter(p);
    if(p.getColor()) {
      assertEquals(check, BKING);
    }
    else {
      assertEquals(check, WKING);
    }
  }

  @Test
  void showValidMoves() {
    String[] moves = {"bB","eE","dD","aA"};
    try {
      game.showValidMoves(moves);
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void showInGameMenu() {
    try {
      game.showInGameMenu();
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }

}