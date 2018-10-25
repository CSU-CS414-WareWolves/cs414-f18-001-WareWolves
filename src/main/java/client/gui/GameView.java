package client.gui;

import client.game.GameBoard;
import client.game.Point;
import java.util.Collection;

public interface GameView {
  // Prints the board from the given state
  void showGameBoard(GameBoard gb);

  // Prints the valid moves for the current player
  void showValidMoves(Collection<Point> list);
}
