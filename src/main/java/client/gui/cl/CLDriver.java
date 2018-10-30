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
   * @return
   */
  public CLLogin getLogin() {
    return login;
  }

  /**
   * Gets CLDriver's CLMenu instance
   * @return
   */
  public CLMenu getMenu() {
    return menu;
  }

  /**
   * Gets CLDriver's CLGameView instance
   * @return
   */
  public CLGameView getGame() {
    return game;
  }

  /**
   * Creates space for readability of the command-line
   * (returns nothing, but prints a long line)
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
    String user = "";
    String pass = "";
    String aU = "admin";
    String aP = "password";

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
          this.clearScreen();
          this.game.showIngameMenu();
          this.game.showGameBoard(new GameBoard("RiIrdDKjJkeERcC"));
          opt = 2;
          break;
        case 2:
          System.out.println("Select a piece (format: D4): ");
          while(piece.equals("")) {
            piece = this.keys.nextLine();
          }
          opt = 3;
          break;
        case 3:
          System.out.println("Enter a valid move (format: L4): ");
          move = this.keys.nextLine();
          // Move is applied and sent to other player
          System.out.println("Submitting move: { "+piece+" -> "+move+" }");
          opt = 0;
          turn = false;
          break;
        case 4:
          System.out.println("Leaving game...");
          opt = 0;
          break;
        case 5:
          opt = 0;
          break;
        default:
          System.out.println("Please enter a valid option/piece/move");
          opt = 1;
          break;
      }
    }
    return 0;
  }

  /**
   * Handle inbox interactions
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

    int opt = 0;
    while(!(opt <= L.size())){
      //Request option
      opt = this.keys.nextInt();
    }
    System.out.println("Option chosen: "+opt);


    return 0;
  }

  /**
   * Handle outbox interactions
   * @return int back to main menu
   */
  public int handleOutbox(){
    this.getMenu().requestUsername();

    String rival = "";
    while(rival.equals("")) {
      rival = this.keys.nextLine();
    }
    //Look for rival in database and send challenge
    System.out.println("Sending challenge to: \""+rival+"\"");
    return 0;
  }

  //@TODO
  // Make a method for housing the
  // switch statement in Main below!
  //@TODO
  // Remove testing arraylists from methods
  public static void main(String[] args) {
    CLLogin login = new CLLogin();
    CLMenu menu = new CLMenu();
    CLGameView game = new CLGameView();

    int opt = 0;
    int transition = 1979;
    //-------for quick testing purposes
    ArrayList<String> G = new ArrayList<String>();
    G.add("theGameMASTER");
    G.add("n00b1");
    //-------

    CLDriver driver = new CLDriver(login, menu, game);

    login.showSplash();
    driver.clearScreen();

    while(transition == 1979) {
      login.showLogin();
      opt = driver.keys.nextInt();
      transition = driver.handleLoginMenu(opt);
    }

    while(transition != 1979) {
      driver.clearScreen();

      switch(transition){
        case 0:
          driver.getMenu().showMenu();
          transition = driver.keys.nextInt();
          break;
        case 1:
          driver.getGame().showCurrentGames(G);
          while(opt > 0 && opt < G.size()) {
            opt = driver.keys.nextInt();
          }
          //String s = G[opt].getGameboard()
          System.out.println("Loading game against player \""+G.get(opt-1)+"\"...");
          opt = 0;
          //Pass this string s eventually
          transition = driver.handleGame();
          break;
        case 2:
          transition = driver.handleInbox();
          break;
        case 3:
          System.out.println("Send!");
          transition = driver.handleOutbox();
          break;
        case 4:
          //@TODO
          // Receive input for profile
          driver.getMenu().requestUsername();
          driver.getMenu().showStats("L33tL0rD",10,7,2,1);
          transition = 0;
          break;
        case 5:
          driver.getMenu().unregisterUser();
          transition = 0;
          break;
        case 6:
          transition = 1979;
          System.out.println("Good bye!");
          break;
        default:
          transition = 0;
          break;
      }
    }
    //end loop
  }
}
