package client.game.pieces;

import client.game.GameBoard;
import client.game.Point;

public class Rook extends Piece {

  public Rook(Point boardLocation, boolean color) {
    super(boardLocation, color);
  }

  @Override
  public Point[] GetValidMoves(GameBoard board) {
    return new Point[0];
  }

  @Override
  public boolean Move(String move, GameBoard board) {
    return false;
  }
}
