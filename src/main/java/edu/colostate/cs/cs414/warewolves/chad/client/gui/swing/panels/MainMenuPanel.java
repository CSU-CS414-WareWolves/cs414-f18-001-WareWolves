package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels;

import edu.colostate.cs.cs414.warewolves.chad.client.gui.ChadGameDriver;
import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.SwingGUIController;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ActiveGameMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.InboxMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.InviteMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ProfileMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ViewMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.Players;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class is the main game menu screen. It allows users to view their active games, invites,
 * and the stats of all the players.
 */
public class MainMenuPanel extends SwingGUIController {
  /**
   * Layout manger for the changing menu panels
   */
  private CardLayout cardLayout;
  /**
   * Classes main panel
   */
  private JPanel mainPanel;
  /**
   * Button for changing to invitesPanel
   */
  private JButton invitesButton;
  /**
   * Button for changing to playerStatsPanel
   */
  private JButton profilesButton;
  /**
   * Button for changing to activeGamesPanel
   */
  private JButton gamesButton;
  /**
   * Panel that displays the other panels
   */
  private JPanel displayPanel;
  /**
   * Panel for active games table
   */
  private ActiveGamesPanel activeGamesPanel;
  /**
   * Panel for invites table
   */
  private InvitesPanel invitesPanel;
  /**
   * Panel for player stats table
   */
  private PlayerStatsPanel playerStatsPanel;

  /**
   * Controller of this panel
   */
  private ChadGameDriver controller;

  /**
   * All the player in the game
   */
  private ArrayList<String> playersList = new ArrayList<>();
  /**
   * Nickname of the logged in player
   */
  private String nickName;

  /**
   * Creates the GUI elements and sets the panels controller for ActionListeners
   *
   * @param controller the controller of the panel
   */
  public MainMenuPanel(ChadGameDriver controller) {

    this.controller = controller;

    $$$setupUI$$$(); // Needed for GUI
    cardLayout = (CardLayout) displayPanel.getLayout();
    cardLayout.show(displayPanel, "Empty"); // Show empty panel at start

    // Set button listeners
    gamesButton.addActionListener(this);
    invitesButton.addActionListener(this);
    profilesButton.addActionListener(this);
  }

  /**
   * Creates all the elements that the GUI needed custom constructors for
   */
  private void createUIComponents() {
    activeGamesPanel = new ActiveGamesPanel(this);
    invitesPanel = new InvitesPanel(this);
    playerStatsPanel = new PlayerStatsPanel(this);
  }

  /**
   * Sends new ViewMessages or pass messages to this panels control;ers
   * @param message the message to send
   */
  @Override
  public void sendMessage(ViewMessage message) {

    switch (message.messageType) {
      case PROFILE:
        ProfileMessage profileMessage = (ProfileMessage) message;
        controller.handleViewMessage(message);
        System.out.println("View Stats: " + profileMessage.nickname);
        break;
      case NEW_INVITE:
        sendPlayerNewInvite();
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

  }

  /**
   * Gives the player a list of all users in the game and sends an invite to the player
   */
  private void sendPlayerNewInvite() {
    // You can not send invite to yourself
    ArrayList<String> removeSelf = removePlayerFromListOfPlayers();
    String player = (String) JOptionPane.showInputDialog(
        this,
        "Select player to invite",
        "Send New Invite",
        JOptionPane.PLAIN_MESSAGE,
        null,
        removeSelf.toArray(),
        removeSelf.get(0));
    // If the player selected a user send the invite
    if ((player != null) && (player.length() > 0)) {
      controller.handleViewMessage(new InviteMessage(nickName, player));
      System.out.println("Sending Invite to: " + player);
    }
  }

  /**
   * Removes the current player from the list of all the users in the game
   * @return list of all the players except the current player
   */
  private ArrayList<String> removePlayerFromListOfPlayers() {
    ArrayList<String> removeSelf = (ArrayList<String>) playersList.clone();
    removeSelf.remove(nickName);
    return removeSelf;
  }


  @Override
  public void receiveMessage(ViewMessage message) {
    // method not need for this class
  }

  /**
   * Process NetworkMessages to populate panel tables
   * @param message the message to process
   */
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
        // Save the players list for new Invites
        Players players = (Players) message;
        playersList.addAll(Arrays.asList(players.players));

        playerStatsPanel.populatePlayersList(message);
        break;
      default:
        System.err.println("MainMenuPanel::receiveMessage received invalid NetworkMessage "
            + message.type);
    }
  }

  /**
   * Receives action from buttons in the panel to switch visible panel and request infromation for
   * the panel
   * @param e the action
   */
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

  /**
   * Setst the nickname for the logged in player and shares the nickname with the invitesPanel
   * @param nickname the nickname of the player
   */
  public void setNickName(String nickname) {
    this.nickName = nickname;
    invitesPanel.setNickname(nickname);
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
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
    displayPanel.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null));
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
