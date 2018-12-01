package client.gui.swing.panels;

import client.gui.swing.SwingGUIController;
import client.presenter.controller.messages.LoginResponseMessage;
import client.presenter.controller.messages.RegisterResponseMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.LoginResponse;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginScreenPanel extends SwingGUIController {

  private CardLayout cardLayout;

  private JPanel mainPanel;
  private LoginPanel loginPanel;
  private RegisterNewAccountPanel registerNewAccountPanel;
  private SwingGUIController controller;

  public LoginScreenPanel(SwingGUIController controller) {
    super();
    this.controller = controller;
    //this.add(mainPanel);
    cardLayout = (CardLayout) mainPanel.getLayout();
    cardLayout.show(mainPanel, "Login");

  }

  @Override
  public void sendMessage(ViewMessage message) {

    controller.sendMessage(message);

  }

  @Override
  public void receiveMessage(ViewMessage message) {

    String messageInfo = "This is a default message!!! I should not be seen";

    if(message instanceof LoginResponseMessage){
      messageInfo = "The password or email address entered is invalid";
    }

    if(message instanceof RegisterResponseMessage){
      RegisterResponseMessage registerResponse = (RegisterResponseMessage) message;
      messageInfo = Arrays.toString(registerResponse.messages);
    }

    JOptionPane.showMessageDialog(this, messageInfo);

  }

  @Override
  public void actionPerformed(ActionEvent e) {

    switch (e.getActionCommand()) {
      case "New Account":
        cardLayout.show(mainPanel, "newAccount");
        break;
      case "Login Screen":
        cardLayout.show(mainPanel, "Login");
        break;
      default:
        System.out.println(e.getActionCommand());
    }

  }

  private void createUIComponents() {

    loginPanel = new LoginPanel(this);
    registerNewAccountPanel = new RegisterNewAccountPanel(this);
  }

  public static void main(String[] args) {

    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

  /**
   * Create the GUI and show it.  For thread safety, this method should be invoked from the
   * event-dispatching thread.
   */
  private static void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("Login Panel Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    LoginScreenPanel demo = new LoginScreenPanel(new TestGameMenuController());
    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

}


