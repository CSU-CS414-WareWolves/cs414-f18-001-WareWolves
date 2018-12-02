package client.gui.cl;

import client.Point;
import client.game.Game;
import client.gui.ChadGameDriver;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.messages.*;
import client.presenter.network.messages.ActiveGameResponse;
import client.presenter.network.messages.GameInfo;
import client.presenter.network.messages.GameRequest;
import client.presenter.network.messages.InboxResponse;
import client.presenter.network.messages.InviteResponse;
import client.presenter.network.messages.LoginResponse;
import client.presenter.network.messages.NetworkMessage;
import client.presenter.network.messages.ProfileResponse;
import client.presenter.network.messages.RegisterResponse;
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
  private int gameid;

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
        //shouldn't receive this one either
        break;
      case GAME_REQUEST:
        GameRequest gr = (GameRequest) message;
        gameid = gr.gameID;
        break;
      case GAME_INFO:
        GameInfo gi = (GameInfo) message;
        handleInGame(gi.gameBoard, gi.turn);
        showGame();
        break;
      case MOVE:
        break;
      case PROFILE_RESPONSE:
        ProfileResponse pr = (ProfileResponse) message;
        //TODO
        break;
      case ACTIVE_GAMES_REQUEST:
        //send back a MenuMessage
        break;
      case ACTIVE_GAMES_RESPONSE:
        ActiveGameResponse agr = (ActiveGameResponse) message;
        handleActiveGames(agr.gameIDs, agr.opponents);
        break;
      case INVITE_REQUEST:
        //possibly not needed?
        //TODO
        break;
      case INVITE_RESPONSE:
        InviteResponse ir = (InviteResponse) message;
        //TODO
        break;
      case RESIGN:
        break;
      case REGISTER_RESPONSE:
        RegisterResponse rr = (RegisterResponse) message;
        handleViewMessage(new RegisterResponseMessage(rr.success, new String[]{}));
        break;
      case INBOX_REQUEST:
        //we don't need this message, only for server
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
          e.printStackTrace();
        }
        //give to Presenter ref
        break;
      case LOGIN:
        try {
          LoginMessage lm = handleLogin();
        } catch (NoSuchAlgorithmException e) {
          //handle error
          e.printStackTrace();
        }
        //give to Presenter ref
        break;
      case UNREGISTER:
        menu.unregisterUser();
        UnregisterMessage urm = handleUnregister();
        //give to Presenter ref
        break;
      case SHOW_VALID_MOVES:
        //Give presenter valid moves
        chadGame.validMoves(((ViewValidMoves)message).location.toString());
        break;
      case MENU:
        //merge in Josh's branch for new Menu ViewMessages
        handleMenuMessage((MenuMessage) message);
        break;
      case MOVE_PIECE:
        MovePieceMessage mpm = handleMovePiece();
        //give to Presenter ref
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
        MovePieceResponse mpr = (MovePieceResponse) message;
        if(mpr.success){
          clearScreen();
          game.showGameBoard(mpr.gameBoard);
        }
        else{
          showGame();
          System.out.println("[!] Invalid move, please select a valid move for your selected piece");
        }
        break;
    }
  }

  /**
   * Handles a given ViewMessage, acts according to the its type
   * @param message a ViewMessage with a type and data dependent on its type
   */
  private void handleMenuMessage(MenuMessage message) {
    MenuMessage mm;
    menu.showMenu(nickname);

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
//        mm = handleInbox();
        //send to Presenter ref
        break;
      case SELECT_GAME:
        //TODO
//        handleSelectGame()
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
  public LoginMessage handleLogin() throws NoSuchAlgorithmException {
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
   * Handles unregister confirmation for current user
   * @return an Unregister message
   */
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
   * Shows the user's active games
   * @param ids list of ids of invitations
   * @param opponents list of nicknames the player has an invite from
   * @return MenuMessage object with a String array = {gameId, opponent's nickname}
   */
  public void handleActiveGames(int[] ids, String[] opponents){
    game.showCurrentGames(ids, opponents);
  }

  /**
   * Handle the game selection screen
   * @param ids list of ids of invitations
   * @param opponents list of nicknames the player has an invite from
   * @return MenuMessage object with a String array = {gameId, opponent's nickname}
   */
  public MenuMessage handleSelectGame(int[] ids, String[] opponents) {
    String[] info = new String[2];
    int option = keys.nextInt();

    info[0] = Integer.toString(ids[option]);
    info[1] = opponents[option];
    gameid = ids[option];

    return new MenuMessage(MenuMessageTypes.SELECT_GAME, info);
  }

  /**
   * Handles the in-game menu interactions
   * @param board a String representation from a GameBoard instance
   * @param turn boolean value to see who's turn it is
   */
  public void handleInGame(String board, boolean turn){
    chadGame = new Game(board, turn);
    //Check gameover
    if(chadGame.gameover()){
      //TODO
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
    game.showGameBoard(chadGame.getBoard());
    game.showInGameMenu();
  }

  /**
   * Handle inbox interactions
   * @param message an InboxResponse with
   */
  public MenuMessage handleInbox(NetworkMessage message){
    clearScreen();
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
    clearScreen();
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

  /**
   * Prints a warning if incorrect input is entered
   * >>May not be needed in this class<<
   */
  private void warningValidOption() {
    System.err.println("[!] Please input a valid option\n");
  }

  public static void main(String[] args) {
  }
}
