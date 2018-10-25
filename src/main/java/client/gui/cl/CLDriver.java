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

  public CLLogin getLogin() {
    return login;
  }

  public CLMenu getMenu() {
    return menu;
  }

  public CLGameView getGame() {
    return game;
  }

  /**
   * Creates space for readability of the command-line
   */
  public void clearScreen() {
    System.out.println("\n-----------------------------------------------------------\n");
  }

  /**
   * Handle main menu interactions
   * @param option the option chosen by the user
   * @return void
   */
  public int handleMainMenuOption(int option){
    switch(option){
      case 1:
        return 2;
      case 2:
        return 3;
      case 3:
        System.out.println("Send!");
        return option;
      case 4:
        menu.requestUsername();
        menu.showStats("L33tL0rD",10,7,2,1);
        return option;
      case 5:
        menu.unregisterUser();
        return option;
      case 6:
        System.out.println("Good bye!");
        return 0;
      default:
        System.out.println("Please enter valid option");
        return 1979;
    }
  }

  /**
   * Handle login menu interactions
   * @param option the option chosen by the user
   * @return the option that was chosen
   */
  public int handleLoginMenu(int option){
    switch (option){
      case 1:
        System.out.println("Login!");
        return 1;
      case 2:
        System.out.println("Register!");
        return 1979;
      case 3:
        return 1980;
      default:
        System.out.println("Please enter a valid option");
        return 1979;
    }
  }



  public static void main(String[] args) {
    CLLogin login = new CLLogin();
    CLMenu menu = new CLMenu();
    CLGameView game = new CLGameView();

    CLDriver driver = new CLDriver(login, menu, game);

    //-------for quick testing purposes
    ArrayList<String> L = new ArrayList<String>();
    L.add("n00b1");
    L.add("DecentRival");
    //-------

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

      if(transition == 0){
        switch(transition){
          case 1:
            System.out.println("~(jumping to Main Menu...)~");
            driver.getMenu().showMenu();
            opt = driver.keys.nextInt();
            transition = driver.handleMainMenuOption(opt);
            break;
          case 2:
            System.out.println("~(jumping to Game Menu...)~");
            driver.getGame().showGameBoard(new GameBoard("RiIrdDKjJkeERcC"));
            break;
          case 3:
            System.out.println("~(jumping to Invite Menu...)~");
            driver.getMenu().viewInvites(L);
            break;
          case 4:
            System.out.println("~(jumping to Invite Menu...)~");
            break;
          default:
              opt = 0;
              break;
        }
      }
    }while(opt != 1980);
  }
}
