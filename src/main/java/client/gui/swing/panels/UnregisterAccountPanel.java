package client.gui.swing.panels;

import client.gui.ChadGameDriver;
import client.gui.swing.SwingGUIController;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.UnregisterMessage;
import client.presenter.controller.messages.UnregisterResponseMessage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class UnregisterAccountPanel extends JPanel{

  private JPanel mainPanel;
  private JTextField nicknameField;
  private JTextField emailField;
  private JPasswordField passwordField;
  private JButton registerAccount;
  private JButton cancelRegister;

  public UnregisterAccountPanel(ChadGameDriver controller) {
    cancelRegister.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          controller.handleViewMessage(new UnregisterMessage(null, null, null));
        } catch (NoSuchAlgorithmException e1) {
          System.err.println("The computer does not have SHA1 algorithm");
        }
      }
    });
    registerAccount.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String email = emailField.getText();
        String nickname = nicknameField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty() || nickname.isEmpty()) {

          JOptionPane.showMessageDialog(mainPanel,
              "You must enter values in the Email, Nickname, and Password fields");

          return;
        }
        try {
          UnregisterMessage message = new UnregisterMessage(email, password, nickname);
          controller.handleViewMessage(message);

        } catch (NoSuchAlgorithmException e1) {
          System.err.println("The computer does not have SHA1 algorithm");
        }

      }
    });
  }
}
