package client.gui.swing.panels;

import client.gui.swing.SwingGUIController;
import client.presenter.controller.ViewMessageType;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.util.ViewMessageFactory;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginPanel extends JPanel {

  private JButton Login;
  private JButton newAccountButton;
  private JLabel emailLabel;
  private JTextField emailTextField;
  private JPasswordField passwordField;
  private JLabel passwordLabel;
  private JPanel LoginPanel;

  SwingGUIController controller;


  LoginPanel(SwingGUIController controller) {
    super();
    this.controller = controller;
    this.add(LoginPanel);

    Login.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String email = emailTextField.getText();
        String password = new String(passwordField.getPassword());
        if (email.isEmpty() || password.isEmpty()) {

          JOptionPane.showMessageDialog(LoginPanel,
              "You must enter values for the Email and Password fields");

          return;
        }
        try {
          LoginMessage message = new LoginMessage(email, password);
          controller.sendMessage(message);

        } catch (NoSuchAlgorithmException e1) {
          System.err.println("The computer does not have SHA1 algorithm");
        }

      }
    });
    newAccountButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.actionPerformed(e);
      }
    });
  }

  // For Testing


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
    LoginPanel demo = new LoginPanel(new TestMainLoginPanelController());
    frame.add(demo.LoginPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  private static class TestMainLoginPanelController extends SwingGUIController {

    @Override
    public void sendMessage(ViewMessage message) {
      if (message instanceof LoginMessage) {
        LoginMessage loginMessage = (LoginMessage) message;

        System.out.println("Email: " + loginMessage.email + " Password: " + loginMessage.password);

      } else {
        throw new IllegalArgumentException("LoginPanel::Sent did not sent a login message - "
            + message.messageType);
      }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

      System.out.println("Tried to change to " + e.getActionCommand());

    }
  }

}
