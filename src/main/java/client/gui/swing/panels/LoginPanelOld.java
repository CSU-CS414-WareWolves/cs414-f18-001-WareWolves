package client.gui.swing.panels;

import client.gui.swing.SwingProperties;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanelOld extends JPanel implements ActionListener {



  private final LoginScreenPanel controller;


  private final JLabel emailLabel = new JLabel("Email Address:");
  private final JLabel passwordLabel = new JLabel("Password:");
  private final JTextField emailTextField = new JTextField(30);
  private final JPasswordField passwordField = new JPasswordField(30);
  private final JButton loginButton = new JButton("Login");
  private final JButton resetButton = new JButton("New Account");
  private final JCheckBox showPasswordCheckBox = new JCheckBox("Show Password");


  public LoginPanelOld(LoginScreenPanel controller){
    super(new BorderLayout());
    this.controller = controller;

    this.setPreferredSize(new Dimension(700, 400));
    createLoginInfoPanel();
    createLoginButtons();
  }

  private void createLoginButtons() {



  }

  private void createLoginInfoPanel() {

    // Email
    JPanel emailPanel = new JPanel(new BorderLayout());
    emailLabel.setFont(SwingProperties.DEFAULT_LABEL_FONT);
    emailTextField.setFont(SwingProperties.DEFAULT_LABEL_FONT);
    emailPanel.add(emailLabel, BorderLayout.WEST);
    emailPanel.add(emailTextField, BorderLayout.EAST);

    emailPanel.setPreferredSize(new Dimension(650, 30));
    emailPanel.revalidate();
    // Password
    JPanel passwordPanel = new JPanel();
    passwordLabel.setFont(SwingProperties.DEFAULT_LABEL_FONT);
    passwordField.setFont(SwingProperties.DEFAULT_LABEL_FONT);
    passwordPanel.add(passwordLabel, BorderLayout.WEST);
    passwordPanel.add(passwordField, BorderLayout.EAST);

    passwordPanel.setPreferredSize(new Dimension(650, 30));
    passwordPanel.revalidate();

    JPanel loginInfo = new JPanel();

    loginInfo.add(emailPanel);
    loginInfo.add(passwordPanel);

    this.add(loginInfo, BorderLayout.CENTER);

  }

  // For Testing

  public static void main(String[] args) {

    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
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
    LoginPanelOld demo = new LoginPanelOld(new LoginScreenPanel());
    frame.add(demo);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

  }
}
