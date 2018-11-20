package client.gui.swing.panels;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class ActiveGamesPanel extends JPanel{

  Object[] columnNames = {"gameIDs", "gameBoards", "opponents", "startDates", "turns", "color", "ended"};
  private JPanel mainPanel;
  private JButton playGameButton;
  private JButton resignGameButton;
  private JTable activeGamesTable;
  private DefaultTableModel gameInfoTable;


  private void createUIComponents() {

    Object[] columnNames = {"gameIDs", "gameBoards", "opponents", "startDates", "turns", "color", "ended"};

    gameInfoTable = new DefaultTableModel(columnNames, 1);
    activeGamesTable = new JTable(gameInfoTable);
    //gameInfoTable.addRow(new Object[] {"gameIDs", "gameBoards", "opponents", "startDates", "turns", "color", "ended"});

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
    ActiveGamesPanel demo = new ActiveGamesPanel();
    frame.add(demo.mainPanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }
}
