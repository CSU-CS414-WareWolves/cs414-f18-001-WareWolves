package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing;

import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ViewMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public abstract class SwingGUIController extends JPanel implements ActionListener {

  public abstract void sendMessage(ViewMessage message);
  public abstract void receiveMessage(ViewMessage message);
  public abstract void receiveMessage(NetworkMessage message);

}
