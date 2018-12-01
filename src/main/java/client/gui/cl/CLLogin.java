package client.gui.cl;

import client.gui.LoginView;

public class CLLogin implements LoginView {
  //
  public void showSplash() {
    StringBuilder res = new StringBuilder();
    res.append("{  W  E  L  C  O  M  E       T  O  }\n");
    res.append("   ______  _    _   ____   _____\n");
    res.append("  |  ____|| |  | | / __ \\ |  __ \\   \n");
    res.append("  | |     | |__| || |__| || |  \\ |  \n");
    res.append("  | |     |  __  ||  __  || |   ||  \n");
    res.append("  | |____ | |  | || |  | || |__/ |  \n");
    res.append("  |______||_|  |_||_|  |_||_____/   \n");
    res.append(" ______  _    _  ____  _____  _____\n");
    res.append("|  ____|| |  | ||  __|| ____||  ___|\n");
    res.append("| |     | |__| || |__ | |___ | |___ \n");
    res.append("| |     |  __  ||  __||___  ||___  |\n");
    res.append("| |____ | |  | || |__  ___| | ___| |\n");
    res.append("|______||_|  |_||____||_____||_____|\n");
    res.append("                                    \n");

    res.append("\n\n{A Chess variant by Christian Freeling (1979)}\n");
    res.append("{     Implementation by WareWolves (2018)    }\n");
    System.out.println(res);
  }
  /**
   * Shows the Login screen options
   */
  public void showLogin() {
    StringBuilder res = new StringBuilder();
    res.append("1.[     Login    ]\n");
    res.append("2.[    Register  ]\n");
    res.append("3.[     Exit     ]\n");
    System.out.println(res);
  }

  /**
   * Informs of a failed login attempt
   */
  public void failedCreds(int i){
    if(i==0) {
      System.out.println("[!] Failed login attempt, please try again.");
    }
    else{
      System.out.println("[!] Failed registration attempt, please try again.");
    }
  }
}
