package client.gui.cl;

import static java.lang.System.exit;

import client.game.GameBoard;
import client.gui.ChadGameDriver;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.NetworkMessage;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CLDriver implements ChadGameDriver {

  private CLLogin login;
  private CLMenu menu;
  private CLGameView game;
  public Scanner keys;

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
   *
   * @param message
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
   *
   * @param message
   */
  public void handleViewMessage(ViewMessage message){
    switch (message.messageType){
      case REGISTER:
        login.showLogin();
        break;
      case LOGIN:
        login.showLogin();
        break;
      case UNREGISTER:
        menu.unregisterUser();
        break;
      case SHOW_VALID_MOVES:
        break;
      case REGISTER_RESPONSE:
        break;
      case LOGIN_RESPONSE:
        break;
      case UNREGISTER_RESPONSE:
        break;
      case SHOW_VALID_MOVES_RESPONSE:
        game.showValidMoves(message.locations);
        break;
      case MENU_RESPONSE:
        break;
      case MOVE_PIECE_RESPONSE:
        break;
    }

  }

  /**
   * Handle login menu interactions
   * @return a new LoginMessage object
   */
  private ViewMessage handleLogin(int option) throws NoSuchAlgorithmException {
    String email;
    String pass;
    switch (option) {
      case 1:
        System.out.println("Enter e-mail:");
        email = keys.nextLine();

        System.out.println("Enter password:");
        pass = keys.nextLine();

        return new LoginMessage(email, pass);
      case 2:
        clearScreen();
        System.out.println("Welcome to Chad Chess!\n Please enter a valid e-mail:");
        email = keys.nextLine();

        System.out.println("Enter a unique nickname:");
        String nick = keys.nextLine();

        System.out.println("Enter a strong password:");
        pass = keys.nextLine();

        return new RegisterMessage(email, pass, nick);
      case 3:
        System.out.println("[!] Good bye!");
        exit(0);
      default:
        warningValidOption();
        return null;
    }
  }

  public ViewMessage handleMenu(String nickname){
    String[] info = {nickname};
    int option = 0;
    while(true) {
      option = keys.nextInt();
      menu.showMenu();
      switch (option) {
        case 1:
          //Resume game
          //@TODO: 11/16/2018 - get the activeGames somehow and then handle em
          MenuMessage activeGames = new MenuMessage(null, null);
          return handleActiveGames(activeGames);
          //After returning the chose, Presenter should call driver.handleInGame() run the game
        case 2:
          //View Inbox
          return new MenuMessage(MenuMessageTypes.INVITES, info);
        case 3:
          //Send outbox
          return handleOutbox();
        case 4:
          //View Stats
          menu.requestUsername();
          return new MenuMessage(MenuMessageTypes.PLAYER_STATS, info);
        case 5:
          //Unregister
          menu.unregisterUser();
          int unreg = keys.nextInt();
          if (unreg == 0) {
            return new MenuMessage(MenuMessageTypes.LOGOUT, info);
          } else {
            option = 1;
            break;
          }
        case 6:
          //Logout
          System.out.println("[!] Hope to see you again soon!");
          return new MenuMessage(MenuMessageTypes.LOGOUT, null);
        default:
          clearScreen();
          warningValidOption();
          menu.showMenu();
          break;
      }
    }
  }

  /**
   * Handle active game screen
   * @param games MenuMessage containing the active games of the user
   *        games.information should contain the nicknames from the active games
   * @return MenuMessage object with
   */
  public MenuMessage handleActiveGames(MenuMessage games){
    //show games
    game.showCurrentGames(games.information);

    String[] info = new String[1];
    int option = keys.nextInt();
    info[0] = games.information[option-1];

    return new MenuMessage(MenuMessageTypes.SELECT_GAME, info);
  }

  /**
   * Handles the in-game menu interactions
   * @param gb The GameBoard object representing the current users selected game
   * @return the option chosen for the in-game menu
   */
  public int handleInGame(GameBoard gb){
    //Show the in-game screen
    showGame(gb);
    int option = keys.nextInt();
    return option;
  }

  public ViewMessage handleBoard(GameBoard gameboard){
    while(true){
      int option = handleInGame(gameboard);
      switch (option){
        case 1:
          //Pick piece
          keys.nextLine();
          //show valid moves
          keys.nextLine();
          break;
        case 2:
          break;
        case 3:
          break;
        default:
          warningValidOption();
      }
    }
  }

  /**
   * Helper method to show in-game view.
   * (returns nothing but prints a nice view)
   */
  public void showGame(GameBoard gb){
    clearScreen();
    game.showGameBoard(gb);
    game.showInGameMenu();
  }

  /**
   * Handle inbox interactions
   * @param mail array of String that are game invites for the user
   */
  public void handleInbox(String[]  mail){
    menu.viewInvites(mail);
    return;
  }

  /**
   * Requests input from
   * @return
   */
  public MenuMessage handleOutbox(){
    menu.requestUsername();
    String[] info = new String[1];
    info[0] = keys.nextLine();
    System.out.println("Invite sent to: " + info[0]);
    return new MenuMessage(MenuMessageTypes.SEND_INVITE, info);
  }

  private void warningValidOption() {
    System.err.println("[!] Please input a valid option\n");
  }

  public static void main(String[] args) {
    CLLogin login = new CLLogin();
    CLMenu menu = new CLMenu();
    CLGameView game = new CLGameView();
//    CLDriver driver = new CLDriver(login, menu, game);
//
//    login.showSplash();
//    driver.clearScreen();
//
//    try {
//      driver.handleLoginMenu();
//    } catch (NoSuchAlgorithmException e) {
//      e.printStackTrace();
//    }
  }
}
