package client.gui.cl;

import static org.junit.jupiter.api.Assertions.*;

import client.presenter.controller.MenuMessageTypes;
import client.presenter.controller.messages.LoginMessage;
import client.presenter.controller.messages.MenuMessage;
import client.presenter.controller.messages.ViewMessage;
import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CLDriverTest {

  private CLDriver driver;

  private CLLogin login;
  private CLMenu menu;
  private CLGameView game;

  @BeforeEach
  void setUp(){
    login = new CLLogin();
    menu = new CLMenu();
    game = new CLGameView();
    driver = new CLDriver(login, menu, game);
  }

  @Test
  void getLogin() {
    CLLogin temp = driver.getLogin();
    Assert.assertTrue(temp.equals(login));
  }

  @Test
  void getMenu() {
    CLMenu temp = driver.getMenu();
    Assert.assertTrue(temp.equals(menu));
  }

  @Test
  void getGame() {
    CLGameView temp = driver.getGame();
    Assert.assertTrue(temp.equals(game));
  }

  @Test
  void clearScreen() {
    driver.clearScreen();
  }

  @Test
  void createAndShowGUI() {
    fail("");
  }

  @Test
  void handleNetworkMessage() {
    fail("");
  }

  @Test
  void handleViewMessage() {
    fail("");
  }

  @Test
  void handleLogin() {
    //TODO
    // run driver.handleLogin() and send in input
    // Do I need to start two threads for this?

    setMyIn("user");
    Scanner sc = new Scanner(System.in);
    sc.nextLine();

    setMyIn("pswd");
    sc = new Scanner(System.in);
    sc.nextLine();

    sc.close();
    fail("");
  }

  @Test
  void handleRegister() {
    fail("");
  }

  @Test
  void handleUnregister() {
    fail("");
  }

  @Test
  void handleActiveGames() {
    int[] ids = {23,12};
    String[] nicks = {"theGameMaster", "AI"};

    try {
      driver.handleActiveGames(ids, nicks);
    } catch(Exception e) {
      fail("");
    }
  }

  @Test
  void handleSelectGame() {
    int[] ids = {23,12};
    String[] nicks = {"theGameMaster", "AI"};

//    MenuMessage check = driver.handleSelectGame(ids, nicks);
//
//    assertTrue(check.menuType.equals(MenuMessageTypes.SELECT_GAME));
    fail("");
  }

  @Test
  void handleInGame() {
    fail("");
  }

  @Test
  void handleMovePiece() {
    fail("");
  }

  @Test
  void handleGameQuit() {
    fail("");
  }

  @Test
  void handleGameResign() {
    fail("");
  }

  @Test
  void showGame() {
    fail("");
  }

  @Test
  void handleInbox() {
    fail("");
  }

  @Test
  void handleOutbox() {
    fail("");
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