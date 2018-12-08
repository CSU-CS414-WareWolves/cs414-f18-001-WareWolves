package edu.colostate.cs.cs414.warewolves.chad.client.presenter;

import edu.colostate.cs.cs414.warewolves.chad.client.Point;
import edu.colostate.cs.cs414.warewolves.chad.client.game.Game;
import edu.colostate.cs.cs414.warewolves.chad.client.gui.ChadGameDriver;
import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.SwingController;
import edu.colostate.cs.cs414.warewolves.chad.client.gui.swing.info.ActiveGameInfo;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.GameRequestMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.InviteMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.InviteMessageResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.LoginMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.LoginResponseMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.MovePieceMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.MovePieceResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ProfileMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.RegisterMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.RegisterResponseMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ResignMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.UnregisterMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.UnregisterResponseMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ViewMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ViewValidMoves;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.controller.messages.ViewValidMovesResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.NetworkManager;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.ActiveGameRequest;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.ActiveGameResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.InboxRequest;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.InboxResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.InviteRequest;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.InviteResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.Login;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.LoginResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.Logout;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.Move;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.NetworkMessage;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.Players;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.ProfileRequest;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.ProfileResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.Register;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.RegisterResponse;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.Resign;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.SeeResults;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.Unregister;
import edu.colostate.cs.cs414.warewolves.chad.client.presenter.network.messages.UnregisterResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
        handleViewRegister((RegisterMessage) message);
        break;
      case LOGIN:
        LoginMessage loginMessage = (LoginMessage) message;
        networkManager.sendMessage(new Login(loginMessage.email, loginMessage.password));
        break;
      case UNREGISTER:
        UnregisterMessage unregisterMessage = (UnregisterMessage) message;
        networkManager.sendMessage(new Unregister(unregisterMessage.email, unregisterMessage.nickname, unregisterMessage.password));
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
      case MENU:
        break;
      case MOVE_PIECE:
        MovePieceMessage moves = (MovePieceMessage) message;
        boolean draw = false;
        boolean ending = false;
        if(chadGame.getTurn() != currentGame.getColor()) {
          viewDriver.handleViewMessage(new MovePieceResponse(getMoveMessage(), chadGame.getBoard()));
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
           MovePieceResponse movePieceResponse = new MovePieceResponse(getMoveMessage(), chadGame.getBoard());
           viewDriver.handleViewMessage(movePieceResponse);
         }
           // Send Move to Server
           // Get piece being moved
           int index = chadGame.getBoard().indexOf(moves.toLocation.toString());
           String board = chadGame.getBoard();
           char piece = board.charAt(index - 1);
           String moveString = piece + moves.fromLocation.toString() + moves.toLocation.toString();
           Move move = new Move(currentGame.getGameID(), moveString, chadGame.getBoard(), ending, draw);
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
        viewDriver.handleViewMessage(new MovePieceResponse(getMoveMessage(), chadGame.getBoard()));

        if(currentGame.getEnded()){
          networkManager.sendMessage(new SeeResults(currentGame.getGameID(), currentGame.getColor()));
        }

        break;
      case NEW_INVITE:
        // Send an invite request to the net manager
        InviteMessage inviteMessage = (InviteMessage) message;
        InviteRequest inviteRequest = new InviteRequest(inviteMessage.sender, inviteMessage.recipient);
        networkManager.sendMessage(inviteRequest);
        networkManager.sendMessage(new InboxRequest(playerNickname));
        break;
      case INVITE_RESPONSE:
        InviteMessageResponse inviteMessageResponse = (InviteMessageResponse) message;
        networkManager.sendMessage(new InviteResponse(inviteMessageResponse.inviteID, inviteMessageResponse.response));
        networkManager.sendMessage(new InboxRequest(playerNickname));
       break;
      case LOGOUT:
        networkManager.sendMessage(new Logout(playerNickname));
        playerNickname = null;
        break;
      case RESIGN:
        // Send a resign request to the net manager
        ResignMessage resignMessage = (ResignMessage) message;
        Resign resign = new Resign(resignMessage.gameID, playerNickname);
        networkManager.sendMessage(resign);
        networkManager.sendMessage(new ActiveGameRequest(playerNickname));
        break;
    }
  }

  private boolean containsInvalidCharacters(String word){
    return word.contains("#") || word.contains(":");
  }

  /**
   * Handles the logic for an new user registering
   * @param message the registration info
   */
  private void handleViewRegister(RegisterMessage message) {
    // Check if messages have invalid characters

    if(!checkForValidUserInfo(message)){
      return;
    }
    // Email and nickname do not contain any invalid characters. Send to network manager.
    networkManager.sendMessage(new Register(message.email, message.nickname, message.password));
  }

  /**
   * Checks to see if the user entered valid nickname and email
   * @param message the message with the user info
   * @return true if user name is valid, false otherwise
   */
  private boolean checkForValidUserInfo(RegisterMessage message) {
    boolean validInfo = true;
    String[] messages =  new String[1];

    if(containsInvalidCharacters(message.nickname)){
      messages[0] = "Invalid Nickname - Nickname can not have # or : in it";
      validInfo = false;
    } else if (containsInvalidCharacters(message.email)){
      messages[0] = "Invalid Email - Emails can not have # or : in them";
      validInfo = false;
    }
    // Send failed Register Response Message with reason
    if(!validInfo){
      viewDriver.handleViewMessage(new RegisterResponseMessage(false, messages));
    }
    return validInfo;
  }


  /**
   * Handles all the messages from NetManager(Not Implemented)
   * @param message the message to process
   */
  public void handleNetMessage(NetworkMessage message){
    System.out.println("Presenter::handleNetMessage:: " + message.type);
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
        if(currentGame == null){
          return;
        }
        Move move = (Move) message;
        // Check if the move message is for the current game
        if( currentGame.getGameID() == move.gameID) {
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
            Point moveFrom = new Point(move.move.substring(1, 3));
            Point moveTo = new Point(move.move.substring(3));

            MovePieceResponse boardBeforeMove = new MovePieceResponse(getCurrentPlayer(chadGame.getTurn()) + " moved.", chadGame.getBoard());
            viewDriver.handleViewMessage(boardBeforeMove);
            String opponentsMoves = chadGame.validMoves(moveFrom.toString());
            viewDriver.handleViewMessage(new ViewValidMovesResponse(new String[] {opponentsMoves}));
            chadGame.move(moveFrom.toString(), moveTo.toString());
            MovePieceResponse movePieceResponse = new MovePieceResponse(getMoveMessage(), chadGame.getBoard());
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
          playerNickname = "";
          currentGame = null;
        }
        else {
          // Not successful
          String[] messages = {"Unable to unregister. User Information did not match, try again."};
          UnregisterResponseMessage unregisterResponseMessage = new UnregisterResponseMessage(unregisterResponse.success, messages);
          viewDriver.handleViewMessage(unregisterResponseMessage);
        }
        break;
    }

  }

  private String getMoveMessage() {
    return getCurrentPlayer(chadGame.getTurn()) + "'s turn. Playing: " + getPlayerColor(chadGame.getTurn()) + ".";
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

  private String getPlayerColor(Boolean turn) {
    if(turn == currentGame.getColor())
      return currentGame.getColor() ? "Black" : "White";
    return !currentGame.getColor() ? "Black" : "White";
  }

  public static void main(String[] args) {
    // args[0] = "cli" or "gui", args[1] server host, args[2] server port
    ChadPresenter app = new ChadPresenter(args[1], args[2], args[0]);
    app.start();
  }
}
