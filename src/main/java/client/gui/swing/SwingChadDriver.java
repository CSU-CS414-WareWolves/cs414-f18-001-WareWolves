package client.gui.swing;

import client.game.Game;
import client.game.GameBoard;
import client.gui.ChadGameDriver;
import client.presenter.controller.ViewMessageType;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import client.presenter.network.messages.GameInfo;
import client.presenter.network.messages.NetworkMessage;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SwingChadDriver implements ChadGameDriver{

  private GameJPanel gamePanel;
  private Game chadGame;

  private int gameID;
  private String playerNickname;


  private static final String DEFAULT_GAME_BOARD =
      "rdCreDRiHRjIrcCkdDreERhHKiIRjJrcDrdERhIRiJrcERhJreCRjH";


  public void handleViewMessage(ViewMessage message){

    switch (message.messageType){
      case REGISTER:
        break;
      case LOGIN:
        break;
      case UNREGISTER:
        break;
      case SHOW_VALID_MOVES:
        if(chadGame.gameover()){return;}
        ViewValidMoves validMovesMessage = (ViewValidMoves) message;
        String validMoves = chadGame.validMoves(validMovesMessage.location.toString());
        gamePanel.setValidMoves(validMoves);
        break;
      case MENU:
        handleMenuMessage((MenuMessage) message);
        break;
      case MOVE_PIECE:
        MovePieceMessage moves = (MovePieceMessage) message;
        chadGame.move(moves.fromLocation.toString(), moves.toLocation.toString());
        setupGame(gameID, chadGame.getBoard(), chadGame.getTurn());

        if(chadGame.gameover()){
          gamePanel.displayGameOverMessage(
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

  private void handleMenuMessage(MenuMessage message) {
    switch (message.menuType){

      case LOGOUT:
        // Send Logout to server
        System.exit(0);
        break;
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




  public SwingChadDriver(){



  }

  public void start(){

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
          createAndShowGUI();
      }
    });
  }

  private void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("Team Warewolves Chad");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    gamePanel = new GameJPanel(this);
    chadGame = new Game();

    setupGame(-1, chadGame.getBoard(), chadGame.getTurn());

    frame.setContentPane(gamePanel);

    //Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  private void setupGame(int gameId, String boardSetup, boolean turn){
    this.gameID = gameId;
    gamePanel.setBoardPieces(boardSetup);
    String playerTurnMessage = getCurrentPlayer(turn) + " player's turn";
    gamePanel.setSetGameStatus(playerTurnMessage);
  }

  private String getCurrentPlayer(boolean turn) {
    return turn ? "The Black" : "The White";
  }


  public static void main(String[] args) {

    SwingChadDriver app = new SwingChadDriver();
    app.start();

  }
}
