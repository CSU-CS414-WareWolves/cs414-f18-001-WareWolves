package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels;

import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.SwingGUIController;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.LoginMessage;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * This panel displays the login screen asking the user for there email and password.
 */
public class LoginPanel extends JPanel {

  /**
   * Button for sending entered login information
   */
  private JButton login;
  /**
   * Button for changing to the register new account panel
   */
  private JButton newAccountButton;
  /**
   * Text field for users to enter their email
   */
  private JTextField emailTextField;
  /**
   * Text field for users to enter their email
   */
  private JPasswordField passwordField;
  /**
   * Main panel for the class
   */
  private JPanel LoginPanel;

  /**
   * Creates the GUI elements and sets the panels controller for ActionListeners
   *
   * @param controller the controller of the panel
   */
  LoginPanel(SwingGUIController controller) {
    this.add(LoginPanel);
    // Setup the login button
    login.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Get the login info
        String email = emailTextField.getText();
        String password = new String(passwordField.getPassword());
        if (email.isEmpty() || password.isEmpty()) {
          // Email and Password must have some info
          JOptionPane.showMessageDialog(LoginPanel,
              "You must enter values for the Email and Password fields");
          return;
        }
        // Send login message to controller
        try {
          LoginMessage message = new LoginMessage(email, password);
          passwordField.setText(""); // Clear password
          controller.sendMessage(message);
        } catch (NoSuchAlgorithmException e1) {
          System.err.println("The computer does not have SHA1 algorithm");
        }

      }
    });
    // Setup the new account button
    newAccountButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // tell controller to switch to new account panel
        controller.actionPerformed(e);
      }
    });
  }


  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    LoginPanel = new JPanel();
    LoginPanel.setLayout(new BorderLayout(0, 0));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new BorderLayout(0, 0));
    LoginPanel.add(panel1, BorderLayout.CENTER);
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    panel1.add(panel2, BorderLayout.SOUTH);
    login = new JButton();
    login.setText("Login");
    panel2.add(login);
    newAccountButton = new JButton();
    newAccountButton.setText("New Account");
    panel2.add(newAccountButton);
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new BorderLayout(0, 0));
    panel1.add(panel3, BorderLayout.CENTER);
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new GridBagLayout());
    panel3.add(panel4, BorderLayout.NORTH);
    final JLabel label1 = new JLabel();
    label1.setText("Account Email ");
    label1.putClientProperty("html.disable", Boolean.FALSE);
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    panel4.add(label1, gbc);
    emailTextField = new JTextField();
    emailTextField.setColumns(30);
    emailTextField.setVerifyInputWhenFocusTarget(true);
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 1;
    panel4.add(emailTextField, gbc);
    passwordField = new JPasswordField();
    passwordField.setColumns(30);
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 2;
    panel4.add(passwordField, gbc);
    final JLabel label2 = new JLabel();
    label2.setText("Password");
    label2.putClientProperty("html.disable", Boolean.FALSE);
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.WEST;
    panel4.add(label2, gbc);
    final JPanel spacer1 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel4.add(spacer1, gbc);
    final JPanel spacer2 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel4.add(spacer2, gbc);
    final JPanel spacer3 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel4.add(spacer3, gbc);
    final JPanel spacer4 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 4;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel4.add(spacer4, gbc);
    final JPanel spacer5 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel4.add(spacer5, gbc);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return LoginPanel;
  }
}
