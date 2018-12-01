package client.gui.swing.panels;

import client.gui.ChadGameDriver;
import client.gui.swing.SwingGUIController;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.NetworkMessage;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SwingController extends SwingGUIController implements ChadGameDriver {

  private JPanel mainPanel;
  private MainMenuPanel mainMenuPanel;
  private LoginScreenPanel loginScreenPanel;




  @Override
  public void handleViewMessage(ViewMessage message) {

  }

  @Override
  public void handleNetMessage(NetworkMessage message) {

  }

  @Override
  public void sendMessage(ViewMessage message) {

  }

  @Override
  public void receiveMessage(ViewMessage message) {

  }

  @Override
  public void actionPerformed(ActionEvent e) {

  }
  public static void main(String[] args) {

    SwingController demoController = new SwingController();

    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        demoController.createAndShowGUI();
      }
    });
  }
  /**
   * Create the GUI and show it.  For thread safety, this method should be invoked from the
   * event-dispatching thread.
   */
  public void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("Login Panel Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    SwingController demo = new SwingController();

    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }


  private void createUIComponents() {
    mainMenuPanel = new MainMenuPanel(this);
    loginScreenPanel = new LoginScreenPanel(this);
  }
}
