package client.gui.swing;

import client.game.Game;
import client.gui.ChadGameDriver;
import client.gui.swing.panels.chadgame.GameJPanel;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import client.presenter.network.messages.GameInfo;
import client.presenter.network.messages.NetworkMessage;
import javax.swing.JFrame;

public class SwingChadDriver implements ChadGameDriver{

  /**
   * The Swing GUI / View
   */
  private GameJPanel gamePanel;
  /**
   * The game / Model
   */
  private Game chadGame;

  /**
   * The currently login play
   */
  private int gameID;
  /**
   * The players name
   */
  private String playerNickname;



  /**
   * Processes a message from the Swing GUI
   * @param message the message to process
   */
  public void handleViewMessage(ViewMessage message){

    switch (message.messageType){
      case REGISTER:
        break;
      case LOGIN:
        break;
      case UNREGISTER:
        break;
      case SHOW_VALID_MOVES:
        // if the game is over no valid moves
        if(chadGame.gameover()){return;}
        // Find the valid moves
        ViewValidMoves validMovesMessage = (ViewValidMoves) message;
        String validMoves = chadGame.validMoves(validMovesMessage.location.toString());
        // Tell GUI to what moves to show
        gamePanel.setValidMoves(validMoves);
        break;
      case MENU:
        handleMenuMessage((MenuMessage) message);
        break;
      case MOVE_PIECE:
        MovePieceMessage moves = (MovePieceMessage) message;
        chadGame.move(moves.fromLocation.toString(), moves.toLocation.toString());
        setupGame(gameID, chadGame.getBoard(), chadGame.getTurn());

        // Show the winner if the game is over
        if(chadGame.gameover()){
          gamePanel.displayMessage(
              getCurrentPlayer(!chadGame.getTurn()) + " player won the game!");
        }
        // Send Move to Server
        break;
      case REGISTER_RESPONSE:
        break;
      case LOGIN_RESPONSE:
        break;
      case UNREGISTER_RESPONSE:
        break;
      case SHOW_VALID_MOVES_RESPONSE:
        break;
      case MENU_RESPONSE:
        break;
      case MOVE_PIECE_RESPONSE:
        break;
    }

  }

  /**
   * Handles all the menu messages from the gui
   * @param message the message to process
   */
  private void handleMenuMessage(MenuMessage message) {
    switch (message.menuType){

      case PLAYER_STATS:
        break;
      case ACTIVE_GAMES:
        break;
      case INVITES:
        break;
      case SELECT_GAME:
        break;
      case SEND_INVITE:
        break;
    }
  }

  /**
   * Handles all the messages from NetManager(Not Implemented)
   * @param message the message to process
   */
  public void handleNetMessage(NetworkMessage message){
    switch (message.type){
      case LOGIN:
        break;
      case LOGIN_RESPONSE:
        break;
      case LOGOUT:
        break;
      case REGISTER:
        break;
      case UNREGISTER:
        break;
      case GAME_REQUEST:
        break;
      case GAME_INFO:
        GameInfo gameInfo = (GameInfo) message;
        chadGame = new Game(gameInfo.gameBoard, gameInfo.turn);
        setupGame(gameInfo.gameID, chadGame.getBoard(), chadGame.getTurn());
        break;
      case MOVE:
        break;
      case ACTIVE_GAMES_REQUEST:
        break;
      case ACTIVE_GAMES_RESPONSE:
        break;
      case INVITE_REQUEST:
        break;
      case INVITE_RESPONSE:
        break;
      case RESIGN:
        break;
      case REGISTER_RESPONSE:
        break;
      case INBOX_REQUEST:
        break;
      case INBOX_RESPONSE:
        break;
    }
  }

  /**
   * Default constructor will need IP and Port for server
   */
  public SwingChadDriver(){
  }

  /**
   * Starts a thread for the Swing GUI
   */
  public void start(){

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
          createAndShowGUI();
      }
    });
  }

  /**
   * Sets up the Java Swing Frame for the game
   */
  public void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("Team Warewolves Chad");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    gamePanel = new GameJPanel(this);
    chadGame = new Game();

    setupGame(-1, chadGame.getBoard(), chadGame.getTurn()); // Setup default game for demo

    frame.setContentPane(gamePanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Sets up a game from the database and saves the gameId to use for  move messages to the server
   * @param gameId the ID for the game
   * @param boardSetup the current setup of the board
   * @param turn the current players turn
   */
  private void setupGame(int gameId, String boardSetup, boolean turn){
    this.gameID = gameId;
    gamePanel.setBoardPieces(boardSetup);
    String playerTurnMessage = getCurrentPlayer(turn) + " player's turn";
    gamePanel.setSetGameStatus(playerTurnMessage);
  }

  /**
   * Gets the Name of the current players turn
   * @param turn the turn
   * @return Black or White String
   */
  private String getCurrentPlayer(boolean turn) {
    return turn ? "The Black" : "The White";
  }


  public static void main(String[] args) {

    SwingChadDriver app = new SwingChadDriver();
    app.start();

  }
}
