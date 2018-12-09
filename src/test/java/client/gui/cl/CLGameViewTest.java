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
    boolean[] ts = {true, false};
    boolean[] color = {false, true};

    try {
      game.showCurrentGames(ids, nicks, ts, color);
    } catch (Exception e) {
      fail("");
    }
  }

  @Test
  void showGameBoard() {
    try {
      String default_gb = "rdCreDRiHRjIrcCkdDreERhHKiIRjJrcDrdERhIRiJrcERhJreCRjH";
      game.showGameBoard(default_gb);
    } catch (Exception e) {
      fail("");
    }
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

}