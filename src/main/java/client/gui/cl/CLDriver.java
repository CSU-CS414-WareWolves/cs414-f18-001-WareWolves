package client.gui.cl;

import client.Point;
import client.game.Game;
import client.gui.ChadGameDriver;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.ViewMessageType;
import client.presenter.controller.messages.*;
import client.presenter.network.messages.ActiveGameResponse;
import client.presenter.network.messages.GameInfo;
import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.LoginResponse;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.Register;
import client.presenter.network.messages.RegisterResponse;
import client.presenter.network.messages.Unregister;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CLDriver implements ChadGameDriver {

  private CLLogin login;
  private CLMenu menu;
  private CLGameView game;
  private Scanner keys;

  // Active player nickname
  private String nickname;

  private Game chadGame;
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
  }

  /**
   * Handles a given NetworkMessage, acts according to the its type
   * @param message a NetworkMessage with a type and data dependent on its type
   */
  public void handleNetMessage(NetworkMessage message){
    switch(message.type){
      case LOGIN:
        //never receives one
        break;
      case LOGIN_RESPONSE:
        LoginResponse lr = (LoginResponse) message;
        handleViewMessage(new LoginResponseMessage(lr.success, lr.nickname));
        break;
      case LOGOUT:
        login.showLogout();
        break;
      case REGISTER:
        //never receives one
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
        ActiveGameResponse agr = (ActiveGameResponse) message;
        handleActiveGames(agr.gameIDs, agr.opponents);
        break;
      case INVITE_REQUEST:
        //send back a MenuMessage
        break;
      case INVITE_RESPONSE:
        break;
      case RESIGN:
        break;
      case REGISTER_RESPONSE:
        RegisterResponse rr = (RegisterResponse) message;
        handleViewMessage(new RegisterResponseMessage(rr.success, new String[]{}));
        break;
      case INBOX_REQUEST:
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
          RegisterMessage rm = handleRegister();
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
        MovePieceMessage mpm = handleMovePiece();
        //give to Presenter ref
        //update own board to show
        chadGame.move(mpm.fromLocation.toString(), mpm.toLocation.toString());
        showGame();
        // go to main menu
        break;
      case REGISTER_RESPONSE:
        RegisterResponseMessage rrm = (RegisterResponseMessage) message;
        if(rrm.success){
          menu.showMenu(nickname);
        }
        else{
          login.failedCreds(1);
        }
        break;
      case LOGIN_RESPONSE:
        LoginResponseMessage lrm = (LoginResponseMessage) message;
        if(lrm.success){
          this.nickname = lrm.nickname;
          menu.showMenu(nickname);
        }
        else{
          login.failedCreds(0);
        }
        break;
      case UNREGISTER_RESPONSE:
        handleUnregister();
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

  /**
   * Handles a given ViewMessage, acts according to the its type
   * @param message a ViewMessage with a type and data dependent on its type
   */
  private void handleMenuMessage(MenuMessage message) {
    MenuMessage mm;
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
//        handleActiveGames();
        //send to Presenter ref
        break;
      case INVITES:
        //TODO
        //send to Presenter ref
        break;
      case SELECT_GAME:
        //TODO
        //send to Presenter ref
        break;
      case SEND_INVITE:
        mm = handleOutbox();
        //send to Presenter ref
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

  public UnregisterMessage handleUnregister() {
    clearScreen();
    String email;
    String pass;

    System.out.println("[!] FOR UNREGISTER CONFIRMATION, PLEASE RE-ENTER YOUR ACCOUNT'S CREDENTIALS");
    System.out.println("E-mail:");
    email = keys.nextLine();
    System.out.println("Password:");
    pass = keys.nextLine();

    try {
      return new UnregisterMessage(email, pass, nickname);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Handle active game screen
   * @param ids list of ids of invitations
   * @param opponents list of nicknames the player has an invite from
   * @return MenuMessage object with
   */
  public MenuMessage handleActiveGames(int[] ids, String[] opponents){
    //show games
    game.showCurrentGames(ids, opponents);
    String[] info = new String[2];
    int option = keys.nextInt();

    info[0] = Integer.toString(ids[option]);
    info[1] = opponents[option];
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
    showGame();
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

  public MovePieceMessage handleMovePiece() {
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
  public MenuMessage handleInbox(NetworkMessage message){
    InboxResponse ir = (InboxResponse) message;
    menu.viewInvites(ir.inviteIDs, ir.recipients);

    String[] info = new String[2];
    int option = keys.nextInt();

    info[0] = Integer.toString(ir.inviteIDs[option]);
    info[1] = ir.recipients[option];
    return new MenuMessage(MenuMessageTypes.INVITES, info);
  }

  /**
   * Takes input from the user to send a number of invites according to their selection
   * @return a MenuMessage of type SEND_INVITE with a String array of nicknames||emails(?)
   */
  public MenuMessage handleOutbox(){
    String bigString = "";
    while (true) {
      menu.requestUsername();
      String temp = keys.nextLine();
      if(temp.toUpperCase().equals("EXIT")){
        break;
      }
      else {
        System.out.println("Invite will be sent to: " + temp);
        bigString += temp + ":";
      }
    }
    String[] info = bigString.split(":");
    return new MenuMessage(MenuMessageTypes.SEND_INVITE, info);
  }

  private void warningValidOption() {
    System.err.println("[!] Please input a valid option\n");
  }

  public static void main(String[] args) {
  }
}
