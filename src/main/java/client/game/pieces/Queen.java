package client.game.pieces;

import client.game.GameBoard;
import client.game.Point;

public class Queen extends Piece {

  public Queen(Point boardLocation, boolean color) {
    super(boardLocation, color);
  }

  @Override
  public Point[] getValidMoves(GameBoard board) {
    return new Point[0];
  }

  @Override
  public boolean move(String move, GameBoard board) {
    return false;
  }
}
