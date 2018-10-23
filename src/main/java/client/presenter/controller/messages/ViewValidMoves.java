package client.presenter.controller.messages;

import client.game.Point;
import client.presenter.controller.ViewMessageType;

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
    super(ViewMessageType.SHOW_VALID_MOVES);
    this.location = new Point(col, row);
  }

}
