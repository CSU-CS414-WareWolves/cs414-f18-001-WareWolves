package client.gui.swing;

import client.gui.ChadGameDriver;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.ViewMessageType;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.network.messages.GameInfo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class GameJPanel extends JPanel implements ActionListener {

  JMenuBar menuBar;
  private final String RESTART_GAME = "Restart Game";
  private final String QUIT_GAME = "Quit Game";
  JLabel turnText;

  ChadGameBoard gameBoard;

  ChadGameDriver driver;

  public GameJPanel(ChadGameDriver driver){
    super(new BorderLayout());

    this.driver = driver;
    this.gameBoard = new ChadGameBoard(driver);

    createMenuBar();
    createGameBoard();
    createStatusBar();


  }

  private void createStatusBar() {
    JPanel statusPanel = new JPanel();
    statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
    this.add(statusPanel, BorderLayout.SOUTH);
    statusPanel.setPreferredSize(new Dimension(this.getWidth(), 16));
    statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
    turnText = new JLabel("Players Turn");
    statusPanel.add(turnText);
  }

  private void createGameBoard() {

    gameBoard = new ChadGameBoard(driver);
    this.add(gameBoard, BorderLayout.CENTER);

  }


  public void createMenuBar() {
    //Create the menu bar.
    menuBar = new JMenuBar();

    //Build the first menu.
    JMenu menu = new JMenu("Game Menu");
    menuBar.add(menu);

    JMenuItem restartGame = new JMenuItem(RESTART_GAME);
    restartGame.addActionListener(this);
    menu.add(restartGame);

    //a group of JMenuItems
    JMenuItem quitGame = new JMenuItem(QUIT_GAME);
    quitGame.addActionListener(this);
    menu.add(quitGame);

    // need better validation
    /*
    menuItem = new JMenuItem("Resign Game");
    menuItem.addActionListener(this);
    menu.add(menuItem);
    */

    this.add(menuBar, BorderLayout.NORTH);
  }



  /**
   * Create the GUI and show it.  For thread safety,
   * this method should be invoked from the
   * event-dispatching thread.
   */
  private static void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("MenuLookDemo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    GameJPanel demo = new GameJPanel(new SwingChadDriver());
    frame.add(demo);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    //Schedule a job for the event-dispatching thread:
    //creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }

  public void setBoardPieces(String piecesLocations) {
    gameBoard.setBoardPieces(piecesLocations);
  }

  public void setSetGameStatus(String gameStatus) {
    turnText.setText(gameStatus);
  }


  public void actionPerformed(ActionEvent e) {

    if(RESTART_GAME.equals(e.getActionCommand())){
      JOptionPane.showMessageDialog(this, "Restarting Game");
      driver.handleNetMessage(new GameInfo(-1, "rdCreDRiHRjIrcCkdDreERhHKiIRjJrcDrdERhIRiJrcERhJreCRjH", false));
    } else if(QUIT_GAME.equals(e.getActionCommand())){
      JOptionPane.showMessageDialog(this, "Quitting Game");
      driver.handleViewMessage(new MenuMessage(MenuMessageTypes.LOGOUT, new String[0]));
    }

  }

  public void displayGameOverMessage(String message){

    JOptionPane.showMessageDialog(this,message);
  }


  public void setValidMoves(String validMoves) {
    gameBoard.setValidMoves(validMoves, true);
  }
}
