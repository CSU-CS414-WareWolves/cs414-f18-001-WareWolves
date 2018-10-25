package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;

public class ViewValidMovesResponse extends ViewMessage {

  /**
   * An array of locations that are valid moves
   */
  public final String[] locations;

  /**
   * Sets the valid move locations
   * @param locations the valid moves in response to the view valid moves request
   */
  protected ViewValidMovesResponse(String[] locations) {
    super(ViewMessageType.MOVE_PIECE_RESPONSE);
    this.locations = locations;
  }
}
