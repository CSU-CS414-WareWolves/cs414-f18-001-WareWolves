package client.game.pieces;

import client.Point;
import java.util.ArrayList;

abstract class LinePiece extends Piece {

  LinePiece(Point boardLocation, boolean color) {
    super(boardLocation, color);
  }

  LinePiece(String str) {
    super(str);
  }

  /**
   * Searches the Board for valid move places in a straight line determined by deltaCol and
   * deltaRow.
   *
   * @param board A Piece[][] that contains this piece.
   * @param deltaCol How much to move the column each step.
   * @param deltaRow How much to move the row each step.
   * @return An ArrayList of points with every valid move in the direction searched.
   */
  ArrayList<Point> search(Piece[][] board, int[] deltaCol, int[] deltaRow) {
    ArrayList<Point> result = new ArrayList<>();
    for (int i = 0; i < deltaCol.length; ++i) {
      int col = this.boardLocation.getArrayCol() + deltaCol[i];
      int row = this.boardLocation.getArrayRow() + deltaRow[i];
      while (row >= 0 && row < 12 && col >= 0 && col < 12 && board[col][row] == null) {
        result.add(new Point(col, row));
        row += deltaRow[i];
        col += deltaCol[i];
      }
      if (row >= 0 && row < 12 && col >= 0 && col < 12 && canCapture(board[col][row])) {
        result.add(new Point(col, row));
      }
    }
    return result;
  }


}
