package edu.colostate.cs.cs414.warewolves.chad.client.gui;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ViewMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;

/**
 * Any Chad Game Driver needs these methods
 */
public interface ChadGameDriver {

  String DEFAULT_GAME_BOARD = "rdCreDRiHRjIrcCkdDreERhHKiIRjJrcDrdERhIRiJrcERhJreCRjH";

  /**
   * Processes a given view message based on the message type
   * @param message the message to process
   */
  void handleViewMessage(ViewMessage message);
  /**
   * Processes a given view message based on the message type
   * @param message the message to process
   */
  void handleNetMessage(NetworkMessage message);
  /**
   * Starts up the GUI interface
   */
  void createAndShowGUI();

}
