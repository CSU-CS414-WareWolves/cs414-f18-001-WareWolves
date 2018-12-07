package edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.panels.chadgame;

import edu.colostate.cs.cs414.warewolves.chad.client.gui.ChadGameDriver;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.LogoutMessage;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class GameJPanel extends JPanel implements ActionListener {

  /**
   * The menu bar
   */
  JMenuBar menuBar;
  /**
   * Name for quit menu action
   */
  private final String QUIT_GAME = "Quit Game";
  /**
   * Status menu text
   */
  JLabel turnText;

  /**
   * The game board panel
   */
  ChadGameBoard gameBoard;
  /**
   * The diver for the panel
   */
  ChadGameDriver driver;

  /**
   * Creates a game panel with an empty game board
   * @param driver the driver for the panel
   */
  public GameJPanel(ChadGameDriver driver) {
    super(new BorderLayout());

    this.driver = driver;
    this.gameBoard = new ChadGameBoard(driver);

    createMenuBar();
    createGameBoard();
    createStatusBar();



  }

  /**
   * Creates the status board panel
   */
  private void createStatusBar() {
    JPanel statusPanel = new JPanel();
    statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
    this.add(statusPanel, BorderLayout.SOUTH);
    statusPanel.setPreferredSize(new Dimension(this.getWidth(), 32));
    statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
    turnText = new JLabel("Players Turn");
    statusPanel.add(turnText);
  }

  /**
   * Creates the game board panel
   */
  private void createGameBoard() {

    gameBoard = new ChadGameBoard(driver);
    this.add(gameBoard, BorderLayout.CENTER);

  }

  /**
   * Creates the menu items
   */
  private void createMenuBar() {
    //Create the menu bar.
    menuBar = new JMenuBar();

    //Build the first menu.
    JMenu menu = new JMenu("Game Menu");
    menuBar.add(menu);


    //a group of JMenuItems
    JMenuItem quitGame = new JMenuItem(QUIT_GAME);
    quitGame.addActionListener(this);
    menu.add(quitGame);


    this.add(menuBar, BorderLayout.NORTH);
  }





  /**
   * Tells the board to setup the game pieces in a given locations
   *
   * @param piecesLocations the piece locations
   */
  public void setBoardPieces(String piecesLocations) {
    gameBoard.setBoardPieces(piecesLocations);
  }

  /**
   * Sets the status board
   */
  public void setSetGameStatus(String gameStatus) {
    turnText.setText(gameStatus);
  }


  /**
   * Logic for the panel menus
   *
   * @param e the menu command
   */
  public void actionPerformed(ActionEvent e) {
    if (QUIT_GAME.equals(e.getActionCommand())) {
      driver.handleViewMessage(new LogoutMessage());
    }

  }

  /**
   * Displays a message centered at the game panel
   */
  public void displayMessage(String message) {

    JOptionPane.showMessageDialog(this, message);
  }

  /**
   * Tells the game board to show all the valid moves for a piece
   *
   * @param validMoves the list of valid moves
   */
  public void setValidMoves(String validMoves) {
    gameBoard.setValidMoves(validMoves, true);
  }

  public void clearValidMoves() { gameBoard.setAllValidMoves(false); }

}
