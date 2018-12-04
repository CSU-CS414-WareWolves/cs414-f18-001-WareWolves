package client.gui.swing.panels;

import client.gui.ChadGameDriver;
import client.gui.swing.SwingGUIController;
import client.gui.swing.panels.testcontrolers.TestSwingController;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.messages.ActiveGameMessage;
import client.presenter.controller.messages.InboxMessage;
import client.presenter.controller.messages.InviteMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.ProfileMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.Players;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainMenuPanel extends SwingGUIController {

  private CardLayout cardLayout;

  private JPanel mainPanel;
  private JButton invitesButton;
  private JButton profilesButton;
  private JButton gamesButton;
  private JPanel displayPanel;
  private ActiveGamesPanel activeGamesPanel;
  private InvitesPanel invitesPanel;
  private PlayerStatsPanel playerStatsPanel;

  private ChadGameDriver controller;

  private ArrayList<String> playersList = new ArrayList<>();
  private String nickName;

  public MainMenuPanel(ChadGameDriver controller) {

    this.controller = controller;

    $$$setupUI$$$();
    cardLayout = (CardLayout) displayPanel.getLayout();
    cardLayout.show(displayPanel, "Empty");

    gamesButton.addActionListener(this);
    invitesButton.addActionListener(this);
    profilesButton.addActionListener(this);
  }

  private void createUIComponents() {
    activeGamesPanel = new ActiveGamesPanel(this);
    invitesPanel = new InvitesPanel(this);
    playerStatsPanel = new PlayerStatsPanel(this);
  }

  @Override
  public void sendMessage(ViewMessage message) {

    switch (message.messageType) {
      case PROFILE:
        ProfileMessage profileMessage = (ProfileMessage) message;
        controller.handleViewMessage(message);
        System.out.println("View Stats: " + profileMessage.nickname);
        break;
      case NEW_INVITE:
        ArrayList<String> removeSelf = (ArrayList<String>) playersList.clone();
        removeSelf.remove(nickName);
        String player = (String) JOptionPane.showInputDialog(
                this,
                "Select player to invite",
                "Send New Invite",
                JOptionPane.PLAIN_MESSAGE,
                null,
                removeSelf.toArray(),
                removeSelf.get(0));
        if ((player != null) && (player.length() > 0)) {
          controller.handleViewMessage(new InviteMessage(nickName, player));
          System.out.println("Sending Invite to: " + player);
        }
        break;
      case INVITE_RESPONSE:
        controller.handleViewMessage(message);
        break;
      case GAME_REQUEST:
        controller.handleViewMessage(message);
        break;
      case RESIGN:
        controller.handleViewMessage(message);
        break;
      default:
        System.err
                .println("MainMenuPanel::sendMessage - unknown message type " + message.getClass());
    }

    if (message instanceof MenuMessage) {
      MenuMessage menuMessage = (MenuMessage) message;
      System.out.println("Invite ID: " + menuMessage.information[0] + " Accepting: "
              + menuMessage.information[1]);
    }

  }

  @Override
  public void receiveMessage(ViewMessage message) {

    switch (message.messageType) {
      case MENU:
        break;
      case REGISTER_RESPONSE:
        break;
      case LOGIN_RESPONSE:
        break;
      case UNREGISTER_RESPONSE:
        break;
      case MENU_RESPONSE:
        break;
      default:
        System.err.println("MainMenuPanel::receiveMessage received invalid ViewMessage "
                + message.messageType);

    }

  }

  @Override
  public void receiveMessage(NetworkMessage message) {

    switch (message.type) {
      case ACTIVE_GAMES_RESPONSE:
        activeGamesPanel.updateTable(message);
        break;
      case INBOX_RESPONSE:
        invitesPanel.updateTable(message);
        break;
      case PROFILE_RESPONSE:
        playerStatsPanel.updateTable(message);
        break;
      case PLAYERS:
        Players players = (Players) message;
        playersList.addAll(Arrays.asList(players.players));
        playerStatsPanel.populatePlayersList(message);
        break;
      default:
        System.err.println("MainMenuPanel::receiveMessage received invalid NetworkMessage "
                + message.type);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    switch (e.getActionCommand()) {
      case "viewGames":
        cardLayout.show(displayPanel, "Games");
        controller.handleViewMessage(new ActiveGameMessage());
        break;
      case "viewProfiles":
        cardLayout.show(displayPanel, "Stats");
        break;
      case "viewInvites":
        cardLayout.show(displayPanel, "Invites");
        controller.handleViewMessage(new InboxMessage());
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
    frame.setResizable(false);

    //Create and set up the content pane.
    MainMenuPanel demo = new MainMenuPanel(new TestSwingController());
    demo.receiveMessage(new Players("19:testUser:testUser2:testUser3"));
    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
    invitesPanel.setNickname(nickName);
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    mainPanel = new JPanel();
    mainPanel.setLayout(new GridBagLayout());
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridBagLayout());
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.anchor = GridBagConstraints.WEST;
    mainPanel.add(panel1, gbc);
    invitesButton = new JButton();
    invitesButton.setActionCommand("viewInvites");
    invitesButton.setText("Invites");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(invitesButton, gbc);
    profilesButton = new JButton();
    profilesButton.setActionCommand("viewProfiles");
    profilesButton.setText("Profiles");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(profilesButton, gbc);
    gamesButton = new JButton();
    gamesButton.setActionCommand("viewGames");
    gamesButton.setHideActionText(false);
    gamesButton.setText("Games");
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(gamesButton, gbc);
    final JPanel spacer1 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer1, gbc);
    final JPanel spacer2 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer2, gbc);
    final JPanel spacer3 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(spacer3, gbc);
    final JPanel spacer4 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.VERTICAL;
    mainPanel.add(spacer4, gbc);
    final JPanel spacer5 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.VERTICAL;
    mainPanel.add(spacer5, gbc);
    displayPanel = new JPanel();
    displayPanel.setLayout(new CardLayout(0, 0));
    displayPanel.setAutoscrolls(false);
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.BOTH;
    mainPanel.add(displayPanel, gbc);
    displayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
    displayPanel.add(activeGamesPanel.$$$getRootComponent$$$(), "Games");
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new BorderLayout(0, 0));
    displayPanel.add(panel2, "Empty");
    displayPanel.add(invitesPanel.$$$getRootComponent$$$(), "Invites");
    displayPanel.add(playerStatsPanel.$$$getRootComponent$$$(), "Stats");
    final JPanel spacer6 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(spacer6, gbc);
    final JPanel spacer7 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 4;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    mainPanel.add(spacer7, gbc);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return mainPanel;
  }
}
