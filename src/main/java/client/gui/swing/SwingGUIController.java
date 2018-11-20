package client.gui.swing;

import client.presenter.controller.messages.ViewMessage;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public abstract class SwingGUIController extends JPanel implements ActionListener {

  public abstract void sendMessage(ViewMessage message);

}
