package client.gui.cl;

import static java.lang.System.exit;

import client.game.GameBoard;
import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.RegisterMessage;
import client.presenter.controller.messages.UnregisterMessage;
import client.presenter.controller.messages.ViewMessage;
import client.presenter.network.messages.Logout;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class CLDriver {

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
   * Handle login menu interactions
   * @return a new LoginMessage object
   */
  public ViewMessage handleLoginMenu() throws NoSuchAlgorithmException {
    String email = "";
    String pass = "";
    
    int option = 0;

    while(true) {
      option = keys.nextInt();

      switch (option) {
        case 1:
          System.out.println("Enter e-mail:");
          email = keys.nextLine();

          System.out.println("Enter password:");
          pass = keys.nextLine();

          return new LoginMessage(email, pass);
        case 2:
          System.out.println("Enter e-mail:");
          email = keys.nextLine();

          System.out.println("Enter nickname:");
          String nick = keys.nextLine();

          System.out.println("Enter password:");
          pass = keys.nextLine();

          return new RegisterMessage(email, pass, nick);
        case 3:
          System.out.println();
          exit(0);
        default:
          System.out.println("[!] Please enter a valid option.");
          clearScreen();
          login.showLogin();
          break;
      }
    }
  }

  /**
   * Handle in-game interactions
   * @return int back to main menu
   */
  public int handleGame(){
    //-------create temp array for quick tests-------
    ArrayList<String> moves = new ArrayList<String>();
    moves.add("D4");
    moves.add("C3");
    //--------------
    int opt = 1;
    String piece = "";
    String move = "";
    boolean turn = true;
    // Have some sort of check to make sure
    // the player can't make a move unless
    // it's their turn.
    if(!turn){
      opt = 5;
      System.out.println("[!] Your opponent has not made their move yet.");
    }
    while(opt!=0 && turn){
      switch(opt){
        case 1:
          /** BOARD **/
          this.showGame();
          String input = "";
          while(input.equals("")) {
            input = this.keys.nextLine();
            if(input.toUpperCase().equals("MOVE")){
              opt = 2;
            }
            else if(input.toUpperCase().equals("EXIT")){
              opt = 3;
            }else if(input.toUpperCase().equals("FORFEIT")){
              opt = 4;
            }
            else{
              System.out.println("Please enter a valid option.");
            }
          }
          break;
        case 2:
          /** MOVE **/
          this.showGame();
          System.out.println("Select a piece (format: D4): ");
          while(piece.equals("")) {
            piece = this.keys.nextLine();
          }
          System.out.println("Enter a valid move (format: L4): ");
          move = this.keys.nextLine();
          // Move is applied and sent to other player
          // swap turns
          System.out.println("Submitting move: { "+piece+" -> "+move+" }");
          opt = 0;
          turn = false;
          // reprint board with move applied
          break;
        case 3:
          /** EXIT **/
          System.out.println("Leaving game...");
          opt = 0;
          break;
        case 4:
          /** FORFEIT **/
          System.out.println("Forfeiting game...");
          System.out.println("Good luck next time!");
          opt = 0;
          break;
      }
    }
    return 0;
  }


  /**
   * Helper method to show in-game view.
   * (returns nothing but a nice view)
   */
  public void showGame(){
    this.clearScreen();
    this.game.showGameBoard(new GameBoard("RiIrdDKjJkeERcC"));
    this.game.showInGameMenu();
  }

  /**
   * Handle inbox interactions
   * (@param ultimately needs the list of requests in order to print them out
   * @return int back to main menu
   */
  public int handleInbox(){
    //-------for quick testing purposes
    ArrayList<String> L = new ArrayList<String>();
    L.add("n00b1");
    L.add("DecentRival");
    //-------

    //Show the invites
    this.getMenu().viewInvites(L);

    int opt = -1;
    while(opt==-1){
      //Request option
      opt = this.keys.nextInt();
    }
    System.out.println("Accepted invite: "+opt);


    return 0;
  }

  /**
   * Handle outbox interactions
   * @return int back to main menu
   */
  public int handleOutbox(){
    while(true) {
      this.clearScreen();
      System.out.println("+++ Invite A Player +++");

      this.getMenu().requestUsername();
      String rival = "";
      while (rival.equals("")) {
        rival = this.keys.nextLine();
      }
      if(rival.toUpperCase().equals("EXIT")){
        System.out.println("Returning to Main Menu...");
        return 0;
      }
      //Look for rival in database and send challenge
      System.out.println("Sending challenge to: \"" + rival + "\"");
    }
  }

  public ViewMessage handleMenu(){
    int option = 0;
    while(true) {
      option = keys.nextInt();

      switch (option) {
        case 1:
          return handleGame();
        case 2:
          return handleInbox();
        case 3:
          return handleOutbox();
        case 4:
          menu.requestUsername();
          menu.showStats();
          break;
        case 5:
          menu.unregisterUser();
          int unreg = keys.nextInt();
          if (unreg == 0) {
            return new MenuMessage(MenuMessageTypes.LOGOUT, null);
          } else {
            option = 1;
            break;
          }
        case 6:
          System.out.println("[!] Hope to see you again soon!");
          //needs current player's nickname
          return new MenuMessage(MenuMessageTypes.LOGOUT, null);
        default:
          clearScreen();
          System.out.println("[!] Please enter a valid option.\n");
          menu.showMenu();
          break;
      }
    }
  }

  /** Runs and handles the Driver for the command line
   *
   */
  public ViewMessage runCLView(int transition) throws NoSuchAlgorithmException{
    switch (transition) {
      case 0:
        login.showSplash();
        login.showLogin();
        return handleLoginMenu();
      case 1:
        menu.showMenu();
        return handleMenu();
      case 2:
        return handleGame();
      case 3:
        return handleInbox();
      case 4:
        return handleOutbox();
        break;
      case 8:
        break;
      default:
        System.out.println("[!] Please select a valid option.");
        transition = 1;
        break;
    }
  }

  //@TODO
  // Remove testing ArrayLists from methods
  public static void main(String[] args) {
    CLLogin login = new CLLogin();
    CLMenu menu = new CLMenu();
    CLGameView game = new CLGameView();

    CLDriver driver = new CLDriver(login, menu, game);

    login.showSplash();
    driver.clearScreen();

    try {
      driver.runCLView(0);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
}
