package client.gui.cl;

import client.gui.MenuView;
import java.util.ArrayList;

public class CLMenu {
  /**
   * Print out menu for user to select an option from
   */
  public void showMenu(String player){
    StringBuilder res = new StringBuilder();
    res.append("~ Hello there, "+player+"! ~\n\n");
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
   * @param players list of nicknames the player has an invite from
   * @return true=invites available, false=invites not available
   */
  public boolean viewInvites(int[] ids, String[] dates, String[] players){
    if(ids.length == 0 || ids == null || ids[0] == -1){
      System.out.println("No invites found!\nSend a game invite from the main menu!");
      return false;
    } else {
      StringBuilder res = new StringBuilder();
      res.append("+++ Inbox +++\n");
      for (int i = 0; i < ids.length; i++) {
        res.append("[" + ids[i] + "]: "+ dates[i] + " - " + players[i] + " has invited you to a game!\n");
      }
      System.out.println(res);
      return true;
    }
  }

  /**
   * Request a username from the current player
   */
  public void requestUsername(){
    System.out.println("Please enter player's username (type EXIT to leave): ");
  }

  /**
   * Prints all player nicknames that are registered
   */
  public void showPlayers(String[] players) {
    StringBuilder res = new StringBuilder();
    res.append("+++ Active Players +++\n");
    for(int i=0; i<players.length; i++) {
      res.append(players[i]);
      if((i+1)%3 == 0) {
        res.append(" \n");
      }
      else {
        res.append("  |  ");
      }
    }
    res.append("\n");
    System.out.println(res);
  }

  /**
   * Print the profile of the player selected
   * @param player requested player
   * @param white player who played white
   * @param black player who played black
   * @param results which player won
   */
  public void showStats(String player, String[] white, String[] black, boolean[] results){
    //This is the format that it will be printed as
    StringBuilder res = new StringBuilder();
    res.append("\n\n+++ "+ player + "'s Profile +++\n\n");
    res.append("|   W player   |   B player   |   Result  |\n");
    for(int i = 0; i<white.length; i++) {
      res.append(white[i]).append(" :: ");
      res.append(black[i]).append(" :: ");
      res.append(playerFromBool(results[i])).append("\n");
    }
    //TODO: W/L ratio calc
    System.out.println(res);
  }

  public String playerFromBool(boolean p) {
    return p ? "white" : "black";
  }

  /**
   * Allows a user to unregister their account after a warning prompt
   */
  public void unregisterUser(){
    StringBuilder res = new StringBuilder();
    res.append("+++ Unregister +++");
    res.append("[!] FOR UNREGISTER CONFIRMATION, PLEASE RE-ENTER YOUR ACCOUNT'S CREDENTIALS");
    System.out.println(res);
  }
}
