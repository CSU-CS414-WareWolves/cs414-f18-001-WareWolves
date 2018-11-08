package client.gui.cl;

import client.game.GameBoard;
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
   * @param option the option chosen by the user
   * @return the option that was chosen
   */
  public int handleLoginMenu(int option){
    //----Temporary, remove once Presenter implemented with CL
    String user = "";
    String pass = "";
    String aU = "admin";
    String aP = "password";
    //----------

    switch (option){
      case 1:
        System.out.println("Username: ");
        while(user.equals("")) {
          user = this.keys.nextLine();
        }
        System.out.println("Password: ");
        pass = this.keys.nextLine();
        if(!(user.equals(aU) && pass.equals(aP))){
          System.out.println("[!] Username does not exist/Password is incorrect");
          return 1979;
        }
        else
          return 0;
        // Jump to main menu
      case 2:
        System.out.println("Registered!");
        return 0;
      case 3:
        System.out.println("Good bye!");
        return 1979;
      default:
        System.out.println("Please enter a valid option");
        return 0;
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

  /** Runs and handles the Driver for the command line
   *
   */
  public void runView(){
    int option;
    int transition = 7;

    while(transition != 1979) {
      option = 0;
      this.clearScreen();
      //-------for quick testing purposes
      String[] G = {"theGameMASTER", "n00b1"};
      //-------

      switch(transition){
        case 0:
          this.getMenu().showMenu();
          transition = this.keys.nextInt();
          break;
        case 1:
          this.getGame().showCurrentGames(G);
          while(option <= 0 || option > G.length) {
            option = this.keys.nextInt();
          }
          //String s = G[opt].getGameboard()
          System.out.println("Loading game against player \""+G[option-1]+"\"...");
          //Pass this string s eventually
          transition = this.handleGame();
          break;
        case 2:
          transition = this.handleInbox();
          break;
        case 3:
          transition = this.handleOutbox();
          break;
        case 4:
          //@TODO
          // Receive input for profile
          this.getMenu().requestUsername();
          this.getMenu().showStats("admin",10,7,2,1);
          transition = 0;
          break;
        case 5:
          this.getMenu().unregisterUser();
          transition = 0;
          break;
        case 6:
          transition = 7;
          System.out.println("Logging off...!");
          break;
        case 7:
          login.showLogin();
          option = this.keys.nextInt();
          transition = this.handleLoginMenu(option);
          break;
        default:
          transition = 0;
          break;
      }
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
    driver.runView();
  }
}
