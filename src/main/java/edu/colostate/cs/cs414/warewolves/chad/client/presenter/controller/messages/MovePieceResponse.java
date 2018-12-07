package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;
import java.util.Objects;

public class MovePieceResponse extends ViewMessage {

  /**
   * A message sent in response to the move piece request
   */
  public final String message;

  public final String gameBoard;

  /**
   * Sets if the request was successful
   * @param message is either who's turn it is, whether white or black has won, or whether it is a draw
   */
  public MovePieceResponse(String message, String board) {
    super(ViewMessageType.MOVE_PIECE_RESPONSE);
    this.message = message;
    this.gameBoard = board;
  }

  @Override
  public boolean equals(Object o){
    if (o == null || !(o instanceof MovePieceResponse)) {
      return false;
    }
    MovePieceResponse other = (MovePieceResponse) o;
    return message.equals(other.message) && gameBoard.equals(other.gameBoard);
  }

  @Override
  public int hashCode() {

    return Objects.hash(message, gameBoard);
  }

}
