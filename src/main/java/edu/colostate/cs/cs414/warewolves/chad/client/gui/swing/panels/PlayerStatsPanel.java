package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels;

import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.SwingGUIController;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ProfileMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.Players;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.ProfileResponse;
import com.intellij.uiDesigner.core.Spacer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * This panel allows a users to select a player and see that players game results.
 */
public class PlayerStatsPanel extends UpdatableJTableInPanel {

  /**
   * Columns for the table on the players stats
   */
  private final String[] statsColumns =
      {"White Player", "Black Player", "Date Started", "Date Finished", "Winner"};

  /**
   * Nickname for the player the user is look up stats for
   */
  private String lookedUpPlayerNickName;
  /**
   * Used to stop repeating stats request on when the user did not select a new user to lookup
   */
  private String currentSelected = "";
  /**
   * Used to stop lookup requests when populating the list of user
   */
  private boolean ignoreComboBox = true;

  /**
   * Main panle of the class
   */
  private JPanel mainPanel;
  /**
   * Table to display the users stats
   */
  private JTable playerStatsTable;
  /**
   * Model that contains playerStatsTable information
   */
  private DefaultTableModel playerStatsModel;
  /**
   * Label to display the win/loss rate of the player
   */
  private JLabel playerStats;
  /**
   * List of all the players in the game
   */
  private JComboBox playerList;

  /**
   * Creates the GUI elements and sets the panels controller for ActionListeners
   *
   * @param controller the controller of the panel
   */
  public PlayerStatsPanel(SwingGUIController controller) {
    $$$setupUI$$$(); // Need for GUI
    playerList.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String profileNickName = (String) playerList.getSelectedItem();
        // Stop unnecessary calls for player stats
        if (profileNickName == null || currentSelected.equals(profileNickName) || ignoreComboBox) {
          return;
        }
        // Set new values to stop  unnecessary calls
        currentSelected = profileNickName;
        lookedUpPlayerNickName = profileNickName;
        controller.sendMessage(new ProfileMessage(profileNickName));

      }
    });
  }

  /**
   * Takes a ProfileResponse and displays that users game stats, and calculates their win rate
   *
   * @param message the message to process
   */
  @Override
  public void updateTable(NetworkMessage message) {
    // Check for correct type of message
    checkValidMessageType(message, ProfileResponse.class, "PlayerStatsPanel");

    ProfileResponse profileResponse = (ProfileResponse) message;

    // Reset current data
    playerStatsModel.setNumRows(0);
    // If the player has no games played display that fact and return
    if (profileResponse.whitePlayers[0].equals("-1")) {
      playerStats.setText(lookedUpPlayerNickName + " has not played any games.");
      return;
    }

    int playerWins = 0;
    int numberOfGames = profileResponse.results.length;

    for (int i = 0; i < numberOfGames; i++) {
      // Find the nickname of the player who won the game
      String winner = getWinnerNickname(profileResponse, i);
      // add game to able
      playerStatsModel.addRow(new Object[]{profileResponse.whitePlayers[i],
          profileResponse.blackPlayers[i], profileResponse.startDates[i],
          profileResponse.endDates[i], winner});
      // Add to players win count if they won
      if (winner.equals(lookedUpPlayerNickName)) {
        playerWins++;
      }
    }

    // Calculate and display the players win rate
    double winRate = numberOfGames > 0 ? playerWins / (double) numberOfGames : 0.0;
    DecimalFormat df = new DecimalFormat("#.##");

    playerStats.setText(
        lookedUpPlayerNickName + " won: " + playerWins + " games out of " + numberOfGames + " "
            + df.format(winRate * 100) + "%");
  }

  /**
   * Gets the winners nickname for a given game
   *
   * @param profileResponse message with all the game results
   * @param index index to check
   * @return the winners nickname
   */
  private String getWinnerNickname(ProfileResponse profileResponse, int index) {
    String winner;
    if (profileResponse.results[index]) {
      winner = profileResponse.blackPlayers[index];
    } else {
      winner = profileResponse.whitePlayers[index];
    }
    return winner;
  }

  /**
   * Creates all the elements that the GUI needed custom constructors for
   */
  private void createUIComponents() {

    /*
     * Attempts to extract all shared logic for creating tables to base class failed.
     *
     * I attempted to make a method that took the tableModel, JTable, and the column names
     * as parameters and initialize the table. However, the table and the model
     * were not initialized after the method call. I was able create the table and then
     * set the settings for the table in a method in the base class
     */

    // Setup the column names for the table
    playerStatsModel = new DefaultTableModel(new Object[][]{}, statsColumns);
    // Stop the user from editing the column info
    playerStatsTable = new JTable(playerStatsModel) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    setTableSettings(playerStatsTable, false);

  }

  /**
   * Populates the list of all the players in the game
   *
   * @param message the message with all the players
   */
  public void populatePlayersList(NetworkMessage message) {

    checkValidMessageType(message, Players.class, "PlayerStatsPanel");

    Players players = (Players) message;

    // Clear Data
    ignoreComboBox = true; // stop ListListener from sending stats requests
    playerList.removeAllItems();
    // Add all players
    for (int i = 0; i < players.players.length; i++) {
      playerList.addItem(players.players[i]);
    }
    // Make sure not player is selected at start
    currentSelected = "";
    playerList.setSelectedIndex(-1);
    ignoreComboBox = false;
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
    mainPanel.setLayout(new BorderLayout(0, 0));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    mainPanel.add(panel1, BorderLayout.NORTH);
    final JLabel label1 = new JLabel();
    label1.setText("Select Player");
    panel1.add(label1);
    final Spacer spacer1 = new Spacer();
    panel1.add(spacer1);
    playerList = new JComboBox();
    playerList.setActionCommand("selectedPlayer");
    playerList.setBackground(new Color(-3681323));
    playerList.setDoubleBuffered(false);
    playerList.setEditable(false);
    playerList.setMaximumSize(new Dimension(500, 30));
    playerList.setMinimumSize(new Dimension(500, 30));
    playerList.setPreferredSize(new Dimension(500, 30));
    playerList.setRequestFocusEnabled(true);
    panel1.add(playerList);
    final JScrollPane scrollPane1 = new JScrollPane();
    mainPanel.add(scrollPane1, BorderLayout.CENTER);
    playerStatsTable.setFillsViewportHeight(true);
    scrollPane1.setViewportView(playerStatsTable);
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new BorderLayout(0, 0));
    mainPanel.add(panel2, BorderLayout.SOUTH);
    playerStats = new JLabel();
    playerStats.setText("Select player to see stats");
    panel2.add(playerStats, BorderLayout.CENTER);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return mainPanel;
  }
}
