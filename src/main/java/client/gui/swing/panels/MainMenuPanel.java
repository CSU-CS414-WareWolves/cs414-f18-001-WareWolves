package client.gui.swing.panels;

import client.gui.ChadGameDriver;
import client.gui.swing.SwingGUIController;
import client.gui.swing.panels.testcontrolers.TestSwingController;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.Players;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
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

  private String[] playersList;
  private String nickName;

  public MainMenuPanel(ChadGameDriver controller) {

    this.controller = controller;

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

    if (message instanceof MenuMessage) {
      MenuMessage menuMessage = (MenuMessage) message;

      switch (menuMessage.menuType) {
        case SELECT_GAME:
          controller.handleViewMessage(message);
          System.out.println("Select Game: " + menuMessage.information[0] + " Opponent: "
              + menuMessage.information[2]);
          break;
        case RESIGN:
          controller.handleViewMessage(message);
          System.out.println("Resign Game: " + menuMessage.information[0] + " Opponent: "
              + menuMessage.information[2]);
          break;
        case PLAYER_STATS:
          controller.handleViewMessage(message);
          System.out.println("View Stats: " + menuMessage.information[0]);
          break;
        case INVITES:
          if(menuMessage.information.length == 0){
            String player = (String) JOptionPane.showInputDialog(
                this,
                "Select player to invite",
                "Send New Invite",
                JOptionPane.PLAIN_MESSAGE,
                null,
                playersList,
                playersList[0]);
            if ((player != null) && (player.length() > 0)) {
              controller.handleViewMessage(new MenuMessage(MenuMessageTypes.SEND_INVITE, new String[] {nickName, player}));
              System.out.println("Send Invite to: " + player);
            }
          } else {
            controller.handleViewMessage(message);
            System.out.println("Invite ID: " + menuMessage.information[0] + " Accepting: "
                + menuMessage.information[1]);
          }
      }

    } else {
      throw new IllegalArgumentException("ActiveGame:: Did not sent a menu message - "
          + message.messageType);
    }

  }

  @Override
  public void receiveMessage(ViewMessage message) {

    switch (message.messageType){
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
        playersList = players.players;
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
        controller.handleViewMessage(new MenuMessage(MenuMessageTypes.ACTIVE_GAMES, new String[] {nickName}));
        break;
      case "viewProfiles":
        cardLayout.show(displayPanel, "Stats");
        break;
      case "viewInvites":
        cardLayout.show(displayPanel, "Invites");
        controller.handleViewMessage(new MenuMessage(MenuMessageTypes.INVITES, new String[] {nickName}));
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
}
