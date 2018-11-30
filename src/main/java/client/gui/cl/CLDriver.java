package client.gui.cl;

import static java.lang.System.exit;

import client.game.Game;
import client.gui.ChadGameDriver;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.LoginResponseMessage;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.controller.messages.ViewValidMoves;
import client.presenter.controller.messages.ViewValidMovesResponse;
import client.presenter.network.messages.NetworkMessage;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class CLDriver implements ChadGameDriver {

  private CLLogin login;
  private CLMenu menu;
  private CLGameView game;
  private Game chadGame;
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
   * Prints the splash and title screen menu
   */
  public void createAndShowGUI(){
    login.showSplash();
    login.showLogin();
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
        menu.showMenu();
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
        ViewValidMoves vvm = (ViewValidMoves) message;
        String validMoves = chadGame.validMoves(vvm.location.toString());
        String[] validMovesArray0 = {validMoves};
        game.showValidMoves(validMovesArray0);
        break;
      case REGISTER_RESPONSE:
        break;
      case LOGIN_RESPONSE:
        LoginResponseMessage lrm = (LoginResponseMessage) message;
        if(lrm.success){
          menu.showMenu();
        }
        else{
          login.failedLogin();
        }
        break;
      case UNREGISTER_RESPONSE:
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
   *
   * @return
   */
  private ViewMessage handleLogin(int option) throws NoSuchAlgorithmException {
    String email;
    String pass;

    System.out.println("Enter your e-mail:");
    email = keys.nextLine();
    System.out.println("Enter your password:");
    pass = keys.nextLine();

    return new LoginMessage(email, pass);
  }

  public ViewMessage handleRegister() throws NoSuchAlgorithmException {
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

  private void warningValidOption() {
    System.err.println("[!] Please input a valid option\n");
  }

  public static void main(String[] args) {

  }
}
