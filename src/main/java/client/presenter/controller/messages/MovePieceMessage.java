package client.presenter.controller.messages;

import client.game.Point;
import client.presenter.controller.ViewMessageType;

/**
 * Message to the controller to move a piece from a location to another location
 */
public class MovePieceMessage extends ViewMessage {

  public final Point fromLocation;
  public final Point toLocation;


  /**
   * Sets the were the piece is currently to were it is being moved to
   * @param fromCol the current col
   * @param fromRow the current row
   * @param toCol the move to col
   * @param toRow the move to row
   */
  public MovePieceMessage(int fromCol, int fromRow, int toCol, int toRow ){
    super(ViewMessageType.MOVE_PIECE);
    this.fromLocation = new Point(fromCol, fromRow);
    this.toLocation = new Point(toCol, toRow);
  }

}
