package client.game.pieces;

import client.Point;
import client.game.GameBoard;
import java.util.Arrays;
import java.util.Objects;

public abstract class Piece {

  Point boardLocation;
  private final boolean color; //Black == true, White == false

  Piece(Point boardLocation, boolean color) {
    this.boardLocation = boardLocation;
    this.color = color;
  }

  Piece(String str) {
    this(new Point(str.substring(0, 2)), Boolean.parseBoolean(str.substring(2)));
  }

  /**
   * Get the color of this Piece.
   *
   * @return True if the Piece is Black, False if it is White.
   */
  public boolean getColor() {
    return this.color;
  }

  /**
   * Get the Point representing this pieces position on its board.
   *
   * @return Point containing the current position.
   */
  public Point getBoardLocation() {
    return this.boardLocation;
  }

  /**
   * Finds the set of Points that the piece can move to, given the board it is currently on.
   *
   * @param board A Piece[][] that contains this piece.
   * @return An array of Points that this Piece can move to legally.
   */
  public abstract Point[] getValidMoves(Piece[][] board);

  /**
   * Moves the Piece to its new position if that position is valid for the Piece.
   *
   * @param move String ([a-l][A-L]) representing the new position to move to.
   * @param board A Piece[][] that contains this piece.
   * @return True if the move was successful, False otherwise.
   */
  public boolean move(Point move, Piece[][] board) {
    if (Arrays.stream(this.getValidMoves(board)).anyMatch(move::equals)) {
      board[this.boardLocation.getArrayCol()][this.boardLocation.getArrayRow()] = null;
      this.boardLocation = move;
      board[this.boardLocation.getArrayCol()][this.boardLocation.getArrayRow()] = this;
      return true;
    }
    return false;
  }

  /**
   * Determines if another Piece is an eligible capture target.
   *
   * @param other Another Piece to compare with
   * @return True if this Piece can capture other, false otherwise.
   */
  boolean canCapture(Piece other) {
    if (other == null || other.color == this.color) {
      return false;
    }
    boolean isKing = (other.getClass() == King.class) && notSameColor(other);
    return isKing || rightOfCapture(other, this) || rightOfCapture(this, other);
  }

  private boolean rightOfCapture(Piece P1, Piece P2) {
    return P1.inOwnCastle() && P2.onOtherWall();
  }

  /**
   * Determines if a Piece is in its own castle.
   *
   * @return True if the piece is in its own castle, false otherwise.
   */
  private boolean inOwnCastle() {
    return inOwnCastle(this.getBoardLocation());
  }

  /**
   * Determines if a Point is in this Pieces castle.
   *
   * @param p Point to check.
   * @return True if p is in this Pieces castle, false otherwise.
   */
  boolean inOwnCastle(Point p) {
    return this.getColor() ? GameBoard.isBlackCastle(p) : GameBoard.isWhiteCastle(p);
  }

  /**
   * Determines if a Point is in its opponent's castle.
   *
   * @param p Point to check.
   * @return True if P is in this Pieces opponent's castle, False otherwise.
   */
  private boolean inOtherCastle(Point p) {
    return this.getColor() ? GameBoard.isWhiteCastle(p) : GameBoard.isBlackCastle(p);
  }

  /**
   * Determines if a Piece is in its opponent's castle.
   *
   * @return True if the piece is in its opponent's castle, False otherwise.
   */
  boolean inOtherCastle() {
    return inOtherCastle(this.getBoardLocation());
  }

  /**
   * Determines if a Piece is on its opponent's wall.
   *
   * @return True if the piece is on its opponent's wall, False otherwise.
   */
  private boolean onOtherWall() {
    return this.color ? GameBoard.isWhiteWall(this.getBoardLocation())
        : GameBoard.isBlackWall(this.getBoardLocation());
  }

  /**
   * Determines if two Pieces are not the same color.
   *
   * @param other A Piece to compare to.
   * @return True if the two pieces are not the same color, false otherwise.
   */
  boolean notSameColor(Piece other) {
    return other == null || other.getColor() != this.getColor();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Piece piece = (Piece) o;
    return color == piece.color &&
        Objects.equals(boardLocation, piece.boardLocation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(boardLocation, color);
  }
}
