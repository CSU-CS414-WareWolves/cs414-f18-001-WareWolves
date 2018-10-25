package client.presenter.controller.messages;

import client.presenter.controller.ViewMessageType;

public class MovePieceResponse extends ViewMessage {

  /**
   * A response of success for the move piece request
   */
  public final boolean success;

  /**
   * Sets if the request was successful
   * @param success success of the move piece request
   */
  protected MovePieceResponse(boolean success) {
    super(ViewMessageType.MOVE_PIECE_RESPONSE);
    this.success = success;
  }
}
