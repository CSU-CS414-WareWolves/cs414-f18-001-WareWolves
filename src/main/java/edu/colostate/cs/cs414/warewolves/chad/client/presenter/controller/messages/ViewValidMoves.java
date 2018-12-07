package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import edu.colostate.cs.cs414.warewolves.chad.client.Point;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;

/**
 * The message asks for the valid moves of a piece on a given row and column
 */
public class ViewValidMoves extends ViewMessage {

  public final Point location;

  /**
   * Set the row and column of the piece
   * @param col the column
   * @param row the row
   */
  public ViewValidMoves(int col, int row ){
    this( new Point(col, row));
  }

  public ViewValidMoves(Point location){
    super(ViewMessageType.SHOW_VALID_MOVES);
    this.location = location;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof ViewValidMoves)) {
      return false;
    }
    ViewValidMoves other = (ViewValidMoves) o;
    return location.equals(other.location);
  }

  @Override
  public int hashCode() {

    return location.hashCode();
  }

}
