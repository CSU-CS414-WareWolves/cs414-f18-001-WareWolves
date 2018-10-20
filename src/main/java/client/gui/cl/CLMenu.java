package client.gui.cl;

import client.gui.MenuView;
import java.util.Scanner;

public class CLMenu implements MenuView {
  Scanner s;
  public CLMenu(){
    s = new Scanner(System.in);
  }
  public static void clearScreen() {
    System.out.print("\n\n");
    System.out.println("-----------------------------------------------------------");
  }
  // Print out menu for user to select
  public void showMenu(){
    clearScreen();
    System.out.println("+++ SELECT OPTION +++");
    System.out.println("1.[ Resume  Game ]");
    System.out.println("2.[ View Invites ]");
    System.out.println("3.[ Send Invites ]");
    System.out.println("4.[ View Profile ]");
    System.out.println("5.[  Unregister  ]");
    System.out.println("6.[    Logout    ]");
  }
  //solely for testing without anything else?
  public void handleMenu(){

  }
  //
  public void resumeGame(){

  }
  //
  public void viewInvite(){

  }
  //
  public void sendInvites(){

  }
  //
  public void viewProfile(){

  }
  //
  public void unregisterUser(){

  }
  //
  public void logout(){

  }
}
