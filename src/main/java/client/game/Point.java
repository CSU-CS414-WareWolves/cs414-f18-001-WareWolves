package client.game;

import java.util.Objects;

public class Point {
  private int col;
  private int row;

  /**
   * Constructs a Point from two int position markers.
   * @param col Column int for the point to represent.
   * @param row Row int for the point to represent.
   */
  public Point(int col, int row) {
    this.col = col;
    this.row = row;
  }

  /**
   * Constructs a Point form a char representing the column and an int
   * @param col Column char a-l for the point to represent
   * @param row Row int for the point to represent
   */
  public Point(char col, int row) {
    this((col - 'a'), row);
  }

  /**
   * Constructs a Point from a String containing a col,row pair where the col is a char a-l
   * @param point String representing the Point to be constructed.
   */
  public Point(String point) {
    this( (point.charAt(0) - 'a'), (point.charAt(1) - 'A'));

  }

  /**
   * Gets the column value of the Point.
   * @return Column represented by the Point.
   */
  public int getArrayCol() {
    return col;
  }

  /**
   * Sets the value of the column represented by the Point.
   * @param col The new value for the column that the point represents.
   */
  public void setCol(int col) {
    this.col = col;
  }

  /**
   * Gets the Row value of the Point.
   * @return Row represented by the Point.
   */
  public int getArrayRow() {
    return row;
  }

  /**
   * Sets the value of the row represented by the Point.
   * @param row The new value for the row that the point represents.
   */
  public void setRow(int row) {
    this.row = row;
  }



  @Override
  public String toString() {
    char c = (char)('a' + col);
    return ("" + c) + (row+1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Point point = (Point) o;
    return col == point.col &&
        row == point.row;
  }

  @Override
  public int hashCode() {

    return Objects.hash(col, row);
  }
}
