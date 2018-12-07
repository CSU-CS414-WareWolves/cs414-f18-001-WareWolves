package edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.ViewMessageType;

/**
 * This is the base class for all message that the view is sending and receiving
 */
public abstract class ViewMessage {

  /**
   * The type of message
   */
  public final ViewMessageType messageType;

  /**
   * Sets the message type for the message
   * @param messageType the message type
   */
  protected ViewMessage(ViewMessageType messageType){
    this.messageType = messageType;
  }

}
