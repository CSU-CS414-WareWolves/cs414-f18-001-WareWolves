package client.game.pieces;

import static org.junit.jupiter.api.Assertions.*;

import client.game.GameBoard;
import client.game.Point;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Test Rook")
class RookTest {

  private GameBoard board;

  @BeforeEach
  void setUp() {
    board = new GameBoard("RlARlBRkARhDRgErgJRaLRbLraKRiJkdDKhHQdAQcGQkHRbARaBqaARbBRcC");
  }

  @DisplayName("ValidMoves")
  @ParameterizedTest(name = "({0})")
  @CsvSource({"lA,true,''", "aL, true,''", "gE, true, aEbEcEdEeEfEhEiEjEkElEgAgBgCgDgFgGgHgI",
      "kA, true, eAfAgAhAiAjAkBkCkDkEkFkG", "gJ, false, aJbJcJdJeJfJhJiJgFgGgHgIgKgL",
      "iJ,true,jJkJlJhJgJiAiBiCiDiEiFiGiHiIiKiL", "hD, True, iDjDkDlDgDfDeDdDhAhBhChEhFhG"})
  void getValidMoves(String point, boolean color, String expectedPoints) {
    Rook testRook = new Rook(new Point(point), color);
    Set<Point> points = new HashSet<>();
    for (int i = 0; i < expectedPoints.length(); i = i + 2) {
      points.add(new Point(expectedPoints.substring(i, i + 2)));
    }
    Point[] result = testRook.getValidMoves(board.getPieces());
    Set<Point> resSet = new HashSet<>(Arrays.asList(result));
    assertEquals(points, resSet, "Expected and Actual moves do not match.");
  }
}