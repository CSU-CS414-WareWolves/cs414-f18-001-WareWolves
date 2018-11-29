package client.gui.swing.panels;

import client.gui.swing.SwingGUIController;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.ViewMessage;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

public class MainMenuPanel extends SwingGUIController {

  private CardLayout cardLayout;

  private JPanel mainPanel;
  private JButton invitesButton;
  private JButton profilesButton;
  private JButton gamesButton;
  private JPanel buttonPanel;
  private JPanel displayPanel;
  private ActiveGamesPanel activeGamesPanel;
  private InvitesPanel invitesPanel1;
  private PlayerStatsPanel playerStatsPanel1;

  public MainMenuPanel() {

    cardLayout = (CardLayout) displayPanel.getLayout();
    cardLayout.show(displayPanel, "Invites");

    gamesButton.addActionListener(this);
    invitesButton.addActionListener(this);
    profilesButton.addActionListener(this);
  }

  private void createUIComponents() {
    activeGamesPanel = new ActiveGamesPanel(this);
    invitesPanel1 = new InvitesPanel(this);
    playerStatsPanel1 = new PlayerStatsPanel();
  }

  @Override
  public void sendMessage(ViewMessage message) {

    if (message instanceof MenuMessage) {
      MenuMessage loginMessage = (MenuMessage) message;

      switch (loginMessage.menuType) {
        case SELECT_GAME:
          System.out.println("Select Game: " + loginMessage.information[0] + " Opponent: "
              + loginMessage.information[2]);
          break;
        case RESIGN_GAME:
          System.out.println("Resign Game: " + loginMessage.information[0] + " Opponent: "
              + loginMessage.information[2]);
      }

    } else {
      throw new IllegalArgumentException("ActiveGame:: Did not sent a menu message - "
          + message.messageType);
    }

  }

  @Override
  public void actionPerformed(ActionEvent e) {

    switch (e.getActionCommand()) {
      case "viewGames":
        cardLayout.show(displayPanel, "Games");
        break;
      case "viewProfiles":
        cardLayout.show(displayPanel, "Stats");
        break;
      case "viewInvites":
        cardLayout.show(displayPanel, "Invites");
        ActiveGamesPanel.loadTestData(activeGamesPanel);
        break;
      default:
        System.out.println(e.getActionCommand());
    }

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
    MainMenuPanel demo = new MainMenuPanel();
    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

}
