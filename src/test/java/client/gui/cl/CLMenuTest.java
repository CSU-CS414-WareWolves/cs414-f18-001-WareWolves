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
    try {
      menu.showMenu("PLAYER");
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void viewInvites() {
    int[] ids = {32,11,75};
    String[] players = {"Him","Her","Them"};
    String[] dates = {"today","yesterday","tomorrow"};
    try {
      menu.viewInvites(ids,players,dates);
    } catch(Exception e){
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void requestUsername() {
    try{
      menu.requestUsername();
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
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
    String player = "RIVAL";
    String[] white = {"RIVAL"};
    String[] black = {"YOU"};
    boolean[] results = {true};
    try {
      menu.showStats(player,white,black,results);
    } catch(Exception e){
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void unregisterUser() {
    try {
      menu.unregisterUser();
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }

}