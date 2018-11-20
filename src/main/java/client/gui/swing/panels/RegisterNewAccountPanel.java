package client.gui.swing.panels;

import client.gui.swing.SwingGUIController;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.RegisterMessage;
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

public class RegisterNewAccountPanel extends JPanel {

  private JPanel newAccountPanel;
  private JTextField nicknameField;
  private JButton registerAccount;
  private JButton cancelRegister;
  private JTextField emailField;
  private JPasswordField passwordField;

  SwingGUIController controller;

  public RegisterNewAccountPanel(SwingGUIController controller) {
    super();
    this.controller = controller;
    this.add(newAccountPanel);
    registerAccount.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String email = emailField.getText();
        String nickname = nicknameField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty() || nickname.isEmpty()) {

          JOptionPane.showMessageDialog(newAccountPanel,
              "You must enter values in the Email, Nickname, and Password fields");

          return;
        }
        try {
          RegisterMessage message = new RegisterMessage(email, password, nickname);
          controller.sendMessage(message);

        } catch (NoSuchAlgorithmException e1) {
          System.err.println("The computer does not have SHA1 algorithm");
        }

      }
    });
    cancelRegister.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.actionPerformed(e);
      }
    });
  }

}


