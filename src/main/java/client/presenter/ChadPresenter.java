package client.presenter;

import client.game.Game;
import client.gui.ChadGameDriver;
import client.gui.cl.CLDriver;
import client.gui.swing.SwingChadDriver;
import client.gui.swing.SwingController;
import client.gui.swing.info.ActiveGameInfo;
import client.presenter.controller.messages.GameRequestMessage;
import client.presenter.controller.messages.InviteMessage;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.LoginResponseMessage;
import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.MovePieceResponse;
import client.presenter.controller.messages.ProfileMessage;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.RegisterResponseMessage;
import client.presenter.controller.messages.UnregisterMessage;
import client.presenter.controller.messages.UnregisterResponseMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import client.presenter.controller.messages.ViewValidMovesResponse;
import client.presenter.controller.util.HashPasswords;
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
import client.presenter.network.messages.Unregister;
import client.presenter.network.messages.UnregisterResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

public class ChadPresenter implements ChadGameDriver{

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

  private ActiveGameInfo currentGame;


  /**
   * Processes a message from the Swing GUI
   * @param message the message to process
   */
  public void handleViewMessage(ViewMessage message) {

    System.out.println(message.messageType);
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
          String[] messages = {"Invalid Nickname - Nickname can not have # or : in it"};
          RegisterResponseMessage registerResponseMessage = new RegisterResponseMessage(false, messages);
          // Send message to gui/cli handle view message
          viewDriver.handleViewMessage(registerResponseMessage);
        }
        else if (emailColon != -1 || emailPound != -1) {
          // Email contains invalid characters
          String[] messages = {"Invalid Email - Emails can not have # or : in them"};
          RegisterResponseMessage registerResponseMessage = new RegisterResponseMessage(false, messages);
          // Send message to gui/cli handle view message
          viewDriver.handleViewMessage(registerResponseMessage);
        }
        else {
          // Email and nickname do not contain any invalid characters. Send to network manager.
            networkManager.sendMessage(new Register(registerMessage.email, registerMessage.nickname,
                registerMessage.password));
        }
        break;
      case LOGIN:
        LoginMessage loginMessage = (LoginMessage) message;
        networkManager.sendMessage(new Login(loginMessage.email, loginMessage.password));
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
        if(chadGame.getTurn() != currentGame.getColor()) {return;}
        // Find the valid moves
        ViewValidMoves validMovesMessage = (ViewValidMoves) message;
        String validMoves = chadGame.validMoves(validMovesMessage.location.toString());
        // Tell GUI to what moves to show
        viewDriver.handleViewMessage(new ViewValidMovesResponse(new String [] {validMoves}));
        break;
      case MOVE_PIECE:
        MovePieceMessage moves = (MovePieceMessage) message;
        boolean draw = false;
        boolean ending = false;
        if(chadGame.getTurn() != currentGame.getColor()) {
          viewDriver.handleViewMessage(new MovePieceResponse(getCurrentPlayer(chadGame.getTurn()) + "'s turn.", chadGame.getBoard()));
        }
        // Checks to see if the move was successful
        if(chadGame.move(moves.fromLocation.toString(), moves.toLocation.toString())){
         // Show the winner if the game is over
         if (chadGame.gameover()) {
           ending = true;
           // Check if draw
           if (chadGame.isDraw()) {
             draw = true;
             // Create message response with draw
             MovePieceResponse movePieceResponse = new MovePieceResponse("The game has ended in a draw.",
                 chadGame.getBoard());
             viewDriver.handleViewMessage(movePieceResponse);
           } else {
             String winner =  playerNickname + " has won the game.";
             // Create message response with winner
             MovePieceResponse movePieceResponse = new MovePieceResponse(winner,
                 chadGame.getBoard());
             viewDriver.handleViewMessage(movePieceResponse);
           }
         } else {
           // Game is not over
           MovePieceResponse movePieceResponse = new MovePieceResponse(getCurrentPlayer(chadGame.getTurn()) + "'s turn.", chadGame.getBoard());
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
           String error = "Invalid Move.";
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
        ActiveGameRequest activeGameRequest = new ActiveGameRequest(playerNickname);
        networkManager.sendMessage(activeGameRequest);
        break;
      case INBOX:
        // Send an inbox request to the net manager
        InboxRequest inboxRequest = new InboxRequest(playerNickname);
        networkManager.sendMessage(inboxRequest);
        break;
      case GAME_REQUEST:
        // Send a game request to the net manager
        GameRequestMessage gameRequestMessage = (GameRequestMessage) message;
        currentGame = new ActiveGameInfo(gameRequestMessage.gameInfo);
        chadGame = new Game(currentGame.getGameBoard(), currentGame.getTurn());
        viewDriver.handleViewMessage(new MovePieceResponse(getCurrentPlayer(chadGame.getTurn()) + "'s turn.", chadGame.getBoard()));
        break;
      case INVITE:
        // Send an invite request to the net manager
        InviteMessage inviteMessage = (InviteMessage) message;
        InviteRequest inviteRequest = new InviteRequest(inviteMessage.sender, inviteMessage.recipient);
        networkManager.sendMessage(inviteRequest);
    }
  }


  /**
   * Handles all the messages from NetManager(Not Implemented)
   * @param message the message to process
   */
  public void handleNetMessage(NetworkMessage message){
    System.out.println("handleNetMessage:: " + message.type);
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
      case MOVE:
        Move move = (Move) message;
        // Check if the move message is for the current game
        if(currentGame.getGameID() == move.gameID) {
          if (move.ending) {
            // The game has ended
            if (move.draw) {
              // The game ends in a draw
              // Show draw
              MovePieceResponse movePieceResponse = new MovePieceResponse("The game has ended in a draw.", move.board);
              viewDriver.handleViewMessage(movePieceResponse);
            } else {
              // Show game ending
              // Creates a string with who won the game
              String winner = getCurrentPlayer(chadGame.getTurn()) + " has won.";
              MovePieceResponse movePieceResponse = new MovePieceResponse(winner, move.board);
              viewDriver.handleViewMessage(movePieceResponse);
            }
          } else {
            // Game is not over
            MovePieceResponse movePieceResponse = new MovePieceResponse(getCurrentPlayer(chadGame.getTurn()) + "'s turn.", chadGame.getBoard());
            viewDriver.handleViewMessage(movePieceResponse);
          }
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
        viewDriver.handleNetMessage(players);
        break;
      case UNREGISTER_RESPONSE:
        UnregisterResponse unregisterResponse = (UnregisterResponse) message;
        if(unregisterResponse.success) {
          // Successfully unregistered
          String[] messages = {"Successfully Unregistered."};
          UnregisterResponseMessage unregisterResponseMessage = new UnregisterResponseMessage(unregisterResponse.success, messages);
          viewDriver.handleViewMessage(unregisterResponseMessage);
        }
        else {
          // Not successful
          String[] messages = {"Unable to unregister. Please try again."};
          UnregisterResponseMessage unregisterResponseMessage = new UnregisterResponseMessage(unregisterResponse.success, messages);
          viewDriver.handleViewMessage(unregisterResponseMessage);
        }
        break;
    }
  }

  public void createAndShowGUI() { }

  /**
   * Default constructor will need IP and Port for server
   */
  public ChadPresenter(String host, String port, String userInterface){
    try {
      InetAddress addr = InetAddress.getByName(host);
      networkManager = new NetworkManager(addr, Integer.parseInt(port), this);
      networkManager.startThread();
    } catch (UnknownHostException e) {
      System.err.println("Unknown Host");
    } catch (IOException e) { }
    if(userInterface.equals("cli")){
      // Instantiate CLI Controller
      viewDriver = new CLDriver(this);
    } else if(userInterface.equals("gui")){
      // Instantiate GUI Controller
      viewDriver = new SwingController(this);
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
   * Gets the Name of the current players turn
   * @param turn the turn
   * @return Black or White String
   */
  private String getCurrentPlayer(boolean turn) {
    return turn == currentGame.getColor() ? playerNickname : currentGame.getOpponent();
  }


  public static void main(String[] args) {
    // args[0] = "cli" or "gui", args[1] server host, args[2] server port
    ChadPresenter app = new ChadPresenter(args[1], args[2], args[0]);
    app.start();
  }
}
