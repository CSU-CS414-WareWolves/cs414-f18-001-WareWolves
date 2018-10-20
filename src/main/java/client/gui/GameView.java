package client.gui;

import client.game.GameBoard;
import client.game.Point;
import java.util.Collection;

public interface GameView {
  // Prints the board from the given state
  public void showGameBoard(GameBoard gb);
  public void showValidMoves(Collection<Point> list);
}
