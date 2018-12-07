package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ViewMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
 * All Swing panels that either send or receive messages extend from this class
 * It needs to extend JPanel to implement JPanel methods
 */
public abstract class SwingGUIController extends JPanel implements ActionListener {

  /**
   * Sends a view message to the panels controller
   * @param message the message to send
   */
  public abstract void sendMessage(ViewMessage message);

  /**
   * Receives a view message from its controller to process message
   * @param message the message to process
   */
  public abstract void receiveMessage(ViewMessage message);

  /**
   * Receives a Network Message message from its controller to process message
   * @param message the message to process
   */
  public abstract void receiveMessage(NetworkMessage message);

}
