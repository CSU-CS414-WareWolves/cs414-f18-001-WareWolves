package client.gui.swing.panels;

import client.gui.swing.SwingGUIController;
import client.presenter.controller.messages.ViewMessage;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginScreenPanel extends SwingGUIController {

  private CardLayout c;

  private JPanel mainPanel;
  private JButton registerAccount;
  private JButton cancelRegister;
  private JPanel newAccountPanelOld;
  private JTextField nicknameField;
  private JLabel NicknameLabel;
  private LoginPanel loginPanel;
  private RegisterNewAccountPanel registerNewAccountPanel;

  public LoginScreenPanel() {
    super();
    //this.add(mainPanel);
    c = (CardLayout) mainPanel.getLayout();
    c.show(mainPanel, "newAccount");

    cancelRegister.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        CardLayout c = (CardLayout) mainPanel.getLayout();
        c.show(mainPanel, "Login");
      }
    });
  }

  @Override
  public void sendMessage(ViewMessage message) {

    System.out.println(message.messageType);

  }

  @Override
  public void actionPerformed(ActionEvent e) {

    switch (e.getActionCommand()) {
      case "New Account":
        c.show(mainPanel, "newAccount");
        break;
      case "Login Screen":
        c.show(mainPanel, "Login");
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
    LoginScreenPanel demo = new LoginScreenPanel();
    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

}


