package client.gui.swing.panels;

import client.gui.swing.SwingGUIController;
import client.gui.swing.panels.testcontrolers.TestGameMenuController;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.ProfileMessage;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.Players;
import client.presenter.network.messages.ProfileResponse;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class PlayerStatsPanel extends UpdatableJTableInPanel {

  private final String[] statsColumns =
      {"White Player", "Black Player", "Date Started", "Date Finished", "Winner"};

  private String playerNickName;
  private String currentSelected = "";
  private boolean ignoreComboBox = true;

  private JPanel mainPanel;
  private JTable playerStatsTable;
  private DefaultTableModel playerStatsModel;
  private JLabel playerStats;
  private JComboBox playerList;

  public PlayerStatsPanel(SwingGUIController controller) {
    playerList.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String profileNickName = (String) playerList.getSelectedItem();
        if(profileNickName ==  null || currentSelected.equals(profileNickName ) || ignoreComboBox){
          return;
        }
        currentSelected = profileNickName;
        playerNickName = profileNickName;
        controller.sendMessage(new ProfileMessage(profileNickName));

      }
    });
  }

  @Override
  public void updateTable(NetworkMessage message) {

    if (!(message instanceof ProfileResponse)) {
      throw new IllegalArgumentException("ActiveGamePanel:: Received message of type "
          + message.getClass() + " expected" + ProfileResponse.class);
    }

    ProfileResponse profileResponse = (ProfileResponse) message;

    // Reset current data
    playerStatsModel.setNumRows(0);
    int playerWins = 0;
    int numberOfGames = profileResponse.results.length;
    for (int i = 0; i < numberOfGames; i++) {
      String winner;
      if(profileResponse.results[i]) {
        winner = profileResponse.blackPlayers[i];
      } else {
        winner = profileResponse.whitePlayers[i];
      }
      playerStatsModel.addRow(new Object[] {profileResponse.whitePlayers[i],
          profileResponse.blackPlayers[i], profileResponse.startDates[i],
          profileResponse.endDates[i], winner});

      if(winner.equals(playerNickName)){
        playerWins++;
      }
    }

    double winRate = numberOfGames > 0 ? playerWins/(double)numberOfGames : 0.0;
    DecimalFormat df = new DecimalFormat("#.##");

    playerStats.setText(playerNickName + " won: " + playerWins + " games out of " + numberOfGames + " " + df.format(winRate*100) + "%");





  }


  private void createUIComponents() {

    playerStatsModel = new DefaultTableModel(new Object[][]{}, statsColumns);
    playerStatsTable = new JTable(playerStatsModel) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    playerStatsTable.setRowSelectionAllowed(true);
    playerStatsTable.setColumnSelectionAllowed(false);
    playerStatsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    playerStatsTable.setAutoCreateRowSorter(true);
    playerStatsTable.getTableHeader().setReorderingAllowed(false);


  }

  public void setPlayerNickName(String playerNickName) {
    this.playerNickName = playerNickName;
  }

  public void populatePlayersList(NetworkMessage message) {

    if (!(message instanceof Players)) {
      throw new IllegalArgumentException("PlayerStatsPanel:: Received message of type "
          + message.getClass() + " expected" + Players.class);
    }

    Players players = (Players) message;
    ignoreComboBox = true;
    playerList.removeAllItems();
    for(int i = 0; i < players.players.length; i++){
      playerList.addItem(players.players[i]);
    }
    currentSelected = "";
    playerList.setSelectedIndex(-1);
    ignoreComboBox = false;
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
    PlayerStatsPanel demo = new PlayerStatsPanel(new TestGameMenuController());

    demo.populatePlayersList(new Players("19:testUser:testUser2:testUser3"));
    demo.setPlayerNickName("testUser2");
    demo.updateTable(new ProfileResponse("18:testUser2:testUser:01-01-18:01-01-18:true#testUser:testUser2:02-14-18:02-14-18:false"));
    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

}
