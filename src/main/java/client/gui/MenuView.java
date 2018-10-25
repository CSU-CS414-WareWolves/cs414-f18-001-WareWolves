package client.gui;

import java.util.ArrayList;

public interface MenuView {
  // Print out menu for user to select
  void showMenu();

  //
  void viewInvites(ArrayList<String> mail);

  //
  void requestUsername();

  // Shows the current users profile
  void showStats(String username, int total, int wgames, int tgames, int lgames);

  //
  void unregisterUser();
}
