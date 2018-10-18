package client.game.pieces;

import client.game.GameBoard;
import client.game.Point;
import java.util.Objects;

public abstract class Piece {

  private Point boardLocation;
  private final boolean color; //Black == true, White == false

  public Piece(Point boardLocation, boolean color) {
    this.boardLocation = boardLocation;
    this.color = color;
  }

  /**
   * Get the color of this Piece.
   * @return True if the Piece is Black, False if it is White.
   */
  public boolean getColor() {
    return this.color;
  }

  /**
   * Get the Point representing this pieces position on its board.
   * @return Point containing the current position.
   */
  public Point getBoardLocation() {
    return this.boardLocation;
  }

  /**
   * Finds the set of Points that the piece can move to, given its position and the current board.
   * @param board A GameBoard that contains this piece.
   * @return A set of Points that this Piece can move to legally.
   */
  public abstract Point[] getValidMoves(GameBoard board);

  /**
   * Moves the Piece to its new position.
   * @param move String ([a-l][0-11]) representing the new position to move to.
   * @param board A GameBoard that contains this piece.
   * @return True if the move was successful, False otherwise.
   */
  public abstract boolean move(String move, GameBoard board);

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
