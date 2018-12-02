package client.gui.cl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CLMenuTest {

  private CLMenu menu;


  @BeforeEach
  void setUp() {
    menu = new CLMenu();
  }

  @Test
  void showMenu() {

  }

  @Test
  void viewInvites() {

  }

  @Test
  void requestUsername() {
  }

  @Test
  void showPlayers() {
    String[] players = {"theGameMaster", "JosiahMay", "MrGeekTron", "bcgdwn", "luisar3", "smileswood", "AI"};
    try {
      menu.showPlayers(players);
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void showStats() {

  }

  @Test
  void unregisterUser() {

  }

}