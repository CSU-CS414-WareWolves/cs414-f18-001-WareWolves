package client.game.pieces;

import client.game.GameBoard;
import client.game.Point;
import java.util.ArrayList;

public class King extends Piece {

  public King(Point boardLocation, boolean color) {
    super(boardLocation, color);
  }

  /**
   * Finds the valid moves for a King in chad chess. The King cannot leave its castle, and moves as
   * a combination of a King and Knight. The King can capture any opponent piece that it can land
   * on.
   *
   * @param board A Piece[][] that contains this piece.
   * @return An array of Points containing all valid moves for this King.
   */
  @Override
  public Point[] getValidMoves(Piece[][] board) {
    ArrayList<Point> result = this.KingMoves(board);
    result.addAll(KnightMoves(board));
    return result.toArray(new Point[0]);
  }

  /**
   * Finds all the valid knight moves from the current Piece's position give the board containing
   * the piece.
   *
   * @param board A Piece[][] representing the current board and containing this Piece.
   * @return All legal moves using Knight movement for the King type Piece.
   */
  private ArrayList<Point> KnightMoves(Piece[][] board) {
    ArrayList<Point> result = new ArrayList<>();
    int deltaCol[] = {1, 2, 2, 1, -1, -2, -2, -1};
    int deltaRow[] = {2, 1, -1, -2, -2, -1, 1, 2};
    for (int i = 0; i < 8; ++i) {
      int col = this.boardLocation.getArrayCol() + deltaCol[i];
      int row = this.boardLocation.getArrayRow() + deltaRow[i];
      Point possible = new Point(col, row);
      boolean insideCastle = this.getColor() ?
          GameBoard.isBlackCastle(possible) : GameBoard.isWhiteCastle(possible);
      if (insideCastle && (board[col][row] == null ||
          notSameColor(board[col][row]))) {
        result.add(possible);
      }
    }
    return result;
  }

  /**
   * Finds all valid King moves from the current Piece's position give the board containing the
   * piece.
   *
   * @param board A Piece[][] representing the current board and containing this Piece.
   * @return All legal moves using King movement for the King type Piece.
   */
  private ArrayList<Point> KingMoves(Piece[][] board) {
    ArrayList<Point> result = new ArrayList<>();
    for (int i = -1; i <= 1; ++i) {
      for (int j = -1; j <= 1; ++j) {
        if (i == 0 && j == 0) {
          continue;
        }
        int col = this.boardLocation.getArrayCol() + i;
        int row = this.boardLocation.getArrayRow() + j;
        Point possible = new Point(row, col);
        boolean insideCastle = this.getColor() ?
            GameBoard.isBlackCastle(possible) : GameBoard.isWhiteCastle(possible);
        if (insideCastle && (board[col][row] == null || notSameColor(board[col][row]))) {
          result.add(possible);
        }
      }
    }
    return result;
  }

  @Override
  public boolean move(String move, GameBoard board) {
    return false;
  }
}
