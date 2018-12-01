package client.gui.cl;

import client.gui.MenuView;
import java.util.ArrayList;

public class CLMenu implements MenuView {
  /**
   * Print out menu for user to select an option from
   */
  public void showMenu(){
    StringBuilder res = new StringBuilder();
    res.append("+++ Main Menu +++\n\n");
    res.append("1.[ Resume  Game ]\n");
    res.append("2.[ View Invites ]\n");
    res.append("3.[ Send Invites ]\n");
    res.append("4.[ View Profile ]\n");
    res.append("5.[  Unregister  ]\n");
    res.append("6.[    Logout    ]\n");
    System.out.println(res);
  }

  /**
   * Print out all the invites that are currently in the user's inbox
   * @param ids list of ids of invitations
   * @param players list of usernames the player has an invite from
   */
  public void viewInvites(int[] ids, String[] players){
    if(ids.length == 0 || ids == null){
      System.out.println("No invites found!\nSend a game invite from the main menu!");
      return;
    } else {
      StringBuilder res = new StringBuilder();
      res.append("+++ Inbox +++\n");
      for (int i = 0; i < ids.length; i++) {
        res.append("[" + ids[i] + "]: " + players[i] + " has invited you to a game!\n");
      }
      System.out.println(res);
    }
  }

  /**
   * Request a username from the current player
   *
   */
  public void requestUsername(){
    System.out.println("Please enter player's username(type EXIT to leave): ");
  }

  /**
   * Print the profile of the player selected,
   * @param username
   * @param total
   * @param wgames
   * @param tgames
   * @param lgames
   */
  public void showStats(String username, int total, int wgames, int tgames, int lgames){
    //This is the format that it will be printed as
    StringBuilder res = new StringBuilder();
    res.append("+++ View Profile +++\n\n");
    res.append("| - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n");
    res.append("| Username: "+username+"\n");
    res.append("| - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n");
    res.append("| Games  : "+total+"\n");
    res.append("| Wins   : "+wgames+"\n");
    res.append("| Ties   : "+tgames+"\n");
    res.append("| Losses : "+lgames+"\n");
    res.append("| - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    System.out.println(res);
  }

  /**
   * Allows a user to unregister their account after a warning prompt
   */
  public void unregisterUser(){
    StringBuilder res = new StringBuilder();
    res.append("+++ Unregister +++");
    res.append(">>> Are you sure you want to UNREGISTER?\n>>> Your game record will be deleted\n");
    res.append("1.[      YES!     ]\n");
    res.append("2.[      NO!!     ]\n");
    System.out.println(res);
  }
}
