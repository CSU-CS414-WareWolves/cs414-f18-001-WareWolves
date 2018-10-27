package client.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
  private Game testGame;
  private Game loadedGame;
  @BeforeEach
  void setUp() {
    testGame = new Game();
    loadedGame = new Game("RaAraBkEeKiIQiJqfE", true);
  }

  @Test
  void turn() {
    assertEquals(false, testGame.turn(), "Should be Whites turn");
    assertEquals(true, loadedGame.turn(), "Should be Blacks turn.");
  }

  @Test
  void move() {
  }

  @Test
  void validMoves() {
  }

  @Test
  void getBoard() {

  }
}