package client.gui.cl;

import static java.lang.System.exit;

import client.Point;
import client.game.Game;
import client.game.GameBoard;
import client.gui.ChadGameDriver;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.LoginResponseMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.MovePieceMessage;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.RegisterResponseMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import client.presenter.controller.messages.ViewValidMovesResponse;
import client.presenter.network.messages.ActiveGameResponse;
import client.presenter.network.messages.GameInfo;
import client.presenter.network.messages.InboxRequest;
import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.NetworkMessage;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CLDriver implements ChadGameDriver {

  private CLLogin login;
  private CLMenu menu;
  private CLGameView game;
  public Scanner keys;

  private Game chadGame;
  private String nickname;
  private int gameID;

  public CLDriver(CLLogin _login, CLMenu _menu, CLGameView _game){
    login = _login;
    menu = _menu;
    game = _game;
    keys = new Scanner(System.in);
  }

  /**
   * Gets CLDriver's CLLogin instance
   * @return class instance of CLLogin
   */
  public CLLogin getLogin() {
    return login;
  }

  /**
   * Gets CLDriver's CLMenu instance
   * @return class instance of CLMenu
   */
  public CLMenu getMenu() {
    return menu;
  }

  /**
   * Gets CLDriver's CLGameView instance
   * @return class instance of CLGameView
   */
  public CLGameView getGame() {
    return game;
  }

  /**
   * Creates space for readability of the command-line
   * (returns nothing, but prints a long line and some space for readability)
   */
  public void clearScreen() {
    System.out.println("\n-----------------------------------------------------------\n");
  }

  /**
   * Prints the splash and title screen menu
   */
  public void createAndShowGUI(){
    login.showSplash();
    login.showLogin();
    chadGame = new Game();
//    setupGame(-1, chadGame.getBoard(), chadGame.getTurn());
  }

  /**
   * Handles a given NetworkMessage, acts according to the its type
   * @param message a NetworkMessage with a type and data dependent on its type
   */
  public void handleNetMessage(NetworkMessage message){
    switch(message.type){
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
        handleInGame(message);
        break;
      case MOVE:
        break;
      case ACTIVE_GAMES_REQUEST:
        //send back a MenuMessage
        break;
      case ACTIVE_GAMES_RESPONSE:
        handleActiveGames(message);
        break;
      case INVITE_REQUEST:
        //send back a MenuMessage
        break;
      case INVITE_RESPONSE:
        break;
      case RESIGN:
        break;
      case REGISTER_RESPONSE:
        break;
      case INBOX_REQUEST:
        //send back a MenuMessage
        break;
      case INBOX_RESPONSE:
        handleInbox(message);
        break;
    }
  }

  /**
   * Handles a given ViewMessage, acts according to the its type
   * @param message a ViewMessage with a type and data dependent on its type
   */
  public void handleViewMessage(ViewMessage message){
    switch (message.messageType){
      case REGISTER:
        try {
          RegisterMessage rm = (RegisterMessage) handleRegister();
        } catch (NoSuchAlgorithmException e) {
          //handle error
        }
        //give presenter rm
        break;
      case LOGIN:
        try {
          LoginMessage lm = handleLogin();
        } catch (NoSuchAlgorithmException e) {
          //handle error
        }
        //give presenter lm
        break;
      case UNREGISTER:
        menu.unregisterUser();
        break;
      case SHOW_VALID_MOVES:
        //Give presenter valid moves
        chadGame.validMoves(((ViewValidMoves)message).location.toString());
        break;
      case MENU:
        handleMenuMessage((MenuMessage) message);
        break;
      case MOVE_PIECE:
        handleMovePiece();
        break;
      case REGISTER_RESPONSE:
        RegisterResponseMessage rrm = (RegisterResponseMessage) message;
        if(rrm.success){
          menu.showMenu();
        }
        else{
          login.failedCreds(1);
        }
        break;
      case LOGIN_RESPONSE:
        LoginResponseMessage lrm = (LoginResponseMessage) message;
        if(lrm.success){
          menu.showMenu();
        }
        else{
          login.failedCreds(0);
        }
        break;
      case UNREGISTER_RESPONSE:
        System.out.println("[!] It's sad to see you go, but consider rejoining soon!");
        System.out.println("[!] Account has been unregistered.");
        //sign off / exit to title screen
        break;
      case SHOW_VALID_MOVES_RESPONSE:
        ViewValidMovesResponse vvmr = (ViewValidMovesResponse) message;
        String[] validMovesArray1 = vvmr.locations;
        game.showValidMoves(validMovesArray1);
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
        System.exit(0);
        break;
      case PLAYER_STATS:
        //TODO
        menu.showStats("",0,0,0,0);
        break;
      case ACTIVE_GAMES:
        //TODO
        break;
      case INVITES:
        //TODO
        break;
      case SELECT_GAME:
        //TODO
        break;
      case SEND_INVITE:
        //TODO
        break;
    }
  }


  /**
   * Handles login for an existing user
   * @return a LoginMessage with the user's input
   */
  private LoginMessage handleLogin() throws NoSuchAlgorithmException {
    clearScreen();
    String email;
    String pass;

    System.out.println("Enter your e-mail:");
    email = keys.nextLine();
    System.out.println("Enter your password:");
    pass = keys.nextLine();

    return new LoginMessage(email, pass);
  }

  /**
   * Handles registration for a new user
   * @return a RegisterMessage with the new user's input
   * @throws NoSuchAlgorithmException
   */
  public RegisterMessage handleRegister() throws NoSuchAlgorithmException {
    clearScreen();
    String email;
    String pass;
    String nick;

    System.out.println("Please enter a valid e-mail:");
    email = keys.nextLine();
    System.out.println("Enter a unique nickname:");
    nick = keys.nextLine();
    System.out.println("Enter a strong password:");
    pass = keys.nextLine();

    return new RegisterMessage(email, pass, nick);
  }

  /**
   * Handle active game screen
   * @param gs MenuMessage containing the active games of the user
   *        games.information should contain the nicknames from the active games
   * @return MenuMessage object with
   */
  public MenuMessage handleActiveGames(NetworkMessage gs){
    //show games
    ActiveGameResponse games = (ActiveGameResponse) gs;
    game.showCurrentGames(games.gameIDs, games.opponents);
    String[] info = new String[2];
    int option = keys.nextInt();

    info[0] = Integer.toString(games.gameIDs[option]);
    info[1] = games.opponents[option];
    return new MenuMessage(MenuMessageTypes.SELECT_GAME, info);
  }

  /**
   * Handles the in-game menu interactions
   * @return the option chosen for the in-game menu
   */
  public ViewMessage handleInGame(NetworkMessage message){
    GameInfo gi = (GameInfo) message;
    chadGame = new Game(gi.gameBoard, gi.turn);
    //Check gameover
    if(chadGame.gameover()){
      //TODO
    }
    //Show the in-game screen
    int option = 0;
    while(true) {
      showGame();
      option = keys.nextInt();
      switch (option) {
        case 1:
          return handleMovePiece();
        case 2:
          return handleGameQuit();
        case 3:
          return handleGameResign();
        default:
          warningValidOption();
      }
    }
  }

  public ViewMessage handleMovePiece() {
    String from;
    String to;
    while (true) {
      System.out.println("~ Select a piece (e.g. \"1a\"): ");
      from = keys.nextLine();
      //Display valid moves for selected piece
      //TODO: helper to convert validMoves String into String[]
      String[] moves = {chadGame.validMoves(from)};
      game.showValidMoves(moves);
      System.out.println("[!] Type \"c\" to cancel piece selection");
      System.out.println("~ Select space to move to (e.g. \"1a\"): ");
      to = keys.nextLine();
      if (!to.equals("c")) {
        break;
      }
    }
    return new MovePieceMessage(new Point(from), new Point(to));
  }

  /**
   * Handles option 2:Quit from in-game menu
   * @return an instance of ?, returns user to main menu
   */
  public ViewMessage handleGameQuit(){
    return null;
  }

  /**
   * Handle option 3:Resign from in-game menu
   * @return an instance of Resign to inform about resignation
   */
  public ViewMessage handleGameResign(){
    //TODO
    //************ ViewMessage for resigning game?
    return null;
  }

  /**
   * Helper method to show in-game view.
   * (returns nothing but prints a nice view)
   */
  public void showGame(){
    clearScreen();
    //TODO: modify showGame() to take the String representation for the gameboard
//    game.showGameBoard(chadGame.getBoard());
    game.showInGameMenu();
  }

  /**
   * Handle inbox interactions
   * @param message an InboxResponse with
   */
  public void handleInbox(NetworkMessage message){
    InboxResponse ir = (InboxResponse) message;
    menu.viewInvites(ir.inviteIDs, ir.recipients);
    return;
  }

  /**
   *
   * @return
   */
  public MenuMessage handleOutbox(){
    menu.requestUsername();
    String[] info = new String[1];
    info[0] = keys.nextLine();
    System.out.println("Invite sent to: " + info[0]);
    return null;
  }

  private void warningValidOption() {
    System.err.println("[!] Please input a valid option\n");
  }

  public static void main(String[] args) {
  }
}
