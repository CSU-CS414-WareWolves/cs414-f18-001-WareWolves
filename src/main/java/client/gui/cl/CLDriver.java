package client.gui.cl;

import client.game.GameBoard;
import java.util.ArrayList;
import java.util.Scanner;
import javax.sound.midi.SysexMessage;

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
    switch (option){
      case 1:
        // Jump to main menu
        return 0;
      case 2:
        System.out.println("Register!");
        return 1979;
      default:
        System.out.println("Please enter a valid option");
        return 1979;
    }
  }

  public int handleGame(){
    //-------create temp array for quick tests-------
    ArrayList<String> moves = new ArrayList<String>();
    moves.add("D4 -> L4");
    moves.add("C3 -> C2");
    //--------------

    int opt = 0;
    String move = "";

    while(opt!=0){
      this.game.showGameBoard(new GameBoard("RiIrdDKjJkeERcC"));
      this.game.showValidMoves(moves);
      System.out.println("Enter a valid move (format: D4 L4), or type EXIT to leave game:\n");
      move = this.keys.nextLine();
      if(move.toUpperCase().equals("EXIT")){
        opt = 0;
        break;
      }
      System.out.println("Chosen move: "+move);
    }

    return 0;
  }

  public int handleInbox(int option){
    //-------for quick testing purposes
    ArrayList<String> L = new ArrayList<String>();
    L.add("n00b1");
    L.add("DecentRival");
    //-------

    this.getMenu().viewInvites(L);
    System.out.println("Option chosen: "+option);
    return 0;
  }

  public int handleOutbox(){
    this.getMenu().requestUsername();
    String rival = this.keys.nextLine();
    //Look for rival
    System.out.println("Sending challenge to: \""+rival+"\"");
    return 0;
  }

  public static void main(String[] args) {
    CLLogin login = new CLLogin();
    CLMenu menu = new CLMenu();
    CLGameView game = new CLGameView();

    CLDriver driver = new CLDriver(login, menu, game);

    login.showSplash();
    int opt = 1979;
    int transition = 0;
    do {
      driver.clearScreen();

      if(opt == 1979) {
        login.showLogin();
        opt = driver.keys.nextInt();
        transition = driver.handleLoginMenu(opt);
      }
      switch(transition){
        case 0:
          System.out.println("~(jumping to Main Menu...)~");
          driver.getMenu().showMenu();
          transition = driver.keys.nextInt();
          break;
        case 1:
          System.out.println("~(jumping to Game Menu...)~");
          //showGame() -> showGameBoard() + showValidMoves()
          driver.handleGame();
          break;
        case 2:
          System.out.println("~(jumping to Inbox Menu...)~");
          opt = driver.keys.nextInt();
          transition = driver.handleInbox(opt);
          break;
        case 3:
          System.out.println("~(jumping to Invite Menu)~");
          System.out.println("Send!");
          transition = driver.handleOutbox();
          break;
        case 4:
          System.out.println("~(jumping to Profile Menu...)~");
          driver.getMenu().requestUsername();
          driver.getMenu().showStats("L33tL0rD",10,7,2,1);
          transition = 0;
          break;
        case 5:
          System.out.println("~(jumping to Unregister Menu...)~");
          driver.getMenu().unregisterUser();
          transition = 0;
          break;
        case 6:
          System.out.println("~(logging off...)~");
          opt = 1979;
          break;
        default:
          transition = 0;
          break;
      }
    }while(opt != 1979);
  }
}
