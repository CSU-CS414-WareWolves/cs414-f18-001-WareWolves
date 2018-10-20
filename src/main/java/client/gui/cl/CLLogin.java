package client.gui.cl;

import client.gui.LoginView;

public class CLLogin implements LoginView {
  //
  public void showSplash() {
    System.out.println("{  W  E  L  C  O  M  E       T  O  }");

    System.out.println("####################################");
    System.out.println("##|  ____|| |##| | / __ \\ |  __ \\ ##");
    System.out.println("##| |#####| |##| || |##| || |##\\ |##");
    System.out.println("##| |#####|  __  ||  __  || |###||##");
    System.out.println("##| |#####| |##| || |##| || |##/ /##");
    System.out.println("##|______||_|##|_||_|##|_||_____/ ##");
    System.out.println("####################################");
    System.out.println("|  ____|| |##| ||  __|| ____||  ___|");
    System.out.println("| |#####| |##| || |###| |####| |####");
    System.out.println("| |#####|  __  ||  __||___  ||___  |");
    System.out.println("| |#####| |##| || |#######| |####| |");
    System.out.println("|______||_|##|_||____||_____||_____|");
    System.out.println("####################################");

    System.out.println("\n\n{A Chess variant by Christian Freeling (1979)}");
    System.out.println("{     Implementation by WareWolves (2018)    }");
  }
  //
  public void showLogin() {

    System.out.println("1.[     Login    ]");
    System.out.println("2.[    Register  ]");
  }
  //
  public void login(String email, String password) {

  }
  //
  public void register(String email, String password, String nickname) {

  }
}
