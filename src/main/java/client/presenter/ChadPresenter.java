package client.presenter;

import client.game.Game;
import client.gui.ChadGameDriver;
import client.gui.swing.GameJPanel;
import client.presenter.controller.messages.ActiveGameMessage;
import client.presenter.controller.messages.GameRequestMessage;
import client.presenter.controller.messages.InboxMessage;
import client.presenter.controller.messages.InviteMessage;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.LoginResponseMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.MovePieceResponse;
import client.presenter.controller.messages.ProfileMessage;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.RegisterResponseMessage;
import client.presenter.controller.messages.UnregisterMessage;
import client.presenter.controller.messages.UnregisterResponseMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import client.presenter.network.NetworkManager;
import client.presenter.network.messages.ActiveGameRequest;
import client.presenter.network.messages.ActiveGameResponse;
import client.presenter.network.messages.GameInfo;
import client.presenter.network.messages.GameRequest;
import client.presenter.network.messages.InboxRequest;
import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.InviteRequest;
import client.presenter.network.messages.Login;
import client.presenter.network.messages.LoginResponse;
import client.presenter.network.messages.Move;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.Players;
import client.presenter.network.messages.ProfileRequest;
import client.presenter.network.messages.ProfileResponse;
import client.presenter.network.messages.Register;
import client.presenter.network.messages.RegisterResponse;
import client.presenter.controller.util.HashPasswords;
import client.presenter.network.messages.Unregister;
import client.presenter.network.messages.UnregisterResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

public class ChadPresenter implements ChadGameDriver{

  /**
   * The Swing GUI / View
   */
  private GameJPanel gamePanel;
  /**
   * The view controller
   */
  private ChadGameDriver viewDriver;
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
   * The network manager that handles messages to the server
   */
  private NetworkManager networkManager; // Initialize (Not Implemented)
  /**
   * Array of all the nicknames of players in the database
   */
  private String[] players;



  /**
   * Processes a message from the Swing GUI
   * @param message the message to process
   */
  public void handleViewMessage(ViewMessage message) {

    switch (message.messageType){
      case REGISTER:
        RegisterMessage registerMessage = (RegisterMessage) message;
        // Check if messages have invalid characters
        int nicknamePound = registerMessage.nickname.indexOf('#');
        int nicknameColon = registerMessage.nickname.indexOf(':');
        int emailPound = registerMessage.email.indexOf('#');
        int emailColon = registerMessage.email.indexOf(':');
        if(nicknameColon != -1 || nicknamePound != -1) {
          // Nickname contains invalid characters
          String[] messages = {"Invalid Nickname"};
          RegisterResponseMessage registerResponseMessage = new RegisterResponseMessage(false, messages);
          // Send message to gui/cli handle view message
          viewDriver.handleViewMessage(registerResponseMessage);
        }
        else if (emailColon != -1 || emailPound != -1) {
          // Email contains invalid characters
          String[] messages = {"Invalid Email"};
          RegisterResponseMessage registerResponseMessage = new RegisterResponseMessage(false, messages);
          // Send message to gui/cli handle view message
          viewDriver.handleViewMessage(registerResponseMessage);
        }
        else {
          // Email and nickname do not contain any invalid characters. Send to network manager.
          try {
            networkManager.sendMessage(new Register(registerMessage.email, registerMessage.nickname,
                HashPasswords.SHA1FromString(registerMessage.password)));
          } catch(NoSuchAlgorithmException e) {
            // Do nothing
          }
        }
        break;
      case LOGIN:
        LoginMessage loginMessage = (LoginMessage) message;
        try {
          networkManager.sendMessage(new Login(loginMessage.email, HashPasswords.SHA1FromString(loginMessage.password)));
        } catch(NoSuchAlgorithmException e) {
          // Do nothing
        }
        break;
      case UNREGISTER:
        UnregisterMessage unregisterMessage = (UnregisterMessage) message;
        try{
          networkManager.sendMessage(new Unregister(unregisterMessage.email, unregisterMessage.nickname, HashPasswords.SHA1FromString(unregisterMessage.password)));
        } catch(NoSuchAlgorithmException e) {
          // Do nothing
        }
        break;
      case SHOW_VALID_MOVES: // Need to change with addition of CLI
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
        boolean draw = false;
        boolean ending = false;
        // Checks to see if the move was successful
        if(chadGame.move(moves.fromLocation.toString(), moves.toLocation.toString())){
          setupGame(gameID, chadGame.getBoard(), chadGame.getTurn());
         // Show the winner if the game is over
         if (chadGame.gameover()) {
           ending = true;
           // Check if draw
           if (chadGame.isDraw()) {
             draw = true;
             // Create message response with draw
             MovePieceResponse movePieceResponse = new MovePieceResponse("Draw",
                 chadGame.getBoard());
             viewDriver.handleViewMessage(movePieceResponse);
           } else {
             String winner = getCurrentPlayer(!chadGame.getTurn()) + " player has won.";
             // Create message response with winner
             MovePieceResponse movePieceResponse = new MovePieceResponse(winner,
                 chadGame.getBoard());
             viewDriver.handleViewMessage(movePieceResponse);
           }
         } else {
           // Game is not over
           MovePieceResponse movePieceResponse = new MovePieceResponse("Opponent's turn", chadGame.getBoard());
           viewDriver.handleViewMessage(movePieceResponse);
         }
           // Send Move to Server
           // Get piece being moved
           int index = chadGame.getBoard().indexOf(moves.toLocation.toString());
           String board = chadGame.getBoard();
           char piece = board.charAt(index - 1);
           String moveString = piece + moves.fromLocation.toString() + moves.toLocation.toString();
           Move move = new Move(gameID, moveString, chadGame.getBoard(), ending, draw);
           networkManager.sendMessage(move);
      } else {
          // Send a move piece response message with an error
           String error = "Invalid Move";
           MovePieceResponse movePieceResponse = new MovePieceResponse(error, chadGame.getBoard());
          viewDriver.handleViewMessage(movePieceResponse);
        }
        break;
      case PROFILE:
        // Send a profile request to the net manager
        ProfileMessage profile = (ProfileMessage) message;
        ProfileRequest profileRequest = new ProfileRequest(profile.nickname);
        networkManager.sendMessage(profileRequest);
        break;
      case ACTIVE_GAMES:
        // Send a active games request to the net manager
        ActiveGameMessage activeGames = (ActiveGameMessage) message;
        ActiveGameRequest activeGameRequest = new ActiveGameRequest(activeGames.nickname);
        networkManager.sendMessage(activeGameRequest);
        break;
      case INBOX:
        // Send an inbox request to the net manager
        InboxMessage inboxMessage = (InboxMessage) message;
        InboxRequest inboxRequest = new InboxRequest(inboxMessage.nickname);
        networkManager.sendMessage(inboxRequest);
        break;
      case GAME_REQUEST:
        // Send a game request to the net manager
        GameRequestMessage gameRequestMessage = (GameRequestMessage) message;
        GameRequest gameRequest = new GameRequest(gameRequestMessage.gameID);
        networkManager.sendMessage(gameRequest);
        break;
      case INVITE:
        // Send an invite request to the net manager
        InviteMessage inviteMessage = (InviteMessage) message;
        InviteRequest inviteRequest = new InviteRequest(inviteMessage.sender, inviteMessage.recipient);
        networkManager.sendMessage(inviteRequest);
    }
  }

  /**
   * Handles all the menu messages from the gui
   * @param message the message to process
   */
  private void handleMenuMessage(MenuMessage message) {
    switch (message.menuType){
      case LOGOUT:
        // Send Logout to server
        System.exit(0);
        break;
    }
  }

  /**
   * Handles all the messages from NetManager(Not Implemented)
   * @param message the message to process
   */
  public void handleNetMessage(NetworkMessage message){
    switch (message.type){
      case LOGIN_RESPONSE:
        LoginResponse loginResponse = (LoginResponse) message;
        // If the login was successful
        if(loginResponse.success) {
          this.playerNickname = loginResponse.nickname;
        }
        LoginResponseMessage loginResponseMessage = new LoginResponseMessage(loginResponse.success, loginResponse.nickname);
        // Send message to gui/cli handle view message
        viewDriver.handleViewMessage(loginResponseMessage);
        break;
      case GAME_INFO:
        GameInfo gameInfo = (GameInfo) message;
        chadGame = new Game(gameInfo.gameBoard, gameInfo.turn);
        setupGame(gameInfo.gameID, chadGame.getBoard(), chadGame.getTurn());
        break;
      case MOVE:
        Move move = (Move) message;
        // Check if the move message is for the current game
        if(this.gameID == move.gameID) {
          if (move.ending) {
            // The game has ended
            if (move.draw) {
              // The game ends in a draw
              // Show draw
              MovePieceResponse movePieceResponse = new MovePieceResponse("Draw", move.board);
              viewDriver.handleViewMessage(movePieceResponse);
            } else {
              // Show game ending
              // Creates a string with who won the game
              String winner = getCurrentPlayer(chadGame.getTurn()) + " player has won.";
              MovePieceResponse movePieceResponse = new MovePieceResponse(winner, move.board);
              viewDriver.handleViewMessage(movePieceResponse);
            }
          } else {
            // Game is not over
            MovePieceResponse movePieceResponse = new MovePieceResponse("Your turn", chadGame.getBoard());
            viewDriver.handleViewMessage(movePieceResponse);
          }
        } else {
          // Not for current game. Do nothing.
        }
        break;
      case ACTIVE_GAMES_RESPONSE:
        ActiveGameResponse activeGameResponse = (ActiveGameResponse) message;
        // Send to the view controller to display Active Games in view with ID, board, opponents, start dates, current turn, color and if it has ended
        viewDriver.handleNetMessage(activeGameResponse);
        break;
      case REGISTER_RESPONSE:
        RegisterResponse registerResponse = (RegisterResponse) message;
        if(registerResponse.success) {
          // Successful Register
          String[] messages = {"Sucessfully Registered."};
          RegisterResponseMessage registerResponseMessage = new RegisterResponseMessage(registerResponse.success, messages);
          viewDriver.handleViewMessage(registerResponseMessage);
        }
        else {
          if(registerResponse.reason) {
            // Nickname already taken
            String[] messages = {"Could not register. Nickname already in use."};
            RegisterResponseMessage registerResponseMessage = new RegisterResponseMessage(registerResponse.success, messages);
            viewDriver.handleViewMessage(registerResponseMessage);
          }
          else {
            // Email already taken
            // Display unsuccessful register email taken (Not Implemented)
            String[] messages = {"Could not register. Email already in use."};
            RegisterResponseMessage registerResponseMessage = new RegisterResponseMessage(registerResponse.success, messages);
            viewDriver.handleViewMessage(registerResponseMessage);
          }
        }
        break;
      case INBOX_RESPONSE:
        InboxResponse inboxResponse = (InboxResponse) message;
        // Send to the view controller to display inbox of messages with ids, senders, recipients, and send dates
        viewDriver.handleNetMessage(inboxResponse);
        break;
      case PROFILE_RESPONSE:
        ProfileResponse profileResponse = (ProfileResponse) message;
        // Send to the view controller to display profile with games player played white and black, start and end dates of games, and results of games
        viewDriver.handleNetMessage(profileResponse);
        break;
      case PLAYERS:
        Players players = (Players) message;
        // Store player array
        this.players = players.players;
        break;
      case UNREGISTER_RESPONSE:
        UnregisterResponse unregisterResponse = (UnregisterResponse) message;
        if(unregisterResponse.success) {
          // Successfully unregistered
          String[] messages = {"Successfully Unregistered."};
          UnregisterResponseMessage unregisterResponseMessage = new UnregisterResponseMessage(unregisterResponse.success, messages);
        }
        else {
          // Not successful
          String[] messages = {"Unable to unregister. Please try again."};
          UnregisterResponseMessage unregisterResponseMessage = new UnregisterResponseMessage(unregisterResponse.success, messages);
        }
        break;
    }
  }

  public void createAndShowGUI() {
  }

  /**
   * Default constructor will need IP and Port for server
   */
  public ChadPresenter(String host, String port, String userInterface){
    try {
      InetAddress addr = InetAddress.getByName(host);
      networkManager = new NetworkManager(addr, Integer.parseInt(port), this);
    } catch (UnknownHostException e) {
      System.err.println("Unknown Host");
    } catch (IOException e) { }
    if(userInterface.equals("cli")){
      // Instantiate CLI Controller
    } else if(userInterface.equals("gui")){
      // Instantiate GUI Controller
    }
  }

  /**
   * Default Constructor no arguments
   */
  public ChadPresenter() {

  }

  /**
   * Starts a thread for the Swing GUI
   */
  public void start(){

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
          viewDriver.createAndShowGUI();
      }
    });
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
    ChadPresenter app = new ChadPresenter(args[1], args[2], args[0]);
    app.start();
  }
}