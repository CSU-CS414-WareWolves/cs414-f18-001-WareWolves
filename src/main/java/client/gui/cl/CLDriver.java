package client.gui.cl;

import java.util.Scanner;

public class CLDriver {
  /**
   * Receives a packet
   * @return [?]
   */
  public void recvPacket() {
    return;
  }

  /**
   * Sends a packet
   * @param
   * @return [?]
   */
  public void sendPacket(){
    return;
  }

  /**
   * [TEST] Handle menu interactions
   * @param option, menu
   * @return void
   */
  public static int handleMainMenuOption(int option, CLMenu menu){
    switch(option){
      case 1:
        System.out.println("Resume!");
        return option;
      case 2:
        System.out.println("Invites!");
        return option;
      case 3:
        System.out.println("Send!");
        return option;
      case 4:
        System.out.println("Profile!");
        return option;
      case 5:
        System.out.println("Uregister!");
        return option;
      case 6:
        System.out.println("Good bye!");
        return 0;
      default:
        System.out.println("Please enter valid option");
        return option;
    }
  }

  public static void main(String[] args) {
    Scanner keys  = new Scanner(System.in);
    CLLogin login = new CLLogin();
    CLMenu menu = new CLMenu();
    CLGameView game = new CLGameView();
    login.showSplash();
    keys.next();

    login.showLogin();
    keys.next();

    int opt = 0;
    do {
      menu.showMenu();
      opt = keys.nextInt();
      opt = handleMainMenuOption(opt, menu);
    }while(opt != 0);
  }
}
