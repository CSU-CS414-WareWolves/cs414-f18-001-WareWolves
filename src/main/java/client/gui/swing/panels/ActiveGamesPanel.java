package client.gui.swing.panels;


import client.gui.swing.SwingGUIController;
import client.gui.swing.info.ActiveGameInfo;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.ViewMessage;
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

public class ActiveGamesPanel extends JPanel implements UpdateJTableList {

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

    resignGameButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        int gameIndex = getGameId();
        if (gameIndex == -1) {
          return;
        }
        ActiveGameInfo gameInfo = activeGames.get(gameIndex);
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane
            .showConfirmDialog(mainPanel, "Are you sure you want to resign the game against "
                + gameInfo.getOpponents(), "Warning", dialogButton);
        if (dialogResult == JOptionPane.YES_OPTION) {
          controller
              .sendMessage(new MenuMessage(MenuMessageTypes.RESIGN_GAME, gameInfo.getInfoArray()));
        }
      }
    });
    playGameButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int gameIndex = getGameId();
        if (gameIndex != -1) {
          ActiveGameInfo gameInfo = activeGames.get(gameIndex);
          controller
              .sendMessage(new MenuMessage(MenuMessageTypes.SELECT_GAME, gameInfo.getInfoArray()));
        }
      }
    });
  }

  private int getGameId() {
    int gameIndex = activeGamesTable.getSelectedRow();
    if (gameIndex != -1) {
      return (int) gameInfoModel.getValueAt(activeGamesTable.convertRowIndexToModel(gameIndex), 0);

    }
    return gameIndex;
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

    for (int i = 0; i < gameInfoMessage.gameIDs.length; i++) {
      activeGames.put(gameInfoMessage.gameIDs[i],
          new ActiveGameInfo(gameInfoMessage.gameIDs[i], gameInfoMessage.gameBoards[i],
              gameInfoMessage.opponents[i], gameInfoMessage.startDates[i], gameInfoMessage.turns[i],
              gameInfoMessage.color[i], gameInfoMessage.ended[i]));
    }

    for (Integer game : activeGames.keySet()) {
      String playersTurn;
      ActiveGameInfo gameInfo = activeGames.get(game);
      if (gameInfo.getColor() == gameInfo.getTurns()) {
        playersTurn = "Your turn";
      } else {
        playersTurn = gameInfo.getOpponents() + "'s turn";
      }

      gameInfoModel.addRow(
          new Object[]{gameInfo.getGameIDs(), gameInfo.getOpponents(),
              gameInfo.getStartDates(), playersTurn, gameInfo.getEnded()});

    }

  }

  public static void loadTestData(ActiveGamesPanel testPanel) {
    ActiveGameResponse testResponse = new ActiveGameResponse(
        new int[]{111, 222, 333},
        new String[]{"RiIrdDKjJkeEQaAqlL", "RiIrdDKjJkeEQaAqlL", "RiIrdDKjJkeEQaAqlL"},
        new String[]{"Me", "Myself", "I"},
        new String[]{"12/12/12", "13/13/13", "14/14/14"},
        new boolean[]{true, false, true},
        new boolean[]{true, true, true},
        new boolean[]{false, false, true});

    testPanel.updateTable(testResponse);

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
    ActiveGamesPanel demo = new ActiveGamesPanel(new TestMainActiveGameController());
    //demo.loadTestData(demo);
    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  private static class TestMainActiveGameController extends SwingGUIController {

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

      throw new IllegalArgumentException("ActiveGame:: Tried to send a action - "
          + e.getActionCommand());

    }
  }

}
