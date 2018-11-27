package client.gui.swing.panels;


import client.gui.swing.info.ActiveGameInfo;
import client.presenter.network.messages.ActiveGameResponse;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

public class ActiveGamesPanel extends JPanel {

  Object[] columnNames = {"Opponent", "Start Date", "Players turn", "Game Finished"};

  private ActiveGameResponse testResponse = new ActiveGameResponse(
      new int[]{1, 2, 3},
      new String[]{"RiIrdDKjJkeEQaAqlL", "RiIrdDKjJkeEQaAqlL", "RiIrdDKjJkeEQaAqlL"},
      new String[]{"Me", "Myself", "I"},
      new String[]{"12/12/12", "13/13/13", "14/14/14"},
      new boolean[]{true, false, true},
      new boolean[]{true, true, true},
      new boolean[]{false, false, true});

  private ArrayList<ActiveGameInfo> activeGames = new ArrayList<>();

  private JPanel mainPanel;
  private JButton playGameButton;
  private JButton resignGameButton;
  private JTable activeGamesTable;
  private DefaultTableModel gameInfoTable;


  public ActiveGamesPanel() {
    resignGameButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateGamesList();

        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane
            .showConfirmDialog(null, "Are you sure you want to resign the game", "Warning",
                dialogButton);
        if (dialogResult == JOptionPane.YES_OPTION) {
          int gameIndex = activeGamesTable.getSelectedRow();
          if (gameIndex != -1) {
            System.out.println(gameInfoTable.getValueAt(gameIndex, 0));
            System.out.println(activeGames.get(gameIndex).getOpponents());
          }
        }

      }
    });
    playGameButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int gameIndex = activeGamesTable.getSelectedRow();
        if (gameIndex != -1) {
          System.out.println(gameInfoTable.getValueAt(gameIndex, 0));
          System.out.println(activeGames.get(gameIndex).getOpponents());
        }
      }
    });
  }

  private void createUIComponents() {

    gameInfoTable = new DefaultTableModel(new Object[][]{}, columnNames);
    activeGamesTable = new JTable(gameInfoTable) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    activeGamesTable.setRowSelectionAllowed(true);
    activeGamesTable.setColumnSelectionAllowed(false);
    activeGamesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

  public void updateGamesList() {

    // Reset current data
    activeGames.clear();
    gameInfoTable.setNumRows(0);

    for (int i = 0; i < testResponse.gameIDs.length; i++) {
      activeGames.add(new ActiveGameInfo(testResponse.gameIDs[i], testResponse.gameBoards[i],
          testResponse.opponents[i], testResponse.startDates[i], testResponse.turns[i],
          testResponse.color[i], testResponse.ended[i]));
    }

    for (ActiveGameInfo game : activeGames) {
      String playersTurn;
      if (game.getColor() == game.getTurns()) {
        playersTurn = "Your turn";
      } else {
        playersTurn = game.getOpponents() + "'s turn";
      }

      gameInfoTable.addRow(
          new Object[]{game.getOpponents(), game.getStartDates(), playersTurn, game.getEnded()});

    }

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
    ActiveGamesPanel demo = new ActiveGamesPanel();
    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

}
