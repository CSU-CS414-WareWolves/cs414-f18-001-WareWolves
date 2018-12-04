package client.gui.swing.panels;


import client.gui.swing.SwingGUIController;
import client.gui.swing.info.ActiveGameInfo;
import client.gui.swing.panels.testcontrolers.TestGameMenuController;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.messages.GameRequestMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.ResignMessage;
import client.presenter.network.messages.ActiveGameResponse;
import client.presenter.network.messages.NetworkMessage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class ActiveGamesPanel extends UpdatableJTableInPanel {

  private final Object[] columnNames = {"GameID", "Opponent", "Start Date", "Players turn",
      "Game Finished"};

  private HashMap<Integer, ActiveGameInfo> activeGames = new HashMap<>();

  private JPanel mainPanel;
  private JButton playGameButton;
  private JButton resignGameButton;
  private JTable activeGamesTable;
  private DefaultTableModel gameInfoModel;
  private SwingGUIController controller;


  public ActiveGamesPanel(SwingGUIController controller) {

    this.controller = controller;
    $$$setupUI$$$();
    resignGameButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        int gameIndex = getHiddenID(activeGamesTable);
        if (gameIndex == -1) {
          return;
        }
        ActiveGameInfo gameInfo = activeGames.get(gameIndex);
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane
            .showConfirmDialog(mainPanel, "Are you sure you want to resign the game against "
                + gameInfo.getOpponent(), "Warning", dialogButton);
        if (dialogResult == JOptionPane.YES_OPTION) {
          controller.sendMessage(new ResignMessage(gameIndex));
        }
      }
    });
    playGameButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int gameIndex = getHiddenID(activeGamesTable);
        if (gameIndex != -1) {
          ActiveGameInfo gameInfo = activeGames.get(gameIndex);
          controller.sendMessage(new GameRequestMessage(gameInfo.getInfoArray()));
        }
      }
    });
  }


  private void createUIComponents() {

    gameInfoModel = new DefaultTableModel(new Object[][]{}, columnNames);
    activeGamesTable = new JTable(gameInfoModel) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    activeGamesTable.setRowSelectionAllowed(true);
    activeGamesTable.setColumnSelectionAllowed(false);
    activeGamesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    activeGamesTable.setAutoCreateRowSorter(true);
    activeGamesTable.getTableHeader().setReorderingAllowed(false);
    TableColumnModel columnControl = activeGamesTable.getColumnModel();
    columnControl.removeColumn(columnControl.getColumn(0));

  }


  public void updateTable(NetworkMessage message) {

    if (!(message instanceof ActiveGameResponse)) {
      throw new IllegalArgumentException("ActiveGamePanel:: Received message of type "
          + message.getClass() + " expected" + ActiveGameResponse.class);
    }

    ActiveGameResponse gameInfoMessage = (ActiveGameResponse) message;

    // Reset current data
    activeGames.clear();
    gameInfoModel.setNumRows(0);

    if (gameInfoMessage.gameIDs[0] == -1) {
      return;
    }

    for (int i = 0; i < gameInfoMessage.gameIDs.length; i++) {
      activeGames.put(gameInfoMessage.gameIDs[i],
          new ActiveGameInfo(gameInfoMessage.gameIDs[i], gameInfoMessage.gameBoards[i],
              gameInfoMessage.opponents[i], gameInfoMessage.startDates[i], gameInfoMessage.turns[i],
              gameInfoMessage.color[i], gameInfoMessage.ended[i]));
    }

    for (Integer game : activeGames.keySet()) {
      String playersTurn;
      ActiveGameInfo gameInfo = activeGames.get(game);
      if (gameInfo.getColor() == gameInfo.getTurn()) {
        playersTurn = "Your turn";
      } else {
        playersTurn = gameInfo.getOpponent() + "'s turn";
      }

      gameInfoModel.addRow(
          new Object[]{gameInfo.getGameID(), gameInfo.getOpponent(),
              gameInfo.getStartDate(), playersTurn, gameInfo.getEnded()});

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
    ActiveGamesPanel demo = new ActiveGamesPanel(new TestGameMenuController());
    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
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
}
