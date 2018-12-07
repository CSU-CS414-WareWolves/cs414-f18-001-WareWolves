package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels;


import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.SwingGUIController;
import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.info.ActiveGameInfo;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.GameRequestMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ResignMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.ActiveGameResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 * This panel displays all the active games a player has open. The user can either start one of the
 * games or resign from the game from this panel. For both cases the infromation about the choice is
 * sent to the Panels controller.
 */
public class ActiveGamesPanel extends UpdatableJTableInPanel {

  /**
   * The column names for the Table of active games
   */
  private final Object[] columnNames = {"GameID", "Opponent", "Start Date", "Players turn",
      "Game Finished"};

  /**
   * All the information about the active games with the gameID as the key
   */
  private HashMap<Integer, ActiveGameInfo> activeGames = new HashMap<>();

  /**
   * Main panel of the class
   */
  private JPanel mainPanel;
  /**
   * Button to play a game
   */
  private JButton playGameButton;
  /**
   * Button to resign a game
   */
  private JButton resignGameButton;
  /**
   * Table to display the active games
   */
  private JTable activeGamesTable;
  /**
   * Model for the activeGamesTable, it stores the data in the table
   */
  private DefaultTableModel gameInfoModel;

  /**
   * Creates the GUI elements and sets the panels controller for ActionListeners
   *
   * @param controller the controller of the panel
   */
  public ActiveGamesPanel(SwingGUIController controller) {

    $$$setupUI$$$(); // Need for GUI creation
    // Setup the resign button
    resignGameButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        // Get the game Id from the selected row in the table
        int gameIndex = getHiddenID(activeGamesTable);
        if (gameIndex == -1) {
          return; // No row selected
        }
        // Get the selected game info
        ActiveGameInfo gameInfo = activeGames.get(gameIndex);
        // Verify the user wants to resign the game
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane
            .showConfirmDialog(mainPanel, "Are you sure you want to resign the game against "
                + gameInfo.getOpponent(), "Warning", dialogButton);
        // Tell the controller the info of the game to be resigned
        if (dialogResult == JOptionPane.YES_OPTION) {
          controller.sendMessage(new ResignMessage(gameIndex));
        }
      }
    });

    // Setup the play game button
    playGameButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Get the game Id from the selected row in the table
        int gameIndex = getHiddenID(activeGamesTable);
        if (gameIndex != -1) {
          // Tell the controller the info of the game the user wants to play
          ActiveGameInfo gameInfo = activeGames.get(gameIndex);
          controller.sendMessage(new GameRequestMessage(gameInfo.getInfoArray()));
        }
      }
    });
  }

  /**
   * Creates all the elements that the GUI needed custom constructors for
   */
  private void createUIComponents() {


    // Setup the column names for the table
    gameInfoModel = new DefaultTableModel(new Object[][]{}, columnNames);
    // Stop the user from editing the column info
    activeGamesTable = new JTable(gameInfoModel) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    // Stop the user from reordering the columns
    activeGamesTable.getTableHeader().setReorderingAllowed(false);

    // Allow only row selection
    activeGamesTable.setRowSelectionAllowed(true);
    activeGamesTable.setColumnSelectionAllowed(false);
    // Allow the user to sort the columns
    activeGamesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    activeGamesTable.setAutoCreateRowSorter(true);
    // Stop the user from seeing the gameID
    TableColumnModel columnControl = activeGamesTable.getColumnModel();
    columnControl.removeColumn(columnControl.getColumn(0));

  }

  /**
   * Populates the Table with the information in an ActiveGameResponse
   *
   * @param message the message with information on the table
   */
  public void updateTable(NetworkMessage message) {
    // Check for correct type of message
    if (!(message instanceof ActiveGameResponse)) {
      throw new IllegalArgumentException("ActiveGamePanel:: Received message of type "
          + message.getClass() + " expected" + ActiveGameResponse.class);
    }

    ActiveGameResponse gameInfoMessage = (ActiveGameResponse) message;

    // Reset current data
    activeGames.clear();
    gameInfoModel.setNumRows(0);

    // -1 represents no active games
    if (gameInfoMessage.gameIDs[0] == -1) {
      return;
    }

    // For all the games in the message save the data about the game
    for (int i = 0; i < gameInfoMessage.gameIDs.length; i++) {
      activeGames.put(gameInfoMessage.gameIDs[i],
          new ActiveGameInfo(gameInfoMessage.gameIDs[i], gameInfoMessage.gameBoards[i],
              gameInfoMessage.opponents[i], gameInfoMessage.startDates[i], gameInfoMessage.turns[i],
              gameInfoMessage.color[i], gameInfoMessage.ended[i]));
    }

    // Populate the table with the relevant information about the games
    for (Integer game : activeGames.keySet()) {
      String playersTurn;
      ActiveGameInfo gameInfo = activeGames.get(game);
      // Find whose turn it is
      if (gameInfo.getColor() == gameInfo.getTurn()) {
        playersTurn = "Your turn";
      } else {
        playersTurn = gameInfo.getOpponent() + "'s turn";
      }
      // Add information to able
      gameInfoModel.addRow(
          new Object[]{gameInfo.getGameID(), gameInfo.getOpponent(),
              gameInfo.getStartDate(), playersTurn, gameInfo.getEnded()});

    }

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
    panel1.setLayout(new GridBagLayout());
    mainPanel.add(panel1, BorderLayout.SOUTH);
    final JPanel spacer1 = new JPanel();
    GridBagConstraints gbc;
    gbc = new GridBagConstraints();
    gbc.gridx = 2;
    gbc.gridy = 0;
    gbc.gridheight = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(spacer1, gbc);
    final JPanel spacer2 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer2, gbc);
    resignGameButton = new JButton();
    resignGameButton.setForeground(new Color(-4507871));
    resignGameButton.setText("Resign Game");
    gbc = new GridBagConstraints();
    gbc.gridx = 3;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(resignGameButton, gbc);
    playGameButton = new JButton();
    playGameButton.setText("Play Game");
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel1.add(playGameButton, gbc);
    final JPanel spacer3 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 1;
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer3, gbc);
    final JPanel spacer4 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer4, gbc);
    final JPanel spacer5 = new JPanel();
    gbc = new GridBagConstraints();
    gbc.gridx = 4;
    gbc.gridy = 1;
    gbc.fill = GridBagConstraints.VERTICAL;
    panel1.add(spacer5, gbc);
    final JScrollPane scrollPane1 = new JScrollPane();
    mainPanel.add(scrollPane1, BorderLayout.CENTER);
    activeGamesTable.setAutoCreateColumnsFromModel(false);
    activeGamesTable.setDragEnabled(false);
    activeGamesTable.setFillsViewportHeight(true);
    activeGamesTable.setSurrendersFocusOnKeystroke(false);
    activeGamesTable.setVisible(true);
    scrollPane1.setViewportView(activeGamesTable);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return mainPanel;
  }

  /*
   * This code is need for packaging project into a jar and running code in Eclipse
   */


}
