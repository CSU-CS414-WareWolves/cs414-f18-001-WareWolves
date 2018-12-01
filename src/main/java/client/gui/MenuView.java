package client.gui;

import java.util.ArrayList;

public interface MenuView {
  // Print out menu for user to select
  void showMenu();

  // View invites the active player has received
  void viewInvites(int[] ids, String[] players);

  // Shows the current users profile
  void showStats(String username, int total, int wgames, int tgames, int lgames);

  // Unregisters a user
  void unregisterUser();
}
