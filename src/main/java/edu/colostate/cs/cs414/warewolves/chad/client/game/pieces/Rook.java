package edu.colostate.cs.cs414.warewolves.chad.client.game.pieces;

import edu.colostate.cs.cs414.warewolves.chad.client.Point;

public class Rook extends LinePiece {

  public Rook(Point boardLocation, boolean color) {
    super(boardLocation, color);
  }

  public Rook(String rook) {
    super(rook);
  }

  /**
   * Finds all the valid moves for a Rook in chadgame chess. A Rook moves along vertical and horizontal
   * lines. A Rook can only capture a enemy non-King piece if the Rook is on an enemy wall and the
   * other piece is in its own castle, or the Rook is in its own castle and the other piece is on
   * the wall. A Rook can always put a King in check if no other pieces are between the Rook and the
   * King. In all other cases the other piece simply blocks the Rook's movement.
   *
   * @param board A Piece[][] that contains this piece.
   * @return An array of Points containing all valid moves for this Rook.
   */
  @Override
  public Point[] getValidMoves(Piece[][] board) {
    int deltaX[] = {1, -1, 0, 0};
    int deltaY[] = {0, 0, 1, -1};

    return this.search(board, deltaX, deltaY).toArray(new Point[0]);
  }


  /**
   * Rook extends functionality of Piece.move to account for promotion.
   * Moves this Piece to the new point, and promotes it to a Queen if in the opponents castle.
   *
   * @param move String ([a-l][A-L]) representing the new position to move to.
   * @param board A Piece[][] that contains this piece.
   */
  @Override
  public boolean move(Point move, Piece[][] board) {
    boolean res = super.move(move, board);
    if (inOtherCastle()) {
      board[this.boardLocation.getArrayCol()][this.boardLocation.getArrayRow()] = new Queen(
          this.boardLocation, this.getColor());
    }
    return res;
  }
}
