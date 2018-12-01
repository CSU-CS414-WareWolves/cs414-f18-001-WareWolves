package client.gui.swing.panels;

import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.ProfileResponse;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class PlayerStatsPanel extends UpdatableJTableInPanel {

  private final String[] statsColumns =
      {"White Player", "Black Player", "Date Started", "Date Finished", "Winner"};

  private String playerNickName;

  private JPanel mainPanel;
  private JTable playerStatsTable;
  private DefaultTableModel playerStatsModel;
  private JLabel playerStats;
  private JComboBox playerList;

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

    for (int i = 0; i < profileResponse.results.length; i++) {
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
}
