package client.gui;

import client.game.GameBoard;
import client.game.Point;
import java.util.ArrayList;
import java.util.Collection;

public interface GameView {
  // Prints a list of current games
  void showCurrentGames(ArrayList<String> games);

  // Prints the board from the given state
  void showGameBoard(GameBoard gb);

  // Prints the valid moves for the current player
  void showValidMoves(ArrayList<String> list);

  // Prints the "in-game" menu
  void showIngameMenu();
}
