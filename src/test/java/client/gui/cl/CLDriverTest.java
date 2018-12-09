package client.gui.cl;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.ChadPresenter;
import client.presenter.network.messages.ActiveGameResponse;
import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CLDriverTest {

  private CLDriver driver;

  private CLLogin login;
  private CLMenu menu;
  private CLGameView game;

  @BeforeEach
  void setUp(){
    driver = new CLDriver(new ChadPresenter());
  }

  @Test
  void clearScreen() {
    driver.clearScreen();
  }

  @Test
  void createAndShowGUI() {
    try {
      setMyIn("3");
      driver.createAndShowGUI();
    } catch(Exception e) {
      e.printStackTrace();
      fail("");
    }
  }

  @Test
  void handleNetMessage() {

  }

  @Test
  void handleViewMessage() {

  }

  @Test
  void handleLogin() {
    try {
      driver.handleLogin();
      setMyIn("test1\ntest1");
      Assertions.assertTrue("test1".equalsIgnoreCase(driver.getNickname()));
    } catch(NoSuchAlgorithmException e){
      fail("");
    }
  }

  @Test
  void handleRegister() {

  }

  @Test
  void handleUnregister() {
  }

  @Test
  void handleActiveGames() {
    int[] ids = {23,12};
    String[] nicks = {"theGameMaster", "AI"};
    boolean[] ts = {true, false};
    boolean[] color = {false, true};

    setMyIn("23");
    driver.showActiveGames(ids, nicks, ts, color);
    Assertions.assertEquals(23, driver.getGameID());
  }

  @Test
  void handleSelectGame() {
    int[] ids = {23,12};
    String[] nicks = {"theGameMaster", "AI"};

//    MenuMessage check = driver.handleSelectGame(ids, nicks);
//
//    assertTrue(check.menuType.equals(MenuMessageTypes.SELECT_GAME));
  }

  /**
   * Set the input stream for automatic keyboard input
   * @param input the String that will be set in the input stream
   */
  void setMyIn(String input) {
    ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
  }
}